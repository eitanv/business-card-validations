package org.evolsw.waveblcards.controller.rest;

import org.evolsw.waveblcards.controller.dto.CardInput;
import org.evolsw.waveblcards.controller.mappers.implementation.InputToCardImpl;
import org.evolsw.waveblcards.controller.services.CardServices;
import org.evolsw.waveblcards.controller.services.StateServices;
import org.evolsw.waveblcards.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    CardServices cardServices;

    @Autowired
    InputToCardImpl inputToCard;

    @Autowired
    StateServices stateServices;

    @GetMapping("/")
    ResponseEntity<List<Card>> getAllCards() {
        List<Card> allCards = cardServices.loadAll();
        System.out.println("All users: " + Arrays.toString(allCards.toArray()));
        return new ResponseEntity<>(allCards, HttpStatus.OK);
    }

    @PostMapping("/trusted")
    ResponseEntity<Card> addTrustedCard(@RequestBody CardInput cardInput) {
        Card newCard = inputToCard.map(cardInput, "T", "Known");
        newCard = cardServices.save(newCard);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @PostMapping("/untrusted")
    ResponseEntity<Card> addUntrustedCard(@RequestBody CardInput cardInput) {
        Card newCard = inputToCard.map(cardInput, "U", "Unknown");
        newCard = cardServices.save(newCard);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @PutMapping("/{id}/state={state}/")
    ResponseEntity<Card> changeState(@PathVariable Long id, @PathVariable String state) {
        Card card = cardServices.load(id);
        if (!stateServices.verifyNewState(id, state)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        card.setState(state);
        card = cardServices.save(card);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
}
