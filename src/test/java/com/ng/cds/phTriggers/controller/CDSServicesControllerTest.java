package com.ng.cds.phTriggers.controller;

import com.ng.cds.phTriggers.model.CDSServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CDSServicesControllerTest {
    @Test
    public void getListOfServices() throws Exception {
    }


    @Autowired
    CDSServiceList info;

    @Test
    public void printServices() {
        System.out.println("info: " + info);
    }


}