package org.evolsw.waveblcards.controller.data;

import lombok.Data;

@Data
public class ChangeStateInput {

    String newState;
    Long cardId;
}
