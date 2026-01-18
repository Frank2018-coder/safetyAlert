package com.safetynet.alerts.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.FirestationMapping;
import com.safetynet.alerts.repository.DataStoreRepository;

@Service
public class FirestationService {
  private static final Logger log = LogManager.getLogger(FirestationService.class);
  private final DataStoreRepository repo;

  public FirestationService(DataStoreRepository repo) {
    this.repo = repo;
  }

  public List<FirestationMapping> findAll() {
    return repo.findAllFirestationMappings();
  }

  public FirestationMapping create(FirestationMapping m) {
    repo.addFirestationMapping(m);
    log.debug("Created firestation mapping {} -> {}", m.getAddress(), m.getStation());
    return m;
  }

  public void updateStationForAddress(String address, String station) {
    repo.updateFirestationByAddress(address, station);
    log.debug("Updated firestation mapping address={} station={}", address, station);
  }

  public void deleteByAddress(String address) {
    repo.deleteFirestationByAddress(address);
    log.debug("Deleted firestation mapping for address={}", address);
  }

  public void deleteByStation(String station) {
    repo.deleteFirestationByStation(station);
    log.debug("Deleted firestation mappings for station={}", station);
  }
}
