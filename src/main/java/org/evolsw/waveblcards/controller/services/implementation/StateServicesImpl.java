package org.evolsw.waveblcards.controller.services.implementation;

import org.evolsw.waveblcards.controller.consts.SourceConsts;
import org.evolsw.waveblcards.controller.consts.StateConsts;
import org.evolsw.waveblcards.controller.data.StateMachineData;
import org.evolsw.waveblcards.controller.services.StateServices;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StateServicesImpl implements StateServices {

    static Set<StateMachineData> stateMachine = new HashSet<>();

    //TODO load from @Configuration/file
    public StateServicesImpl() {
        stateMachine.add(new StateMachineData(StateConsts.MANUAL_APPROVED, StateConsts.KNOWN, SourceConsts.TRUSTED_SOURCE));
        stateMachine.add(new StateMachineData(StateConsts.KNOWN, StateConsts.MANUAL_APPROVED, SourceConsts.TRUSTED_SOURCE));
        //TODO Split to two sets with factory
        stateMachine.add(new StateMachineData(StateConsts.PENDING_VERIFICATION, StateConsts.UNKNOWN, SourceConsts.UNTRUSTED_SOURCE));
        stateMachine.add(new StateMachineData(StateConsts.UNKNOWN, StateConsts.PENDING_VERIFICATION, SourceConsts.UNTRUSTED_SOURCE));
        stateMachine.add(new StateMachineData(StateConsts.STRONG_APPROVED, StateConsts.PENDING_VERIFICATION, SourceConsts.UNTRUSTED_SOURCE));
        stateMachine.add(new StateMachineData(StateConsts.PENDING_VERIFICATION, StateConsts.STRONG_APPROVED, SourceConsts.UNTRUSTED_SOURCE));
    }

    @Override
    public boolean verifyNewState(StateMachineData stateMachineData) {
        boolean result = false;
        result = stateMachine.contains(stateMachineData);
        return result;
    }


}
