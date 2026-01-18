package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AgeCalculator {
  private static final Logger log = LogManager.getLogger(AgeCalculator.class);
  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public int ageInYears(String birthdate) {
    LocalDate dob = LocalDate.parse(birthdate, FMT);
    int age = Period.between(dob, LocalDate.now()).getYears();
    log.debug("Computed age={} from birthdate={}", age, birthdate);
    return age;
  }

  public boolean isChild(int age) {
    return age <= 18;
  }
}
