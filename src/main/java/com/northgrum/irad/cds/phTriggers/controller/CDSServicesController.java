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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/cds-services")
public class CDSServicesController {

    private Log log = LogFactory.getLog(CDSServicesController.class);


    private static final String LAB_TEST_OBESRVATION_PATH = "$['resource']['resourceType']";
    private static final String LAB_TEST_LABORATORY_CATEGORY_PATH = "$['resource']['category']['text']";
    private static final String LAB_TEST_CODING_PATH = "$['resource']['code']['coding'][*]['code']";

    private static final String OBSERVATION = "observation";
    private static final String LABORATORY = "laboratory";
    public static final String LAB_OBS_TEST_NAME = "Lab obs Test Name";


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
    @RequestMapping(value = "phtriggers_rctc", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity checkCodeReportability(@RequestBody String body)  {
        CDSResponse response = new CDSResponse();
        try {
            Card card = new Card();
            DocumentContext jsonContext = JsonPath.parse(body);
            List<RCTCCode> matchingCodes = checkLabTestCodes(jsonContext);
            if (matchingCodes != null && matchingCodes.size() > 0 ) {
                card.setSummary("This case is Possibly Reportable. It should be sent as an eICR to your Public Health intermediary for final Adjudication");
                card.setDetail("The following codes were found as candidates: " + matchingCodes.stream().map(m -> m.getCode()).collect(Collectors.joining(",")));
            } else {
                card.setSummary("This case is Not Reportable. No Action is required");
                card.setDetail("No codes that flag reports were found.");
            }
            response.addCard(card);
        } catch (PathNotFoundException e) {
            Card card = new Card();
            card.setSummary("This case is Not Reportable. No Action is required");
            card.setDetail("No codes that flag reports were found.");
            response.addCard(card);
        } catch (JsonPathException e) {
            ErrorMessage error = new ErrorMessage("BAD_PAYLOAD", "Unable to process payload!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage("Ooops!", "Unable to process request!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        return ResponseEntity.ok(response);
    }

    private List<RCTCCode> checkLabTestCodes(DocumentContext jsonContext) {
        List<RCTCCode> result = new ArrayList<>();
        String observation = jsonContext.read(LAB_TEST_OBESRVATION_PATH);
        if (OBSERVATION.equalsIgnoreCase(observation)) {
            String lab = jsonContext.read(LAB_TEST_LABORATORY_CATEGORY_PATH);
            if (LABORATORY.equalsIgnoreCase(lab)) {
                List<String> codes = jsonContext.read(LAB_TEST_CODING_PATH);
                for (String code : codes) {
                    RCTCCode match = rctcServices.getCode(LAB_OBS_TEST_NAME, code);
                    if (match != null) result.add(match);
                }

            }
        }
        return result;
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
    public ResponseEntity<ErrorMessage> resolveException(HttpServletRequest request) {
        ErrorMessage error = new ErrorMessage("BAD_REQUEST", "Unable to process payload");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
