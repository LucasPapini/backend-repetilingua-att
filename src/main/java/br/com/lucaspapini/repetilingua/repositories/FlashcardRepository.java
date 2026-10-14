package br.com.lucaspapini.repetilingua.repositories;

import br.com.lucaspapini.repetilingua.entities.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
}
