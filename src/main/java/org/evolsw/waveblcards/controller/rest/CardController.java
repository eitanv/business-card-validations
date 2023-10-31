package org.evolsw.waveblcards.controller.rest;

import org.evolsw.waveblcards.controller.consts.SourceConsts;
import org.evolsw.waveblcards.controller.consts.StateConsts;
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
    final CardServices cardServices;
    final InputToCardImpl inputToCard;
    final StateServices stateServices;
    final RandomServices randomServices;

    public CardController(CardServices cardServices, InputToCardImpl inputToCard, StateServices stateServices, RandomServices randomServices) {
        this.cardServices = cardServices;
        this.inputToCard = inputToCard;
        this.stateServices = stateServices;
        this.randomServices = randomServices;
    }

    @GetMapping("/")
    ResponseEntity<List<Card>> getAllCards() {
        logger.info("{GET [/cards/]} REST called");
        List<Card> allCards = cardServices.loadAll();
        logger.debug("getAllCards() : All cards are " + Arrays.toString(allCards.toArray()));
        return new ResponseEntity<>(allCards, HttpStatus.OK);
    }

    @PostMapping("/trusted")
    ResponseEntity<Card> addTrustedCard(@RequestBody CardInput cardInput) {
        logger.info("{POST [/cards/trusted]} REST called");
        Card newCard = createCard(cardInput, true);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @PostMapping("/untrusted")
    ResponseEntity<Card> addUntrustedCard(@RequestBody CardInput cardInput) {
        logger.info("{POST [/cards/untrusted]} REST called");
        Card newCard = createCard(cardInput, false);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    private Card createCard(CardInput cardInput, boolean isTrusted) {
        final String METHOD_NAME = isTrusted ? "addTrustedCard(CardInput)" : "addUnTrustedCard(CardInput)";
        logger.debug(METHOD_NAME + " inputs: " + cardInput);

        Card newCard = inputToCard.map(cardInput, isTrusted ? SourceConsts.TRUSTED_SOURCE : SourceConsts.UNTRUSTED_SOURCE, isTrusted ? StateConsts.KNOWN : StateConsts.UNKNOWN);
        cardServices.save(newCard);

        logger.debug(METHOD_NAME + " New Card: " + newCard);
        return newCard;
    }

    @PutMapping("/card/state")
    ResponseEntity<Card> changeState(@RequestBody ChangeStateInput changeStateInput) {
        logger.info("{PUT [/cards/card/state]} REST called");
        logger.debug("changeState(ChangeStateInput) inputs: " + changeStateInput);

        Card card = cardServices.load(changeStateInput.getCardId());
        if (!stateServices.verifyNewState(new StateMachineData(card.getState(), changeStateInput.getNewState(), card.getSource()))) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        card.setState(changeStateInput.getNewState());
        Long lastVerificationCode = randomServices.generateVerificationCode();
        card.setLastVerificationCode(lastVerificationCode);
        logger.debug("changeState(ChangeStateInput) verification code: " + lastVerificationCode);
        Card newCard = cardServices.save(card);
        logger.info("changeState(ChangeStateInput) Change card: " + newCard);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }
}
