package org.evolsw.waveblcards.controller.services.implementation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomServicesImplTest {

    @Test
    void generateVerificationCode() {

        RandomServicesImpl randomServices = new RandomServicesImpl();
        Long generateVerificationCode = randomServices.generateVerificationCode();
        assertTrue(generateVerificationCode>0 && generateVerificationCode<1000000000);

        Long generateVerificationCode2;
        for(int i=1;i<=10000;i++) {
            generateVerificationCode2 = randomServices.generateVerificationCode();
            assertTrue(generateVerificationCode2>0 && generateVerificationCode2<1000000000);
            assertNotEquals(generateVerificationCode, generateVerificationCode2);
        }
    }
}