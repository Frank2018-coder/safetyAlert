package com.safetynet.alerts.dto;

import java.util.List;

public record PersonInfoDTO(
    String firstName,
    String lastName,
    String address,
    int age,
    String email,
    List<String> medications,
    List<String> allergies
) {}
