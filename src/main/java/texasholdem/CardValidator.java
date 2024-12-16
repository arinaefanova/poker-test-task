package texasholdem;

import java.util.Arrays;

public class CardValidator {

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
        return Arrays.stream(CardRanks.values()).anyMatch(e -> e.getLetter() == rank);
    }

    public static boolean isValidSuit(char suit) {
        return Arrays.stream(CardSuits.values()).anyMatch(e -> e.getSuit() == suit);
    }
}
