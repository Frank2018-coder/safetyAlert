package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.FireResidentDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.FirestationCoverageDTO;
import com.safetynet.alerts.dto.FirestationPersonDTO;
import com.safetynet.alerts.dto.FloodHouseholdDTO;
import com.safetynet.alerts.dto.FloodResponseDTO;
import com.safetynet.alerts.dto.HouseholdMemberDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.exception.NotFoundException;
import com.safetynet.alerts.model.FirestationMapping;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataStoreRepository;

@Service
public class AlertService {
  private static final Logger log = LogManager.getLogger(AlertService.class);

  private final DataStoreRepository repo;
  private final AgeCalculator ageCalculator;

  public AlertService(DataStoreRepository repo, AgeCalculator ageCalculator) {
    this.repo = repo;
    this.ageCalculator = ageCalculator;
  }

  public FirestationCoverageDTO firestationCoverage(String stationNumber) {
    List<String> addresses = repo.findAllFirestationMappings().stream()
        .filter(m -> m.getStation().equalsIgnoreCase(stationNumber))
        .map(FirestationMapping::getAddress)
        .distinct()
        .toList();

    log.debug("Station {} covers addresses {}", stationNumber, addresses);

    Map<String, MedicalRecord> medicalByName = medicalRecordByName();
    List<Person> covered = repo.findAllPersons().stream()
        .filter(p -> addresses.stream().anyMatch(a -> a.equalsIgnoreCase(p.getAddress())))
        .sorted(Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName))
        .toList();

    int childCount = 0;
    int adultCount = 0;
    List<FirestationPersonDTO> persons = new ArrayList<>();

    for (Person p : covered) {
      persons.add(new FirestationPersonDTO(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone()));
      MedicalRecord r = medicalByName.get(nameKey(p.getFirstName(), p.getLastName()));
      if (r != null) {
        int age = ageCalculator.ageInYears(r.getBirthdate());
        if (ageCalculator.isChild(age)) childCount++; else adultCount++;
      } else {
        // If no record exists, count as adult (conservative)
        adultCount++;
      }
    }

