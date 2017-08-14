package com.ng.cds.phTriggers.controller;

import com.ng.cds.phTriggers.About;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by marcelo on 10/4/16.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application.yml")
public class InfoServiceControllerTest {

    @Autowired
    private About about;

    private String rootAPIIURL;

    @Before
    public void setUp() throws Exception {
        this.rootAPIIURL = "cds-services";

    }

    @Test
    public void about() throws Exception {
        when().
                get(this.rootAPIIURL + "info/about").
        then().
                statusCode(200).
                body("summary", containsString("Spring REST services"));
    }

    @Test
    public void printAbout() throws Exception {
        System.out.println("about = " + about);
    }

}