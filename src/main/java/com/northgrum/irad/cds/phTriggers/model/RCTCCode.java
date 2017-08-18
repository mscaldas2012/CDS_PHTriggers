package com.northgrum.irad.cds.phTriggers.model;


import lombok.Data;

import java.util.List;

@Data
public class RCTCCode {

    private String code;
    private String descriptor;
    private String codeSystem;
    private String version;
    private String status;
    private String remapInfo;
    private String[] cdsHookEvents;
}
