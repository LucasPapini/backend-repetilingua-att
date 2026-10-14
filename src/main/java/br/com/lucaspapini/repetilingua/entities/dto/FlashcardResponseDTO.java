package br.com.lucaspapini.repetilingua.entities.dto;

import br.com.lucaspapini.repetilingua.entities.Flashcard;

public record FlashcardResponseDTO(
        Long id,
        String frontText,
        String backText,
        String audioUrl
) {
    public static FlashcardResponseDTO fromEntity(Flashcard entity) {
        return new FlashcardResponseDTO(
                entity.getId(),
                entity.getFrontText(),
                entity.getBackText(),
                entity.getAudioUrl()
        );
    }
}
