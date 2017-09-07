/**
 * Copyright notice (c) 2017 Northrop Grumman Services Corporation
 */
package com.northgrum.irad.cds.phTriggers.repository;

import com.northgrum.irad.cds.phTriggers.model.RCTCCategory;
import com.northgrum.irad.cds.phTriggers.model.RCTCCode;
import com.northgrum.irad.cds.util.CSVReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RCTCCodesRepository {

    private Map<String, RCTCCategory> cache = new HashMap<>();

    //@Value("${triggersConfig.rctc_codes}")
    private String codeFileName = "rctc_codes.csv";

    private CSVReader reader = new CSVReader();

    public RCTCCodesRepository() {
        try {
            //TODO::Read file from external path to pass codes outside Jar..
            this.reader.readFromResource(codeFileName, mapToItem);
        } catch (IOException e) {
            System.out.println("Unable to start repository - File not provided or Can'f find file - " + codeFileName);
        }
    }


    public RCTCCode getCode(String category, String code) {
        RCTCCategory cat = cache.get(category);
        if (cat != null && cat.getCodes() != null && !cat.getCodes().isEmpty()) {
            List<RCTCCode> codes = cat.getCodes().stream().filter(r -> r.getCode().equals(code)).collect(Collectors.toList());
            if (codes != null && codes.size() > 0)
                return codes.get(0);
        }
        return null;
    }


    private Function<String, RCTCCode> mapToItem = (line) -> {
        String[] rec = line.split(CSVReader.CSV_SPLITTER_WITH_QUOTES);
        RCTCCode item = new RCTCCode();
        String cat = rec[0].trim().replaceAll("\"", "");
        if (cache.get(cat) == null) {
            RCTCCategory category = new RCTCCategory();
            category.setCategory(cat);
            category.setRctcOid(rec[7].trim().replaceAll("\"", ""));
            category.setDefinitionVersion(rec[8].trim().replaceAll("\"", ""));
            category.setEffectiveDate(rec[9].trim().replaceAll("\"", ""));
            cache.put(cat, category);
        }
        item.setCode(rec[1].trim().replaceAll("\"", ""));//<-- this is the first column in the csv file
        item.setDescriptor(rec[2].trim().replaceAll("\"", ""));
        item.setCodeSystem(rec[3].trim().replaceAll("\"", ""));
        item.setVersion(rec[4]);
        item.setStatus(rec[5]);
        item.setRemapInfo(rec[6]);
        item.setCdsHookEvents(rec[10].split(",'"));
        cache.get(cat).addCode(item);
        //more initialization goes here
        return item;
    };

    public List<RCTCCode> getCodesByCategory(String category) {
        return cache.get(category).getCodes();
    }
}
