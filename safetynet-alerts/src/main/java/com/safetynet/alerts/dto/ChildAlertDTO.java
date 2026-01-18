package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

public class ChildAlertDTO {
  private String firstName;
  private String lastName;
  private int age;
  private List<HouseholdMemberDTO> otherHouseholdMembers = new ArrayList<>();

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }

  public List<HouseholdMemberDTO> getOtherHouseholdMembers() { return otherHouseholdMembers; }
  public void setOtherHouseholdMembers(List<HouseholdMemberDTO> otherHouseholdMembers) {
    this.otherHouseholdMembers = otherHouseholdMembers == null ? new ArrayList<>() : otherHouseholdMembers;
  }
}
