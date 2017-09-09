/**
 * Copyright notice (c) 2017 Northrop Grumman Services Corporation
 */

package com.northgrum.irad.cds.phTriggers.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Card {
    private String summary;
    private String detail;
    private String indicator = "info";
    private List<Suggestion> suggestions = new ArrayList<>();
    private Source source = new Source();

}

@Data
class Suggestion {
    private String label;
    private String uuid;
    private List<Action> actions;
}

@Data
class Action {
    private String type = "create";
    private String description ;
    private String resources;
}

@Data
class Source {
    private String label = "PHTriggers RCTC Service";
}
