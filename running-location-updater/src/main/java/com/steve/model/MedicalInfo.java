package com.steve.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MedicalInfo {
    private Long medicalInfoId;
    private String bandMake;
    private String medicalInfoClassification;
    private String description;
    private String aidInstructions;
}
