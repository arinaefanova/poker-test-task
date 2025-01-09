package texasholdem;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PokerHandEvaluator {

    private static final List<Integer> WHEEL_STRAIGHT_RANKS = List.of(2, 3, 4, 5, 14);
    private static final Set<Character> ROYAL_FLUSH_RANKS = Set.of('A', 'K', 'Q', 'J', 'T');

    // Number of cards of the same suit in the hand
    private static class SuitFrequency {
        public static final int FLUSH_CARDS_IN_SUIT = 5;
    }

    // Number of unique ranks in the hand
    private static class UniqueRanks {
        public static final int HIGH_CARD_OR_STRAIGHT_OR_FLUSH = 5;
        public static final int ONE_PAIR = 4;
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
     * Evaluates the poker hand and determines its ranking based on poker rules.
     *
     * The evaluation is based on the number of unique ranks in the hand:
     * - 5 unique ranks: Possible **HIGH_CARD**, **STRAIGHT**, **FLUSH**, **STRAIGHT_FLUSH** or **ROYAL_FLUSH**.
     * - 4 unique ranks: Corresponds to **ONE_PAIR**.
     * - 3 unique ranks: Could be either **TWO_PAIR** or **SET**.
     * - 2 unique ranks: Could be either **FULL_HOUSE** or **FOUR_OF_A_KIND**.
     *
     * If no valid ranking is found (which should not occur with valid input), an exception is thrown.
     *
     * @param pokerHand The poker pokerHand to evaluate
     * @return A {@link Combination} object representing the highest-ranking combination.
     * @throws IllegalStateException if the hand is in an unexpected state.
     */
    public Combination evaluate(PokerHand pokerHand) {
        Map<CardRank, Integer> rankCounts = pokerHand.getRankCounts();
        int uniqueRanks = rankCounts.size();

        return switch (uniqueRanks) {
            case UniqueRanks.HIGH_CARD_OR_STRAIGHT_OR_FLUSH -> evaluateUniqueRanksFive(pokerHand, rankCounts);
            case UniqueRanks.ONE_PAIR -> assignOnePair(pokerHand, rankCounts);
            case UniqueRanks.TWO_PAIR_OR_SET -> rankCounts.containsValue(RankFrequency.TRIPLE)
                    ? assignSet(pokerHand, rankCounts)
                    : assignTwoPair(pokerHand, rankCounts);
            case UniqueRanks.FULL_HOUSE_OR_FOUR_OF_A_KIND -> rankCounts.containsValue(RankFrequency.QUADRUPLE)
                    ? assignFourOfAKind(pokerHand, rankCounts)
                    : assignFullHouse(pokerHand, rankCounts);
            default -> throw new IllegalStateException("Unexpected hand state");
        };
    }

    /**
     * Evaluates a poker hand with exactly five unique ranks and determines its combination.
     * Checks for **Straight Flush** or **Royal Flush**, **Flush**, **Straight** and **High Card** in priority order.
     *
     * @param pokerHand The poker hand to evaluate.
     * @param rankCounts A map of card ranks to their frequencies.
     * @return A {@link Combination} representing the determined combination.
     */
    private Combination evaluateUniqueRanksFive(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        boolean hasFlush = pokerHand.getSuitCounts().containsValue(SuitFrequency.FLUSH_CARDS_IN_SUIT);
        boolean hasStraight = getSequence(pokerHand) != null;

        if (hasFlush && hasStraight) {
            return evaluateStraightFlushOrRoyalFlush(pokerHand, rankCounts);
        } else if (hasFlush) {
            return assignFlush(pokerHand, rankCounts);
        } else if (hasStraight) {
            return assignStraight(pokerHand, rankCounts);
        } else {
            return assignHighCard(pokerHand, rankCounts);
        }
    }

    /**
     * Helper method to evaluate whether a poker hand is a **Straight Flush** or **Royal Flush**.
     * Determines the exact combination based on the ranks of the cards in the hand.
     *
     * @param pokerHand The poker hand to evaluate.
     * @param rankCounts A map of card ranks to their frequencies.
     * @return A {@link Combination} representing either a **Royal Flush** or a **Straight Flush**.
     */
    private Combination evaluateStraightFlushOrRoyalFlush(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        boolean hasRoyalFlushRanks = ROYAL_FLUSH_RANKS.stream()
                .allMatch(rank -> pokerHand.getCards().stream()
                        .map(pokerHand::getRank)
                        .anyMatch(cardRank -> cardRank.getLetter() == rank));
        return hasRoyalFlushRanks
                ? assignRoyalFlush(pokerHand, rankCounts)
                : assignStraightFlush(pokerHand, rankCounts);
    }

    /**
     * Determines if the hand contains a sequential series of ranks.
     * If the ranks form a valid straight or the special "wheel" straight, returns the sequence.
     *
     * @param pokerHand The poker hand to evaluate.
     * @return A list of integers representing the sequence of ranks, or null if no valid sequence is found.
     */
    private List<Integer> getSequence(PokerHand pokerHand) {
        List<Integer> sequence = pokerHand.getCards().stream()
                .map(card -> CardRank.of(pokerHand.getRank(card).getLetter()).getWeight())
                .sorted()
                .toList();
        boolean isSequential = IntStream.range(1, sequence.size())
                .allMatch(i -> sequence.get(i) - sequence.get(i - 1) == 1);
        return (isSequential || sequence.equals(WHEEL_STRAIGHT_RANKS)) ? sequence : null;
    }

    /**
     * Creates a list of combination-cards or kickers based on the frequency of ranks in the hand.
     *
     * @param rankCounts A map of rank frequencies (rank -> count)
     * @param frequency The frequency of the rank to create a combination (e.g., pair, set)
     * @param pokerHand The poker hand to evaluate.
     * @return A list of map entries representing the combination (rank - position of the card)
     */
    private List<Card> createCardList(Map<CardRank, Integer> rankCounts, Integer frequency, PokerHand pokerHand) {
        return pokerHand.getCards().stream()
                .filter(card -> rankCounts.get(card.getRank()).equals(frequency))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private void sortWheelStraight(List<Card> combination) {
        combination.replaceAll(card -> card.getRank() == CardRank.ACE ? new WheelStraightCard(card) : card);
        combination.sort(Collections.reverseOrder());
    }

    /**
     * Assigns the **High Card** ranking for the given hand and sets the kickers.
     * A High Card is simply the highest card in the hand, when no other combination is formed.
     * All 5 cards are placed in the kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks to their frequencies.
     * @return A {@link Combination} object with the **HandRanking.HIGH_CARD**
     */
    private Combination assignHighCard(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = Collections.emptyList();
        List<Card> kickers = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        return new Combination(HandRanking.HIGH_CARD, combination, kickers);
    }

    /**
     * Assigns the **One Pair** combination to the given poker hand, setting the combination cards and kickers.
     * One Pair consists of two cards of the same rank and the remaining three cards are kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.ONE_PAIR**.
     */
    private Combination assignOnePair(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.PAIR, pokerHand);
        List<Card> kickers = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        return new Combination(HandRanking.ONE_PAIR, combination, kickers);
    }

    /**
     * Assigns the **Two Pair** combination to the given poker hand, setting the combination cards and kickers.
     * Two Pairs consists of two pairs of cards of the same rank and one kicker.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.TWO_PAIR**
     */
    private Combination assignTwoPair(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.PAIR, pokerHand);
        List<Card> kickers = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        return new Combination(HandRanking.TWO_PAIR, combination, kickers);
    }

    /**
     * Assigns the **Set** (Three of a Kind) combination to the given poker hand, setting the combination cards and kickers.
     * A Set consists of three cards of the same rank and the remaining two cards are kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.SET**
     */
    private Combination assignSet(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.TRIPLE, pokerHand);
        List<Card> kickers = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        return new Combination(HandRanking.SET, combination, kickers);
    }

    /**
     * Assigns the **Straight** combination to the given poker hand, setting the combination cards.
     * A Straight consists of five consecutive cards of any suit, or a "wheel straight" (Ace through 5).
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.STRAIGHT**
     */
    private Combination assignStraight(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, 1, pokerHand);
        List<Card> kickers = Collections.emptyList();
        List<Integer> sequence = getSequence(pokerHand);
        if (sequence != null && sequence.equals(WHEEL_STRAIGHT_RANKS)) {
            sortWheelStraight(combination);
        }
        return new Combination(HandRanking.STRAIGHT, combination, kickers);
    }

    /**
     * Assigns the **Flush** combination to the given poker hand, setting the combination cards.
     * A Flush consists of five cards of the same suit, but not in a sequence.
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.FLUSH**
     */
    private Combination assignFlush(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        List<Card> kickers = Collections.emptyList();
        return new Combination(HandRanking.FLUSH, combination, kickers);
    }

    /**
     * Assigns the **Full House** combination to the given poker hand, setting the combination cards and kickers.
     * A Full House consists of three cards of one rank and two cards of another rank.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.FULL_HOUSE**
     */
    private Combination assignFullHouse(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.TRIPLE, pokerHand);
        List<Card> kickers = createCardList(rankCounts, RankFrequency.PAIR, pokerHand);
        return new Combination(HandRanking.FULL_HOUSE, combination, kickers);
    }

    /**
     * Assigns the **Four Of A Kind** combination to the given poker hand, setting the combination cards and kickers.
     * Four of a Kind consists of four cards of the same rank and one kicker card.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.FOUR_OF_A_KIND**
     */
    private Combination assignFourOfAKind(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.QUADRUPLE, pokerHand);
        List<Card> kickers = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        return new Combination(HandRanking.FOUR_OF_A_KIND, combination, kickers);
    }

    /**
     * Assigns the **Straight Flush** combination to the given poker hand, setting the combination cards.
     * A Straight Flush consists of five consecutive cards of the same suit.
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.STRAIGHT_FLUSH**
     */
    private Combination assignStraightFlush(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        List<Card> kickers = Collections.emptyList();
        List<Integer> sequence = getSequence(pokerHand);
        if (sequence != null && sequence.equals(WHEEL_STRAIGHT_RANKS)) {
            sortWheelStraight(combination);
        }
        return new Combination(HandRanking.STRAIGHT_FLUSH, combination, kickers);
    }

    /**
     * Assigns the **Royal Flush** combination to the given poker hand, setting the combination cards.
     * (a straight flush from 10 to Ace).
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand
     * @param rankCounts A map of card ranks and their frequencies.
     * @return A {@link Combination} object with the **HandRanking.ROYAL_FLUSH**
     */
    private Combination assignRoyalFlush(PokerHand pokerHand, Map<CardRank, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);
        List<Card> kickers = Collections.emptyList();
        return new Combination(HandRanking.ROYAL_FLUSH, combination, kickers);
    }
}
