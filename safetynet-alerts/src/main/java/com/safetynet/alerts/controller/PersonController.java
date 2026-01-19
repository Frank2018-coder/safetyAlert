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

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;

@RestController
public class PersonController {
  private final PersonService service;

  public PersonController(PersonService service) {
    this.service = service;
  }

  /**
   *  ajouter une nouvelle personne ;
   * @param person
   * @return
   */
  @PostMapping("/person")
  @ResponseStatus(HttpStatus.CREATED)
  public Person create(@Valid @RequestBody Person person) {
    return service.create(person);
  }

  /**
   * mettre à jour une personne existante (pour le moment, supposons que le prénom et le nom de
   * famille ne changent pas, mais que les autres champs peuvent être modifiés) ;
   * @param person
   * @return
   */
  @PutMapping("/person")
  public Person update(@Valid @RequestBody Person person) {
    return service.update(person);
  }

  /**
   * supprimer une personne (utilisez une combinaison de prénom et de nom comme identificateur
   * unique).
   * @param firstName
   * @param lastName
   */
  @DeleteMapping("/person")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
    service.delete(firstName, lastName);
  }

}
