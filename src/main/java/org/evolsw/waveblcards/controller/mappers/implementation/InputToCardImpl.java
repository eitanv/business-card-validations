package org.evolsw.waveblcards.controller.mappers.implementation;

import org.evolsw.waveblcards.controller.dto.CardInput;
import org.evolsw.waveblcards.controller.mappers.InputToCard;
import org.evolsw.waveblcards.model.Card;
import org.springframework.stereotype.Service;

@Service
public class InputToCardImpl implements InputToCard {
    public Card map(CardInput cardInput, String source, String state)
    {
        Card card = new Card();
        card.setSource(source);
        card.setState(state);
        card.setName(cardInput.getName());
        card.setAddress(cardInput.getAddress());
        return card;
    }
}
