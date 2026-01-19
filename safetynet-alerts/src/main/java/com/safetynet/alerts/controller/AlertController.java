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

  /**
   * Cette url doit retourner une liste des personnes couvertes par la caserne de pompiers correspondante
   * Donc, si le numéro de station = 1, elle doit renvoyer les habitants couverts par la station numéro 1. La liste
   * doit inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro de téléphone. De plus,
   * elle doit fournir un décompte du nombre d'adultes et du nombre d'enfants (tout individu âgé de 18 ans ou
   * moins) dans la zone desservie.
   * @param stationNumber
   * @return
   */
  @GetMapping("/firestation")
  public FirestationCoverageDTO firestation(@RequestParam("stationNumber") String stationNumber) {
    return service.firestationCoverage(stationNumber);
  }

  /**
   * Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou moins) habitant à cette adresse.
   * La liste doit comprendre le prénom et le nom de famille de chaque enfant, son âge et une liste des autres
   * membres du foyer. S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide.
   * @param address
   * @return
   */
  @GetMapping("/childAlert")
  public List<ChildAlertDTO> childAlert(@RequestParam("address") String address) {
    return service.childAlert(address);
  }

  /**
   * Cette url doit retourner une liste des numéros de téléphone des résidents desservis par la caserne de
   * pompiers. Nous l'utiliserons pour envoyer des messages texte d'urgence à des foyers spécifiques
   * @param stationNumber
   * @return
   */
  @GetMapping("/phoneAlert")
  public List<String> phoneAlert(@RequestParam("firestation") String stationNumber) {
    return service.phoneAlert(stationNumber);
  }

  /**
   * Cette url doit retourner la liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne
   * de pompiers la desservant. La liste doit inclure le nom, le numéro de téléphone, l'âge et les antécédents
   * médicaux (médicaments, posologie et allergies) de chaque personne
   * @param address
   * @return
   */
  @GetMapping("/fire")
  public FireResponseDTO fire(@RequestParam("address") String address) {
    return service.fireAtAddress(address);
  }

  /**
   * Cette url doit retourner une liste de tous les foyers desservis par la caserne. Cette liste doit regrouper les
   * personnes par adresse. Elle doit aussi inclure le nom, le numéro de téléphone et l'âge des habitants, et
   * faire figurer leurs antécédents médicaux (médicaments, posologie et allergies) à côté de chaque nom.
   * @param stations
   * @return
   */
  @GetMapping("/flood/stations")
  public FloodResponseDTO flood(@RequestParam("stations") String stations) {
    List<String> stationNumbers = Arrays.stream(stations.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    return service.floodStations(stationNumbers);
  }

  /**
   * Cette url doit retourner le nom, l'adresse, l'âge, l'adresse mail et les antécédents médicaux (médicaments,
   * posologie, allergies) de chaque habitant. Si plusieurs personnes portent le même nom, elles doivent
   * toutes apparaître.
   * @param lastName
   * @return
   */
  @GetMapping("/personInfo")
  public List<PersonInfoDTO> personInfo(@RequestParam("lastName") String lastName) {
    return service.personInfoByLastName(lastName);
  }

  /**
   * Cette url doit retourner les adresses mail de tous les habitants de la ville.
   * @param city
   * @return
   */
  @GetMapping("/communityEmail")
  public List<String> communityEmail(@RequestParam("city") String city) {
    return service.communityEmailByCity(city);
  }

}
