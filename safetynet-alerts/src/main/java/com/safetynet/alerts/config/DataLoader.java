package com.safetynet.alerts.config;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.DataStore;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {
  private static final Logger log = LogManager.getLogger(DataLoader.class);

  private final ObjectMapper objectMapper;
  private final DataStoreHolder holder;

  public DataLoader(ObjectMapper objectMapper, DataStoreHolder holder) {
    this.objectMapper = objectMapper;
    this.holder = holder;
  }

  @PostConstruct
  public void load() throws IOException {
    var resource = new ClassPathResource("data.json");
    DataStore dataStore = objectMapper.readValue(resource.getInputStream(), DataStore.class);
    holder.setDataStore(dataStore);
    log.info("Loaded data.json: {} persons, {} firestation mappings, {} medical records", 
        dataStore.getPersons().size(), dataStore.getFirestations().size(), dataStore.getMedicalrecords().size());
  }

}
