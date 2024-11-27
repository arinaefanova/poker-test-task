package texasholdem;

import java.util.Map;
import java.util.Set;

public class CardValidator {

    public static final Map<Character, Integer> CARD_POSITIONS = Map.ofEntries(
            Map.entry('2', 2),
            Map.entry('3', 3),
            Map.entry('4', 4),
            Map.entry('5', 5),
            Map.entry('6', 6),
            Map.entry('7', 7),
            Map.entry('8', 8),
            Map.entry('9', 9),
            Map.entry('T', 10),
            Map.entry('J', 11),
            Map.entry('Q', 12),
            Map.entry('K', 13),
            Map.entry('A', 14)
    );

    public static final Set<Character> CARD_SUITS = Set.of('S', 'H', 'D', 'C');

    public static String[] validatePokerHand(String cardCombination) {
        String[] splitCards = cardCombination.split("\\s+");
        if (splitCards.length != 5) {
            throw new IllegalArgumentException("Poker Hand must contain exactly 5 cards.");
        }
        for (String card : splitCards) {
            validateCard(card);
        }
        return splitCards;
    }

    private static void validateCard(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card + ". Each card must be 2 characters long.");
        }
        char rank = card.charAt(0);
        char suit = card.charAt(1);

        if (!isValidRank(rank)) {
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
        if (!isValidSuit(suit)) {
            throw new IllegalArgumentException("Invalid card suit: " + suit);
        }
    }

    public static boolean isValidRank(char rank) {
        return CARD_POSITIONS.containsKey(rank);
    }

    public static boolean isValidSuit(char suit) {
        return CARD_SUITS.contains(suit);
    }
}
