package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

public class FireResponseDTO {
  private List<String> stationNumbers = new ArrayList<>();
  private List<FireResidentDTO> residents = new ArrayList<>();

  public List<String> getStationNumbers() { return stationNumbers; }
  public void setStationNumbers(List<String> stationNumbers) { this.stationNumbers = stationNumbers == null ? new ArrayList<>() : stationNumbers; }

  public List<FireResidentDTO> getResidents() { return residents; }
  public void setResidents(List<FireResidentDTO> residents) { this.residents = residents == null ? new ArrayList<>() : residents; }
}
