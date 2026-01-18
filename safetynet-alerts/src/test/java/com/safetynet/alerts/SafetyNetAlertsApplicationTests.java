package com.safetynet.alerts;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class SafetyNetAlertsApplicationTests {

  @Autowired
  private MockMvc mvc;

  @Test
  @Order(1)
  void firestationCoverage_station1_hasCountsAndPeople() throws Exception {
    mvc.perform(get("/firestation").param("stationNumber", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.adultCount", is(5)))
        .andExpect(jsonPath("$.childCount", is(1)))
        .andExpect(jsonPath("$.persons", hasSize(6)))
        .andExpect(jsonPath("$.persons[0].firstName", not(isEmptyOrNullString())));
  }

  @Test
  @Order(2)
  void childAlert_addressWithChildren_returnsChildren() throws Exception {
    mvc.perform(get("/childAlert").param("address", "1509 Culver St"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].age", greaterThanOrEqualTo(0)))
        .andExpect(jsonPath("$[0].otherHouseholdMembers", notNullValue()));
  }

  @Test
  @Order(3)
  void phoneAlert_station3_returnsPhoneList() throws Exception {
    mvc.perform(get("/phoneAlert").param("firestation", "3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasItem("841-874-6512")));
  }

  @Test
  @Order(4)
  void fire_address_returnsStationsAndResidents() throws Exception {
    mvc.perform(get("/fire").param("address", "112 Steppes Pl"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stationNumbers", hasItems("3", "4")))
        .andExpect(jsonPath("$.residents", hasSize(3)))
        .andExpect(jsonPath("$.residents[0].medications", notNullValue()));
  }

  @Test
  @Order(5)
  void flood_stations_returnsHouseholdsGrouped() throws Exception {
    mvc.perform(get("/flood/stations").param("stations", "1,2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.households", not(empty())))
        .andExpect(jsonPath("$.households[*].address", hasItem("644 Gershwin Cir")));
  }

  @Test
  @Order(6)
  void personInfo_boyd_returnsAllBoyds() throws Exception {
    mvc.perform(get("/personInfo").param("lastName", "Boyd"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(6)))
        .andExpect(jsonPath("$[0].email", containsString("@")));
  }

  @Test
  @Order(7)
  void communityEmail_culver_returnsUniqueEmails() throws Exception {
    mvc.perform(get("/communityEmail").param("city", "Culver"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasItem("jaboyd@email.com")))
        .andExpect(jsonPath("$", hasSize(15)));
  }

  @Test
  @Order(8)
  void crud_person_create_update_delete() throws Exception {
    String create = "{" +
        "\"firstName\":\"Test\"," +
        "\"lastName\":\"User\"," +
        "\"address\":\"1 Test St\"," +
        "\"city\":\"Culver\"," +
        "\"zip\":\"00000\"," +
        "\"phone\":\"000-000-0000\"," +
        "\"email\":\"test.user@email.com\"" +
        "}";

    mvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(create))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName", is("Test")));

    String update = "{" +
        "\"firstName\":\"Test\"," +
        "\"lastName\":\"User\"," +
        "\"address\":\"1 Test St\"," +
        "\"city\":\"Culver\"," +
        "\"zip\":\"11111\"," +
        "\"phone\":\"000-000-0001\"," +
        "\"email\":\"test.user@email.com\"" +
        "}";

    mvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(update))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.zip", is("11111")));

    mvc.perform(delete("/person").param("firstName", "Test").param("lastName", "User"))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(9)
  void crud_firestation_create_update_delete() throws Exception {
    String create = "{\"address\":\"1 Test St\",\"station\":\"9\"}";
    mvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).content(create))
        .andExpect(status().isCreated());

    mvc.perform(put("/firestation").param("address", "1 Test St").param("station", "10"))
        .andExpect(status().isNoContent());

    mvc.perform(delete("/firestation").param("address", "1 Test St"))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(10)
  void crud_medicalRecord_create_update_delete() throws Exception {
    String create = "{" +
        "\"firstName\":\"Test\"," +
        "\"lastName\":\"User\"," +
        "\"birthdate\":\"01/01/2000\"," +
        "\"medications\":[\"abc:1mg\"]," +
        "\"allergies\":[\"pollen\"]" +
        "}";

    mvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(create))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.birthdate", is("01/01/2000")));

    String update = "{" +
        "\"firstName\":\"Test\"," +
        "\"lastName\":\"User\"," +
        "\"birthdate\":\"01/01/2000\"," +
        "\"medications\":[\"abc:2mg\"]," +
        "\"allergies\":[]" +
        "}";

    mvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).content(update))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.medications", hasItem("abc:2mg")))
        .andExpect(jsonPath("$.allergies", hasSize(0)));

    mvc.perform(delete("/medicalRecord").param("firstName", "Test").param("lastName", "User"))
        .andExpect(status().isNoContent());
  }
}
