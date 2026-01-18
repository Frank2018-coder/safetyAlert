package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;

public class FirestationMapping {
  @NotBlank
  private String address;
  @NotBlank
  private String station;

  public FirestationMapping() {}

  public FirestationMapping(String address, String station) {
    this.address = address;
    this.station = station;
  }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getStation() { return station; }
  public void setStation(String station) { this.station = station; }
}
