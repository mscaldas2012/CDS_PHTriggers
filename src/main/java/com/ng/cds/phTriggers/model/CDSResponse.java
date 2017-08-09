package com.ng.cds.phTriggers.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CDSResponse {
    public List<Card> cards;
    public Map<String, String> decisions;

}
