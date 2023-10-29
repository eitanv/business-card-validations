package org.evolsw.waveblcards.controller.mappers;

import org.evolsw.waveblcards.controller.data.CardInput;
import org.evolsw.waveblcards.model.Card;

public interface InputToCard {
    public Card map(CardInput cardInput, String source, String state);
}
