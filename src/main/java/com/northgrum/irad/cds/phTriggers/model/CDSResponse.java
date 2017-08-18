package com.northgrum.irad.cds.phTriggers.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class CDSResponse {
    public List<Card> cards;
    public Map<String, String> decisions;

    public void addCard(Card card) {
        if (cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
    }
}
