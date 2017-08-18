package com.northgrum.irad.cds.phTriggers.util;

import com.northgrum.irad.cds.util.CSVReader;
import lombok.Data;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CSVReaderTest {
    CSVReader reader = new CSVReader();

    @Test
    public void readFileFromPath() throws Exception {
        List<String[]> result = reader.readFromPath("src/test/resources/testCSV.txt");
        printResults(result);
    }

    @Test
    public void readFileFromResource() throws IOException {
        List<String[]> result = reader.readFromResource("testCSV.txt");
        printResults(result);
    }

    @Test
    public void testReadFileIntoObject() throws IOException {
        List<Country> inputList = new ArrayList<>();
        inputList = reader.readFromResource("testcSV.txt", mapToItem);
        for (Country c: inputList) {
            System.out.println("c = " + c);
        }
    }

    private Function<String, Country> mapToItem = (line) -> {
        String[] p = line.split(CSVReader.CSV_SPLITTER_WITH_QUOTES);// a CSV has comma separated lines
        Country item = new Country();
        item.setId(p[0].trim().replaceAll("\"", ""));//<-- this is the first column in the csv file
        item.setCode(p[1].trim().replaceAll("\"", ""));
        item.setName(p[2].trim().replaceAll("\"", ""));
        //more initialization goes here
        return item;
    };

    private void printResults(List<String[]> result) {
        result.stream().forEach(l -> {
            for(String i: l) {
                System.out.print(i + "\t ");
            }
            System.out.println();
        });
    }

}
@Data
class Country {
    private String id;
    private String code;
    private String name;

}