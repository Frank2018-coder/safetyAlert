package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

public class FloodResponseDTO {
  private List<FloodHouseholdDTO> households = new ArrayList<>();

  public List<FloodHouseholdDTO> getHouseholds() { return households; }
  public void setHouseholds(List<FloodHouseholdDTO> households) { this.households = households == null ? new ArrayList<>() : households; }
}
