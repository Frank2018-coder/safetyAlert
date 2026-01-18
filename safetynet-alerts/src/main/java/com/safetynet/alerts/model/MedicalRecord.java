package com.safetynet.alerts.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class MedicalRecord {
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String birthdate; // MM/dd/yyyy
  private List<String> medications = new ArrayList<>();
  private List<String> allergies = new ArrayList<>();

  public MedicalRecord() {}

  public MedicalRecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
    this.medications = medications == null ? new ArrayList<>() : medications;
    this.allergies = allergies == null ? new ArrayList<>() : allergies;
  }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getBirthdate() { return birthdate; }
  public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

  public List<String> getMedications() { return medications; }
  public void setMedications(List<String> medications) { this.medications = medications == null ? new ArrayList<>() : medications; }

  public List<String> getAllergies() { return allergies; }
  public void setAllergies(List<String> allergies) { this.allergies = allergies == null ? new ArrayList<>() : allergies; }
}
