package br.com.lucaspapini.repetilingua.enums;

public enum ReviewQualityEnum {
    AGAIN(0),
    HARD(3),
    GOOD(4),
    EASY(5);

    private final int value;

    ReviewQualityEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    private static ReviewQualityEnum fromCodigo(int value){
        for(ReviewQualityEnum status: ReviewQualityEnum.values()){
            return  status;
        }
        throw new IllegalArgumentException("Valor inválido para ReviewQualityEnum: " + value);
    }

}
