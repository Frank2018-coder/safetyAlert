package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Optional;

import com.safetynet.alerts.model.FirestationMapping;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

public interface DataStoreRepository {
  List<Person> findAllPersons();
  Optional<Person> findPerson(String firstName, String lastName);
  void addPerson(Person person);
  void updatePerson(Person person);
  void deletePerson(String firstName, String lastName);

  List<FirestationMapping> findAllFirestationMappings();
  void addFirestationMapping(FirestationMapping mapping);
  void updateFirestationByAddress(String address, String station);
  void deleteFirestationByAddress(String address);
  void deleteFirestationByStation(String station);

  List<MedicalRecord> findAllMedicalRecords();
  Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName);
  void addMedicalRecord(MedicalRecord record);
  void updateMedicalRecord(MedicalRecord record);
  void deleteMedicalRecord(String firstName, String lastName);
}
