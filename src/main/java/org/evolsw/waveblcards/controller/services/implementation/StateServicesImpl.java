package org.evolsw.waveblcards.controller.services.implementation;

import org.evolsw.waveblcards.controller.services.StateServices;
import org.springframework.stereotype.Service;

@Service
public class StateServicesImpl implements StateServices {
    @Override
    public boolean verifyNewState(Long cardId, String newState) {
        return true;
    }
}
