package org.evolsw.waveblcards.controller.services.implementation;

import org.evolsw.waveblcards.model.Card;
import org.evolsw.waveblcards.model.jpa.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CardServicesImplTest {
    private final CardRepository cardRepository = Mockito.mock(CardRepository.class);

    private CardServicesImpl cardServicesImpl;

    private Card newCard;
    private Card anotherCard;

    final String someAddress = "Some Address";
    final String myName = "My name";

    @BeforeEach
    void initUseCase() {
        cardServicesImpl = new CardServicesImpl(cardRepository);
        newCard = new Card();
        newCard.setAddress(someAddress);
        newCard.setName(myName);
        newCard.setSource("A");
        newCard.setState("B");
        newCard.setCardId(10001L);
        newCard.setLastVerificationCode(null);

        anotherCard = new Card();
        anotherCard.setAddress(someAddress);
        anotherCard.setName(myName);
        anotherCard.setSource("T");
        anotherCard.setState("Known");
        anotherCard.setCardId(10002L);
        anotherCard.setLastVerificationCode(null);
    }

    @Test
    void loadAllEmpty() {
        List<Card> all = cardServicesImpl.loadAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void loadAllOne() {
        List<Card> one = new LinkedList<>();
        one.add(newCard);
        when(cardRepository.findAll()).thenReturn(one);

        List<Card> all = cardServicesImpl.loadAll();
        assertEquals(all, one);
    }

    @Test
    void loadAllMany() {
        List<Card> many = new LinkedList<>();
        many.add(newCard);
        many.add(anotherCard);
        when(cardRepository.findAll()).thenReturn(many);

        List<Card> all = cardServicesImpl.loadAll();
        assertEquals(all, many);
    }

    @Test
    void load() {
        when(cardRepository.getReferenceById(10001L)).thenReturn(newCard);
        assertEquals(cardServicesImpl.load(10001L), newCard);
    }

    @Test
    void loadDoesntExists() {
        when(cardRepository.getReferenceById(anyLong())).thenReturn(null);
        assertNull(cardServicesImpl.load(10001L));
    }

    @Test
    void save() {
        when(cardRepository.save(any(Card.class))).thenReturn(newCard);

        assertEquals(cardServicesImpl.save(newCard), newCard);
    }

}