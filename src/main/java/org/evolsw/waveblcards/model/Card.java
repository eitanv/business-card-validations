package org.evolsw.waveblcards.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ADDRESS_BOOK")
@Data
@SequenceGenerator(name = "start@100001", sequenceName = "start@100001", initialValue = 100001)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "start@100001")
    Long cardId;
    String source;
    String state;
    String name;
    String address;
    Long lastVerificationCode;
}
