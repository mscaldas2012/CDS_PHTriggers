package com.northgrum.irad.cds.phTriggers.util;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import net.sf.saxon.functions.Collection;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestJSONPath {


    String jsonpathCreatorNamePath = "$['tool']['jsonpath']['creator']['name']";
    String jsonpathCreatorLocationPath = "$['tool']['jsonpath']['creator']['location'][*]";

    @Test
    public void testSimpleFile() throws IOException {
        String jsonDataSourceString = readFile("quickJsonExample.txt");
        //Creating parser
        DocumentContext jsonContext = JsonPath.parse(jsonDataSourceString);
        //Reading some stuff
        String jsonpathCreatorName = jsonContext.read(jsonpathCreatorNamePath);
        List<String> jsonpathCreatorLocation = jsonContext.read(jsonpathCreatorLocationPath);
        //printing the stuff:
        System.out.println("jsonpathCreatorName = " + jsonpathCreatorName);
        for(String s: jsonpathCreatorLocation) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void testCodeExtractions() {
        String payload = readFile("phTrigger-payload.txt");
        DocumentContext jsonContext = JsonPath.parse(payload);
        try {
            String observation = jsonContext.read("$['resource']['resourceType']");
            assertTrue("observation".equalsIgnoreCase(observation));
            String lab = jsonContext.read("$['resource']['category']['text']");
            assertTrue("laboratory".equalsIgnoreCase(lab));

            List<String> codes = jsonContext.read("$['resource']['code']['coding'][*]['code']");
            //codes.forEach(System.out::println);
            String concat = codes.stream().map(Object::toString).collect(Collectors.joining(","));
            System.out.println("concat = " + concat);
        } catch (PathNotFoundException e) {
            fail("Path supposed to be there on test file.");
        }

    }

    public static String readFile(String file)  {
        try (InputStream is = new TestJSONPath().getClass().getClassLoader().getResourceAsStream(file);
            Scanner scanner = new Scanner(is)) {
               return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
