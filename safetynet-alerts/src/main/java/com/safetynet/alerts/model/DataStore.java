package com.safetynet.alerts.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataStore {
  @JsonProperty("persons")
  private List<Person> persons = new ArrayList<>();

  @JsonProperty("firestations")
  private List<FirestationMapping> firestations = new ArrayList<>();

  @JsonProperty("medicalrecords")
  private List<MedicalRecord> medicalrecords = new ArrayList<>();

  public List<Person> getPersons() { return persons; }
  public void setPersons(List<Person> persons) { this.persons = persons == null ? new ArrayList<>() : persons; }

  public List<FirestationMapping> getFirestations() { return firestations; }
  public void setFirestations(List<FirestationMapping> firestations) { this.firestations = firestations == null ? new ArrayList<>() : firestations; }

  public List<MedicalRecord> getMedicalrecords() { return medicalrecords; }
  public void setMedicalrecords(List<MedicalRecord> medicalrecords) { this.medicalrecords = medicalrecords == null ? new ArrayList<>() : medicalrecords; }
}
