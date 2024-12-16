package texasholdem;

import java.util.Arrays;

public enum CardRanks {
    TWO('2', 2),
    THREE('3', 3),
    FOUR('4', 4),
    FIVE('5', 5),
    SIX('6', 6),
    SEVEN('7', 7),
    EIGHT('8', 8),
    NINE('9', 9),
    TEN('T', 10),
    JACK('J', 11),
    QUEEN('Q', 12),
    KING('K', 13),
    ACE('A', 14);

    private final char letter;
    private final int weight;

    CardRanks(char letter, int weight) {
        this.letter = letter;
        this.weight = weight;
    }

    public static boolean isValidRank(char rank) {
        return Arrays.stream(CardRanks.values()).anyMatch(e -> e.getLetter() == rank);
    }

    public static CardRanks getByLetter( char letter){
        return Arrays.stream(CardRanks.values())
                .filter(e -> e.getLetter() == letter)
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException(
                        "No card ranks with letter: " + letter
                ));
    }

    public static int getWeightByLetter(char letter){
        return Arrays.stream(CardRanks.values())
                .filter(e -> e.getLetter() == letter)
                .mapToInt(CardRanks::getWeight)
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException(
                        "No card ranks with letter: " + letter
                ));
    }

    public char getLetter() {
        return letter;
    }

    public int getWeight() {
        return weight;
    }
}
