package org.evolsw.waveblcards.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.evolsw.waveblcards.controller.data.CardInput;
import org.evolsw.waveblcards.controller.data.ChangeStateInput;
import org.evolsw.waveblcards.controller.data.StateMachineData;
import org.evolsw.waveblcards.controller.mappers.implementation.InputToCardImpl;
import org.evolsw.waveblcards.controller.services.implementation.CardServicesImpl;
import org.evolsw.waveblcards.controller.services.implementation.RandomServicesImpl;
import org.evolsw.waveblcards.controller.services.implementation.StateServicesImpl;
import org.evolsw.waveblcards.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {

    final String someAddress = "Some Address";
    final String myName = "My name";
    @MockBean
    CardServicesImpl cardServices;
    @MockBean
    InputToCardImpl inputToCard;
    @MockBean
    StateServicesImpl stateServices;
    @MockBean
    RandomServicesImpl randomServices;
    Card untrustedCard;
    Card untrustedCardChanged;
    Card trustedCard;
    Card trustedCardChanged;
    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        trustedCard = new Card();
        trustedCard.setAddress(someAddress);
        trustedCard.setName(myName);
        trustedCard.setSource("T");
        trustedCard.setState("Known");
        trustedCard.setCardId(10001L);
        trustedCard.setLastVerificationCode(null);

        trustedCardChanged = new Card();
        trustedCardChanged.setAddress(someAddress);
        trustedCardChanged.setName(myName);
        trustedCardChanged.setSource("T");
        trustedCardChanged.setState("Manual Approved");
        trustedCardChanged.setCardId(10001L);
        trustedCardChanged.setLastVerificationCode(900000005L);

        untrustedCard = new Card();
        untrustedCard.setAddress(someAddress + "2");
        untrustedCard.setName(myName + "2");
        untrustedCard.setSource("U");
        untrustedCard.setState("UnKnown");
        untrustedCard.setCardId(10002L);
        untrustedCard.setLastVerificationCode(null);

        untrustedCardChanged = new Card();
        untrustedCardChanged.setAddress(someAddress + "2");
        untrustedCardChanged.setName(myName + "2");
        untrustedCardChanged.setSource("U");
        untrustedCardChanged.setState("Pending Verification");
        untrustedCardChanged.setCardId(10002L);
        untrustedCardChanged.setLastVerificationCode(900000004L);
    }

    @Test
    void getAllCards() throws Exception {

        List<Card> many = new LinkedList<>();
        many.add(trustedCard);
        many.add(untrustedCard);
        when(cardServices.loadAll()).thenReturn(many);

        mvc.perform(MockMvcRequestBuilders.get("/cards/").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].cardId").value(10001)).andExpect(MockMvcResultMatchers.jsonPath("$[1].cardId").value(10002));
    }

    @Test
    void getAllCardsEmptyList() throws Exception {

        List<Card> empty = new LinkedList<>();
        when(cardServices.loadAll()).thenReturn(empty);

        mvc.perform(MockMvcRequestBuilders.get("/cards/").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void addTrustedCard() throws Exception {

        when(inputToCard.map(any(CardInput.class), anyString(), anyString())).thenReturn(trustedCard);
        trustedCard.setLastVerificationCode(900000001L);
        when(cardServices.save(any(Card.class))).thenReturn(trustedCard);

        CardInput newCardInput = new CardInput();
        newCardInput.setName(myName);
        newCardInput.setAddress(someAddress);
        mvc.perform(MockMvcRequestBuilders.post("/cards/trusted").content(asJsonString(newCardInput)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("cardId").value(10001)).andExpect(MockMvcResultMatchers.jsonPath("name").value(myName)).andExpect(MockMvcResultMatchers.jsonPath("address").value(someAddress)).andExpect(MockMvcResultMatchers.jsonPath("state").value("Known")).andExpect(MockMvcResultMatchers.jsonPath("source").value("T")).andExpect(MockMvcResultMatchers.jsonPath("lastVerificationCode").value(900000001));
    }

    @Test
    void addUntrustedCard() throws Exception {

        when(inputToCard.map(any(CardInput.class), anyString(), anyString())).thenReturn(untrustedCard);
        untrustedCard.setLastVerificationCode(900000002L);
        when(cardServices.save(any(Card.class))).thenReturn(untrustedCard);

        CardInput newCardInput = new CardInput();
        newCardInput.setName(myName + "2");
        newCardInput.setAddress(someAddress + "2");
        mvc.perform(MockMvcRequestBuilders.post("/cards/untrusted").content(asJsonString(newCardInput)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("cardId").value(10002)).andExpect(MockMvcResultMatchers.jsonPath("name").value(myName + "2")).andExpect(MockMvcResultMatchers.jsonPath("address").value(someAddress + "2")).andExpect(MockMvcResultMatchers.jsonPath("state").value("UnKnown")).andExpect(MockMvcResultMatchers.jsonPath("source").value("U")).andExpect(MockMvcResultMatchers.jsonPath("lastVerificationCode").value(900000002));
    }

    @Test
    void changeStateUntrustedToPending() throws Exception {

        when(cardServices.load(any(Long.class))).thenReturn(untrustedCard);
        ReflectionTestUtils.setField(stateServices, "stateMachine", new HashSet<>(), Set.class);
        when(stateServices.verifyNewState(any(StateMachineData.class))).thenReturn(true);
        when(cardServices.save(any(Card.class))).thenReturn(untrustedCardChanged);

        ChangeStateInput changeStateInput = new ChangeStateInput();
        changeStateInput.setNewState("Pending Verification");
        changeStateInput.setCardId(10002L);
        mvc.perform(MockMvcRequestBuilders.put("/cards/card/state").content(asJsonString(changeStateInput)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("cardId").value(10002))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(myName + "2"))
                .andExpect(MockMvcResultMatchers.jsonPath("address").value(someAddress + "2"))
                .andExpect(MockMvcResultMatchers.jsonPath("state").value("Pending Verification"))
                .andExpect(MockMvcResultMatchers.jsonPath("source").value("U"))
                .andExpect(MockMvcResultMatchers.jsonPath("lastVerificationCode").value(900000004));
    }

    @Test
    void changeStateTrustedToManual() throws Exception {

        when(cardServices.load(any(Long.class))).thenReturn(trustedCard);
        ReflectionTestUtils.setField(stateServices, "stateMachine", new HashSet<>(), Set.class);
        when(stateServices.verifyNewState(any(StateMachineData.class))).thenReturn(true);
        when(cardServices.save(any(Card.class))).thenReturn(trustedCardChanged);

        ChangeStateInput changeStateInput = new ChangeStateInput();
        changeStateInput.setNewState("Manual Approved");
        changeStateInput.setCardId(10001L);
        mvc.perform(MockMvcRequestBuilders.put("/cards/card/state").content(asJsonString(changeStateInput)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("cardId").value(10001))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(myName))
                .andExpect(MockMvcResultMatchers.jsonPath("address").value(someAddress))
                .andExpect(MockMvcResultMatchers.jsonPath("state").value("Manual Approved"))
                .andExpect(MockMvcResultMatchers.jsonPath("source").value("T"))
                .andExpect(MockMvcResultMatchers.jsonPath("lastVerificationCode").value(900000005));
    }

    @Test
    void changeStateTrustedToUnknown() throws Exception {

        when(cardServices.load(any(Long.class))).thenReturn(trustedCard);
        ReflectionTestUtils.setField(stateServices, "stateMachine", new HashSet<>(), Set.class);
        when(stateServices.verifyNewState(any(StateMachineData.class))).thenReturn(false);
        when(cardServices.save(any(Card.class))).thenReturn(trustedCardChanged);

        ChangeStateInput changeStateInput = new ChangeStateInput();
        changeStateInput.setNewState("Unknown");
        changeStateInput.setCardId(10001L);
        mvc.perform(MockMvcRequestBuilders.put("/cards/card/state").content(asJsonString(changeStateInput))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}