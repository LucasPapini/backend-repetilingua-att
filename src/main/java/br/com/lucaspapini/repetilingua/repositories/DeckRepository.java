package br.com.lucaspapini.repetilingua.repositories;

import br.com.lucaspapini.repetilingua.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository  extends JpaRepository<Deck, Long> {
}
