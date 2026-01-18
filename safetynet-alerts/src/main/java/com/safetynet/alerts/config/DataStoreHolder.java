package com.safetynet.alerts.config;

import org.springframework.stereotype.Component;

import com.safetynet.alerts.model.DataStore;

@Component
public class DataStoreHolder {
  private DataStore dataStore = new DataStore();

  public DataStore getDataStore() {
    return dataStore;
  }

  public void setDataStore(DataStore dataStore) {
    this.dataStore = dataStore;
  }
}
