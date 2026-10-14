package br.com.lucaspapini.repetilingua.entities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FlashcardCreateRequestDTO(
        @NotBlank(message = "A frase em inglês é obrigatória.")
        @Size(min = 2, message = "A frase deve ter no mínimo 2 caracteres.")
        String frontText,

        @NotBlank(message = "A tradução é obrigatória.")
        String backText,

       Long deckId

) {
}
