package br.com.lucaspapini.repetilingua.repositories;

import br.com.lucaspapini.repetilingua.entities.FlashcardProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardProgressRepository extends JpaRepository<FlashcardProgress, Long> {
}
