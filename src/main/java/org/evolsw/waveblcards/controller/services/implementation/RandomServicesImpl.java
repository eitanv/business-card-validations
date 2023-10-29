package org.evolsw.waveblcards.controller.services.implementation;

import org.evolsw.waveblcards.controller.services.RandomServices;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomServicesImpl implements RandomServices {

    private final static Random random = new Random();

    @Override
    public Long generateVerificationCode() {

        return random.nextLong(1000000000);
    }
}
