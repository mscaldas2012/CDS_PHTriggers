package com.ng.cds.phTriggers.repository;

import com.ng.cds.util.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RCTCCodesRepository {

    @Value("${triggersConfig.rctc_codes}")
    private String codeFileName;

    private CSVReader reader = new CSVReader();

    public List<String[]> readCodes() throws IOException {
        return reader.readFromResource(codeFileName);
    }
}
