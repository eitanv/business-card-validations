package org.evolsw.waveblcards.controller.services;

import org.evolsw.waveblcards.model.Card;

import java.util.List;

public interface CardServices {

    List<Card> loadAll();

    Card load(Long id);

    public Card save(Card card);
}
