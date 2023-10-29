package org.evolsw.waveblcards.controller.rest;

import org.evolsw.waveblcards.controller.data.CardInput;
import org.evolsw.waveblcards.controller.data.ChangeStateInput;
import org.evolsw.waveblcards.controller.data.StateMachineData;
import org.evolsw.waveblcards.controller.mappers.implementation.InputToCardImpl;
import org.evolsw.waveblcards.controller.services.CardServices;
import org.evolsw.waveblcards.controller.services.RandomServices;
import org.evolsw.waveblcards.controller.services.StateServices;
import org.evolsw.waveblcards.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final Logger logger = LoggerFactory.getLogger(CardController.class);
    CardServices cardServices;
    InputToCardImpl inputToCard;
    StateServices stateServices;
    RandomServices randomServices;

    public CardController(CardServices cardServices, InputToCardImpl inputToCard, StateServices stateServices, RandomServices randomServices) {
        this.cardServices = cardServices;
        this.inputToCard = inputToCard;
        this.stateServices = stateServices;
        this.randomServices = randomServices;
    }

    @GetMapping("/")
    ResponseEntity<List<Card>> getAllCards() {
        logger.info("{GET [/cards/]}: getAllCards() REST called");
        List<Card> allCards = cardServices.loadAll();
        logger.debug("getAllCards() : All cards: " + Arrays.toString(allCards.toArray()));
        return new ResponseEntity<>(allCards, HttpStatus.OK);
    }

    @PostMapping("/trusted")
    ResponseEntity<Card> addTrustedCard(@RequestBody CardInput cardInput) {
        logger.info("{POST [/cards/trusted]}: addTrustedCard(CardInput) REST called");
        logger.debug("addTrustedCard(CardInput) inputs: " + cardInput);
        Card newCard = inputToCard.map(cardInput, "T", "Known");
        newCard = cardServices.save(newCard);
        logger.debug("addTrustedCard(CardInput) New Card: " + newCard);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @PostMapping("/untrusted")
    ResponseEntity<Card> addUntrustedCard(@RequestBody CardInput cardInput) {
        logger.info("{POST [/cards/untrusted]}: addUntrustedCard(CardInput) REST called");
        logger.debug("addUntrustedCard(CardInput) inputs: " + cardInput);
        Card newCard = inputToCard.map(cardInput, "U", "Unknown");
        newCard = cardServices.save(newCard);
        logger.debug("addUntrustedCard(CardInput) New Card: " + newCard);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @PutMapping("/card/state")
    ResponseEntity<Card> changeState(@RequestBody ChangeStateInput changeStateInput) {
        logger.info("{PUT [/cards/card/state]}: changeState(ChangeStateInput) REST called");
        logger.debug("changeState(ChangeStateInput) inputs: " + changeStateInput);
        Card card = cardServices.load(changeStateInput.getCardId());
        if (!stateServices.verifyNewState(new StateMachineData(card.getState(), changeStateInput.getNewState(), card.getSource()))) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        card.setState(changeStateInput.getNewState());
        Long lastVerificationCode = randomServices.generateVerificationCode();
        card.setLastVerificationCode(lastVerificationCode);
        logger.info("changeState(ChangeStateInput) verification code: " + lastVerificationCode);
        card = cardServices.save(card);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
}
