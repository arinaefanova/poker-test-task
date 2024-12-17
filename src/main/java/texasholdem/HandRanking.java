package texasholdem;

public enum HandRanking {
    ROYAL_FLUSH(900),
    STRAIGHT_FLUSH(800),
    FOUR_OF_A_KIND(700),
    FULL_HOUSE(600),
    FLUSH(500),
    STRAIGHT(400),
    SET(300),
    TWO_PAIR(200),
    ONE_PAIR(100),
    HIGH_CARD(0);

    private final int weight;

    HandRanking(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
