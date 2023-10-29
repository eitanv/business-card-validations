package org.evolsw.waveblcards.controller.services.implementation;

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
        stateMachine.add(new StateMachineData("Manual Approved", "Known", "T"));
        stateMachine.add(new StateMachineData("Known", "Manual Approved", "T"));
        //TODO Split to two sets with factory
        stateMachine.add(new StateMachineData("Pending Verification", "Unknown", "U"));
        stateMachine.add(new StateMachineData("Unknown", "Pending Verification", "U"));
        stateMachine.add(new StateMachineData("Strong Approved", "Pending Verification", "U"));
        stateMachine.add(new StateMachineData("Pending Verification", "Strong Approved", "U"));
    }

    @Override
    public boolean verifyNewState(StateMachineData stateMachineData) {
        boolean result = false;
        result = stateMachine.contains(stateMachineData);
        return result;
    }


}
