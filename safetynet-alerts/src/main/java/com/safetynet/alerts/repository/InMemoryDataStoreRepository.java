package com.safetynet.alerts.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynet.alerts.config.DataStoreHolder;
import com.safetynet.alerts.model.FirestationMapping;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

@Repository
public class InMemoryDataStoreRepository implements DataStoreRepository {

  private final DataStoreHolder holder;

  public InMemoryDataStoreRepository(DataStoreHolder holder) {
    this.holder = holder;
  }

  @Override
  public synchronized List<Person> findAllPersons() {
    return new ArrayList<>(holder.getDataStore().getPersons());
  }

  @Override
  public synchronized Optional<Person> findPerson(String firstName, String lastName) {
    return holder.getDataStore().getPersons().stream()
        .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
        .findFirst();
  }

  @Override
  public synchronized void addPerson(Person person) {
    holder.getDataStore().getPersons().add(person);
  }

  @Override
  public synchronized void updatePerson(Person person) {
    deletePerson(person.getFirstName(), person.getLastName());
    addPerson(person);
  }

  @Override
  public synchronized void deletePerson(String firstName, String lastName) {
    holder.getDataStore().getPersons().removeIf(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName));
  }

  @Override
  public synchronized List<FirestationMapping> findAllFirestationMappings() {
    return new ArrayList<>(holder.getDataStore().getFirestations());
  }

  @Override
  public synchronized void addFirestationMapping(FirestationMapping mapping) {
    holder.getDataStore().getFirestations().add(mapping);
  }

  @Override
  public synchronized void updateFirestationByAddress(String address, String station) {
    holder.getDataStore().getFirestations().stream()
        .filter(m -> m.getAddress().equalsIgnoreCase(address))
        .forEach(m -> m.setStation(station));
  }

  @Override
  public synchronized void deleteFirestationByAddress(String address) {
    holder.getDataStore().getFirestations().removeIf(m -> m.getAddress().equalsIgnoreCase(address));
  }

  @Override
  public synchronized void deleteFirestationByStation(String station) {
    holder.getDataStore().getFirestations().removeIf(m -> m.getStation().equalsIgnoreCase(station));
  }

  @Override
  public synchronized List<MedicalRecord> findAllMedicalRecords() {
    return new ArrayList<>(holder.getDataStore().getMedicalrecords());
  }

  @Override
  public synchronized Optional<MedicalRecord> findMedicalRecord(String firstName, String lastName) {
    return holder.getDataStore().getMedicalrecords().stream()
        .filter(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName().equalsIgnoreCase(lastName))
        .findFirst();
  }

  @Override
  public synchronized void addMedicalRecord(MedicalRecord record) {
    holder.getDataStore().getMedicalrecords().add(record);
  }

  @Override
  public synchronized void updateMedicalRecord(MedicalRecord record) {
    deleteMedicalRecord(record.getFirstName(), record.getLastName());
    addMedicalRecord(record);
  }

  @Override
  public synchronized void deleteMedicalRecord(String firstName, String lastName) {
    holder.getDataStore().getMedicalrecords().removeIf(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName().equalsIgnoreCase(lastName));
  }
}
