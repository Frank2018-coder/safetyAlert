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

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;

@RestController
public class MedicalRecordController {
  private final MedicalRecordService service;

  public MedicalRecordController(MedicalRecordService service) {
    this.service = service;
  }

  @PostMapping("/medicalRecord")
  @ResponseStatus(HttpStatus.CREATED)
  public MedicalRecord create(@Valid @RequestBody MedicalRecord record) {
    return service.create(record);
  }

  @PutMapping("/medicalRecord")
  public MedicalRecord update(@Valid @RequestBody MedicalRecord record) {
    return service.update(record);
  }

  @DeleteMapping("/medicalRecord")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
    service.delete(firstName, lastName);
  }
}
