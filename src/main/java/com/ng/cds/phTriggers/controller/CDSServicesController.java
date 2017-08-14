package com.ng.cds.phTriggers.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ng.cds.phTriggers.model.CDSResponse;
import com.ng.cds.phTriggers.model.CDSServiceList;
import com.ng.cds.phTriggers.model.RCTCCode;
import com.ng.cds.phTriggers.service.RCTCCodeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/cds-services")
public class CDSServicesController {

    @Autowired
    private CDSServiceList services;



    @Autowired
    private RCTCCodeServices rctcServices;

    @RequestMapping("/")
    @ResponseBody
    public ResponseEntity<CDSServiceList> getListOfServices()  throws JsonProcessingException {
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
    public CDSResponse checkCodeReportability(@RequestBody String body) {
        CDSResponse response = new CDSResponse();
        //List decisions = new ArrayList();
        Map<String, String> decisions = new HashMap<>();
        decisions.put("code", "789-6");
        decisions.put("reportability", "positive");

        response.setDecisions(decisions);

        return response;
    }

    @RequestMapping(value="/code/{category}")
    public List<RCTCCode> getCodesByCategory(@PathVariable String category) {
        return rctcServices.getCodesByCategory(category);
    }

    @RequestMapping(value = "/code/{category}/{code_id}")
    public RCTCCode getCode(@PathVariable String category, @PathVariable String code_id) {
        return rctcServices.getCode(category, code_id);

    }
}
