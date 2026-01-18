package com.safetynet.alerts.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.FirestationMapping;
import com.safetynet.alerts.service.FirestationService;

@RestController
public class FirestationController {
  private final FirestationService service;

  public FirestationController(FirestationService service) {
    this.service = service;
  }

  @PostMapping("/firestation")
  @ResponseStatus(HttpStatus.CREATED)
  public FirestationMapping create(@Valid @RequestBody FirestationMapping mapping) {
    return service.create(mapping);
  }

  @PutMapping("/firestation")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateStation(@RequestParam("address") String address, @RequestParam("station") String station) {
    service.updateStationForAddress(address, station);
  }

  @DeleteMapping("/firestation")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@RequestParam(value = "address", required = false) String address,
                     @RequestParam(value = "station", required = false) String station) {
    if (address != null && !address.isBlank()) {
      service.deleteByAddress(address);
      return;
    }
    if (station != null && !station.isBlank()) {
      service.deleteByStation(station);
      return;
    }
    // Nothing to delete - no params
    throw new IllegalArgumentException("Provide either address or station");
  }
}
