package org.evolsw.waveblcards.controller.services.implementation;

import org.evolsw.waveblcards.controller.data.StateMachineData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateServicesImplTest {

    private StateServicesImpl stateServices;

    @BeforeEach
    void setUp() {

        stateServices = new StateServicesImpl();

    }

    @Test
    void verifyNewState() {
        StateMachineData knownToManual = new StateMachineData("Known", "Manual Approved", "T");
        assertTrue(stateServices.verifyNewState(knownToManual));
    }

    @Test
    void verifyNewStateFalse() {
        StateMachineData knownToManual = new StateMachineData("Known", "Manual", "T");
        assertFalse(stateServices.verifyNewState(knownToManual));
    }

    @Test
    void verifyNewStateFalseSource() {
        StateMachineData knownToManual = new StateMachineData("Known", "Manual Approved", "U");
        assertFalse(stateServices.verifyNewState(knownToManual));
    }
}