package org.evolsw.waveblcards.controller.mappers.implementation;

import org.evolsw.waveblcards.controller.data.CardInput;
import org.evolsw.waveblcards.model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputToCardImplTest {

    @Test
    void map() {
        InputToCardImpl inputToCard = new InputToCardImpl();
        CardInput cardInput = new CardInput();
        String someAddress = "Some Address";
        String myName = "My name";
        cardInput.setAddress(someAddress);
        cardInput.setName(myName);
        Card mappedCard = inputToCard.map(cardInput, "A", "B");
        Card newCard = new Card();
        newCard.setAddress(someAddress);
        newCard.setName(myName);
        newCard.setSource("A");
        newCard.setState("B");
        assertEquals(mappedCard, newCard);
    }
}