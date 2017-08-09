package com.ng.cds.phTriggers.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ng.cds.phTriggers.model.CDSResponse;
import com.ng.cds.phTriggers.model.CDSServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/cds-services")
public class CDSServicesController {

    @Autowired
    private CDSServiceList services;

    @RequestMapping("/")
    @ResponseBody
    public ResponseEntity getListOfServices()  throws JsonProcessingException {
        return ResponseEntity.ok(services );
    }

    /**
     * This method is one of the Services we implement as a cds-hook.
     * It receives a message and scans it for codes then validates those codes against a set list to determine
     * if this code should be reported to Public Health Entities or not.
     *
     * @return
     */
    @RequestMapping(value = "code_reportability", method = RequestMethod.POST)
    @ResponseBody
    public CDSResponse checkCodes() {
        CDSResponse response = new CDSResponse();
        //List decisions = new ArrayList();
        Map<String, String> decisions = new HashMap();
        decisions.put("code", "789-6");
        decisions.put("reportability", "positive");

        response.setDecisions(decisions);

        return response;
    }
}
