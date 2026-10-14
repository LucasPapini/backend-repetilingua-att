-- 1. Cria primeiro a tabela que NÃO tem dependências (tb_deck)
CREATE TABLE tb_deck (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Cria a tb_flashcard já com a Foreign Key apontando para tb_deck
CREATE TABLE tb_flashcard (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    front_text VARCHAR(255) NOT NULL,
    back_text VARCHAR(255) NOT NULL,
    audio_url VARCHAR(255),
    deck_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_flashcard_deck FOREIGN KEY (deck_id) REFERENCES tb_deck(id) ON DELETE CASCADE
);

-- 3. Cria a tb_flashcard_progress apontando para tb_flashcard
CREATE TABLE tb_flashcard_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flashcard_id BIGINT NOT NULL,
    interval_days INT DEFAULT 0 NOT NULL,
    ease_factor DOUBLE DEFAULT 2.5 NOT NULL,
    repetitions INT DEFAULT 0 NOT NULL,
    next_review_date DATE NOT NULL,
    last_reviewed_at TIMESTAMP,
    CONSTRAINT fk_flashcard_progress_flashcard FOREIGN KEY (flashcard_id) REFERENCES tb_flashcard(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_flashcard UNIQUE (user_id, flashcard_id)
);