package com.northgrum.irad.cds.phTriggers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northgrum.irad.cds.phTriggers.model.CDSResponse;
import com.northgrum.irad.cds.phTriggers.model.CDSServiceList;
import com.northgrum.irad.cds.phTriggers.util.TestJSONPath;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CDSServicesControllerTest {


    //@Value("${server.contextPath}")
    private String rootAPIURL = "http://localhost:10005/cds-services";
    @Test
    public void testGetListOfServices() throws Exception {
        Response response = when().get(this.rootAPIURL );
        assertTrue(response.statusCode() == 200);
        //System.out.println("response.asString() = " + response.asString());
        ObjectMapper mapper = new ObjectMapper();
        CDSServiceList services = mapper.readValue(response.asString(), CDSServiceList.class);
        assertTrue(services != null) ;
        assertTrue(services.getServices() != null && services.getServices().size() == 1);
        assertTrue(services.getServices().get(0).getId().equals("phtriggers_rctc"));
        assertTrue(services.getServices().get(0).getHook().getName().equals("lab-order-create"));
    }


    @Test
    public void testMatchCodes() throws IOException {
        String payload = TestJSONPath.readFile("phTrigger-payload.txt");

        Response response =
                given()
                        .contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post(this.rootAPIURL + "/phtriggers_rctc");

        assertTrue(response.statusCode() == 200);
        //System.out.println("response.asString() = " + response.asString());
        ObjectMapper mapper = new ObjectMapper();
        CDSResponse cards = mapper.readValue(response.asString(), CDSResponse.class);
        assertTrue(cards != null);
        assertTrue(cards.getCards() != null && cards.getCards().size() == 1);
        assertTrue(cards.getCards().get(0).getSummary().contains("Possibly Reportable"));

    }


    @Autowired
    CDSServiceList info;

    @Test
    public void printServices() {
        System.out.println("info: " + info);
    }


}