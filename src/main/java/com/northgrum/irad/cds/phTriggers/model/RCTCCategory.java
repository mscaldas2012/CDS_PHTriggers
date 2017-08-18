package com.northgrum.irad.cds.phTriggers.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RCTCCategory {
    private String category;
    private String rctcOid;
    private String definitionVersion;
    private String effectiveDate;

    List<RCTCCode> codes;

    public void addCode(RCTCCode item) {
        if (codes == null) {
            codes = new ArrayList<>();
        }
        codes.add(item);
    }
}
