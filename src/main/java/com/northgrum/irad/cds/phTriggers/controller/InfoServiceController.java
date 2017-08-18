package com.northgrum.irad.cds.phTriggers.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northgrum.irad.cds.phTriggers.About;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by marcelo on 10/4/16.
 */

@RestController
@RequestMapping("/info/")
//@ApiVersion({1,})
public class InfoServiceController {
    @Autowired
    private About about;

    @RequestMapping(value="/about")
    @ResponseBody
    public About about() throws JsonProcessingException {
        return about;
    }
    @RequestMapping(value="/version")
    @ResponseBody
    public String version() {
        return "1.0";
    }

    @RequestMapping("/ping")
    public String ping() {
        return "Hello There! I'm alive.\nYou pinged me at " + ZonedDateTime.now().format( DateTimeFormatter.ISO_INSTANT );
    }



}

