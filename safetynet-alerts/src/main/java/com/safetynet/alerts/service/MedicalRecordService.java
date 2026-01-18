package com.safetynet.alerts.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.exception.ConflictException;
import com.safetynet.alerts.exception.NotFoundException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataStoreRepository;

@Service
public class MedicalRecordService {
  private static final Logger log = LogManager.getLogger(MedicalRecordService.class);
  private final DataStoreRepository repo;

  public MedicalRecordService(DataStoreRepository repo) {
    this.repo = repo;
  }

  public List<MedicalRecord> findAll() {
    return repo.findAllMedicalRecords();
  }

  public MedicalRecord create(MedicalRecord record) {
    repo.findMedicalRecord(record.getFirstName(), record.getLastName())
        .ifPresent(r -> { throw new ConflictException("Medical record already exists: " + key(record)); });
    repo.addMedicalRecord(record);
    log.debug("Created medical record {}", key(record));
    return record;
  }

  public MedicalRecord update(MedicalRecord record) {
    repo.findMedicalRecord(record.getFirstName(), record.getLastName())
        .orElseThrow(() -> new NotFoundException("Medical record not found: " + key(record)));
    repo.updateMedicalRecord(record);
    log.debug("Updated medical record {}", key(record));
    return record;
  }

  public void delete(String firstName, String lastName) {
    repo.findMedicalRecord(firstName, lastName)
        .orElseThrow(() -> new NotFoundException("Medical record not found: " + firstName + " " + lastName));
    repo.deleteMedicalRecord(firstName, lastName);
    log.debug("Deleted medical record {} {}", firstName, lastName);
  }

  private static String key(MedicalRecord r) {
    return r.getFirstName() + " " + r.getLastName();
  }
}
