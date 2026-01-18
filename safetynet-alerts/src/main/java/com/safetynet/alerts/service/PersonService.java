package com.safetynet.alerts.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.exception.ConflictException;
import com.safetynet.alerts.exception.NotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataStoreRepository;

@Service
public class PersonService {
  private static final Logger log = LogManager.getLogger(PersonService.class);
  private final DataStoreRepository repo;

  public PersonService(DataStoreRepository repo) {
    this.repo = repo;
  }

  public List<Person> findAll() {
    return repo.findAllPersons();
  }

  public Person create(Person person) {
    repo.findPerson(person.getFirstName(), person.getLastName())
        .ifPresent(p -> { throw new ConflictException("Person already exists: " + key(person)); });
    repo.addPerson(person);
    log.debug("Created person {}", key(person));
    return person;
  }

  public Person update(Person person) {
    repo.findPerson(person.getFirstName(), person.getLastName())
        .orElseThrow(() -> new NotFoundException("Person not found: " + key(person)));
    repo.updatePerson(person);
    log.debug("Updated person {}", key(person));
    return person;
  }

  public void delete(String firstName, String lastName) {
    repo.findPerson(firstName, lastName)
        .orElseThrow(() -> new NotFoundException("Person not found: " + firstName + " " + lastName));
    repo.deletePerson(firstName, lastName);
    log.debug("Deleted person {} {}", firstName, lastName);
  }

  private static String key(Person p) {
    return p.getFirstName() + " " + p.getLastName();
  }
}
