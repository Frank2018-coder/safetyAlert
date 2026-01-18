package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

public class FireResidentDTO {
  private String firstName;
  private String lastName;
  private String phone;
  private int age;
  private List<String> medications = new ArrayList<>();
  private List<String> allergies = new ArrayList<>();

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }

  public List<String> getMedications() { return medications; }
  public void setMedications(List<String> medications) { this.medications = medications == null ? new ArrayList<>() : medications; }

  public List<String> getAllergies() { return allergies; }
  public void setAllergies(List<String> allergies) { this.allergies = allergies == null ? new ArrayList<>() : allergies; }
}
