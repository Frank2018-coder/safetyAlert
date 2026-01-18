# SafetyNet Alerts (Stack Technique 2)

Projet Spring Boot conforme aux spécifications fournies :
- MVC + principes SOLID (controllers / services / repository / DTO)
- Parsing JSON via **Jackson**
- Logs via **Log4j2** (INFO pour succès, ERROR pour erreurs, DEBUG pour étapes/calc)
- Tests **JUnit 5** + **MockMvc** couvrant tous les endpoints
- Rapport de tests **Surefire**
- Couverture **JaCoCo** avec règle minimale **80%**

## Stack
- Java 17
- Spring Boot
- Maven

## Démarrage

```bash
mvn clean test
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`.

## Données
Les données sont chargées au démarrage depuis `src/main/resources/data.json`.

## URLs (GET)
- `/firestation?stationNumber=<station_number>`
- `/childAlert?address=<address>`
- `/phoneAlert?firestation=<firestation_number>`
- `/fire?address=<address>`
- `/flood/stations?stations=1,2,3` (liste séparée par virgules)
- `/personInfo?lastName=<lastName>`
- `/communityEmail?city=<city>`

## Endpoints CRUD
### Person
- `POST /person` (body JSON Person)
- `PUT /person` (body JSON Person)
- `DELETE /person?firstName=<firstName>&lastName=<lastName>`

### Firestation
- `POST /firestation` (body JSON {address,station})
- `PUT /firestation?address=<address>&station=<station>`
- `DELETE /firestation?address=<address>` **ou** `DELETE /firestation?station=<station>`

### MedicalRecord
- `POST /medicalRecord` (body JSON)
- `PUT /medicalRecord` (body JSON)
- `DELETE /medicalRecord?firstName=<firstName>&lastName=<lastName>`

## Build artefacts
- Tests : `target/surefire-reports/`
- JaCoCo : `target/site/jacoco/`

