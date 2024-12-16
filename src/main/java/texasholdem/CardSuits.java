package texasholdem;

public enum CardSuits {
    SPADES('S'),
    HEARTS('H'),
    DIAMONDS('D'),
    CLUBS('C');

    private final char suit;

    CardSuits(char suit){
        this.suit = suit;
    }

    public char getSuit() {
        return suit;
    }
}
