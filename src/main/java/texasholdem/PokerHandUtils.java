package texasholdem;
import java.util.*;
import java.util.stream.Collectors;

class PokerHandUtils {

    private static final List<Integer> WHEEL_STRAIGHT_RANKS = List.of(2, 3, 4, 5, 14);
    private static final Set<Character> ROYAL_FLUSH_RANKS = Set.of('A', 'K', 'Q', 'J', 'T');

    // Number of cards of the same suit in the hand
    private static class SuitFrequency {
        public static final int FLUSH_CARDS_IN_SUIT = 5;
    }

    // Number of unique ranks in the hand
    private static class UniqueRanks {
        public static final int HIGH_CARD_OR_ONE_PAIR = 4;
        public static final int TWO_PAIR_OR_SET = 3;
        public static final int FULL_HOUSE_OR_FOUR_OF_A_KIND = 2;
    }

    // Frequency of cards with the same rank in the hand
    private static class RankFrequency {
        public static final int SINGLE = 1;
        public static final int PAIR = 2;
        public static final int TRIPLE = 3;
        public static final int QUADRUPLE = 4;
    }

    /**
     * The method iterates through all possible hand rankings and returns the first match it finds.
     * If no other hand ranking matches, it defaults to **HIGH_CARD**.
     *
     * @param hand The poker hand to evaluate
     * @return The ranking of the hand
     */
     static HandRankings evaluate(PokerHand hand) {
        return Arrays.stream(HandRankings.values())
                .filter(type -> type.matches(hand))
                .findFirst()
                .orElse(HandRankings.HIGH_CARD);
    }

