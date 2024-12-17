package texasholdem;

import java.util.Arrays;

public enum CardSuit {
    SPADES('S'),
    HEARTS('H'),
    DIAMONDS('D'),
    CLUBS('C');

    private final char suit;

    CardSuit(char suit){
        this.suit = suit;
    }

    public static CardSuit of(char letter){
        return Arrays.stream(CardSuit.values())
                .filter(e -> e.getSuit() == letter)
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException(
                        "No card suit with letter: " + letter
                ));
    }

    public char getSuit() {
        return suit;
    }
}
