/**
 * Copyright notice (c) 2017 Northrop Grumman Services Corporation
 */
package com.northgrum.irad.cds.phTriggers.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.PathNotFoundException;
import com.northgrum.irad.cds.phTriggers.model.*;
import com.northgrum.irad.cds.phTriggers.service.RCTCCodeServices;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
//@RequestMapping("/cds-services")
public class CDSServicesController {

    private Log log = LogFactory.getLog(CDSServicesController.class);


    private static final String LAB_TEST_OBESRVATION_PATH = "$..resourceType";
    private static final String LAB_TEST_LABORATORY_CATEGORY_PATH = "$..category.coding[*].code";
    private static final String LAB_TEST_CODING_PATH = "$..code.coding[*].code";

    private static final String FHIR_SERVER_PATH = "$.fhirServer";
    private static final String PATIENT_ID_PATH = "$.patient";

    private static final String OBSERVATION = "observation";
    private static final String LABORATORY = "laboratory";
    public static final String LAB_OBS_TEST_NAME = "LAB_OBS_TEST_NAME";


    @Autowired
    private CDSServiceList services;

    @Autowired
    private RCTCCodeServices rctcServices;

    @RequestMapping("/")
    @ResponseBody
    public ResponseEntity<CDSServiceList> getListOfServices(@Context HttpServletRequest request)  throws JsonProcessingException {
        log.info("AUDIT - " + request.getRemoteAddr() + " list of Services called.");
        return ResponseEntity.ok(services );
    }

    /**
     * This method is one of the Services we implement as a cds-hook.
     * It receives a message and scans it for codes then validates those codes against a set list to determine
     * if this code should be reported to Public Health Entities or not.
     *
     * @return
     */
    @RequestMapping(value = "phtriggers_rctc", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity checkCodeReportability(@RequestBody String body, @Context HttpServletRequest request)  {
        log.info("AUDIT - " + request.getRemoteAddr() + " phtriggers_rctc service called.");
        CDSResponse response = new CDSResponse();
        try {
            Card card = new Card();
            DocumentContext jsonContext = JsonPath.parse(body);
            List<RCTCCode> matchingCodes = checkLabTestCodes(jsonContext);
            if (matchingCodes != null && matchingCodes.size() > 0 ) {
                card.setSummary("Match found in RCTC");
                card.setDetail("This encounter event includes a code that matched a reportable condition trigger code. An eICR should be sent to Public Health/Intermediary for secondary adjudication");
                response.addCard(card);
            }
            //Get Patient info and log it:
            try {
                String fhiServer = jsonContext.read(FHIR_SERVER_PATH);
                if (fhiServer != null ) {
                    String patient = jsonContext.read(PATIENT_ID_PATH);
                    if (patient != null) { //Calling method asynchronously to return results faster...
                        new Thread(() -> {
                            log.info("Patient Info: " + getPatientInfo(fhiServer+ "/Binary/$autogen-ccd-if?patient=" + patient).substring(0, 200) + "...");
                        }).start();
                    }
                }
            } catch (PathNotFoundException e) {
                log.warn("FHIR Server ULR or Patient not found.");

            }
        } catch (JsonPathException e) {
            e.printStackTrace();
            log.error("Unable to parse JSON Sent to phtriggers_rctc. Sending Bad Payload error to user");
            ErrorMessage error = new ErrorMessage("BAD_PAYLOAD", "Unable to process payload!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unable to process request to phtriggers_rctc. Sending Internal Server Error to user");
            ErrorMessage error = new ErrorMessage("Ooops!", "Unable to process request!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        return ResponseEntity.ok(response);
    }

    private List<RCTCCode> checkLabTestCodes(DocumentContext jsonContext) {
        List<RCTCCode> result = new ArrayList<>();
        List<String> observations = jsonContext.read(LAB_TEST_OBESRVATION_PATH);
        if (observations != null && observations.size() > 0 && OBSERVATION.equalsIgnoreCase(observations.get(0))) {
            List<String> labs = jsonContext.read(LAB_TEST_LABORATORY_CATEGORY_PATH);
            if (labs != null && labs.size() > 0 && LABORATORY.equalsIgnoreCase(labs.get(0))) {
                List<String> codes = jsonContext.read(LAB_TEST_CODING_PATH);
                for (String code : codes) {
                    RCTCCode match = rctcServices.getCode(LAB_OBS_TEST_NAME, code);
                    if (match != null) result.add(match);
                }

            } else {
                log.info("No Laboratory node found");
            }
        } else {
            log.info ("No obsevation node found");
        }
        return result;
    }

    private String getPatientInfo(String url) {
        log.info("Calling " + url);
        HttpHeaders headers = new HttpHeaders();
        MediaType mt = new MediaType("application", "json+fhir");

        headers.setAccept(Collections.singletonList(mt));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
        //This didn't work to add the Accept header:
        //return restTemplate . getForObject(url, String.class, entity);

    }

    @RequestMapping(value="/code/{category}")
    public List<RCTCCode> getCodesByCategory(@PathVariable String category) {
        return rctcServices.getCodesByCategory(category);
    }

    @RequestMapping(value = "/code/{category}/{code_id}")
    public RCTCCode getCode(@PathVariable String category, @PathVariable String code_id) {
        return rctcServices.getCode(category, code_id);

    }

//    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @RequestMapping("/error")
//    public ResponseEntity<ErrorMessage> resolveException(HttpServletRequest request) {
//        ErrorMessage error = new ErrorMessage("BAD_REQUEST", "Unable to process payload");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
}
