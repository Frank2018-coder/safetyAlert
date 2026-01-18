package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

public class FirestationCoverageDTO {
  private List<FirestationPersonDTO> persons = new ArrayList<>();
  private int adultCount;
  private int childCount;

  public List<FirestationPersonDTO> getPersons() { return persons; }
  public void setPersons(List<FirestationPersonDTO> persons) { this.persons = persons == null ? new ArrayList<>() : persons; }

  public int getAdultCount() { return adultCount; }
  public void setAdultCount(int adultCount) { this.adultCount = adultCount; }

  public int getChildCount() { return childCount; }
  public void setChildCount(int childCount) { this.childCount = childCount; }
}
