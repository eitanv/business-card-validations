package org.evolsw.waveblcards.controller.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StateMachineData {

    String from;
    String to;
    String source;
}
