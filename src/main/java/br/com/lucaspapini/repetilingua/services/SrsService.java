package br.com.lucaspapini.repetilingua.services;

import br.com.lucaspapini.repetilingua.entities.FlashcardProgress;
import br.com.lucaspapini.repetilingua.enums.ReviewQualityEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SrsService {
    /**
     * Processa a resposta do usuário e atualiza o progresso do flashcard.
     * @param progress Objeto de progresso do usuário para o card.
     * @param quality Nota dada pelo usuário (0: Errei, 3: Difícil, 4: Bom, 5: Fácil).
     */
    public void processReview(FlashcardProgress progress, ReviewQualityEnum quality) {
        int repetitions = progress.getRepetitions();
        int interval = progress.getIntervalDays();
        double easeFactor = progress.getEaseFactor();

        // Compara o valor numérico da nota informada
        if (quality.getValue() < ReviewQualityEnum.HARD.getValue()) {
            // ERROU (AGAIN): reseta a sequência de acertos
            repetitions = 0;
            interval = 1;
        } else {
            // ACERTOU (HARD, GOOD ou EASY): ajusta o intervalo
            if (repetitions == 0) {
                interval = 1;
            } else if (repetitions == 1) {
                interval = 6;
            } else {
                interval = (int) Math.round(interval * easeFactor);
            }
            repetitions++;
        }

        // Cálculo do Fator de Facilidade (Ease Factor) usando a nota recebida
        easeFactor = easeFactor + (0.1 - (5 - quality.getValue()) * (0.08 + (5 - quality.getValue()) * 0.02));
        if (easeFactor < 1.3) {
            easeFactor = 1.3;
        }

        progress.setRepetitions(repetitions);
        progress.setIntervalDays(interval);
        progress.setEaseFactor(easeFactor);
        progress.setNextReviewDate(LocalDate.now().plusDays(interval));
    }
}
