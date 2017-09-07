/**
 * Copyright notice (c) 2017 Northrop Grumman Services Corporation
 */
package com.northgrum.irad.cds.phTriggers.service;

import com.northgrum.irad.cds.phTriggers.model.RCTCCode;
import com.northgrum.irad.cds.phTriggers.repository.RCTCCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RCTCCodeServices {

    @Autowired
    private RCTCCodesRepository repo;

    public RCTCCode getCode(String category, String code) {
        return repo.getCode(category, code);
    }

    public List<RCTCCode> getCodesByCategory(String category) {
        return repo.getCodesByCategory(category);
    }
}