    /**
     * Creates a list of combination cards based on the frequency of ranks in the hand.
     *
     * @param rankCounts A map of rank frequencies (rank -> count)
     * @param value The frequency of the rank to create a combination (e.g., pair, set)
     * @return A list of map entries representing the combination (rank - position of the card)
     */
    static List<Map.Entry<Character, Integer>> createCombination(Map<Character, Integer> rankCounts, Integer value) {
        return rankCounts.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(entry -> Map.entry(entry.getKey(), CardRanks.getWeightByLetter(entry.getKey())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Creates a list of kickers (cards that are not part of the main combination).
     *
     * @param rankCounts A map of rank frequencies (rank -> count)
     * @param value The frequency to filter by (e.g., 1 for single cards, 2 for pairs)
     * @return A sorted list of kickers represented by rank and position
     */
    static List<Map.Entry<Character, Integer>> createKickers(Map<Character, Integer> rankCounts, Integer value) {
        return rankCounts.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(entry -> Map.entry(entry.getKey(), CardRanks.getWeightByLetter(entry.getKey())))
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .toList();
    }

    /**
     * Determines if the given poker hand is a High Card.
     * A High Card is simply the highest card in the hand, when no other combination is formed.
     *
     * This method populates the kickers, which are the single cards in the hand.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true, since a high card is always present
     */
    static boolean isHighCard(PokerHand pokerHand) {
        pokerHand.setKickers(createKickers(pokerHand.getRankCounts(), RankFrequency.SINGLE));
        return true;
    }

    /**
     * Determines if the given poker hand contains One Pair.
     * One Pair consists of two cards of the same rank and the remaining three cards are kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains One Pair, false otherwise
     */
    static boolean isOnePair(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasOnePair = rankCounts.size() == UniqueRanks.HIGH_CARD_OR_ONE_PAIR
                && rankCounts.containsValue(RankFrequency.PAIR);

        if (hasOnePair) {
            pokerHand.setCombination(createCombination(rankCounts, RankFrequency.PAIR));
            pokerHand.setKickers(createKickers(rankCounts, RankFrequency.SINGLE));
        }

        return hasOnePair;
    }

    /**
     * Determines if the given poker hand contains Two Pairs.
     * Two Pairs consists of two pairs of cards of the same rank and one kicker.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains Two Pairs, false otherwise
     */
     static boolean isTwoPair(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasTwoPair = rankCounts.size() == UniqueRanks.TWO_PAIR_OR_SET
                && rankCounts.containsValue(RankFrequency.PAIR);

        if (hasTwoPair) {
            pokerHand.setCombination(createCombination(rankCounts, RankFrequency.PAIR));
            pokerHand.setKickers(createKickers(rankCounts, RankFrequency.SINGLE));
        }

        return hasTwoPair;
    }

    /**
     * Determines if the given poker hand contains a Set (Three of a Kind).
     * A Set consists of three cards of the same rank and the remaining two cards are kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains a Set, false otherwise
     */
     static boolean isSet(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasSet = rankCounts.size() == UniqueRanks.TWO_PAIR_OR_SET
                && pokerHand.getRankCounts().containsValue(RankFrequency.TRIPLE);

        if (hasSet) {
            pokerHand.setCombination(createCombination(rankCounts, RankFrequency.TRIPLE));
            pokerHand.setKickers(createKickers(rankCounts, RankFrequency.SINGLE));
        }

        return hasSet;
    }

    /**
     * Determines if the given poker hand contains a Straight.
     * A Straight consists of five consecutive cards of any suit.
     *
     * This method checks if the card positions in the hand form a consecutive sequence,
     * or if the hand is a "wheel straight" (Ace through 5).
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains a Straight, false otherwise
     */
    static boolean isStraight(PokerHand pokerHand) {
        if (pokerHand.isStraightCached() != null) {
            return pokerHand.isStraightCached();
        }
        List<Integer> positions = pokerHand.getCardsAtHand().stream()
                .map(card -> CardRanks.getWeightByLetter(pokerHand.getRank(card)))
                .sorted()
                .toList();

        boolean regularStraight = true;
        for (int i = 1; i < positions.size(); i++) {
            if (positions.get(i) - positions.get(i - 1) != 1) {
                regularStraight = false;
                break;
            }
        }

        boolean wheelStraight = positions.equals(WHEEL_STRAIGHT_RANKS);
        boolean hasStraight = regularStraight || wheelStraight;

        if (regularStraight || wheelStraight) {
            Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
            List<Map.Entry<Character, Integer>> combination = createCombination(rankCounts, 1);

            if (wheelStraight) {
                combination.stream()
                        .filter(entry -> entry.getKey() == 'A')
                        .findFirst()
                        .ifPresent(entry -> combination.replaceAll(e ->
                                e.getKey() == 'A' ? new AbstractMap.SimpleEntry<>(e.getKey(), 1) : e));
            }
            pokerHand.setCombination(combination);
        }

        pokerHand.setStraightCached(hasStraight);
        return hasStraight;
    }

    /**
     * Determines if the given poker hand contains a Flush.
     * A Flush consists of five cards of the same suit, but not in a sequence.
     *
     * This method checks if there are 5 cards of the same suit and sets the combination accordingly.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains a Flush, false otherwise
     */
    static boolean isFlush(PokerHand pokerHand) {
        if (pokerHand.isFlushCached() != null) {
            return pokerHand.isFlushCached();
        }
        Map<Character, Integer> suitCounts = pokerHand.getSuitCounts();
        boolean hasFlush = suitCounts.containsValue(SuitFrequency.FLUSH_CARDS_IN_SUIT);

        if (hasFlush) {
            Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
            pokerHand.setCombination(createCombination(rankCounts, RankFrequency.SINGLE));
        }
        pokerHand.setFlushCached(hasFlush);
        return hasFlush;
    }

    /**
     * Determines if the given poker hand contains a Full House.
     * A Full House consists of three cards of one rank and two cards of another rank.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains a Full House, false otherwise
     */
    static boolean isFullHouse(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasFullHouse = rankCounts.size() == UniqueRanks.FULL_HOUSE_OR_FOUR_OF_A_KIND
                && rankCounts.containsValue(RankFrequency.TRIPLE)
                && rankCounts.containsValue(RankFrequency.PAIR);

        if (hasFullHouse) {
            pokerHand.setCombination(createCombination(rankCounts, RankFrequency.TRIPLE));
            pokerHand.setKickers(createKickers(rankCounts, RankFrequency.PAIR));
        }
        return hasFullHouse;
    }

    /**
     * Determines if the given poker hand contains Four of a Kind.
     * Four of a Kind consists of four cards of the same rank and one kicker card.
     *
     * @param pokerHand The poker hand to evaluate
     * @return true if the hand contains Four of a Kind, false otherwise
     */
    static boolean isFourOfAKind(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasFourOfAKind = rankCounts.size() == UniqueRanks.FULL_HOUSE_OR_FOUR_OF_A_KIND
                && rankCounts.containsValue(RankFrequency.QUADRUPLE);

        if (hasFourOfAKind) {
            pokerHand.setCombination(createCombination(rankCounts, RankFrequency.QUADRUPLE));
            pokerHand.setKickers(createKickers(rankCounts, RankFrequency.SINGLE));
        }

        return hasFourOfAKind;
    }

    /**
     * This method first checks if the hand is a Flush and a Straight, and if both conditions are satisfied, it returns true.
     * Both methods (isFlush and isStraight) use caching to avoid redundant calculations.
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand
     * @return true if the hand is a straight flush
     */
    static boolean isStraightFlush(PokerHand pokerHand) {
        return isFlush(pokerHand) && isStraight(pokerHand);
    }

    /**
     * This method checks if the hand is a Flush and whether it contains all the ranks of a Royal Flush
     * (a straight flush from 10 to Ace).
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand
     * @return true if the hand is a royal flush
     */
    static boolean isRoyalFlush(PokerHand pokerHand) {
        Set<Character> cardSet = pokerHand.getCardsAtHand().stream()
                .map(pokerHand::getRank)
                .collect(Collectors.toSet());
        return isFlush(pokerHand) && cardSet.containsAll(ROYAL_FLUSH_RANKS);
    }
}