    FirestationCoverageDTO dto = new FirestationCoverageDTO();
    dto.setPersons(persons);
    dto.setAdultCount(adultCount);
    dto.setChildCount(childCount);
    return dto;
  }

  public List<ChildAlertDTO> childAlert(String address) {
    Map<String, MedicalRecord> medicalByName = medicalRecordByName();

    List<Person> household = repo.findAllPersons().stream()
        .filter(p -> p.getAddress().equalsIgnoreCase(address))
        .sorted(Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName))
        .toList();

    if (household.isEmpty()) {
      return List.of();
    }

    List<HouseholdMemberDTO> otherMembers = household.stream()
        .map(p -> new HouseholdMemberDTO(p.getFirstName(), p.getLastName()))
        .toList();

    List<ChildAlertDTO> children = new ArrayList<>();
    for (Person p : household) {
      MedicalRecord r = medicalByName.get(nameKey(p.getFirstName(), p.getLastName()));
      if (r == null) continue;
      int age = ageCalculator.ageInYears(r.getBirthdate());
      if (ageCalculator.isChild(age)) {
        ChildAlertDTO c = new ChildAlertDTO();
        c.setFirstName(p.getFirstName());
        c.setLastName(p.getLastName());
        c.setAge(age);
        c.setOtherHouseholdMembers(otherMembers.stream()
            .filter(m -> !(m.getFirstName().equalsIgnoreCase(p.getFirstName()) && m.getLastName().equalsIgnoreCase(p.getLastName())))
            .toList());
        children.add(c);
      }
    }

    return children;
  }

  public List<String> phoneAlert(String stationNumber) {
    Set<String> addresses = repo.findAllFirestationMappings().stream()
        .filter(m -> m.getStation().equalsIgnoreCase(stationNumber))
        .map(FirestationMapping::getAddress)
        .collect(Collectors.toSet());

    Set<String> phones = repo.findAllPersons().stream()
        .filter(p -> addresses.stream().anyMatch(a -> a.equalsIgnoreCase(p.getAddress())))
        .map(Person::getPhone)
        .collect(Collectors.toSet());

    return phones.stream().sorted().toList();
  }

  public FireResponseDTO fireAtAddress(String address) {
    List<Person> persons = repo.findAllPersons().stream()
        .filter(p -> p.getAddress().equalsIgnoreCase(address))
        .toList();

    if (persons.isEmpty()) {
      throw new NotFoundException("No residents found for address: " + address);
    }

    List<String> stationNumbers = repo.findAllFirestationMappings().stream()
        .filter(m -> m.getAddress().equalsIgnoreCase(address))
        .map(FirestationMapping::getStation)
        .distinct()
        .sorted()
        .toList();

    if (stationNumbers.isEmpty()) {
      throw new NotFoundException("No firestation mapping found for address: " + address);
    }

    Map<String, MedicalRecord> medicalByName = medicalRecordByName();
    List<FireResidentDTO> residents = persons.stream()
        .sorted(Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName))
        .map(p -> toFireResident(p, medicalByName))
        .toList();

    FireResponseDTO dto = new FireResponseDTO();
    dto.setStationNumbers(stationNumbers);
    dto.setResidents(residents);
    return dto;
  }

  public FloodResponseDTO floodStations(List<String> stationNumbers) {
    Set<String> stations = stationNumbers.stream().map(String::trim).collect(Collectors.toSet());

    Map<String, Set<String>> addressToStations = new HashMap<>();
    for (FirestationMapping m : repo.findAllFirestationMappings()) {
      if (stations.stream().anyMatch(s -> s.equalsIgnoreCase(m.getStation()))) {
        addressToStations.computeIfAbsent(m.getAddress(), a -> new HashSet<>()).add(m.getStation());
      }
    }

    Map<String, MedicalRecord> medicalByName = medicalRecordByName();

    List<FloodHouseholdDTO> households = new ArrayList<>();
    for (var entry : addressToStations.entrySet()) {
      String address = entry.getKey();
      List<Person> residentsAt = repo.findAllPersons().stream()
          .filter(p -> p.getAddress().equalsIgnoreCase(address))
          .sorted(Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName))
          .toList();

      FloodHouseholdDTO h = new FloodHouseholdDTO();
      h.setAddress(address);
      h.setStationNumbers(entry.getValue().stream().sorted().toList());
      h.setResidents(residentsAt.stream().map(p -> toFireResident(p, medicalByName)).toList());
      households.add(h);
    }

    households.sort(Comparator.comparing(FloodHouseholdDTO::getAddress));

    FloodResponseDTO resp = new FloodResponseDTO();
    resp.setHouseholds(households);
    return resp;
  }

  public List<PersonInfoDTO> personInfoByLastName(String lastName) {
    Map<String, MedicalRecord> medicalByName = medicalRecordByName();

    List<Person> persons = repo.findAllPersons().stream()
        .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
        .sorted(Comparator.comparing(Person::getFirstName))
        .toList();

    if (persons.isEmpty()) {
      throw new NotFoundException("No persons found with lastName: " + lastName);
    }

    return persons.stream().map(p -> {
      MedicalRecord r = medicalByName.get(nameKey(p.getFirstName(), p.getLastName()));
      int age = r == null ? -1 : ageCalculator.ageInYears(r.getBirthdate());
      return new PersonInfoDTO(
          p.getFirstName(),
          p.getLastName(),
          p.getAddress(),
          age,
          p.getEmail(),
          r == null ? List.of() : r.getMedications(),
          r == null ? List.of() : r.getAllergies()
      );
    }).toList();
  }

  public List<String> communityEmailByCity(String city) {
    return repo.findAllPersons().stream()
        .filter(p -> p.getCity().equalsIgnoreCase(city))
        .map(Person::getEmail)
        .distinct()
        .sorted()
        .toList();
  }

  private FireResidentDTO toFireResident(Person p, Map<String, MedicalRecord> medicalByName) {
    FireResidentDTO dto = new FireResidentDTO();
    dto.setFirstName(p.getFirstName());
    dto.setLastName(p.getLastName());
    dto.setPhone(p.getPhone());

    MedicalRecord r = medicalByName.get(nameKey(p.getFirstName(), p.getLastName()));
    if (r != null) {
      dto.setAge(ageCalculator.ageInYears(r.getBirthdate()));
      dto.setMedications(r.getMedications());
      dto.setAllergies(r.getAllergies());
    } else {
      dto.setAge(-1);
      dto.setMedications(List.of());
      dto.setAllergies(List.of());
    }
    return dto;
  }

  private Map<String, MedicalRecord> medicalRecordByName() {
    return repo.findAllMedicalRecords().stream()
        .collect(Collectors.toMap(r -> nameKey(r.getFirstName(), r.getLastName()), r -> r, (a, b) -> a));
  }

  private static String nameKey(String firstName, String lastName) {
    return (firstName + "|" + lastName).toLowerCase();
  }
}
