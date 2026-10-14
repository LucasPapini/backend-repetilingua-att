package br.com.lucaspapini.repetilingua.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tb_flashcard_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "flashcard_id"})
})
@Builder
public class FlashcardProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_id", nullable = false)
    private Flashcard flashcard;

    @Column(name = "interval_days", nullable = false)
    private Integer intervalDays = 0;

    @Column(name = "ease_factor", nullable = false)
    private Double easeFactor = 2.5;

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions = 0;

    @Column(name = "next_review_date", nullable = false)
    private LocalDate nextReviewDate;

    @Column(name = "last_reviewed_at")
    private Instant lastReviewedAt;

    public FlashcardProgress() {
    }

    public FlashcardProgress(Long id, Long userId, Flashcard flashcard, Integer intervalDays, Double easeFactor, Integer repetitions, LocalDate nextReviewDate, Instant lastReviewedAt) {
        this.id = id;
        this.userId = userId;
        this.flashcard = flashcard;
        this.intervalDays = intervalDays;
        this.easeFactor = easeFactor;
        this.repetitions = repetitions;
        this.nextReviewDate = nextReviewDate;
        this.lastReviewedAt = lastReviewedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    public Integer getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(Integer intervalDays) {
        this.intervalDays = intervalDays;
    }

    public Double getEaseFactor() {
        return easeFactor;
    }

    public void setEaseFactor(Double easeFactor) {
        this.easeFactor = easeFactor;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public LocalDate getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(LocalDate nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public Instant getLastReviewedAt() {
        return lastReviewedAt;
    }

    public void setLastReviewedAt(Instant lastReviewedAt) {
        this.lastReviewedAt = lastReviewedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FlashcardProgress that = (FlashcardProgress) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
