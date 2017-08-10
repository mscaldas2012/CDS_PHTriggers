package com.ng.cds.phTriggers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ng.cds.phTriggers.model.CDSServiceList;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.restassured.RestAssured.when;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CDSServicesControllerTest {


    @Value("${server.contextPath}")
    private String rootAPIURL ;//= "/cds-services";
    @Test
    public void testGetListOfServices() throws Exception {
//        when().
//                get(this.rootAPIURL + "/").
//                then().
//                statusCode(200).
//                body(containsString("code_reportability"));
        Response response = when().get(this.rootAPIURL );
        assertTrue(response.statusCode() == 200);
        //System.out.println("response.asString() = " + response.asString());
        ObjectMapper mapper = new ObjectMapper();
        CDSServiceList services = mapper.readValue(response.asString(), CDSServiceList.class);
        assertTrue(services != null) ;
        assertTrue(services.getServices() != null && services.getServices().size() == 1);
        assertTrue(services.getServices().get(0).getId().equals("code_reportability"));
    }

    @Autowired
    CDSServiceList info;

    @Test
    public void printServices() {
        System.out.println("info: " + info);
    }


}