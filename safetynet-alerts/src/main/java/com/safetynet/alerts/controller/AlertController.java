package com.safetynet.alerts.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.FirestationCoverageDTO;
import com.safetynet.alerts.dto.FloodResponseDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.AlertService;

@RestController
public class AlertController {
  private final AlertService service;

  public AlertController(AlertService service) {
    this.service = service;
  }

  @GetMapping("/firestation")
  public FirestationCoverageDTO firestation(@RequestParam("stationNumber") String stationNumber) {
    return service.firestationCoverage(stationNumber);
  }

  @GetMapping("/childAlert")
  public List<ChildAlertDTO> childAlert(@RequestParam("address") String address) {
    return service.childAlert(address);
  }

  @GetMapping("/phoneAlert")
  public List<String> phoneAlert(@RequestParam("firestation") String stationNumber) {
    return service.phoneAlert(stationNumber);
  }

  @GetMapping("/fire")
  public FireResponseDTO fire(@RequestParam("address") String address) {
    return service.fireAtAddress(address);
  }

  @GetMapping("/flood/stations")
  public FloodResponseDTO flood(@RequestParam("stations") String stations) {
    List<String> stationNumbers = Arrays.stream(stations.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    return service.floodStations(stationNumbers);
  }

  @GetMapping("/personInfo")
  public List<PersonInfoDTO> personInfo(@RequestParam("lastName") String lastName) {
    return service.personInfoByLastName(lastName);
  }

  @GetMapping("/communityEmail")
  public List<String> communityEmail(@RequestParam("city") String city) {
    return service.communityEmailByCity(city);
  }
}
