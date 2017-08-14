package com.ng.cds.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;



public class CSVReader<T> {

    public static final String CSV_SPLITTER_WITH_QUOTES = ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

    public  List<T> readFromResource(String filename) throws IOException {
        CSVReader r = new CSVReader();
        InputStream is = r.getClass().getClassLoader().getResourceAsStream(filename);
        return readFile(is, defaultMapping);
    }

    public  List<T> readFromPath(String filename) throws IOException {
        InputStream is = new FileInputStream(new File(filename));
        return readFile(is, defaultMapping);
    }

    //Read directly into objects passing fucntio:
    public  List<T> readFromResource(String filename, Function f) throws IOException {
        //CSVReader r = new CSVReader();
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        return readFile(is, f);
    }


    protected  List<T> readFile(InputStream is, Function f) throws IOException {
        //List<String[]> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        // skip the header of the csv: br.lines().skip(1);
        List result = new ArrayList<>();
        result = (List) br.lines().map(f).collect(Collectors.toList());
        br.close();
        return result;
    }

    private  Function<String, String[]> defaultMapping = (line) -> line.split(CSV_SPLITTER_WITH_QUOTES);

}
