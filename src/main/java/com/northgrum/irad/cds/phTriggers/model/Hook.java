/**
 * Copyright notice (c) 2017 Northrop Grumman Services Corporation
 */
package com.northgrum.irad.cds.phTriggers.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Hook {
    @JsonProperty("patient-view")
    PATIENT_VIEW("patient-view"),
    @JsonProperty("medication-prescribe")
    MEDICATION_PRESCRIBE("medicaiton-prescribe"),
    @JsonProperty("order-view")
    ORDER_VIEW("order-view"),
    @JsonProperty("lab-order-create")
    LAB_ORDER_CREATE("lab-order-create");

    private String name;

    Hook(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

