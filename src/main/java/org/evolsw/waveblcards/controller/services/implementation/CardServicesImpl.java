package org.evolsw.waveblcards.controller.services.implementation;

import org.evolsw.waveblcards.controller.services.CardServices;
import org.evolsw.waveblcards.model.Card;
import org.evolsw.waveblcards.model.jpa.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServicesImpl implements CardServices {

    CardRepository cardRepository;

    public CardServicesImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> loadAll() {
        return cardRepository.findAll();
    }

    @Override
    public Card load(Long id) {
        return cardRepository.getReferenceById(id);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

}
