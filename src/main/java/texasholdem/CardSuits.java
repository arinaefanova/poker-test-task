package texasholdem;

import java.util.Arrays;

public enum CardSuits {
    SPADES('S'),
    HEARTS('H'),
    DIAMONDS('D'),
    CLUBS('C');

    private final char suit;

    CardSuits(char suit){
        this.suit = suit;
    }

    public static boolean isValidSuit(char suit) {
        return Arrays.stream(CardSuits.values()).anyMatch(e -> e.getSuit() == suit);
    }

    public static CardSuits getByLetter( char letter){
        return Arrays.stream(CardSuits.values())
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
