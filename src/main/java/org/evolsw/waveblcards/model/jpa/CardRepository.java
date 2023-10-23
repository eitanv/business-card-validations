package org.evolsw.waveblcards.model.jpa;

import org.evolsw.waveblcards.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface CardRepository extends JpaRepository<Card, Long> {

}
