package texasholdem;
import java.util.*;
import java.util.stream.Collectors;

public class PokerHandEvaluator {

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
     * The method checks the hand against all possible poker rankings, in order of priority.
     * If a matching ranking is found, it returns the corresponding hand ranking.
     * If no other ranking matches, the hand is classified as **HIGH_CARD**.
     *
     * @param pokerHand The poker pokerHand to evaluate
     * @return The highest-ranking hand that matches the given poker hand, or **HIGH_CARD** if no other ranking matches.
     */
    public HandRanking evaluate(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();

        Map<Character, Integer> suitCounts = pokerHand.getSuitCounts();
        boolean hasFlush = suitCounts.containsValue(SuitFrequency.FLUSH_CARDS_IN_SUIT);

        if (hasFlush) {
            boolean hasRoyalFlashRanks = ROYAL_FLUSH_RANKS.stream()
                    .allMatch(rank -> pokerHand.getCardsAtHand().stream()
                            .map(pokerHand::getRank)
                            .anyMatch(cardRank -> cardRank == rank));

            if (hasRoyalFlashRanks){
                return assignRoyalFlush(pokerHand, rankCounts);
            }

            List<Integer> sequence = getSequence(pokerHand);
            if (sequence != null){
                return assignStraightFlush(pokerHand, sequence, rankCounts);
            }

            return assignFlush(pokerHand, rankCounts);
        }

        List<Integer> sequence = getSequence(pokerHand);
        if (sequence != null){
            return assignStraight(pokerHand, sequence, rankCounts);
        }

        int uniqueRanks = rankCounts.size();
        boolean hasPair = rankCounts.containsValue(RankFrequency.PAIR);
        boolean hasTriple = rankCounts.containsValue(RankFrequency.TRIPLE);

        if (uniqueRanks == UniqueRanks.FULL_HOUSE_OR_FOUR_OF_A_KIND) {
            if (hasTriple && hasPair) {
                return assignFullHouse(pokerHand, rankCounts);
            }

            boolean hasQuadruple = rankCounts.containsValue(RankFrequency.QUADRUPLE);

            if (hasQuadruple) {
                return assignFourOfAKind(pokerHand, rankCounts);
            }
        }

        if (uniqueRanks == UniqueRanks.TWO_PAIR_OR_SET) {
            if (hasTriple) {
                return assignSet(pokerHand, rankCounts);
            }
            if (hasPair) {
                return assignTwoPair(pokerHand, rankCounts);
            }
        }

        if (uniqueRanks == UniqueRanks.HIGH_CARD_OR_ONE_PAIR && hasPair) {
            return assignOnePair(pokerHand, rankCounts);
        }

        return assignHighCard(pokerHand, rankCounts);
    }

    /**
     * Determines if the hand contains a sequential series of ranks.
     * If the ranks form a valid straight or the special "wheel" straight, returns the sequence.
     *
     * @param pokerHand The poker hand to evaluate.
     * @return A list of integers representing the sequence of ranks, or null if no valid sequence is found.
     */
    private List<Integer> getSequence(PokerHand pokerHand) {
        List<Integer> sequence = pokerHand.getCardsAtHand().stream()
                .map(card -> CardRank.of(pokerHand.getRank(card)).getWeight())
                .sorted()
                .toList();

        boolean isSequential = true;
        for (int i = 1; i < sequence.size(); i++) {
            if (sequence.get(i) - sequence.get(i - 1) != 1) {
                isSequential = false;
                break;
            }
        }

        if (!isSequential && !sequence.equals(WHEEL_STRAIGHT_RANKS)) {
            return null;
        }

        return sequence;
    }

    /**
     * Creates a list of combination-cards or kickers based on the frequency of ranks in the hand.
     *
     * @param rankCounts A map of rank frequencies (rank -> count)
     * @param frequency The frequency of the rank to create a combination (e.g., pair, set)
     * @param pokerHand The poker hand to evaluate.
     * @return A list of map entries representing the combination (rank - position of the card)
     */
     private List<Card> createCardList(Map<Character, Integer> rankCounts, Integer frequency, PokerHand pokerHand) {
         return pokerHand.getCardsAtHand().stream()
                 .filter(card -> Objects.equals(rankCounts.get(card.getRank().getLetter()), frequency))
                 .sorted(Comparator.reverseOrder())
                 .collect(Collectors.toList());
    }

    private void sortWheelStraight(List<Card> combination) {
        combination.stream()
                .filter(card -> card.getRank() == CardRank.ACE)
                .findFirst()
                .ifPresent(card -> combination.replaceAll(c -> {
                    if (c.getRank() == CardRank.ACE) {
                        return new WheelStraightCard(c);
                    }
                    return c;
                }));
        combination.sort(null);
        Collections.reverse(combination);
    }

    /**
     * Returns the **High Card** ranking for the given hand and sets the kickers.
     * A High Card is simply the highest card in the hand, when no other combination is formed.
     * All 5 cards are placed in the kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks to their frequencies.
     * @return The **HandRanking.HIGH_CARD**.
     */
    private HandRanking assignHighCard(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setKickers(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));
        return HandRanking.HIGH_CARD;
    }

    /**
     * Assigns the **One Pair** combination to the given poker hand, setting the combination cards and kickers.
     * One Pair consists of two cards of the same rank and the remaining three cards are kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.ONE_PAIR**.
     */
    private HandRanking assignOnePair(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setCombination(createCardList(rankCounts, RankFrequency.PAIR, pokerHand));
        pokerHand.setKickers(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));

        return HandRanking.ONE_PAIR;
    }

    /**
     * Assigns the **Two Pair** combination to the given poker hand, setting the combination cards and kickers.
     * Two Pairs consists of two pairs of cards of the same rank and one kicker.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.TWO_PAIR**
     */
    private HandRanking assignTwoPair(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setCombination(createCardList(rankCounts, RankFrequency.PAIR, pokerHand));
        pokerHand.setKickers(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));

        return HandRanking.TWO_PAIR;
    }

    /**
     * Assigns the **Set** (Three of a Kind) combination to the given poker hand, setting the combination cards and kickers.
     * A Set consists of three cards of the same rank and the remaining two cards are kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.SET**
     */
    private HandRanking assignSet(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
            pokerHand.setCombination(createCardList(rankCounts, RankFrequency.TRIPLE, pokerHand));
            pokerHand.setKickers(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));
        return HandRanking.SET;
    }

    /**
     * Assigns the **Straight** combination to the given poker hand, setting the combination cards.
     * A Straight consists of five consecutive cards of any suit, or a "wheel straight" (Ace through 5).
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param sequence A list representing the sequence of card ranks.
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.STRAIGHT**
     */
    private HandRanking assignStraight(PokerHand pokerHand, List<Integer> sequence, Map<Character, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, 1, pokerHand);

        if (sequence.equals(WHEEL_STRAIGHT_RANKS)) {
            sortWheelStraight(combination);
        }

        pokerHand.setCombination(combination);
        return HandRanking.STRAIGHT;
    }

    /**
     * Assigns the **Flush** combination to the given poker hand, setting the combination cards.
     * A Flush consists of five cards of the same suit, but not in a sequence.
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.FLUSH**
     */
    private HandRanking assignFlush(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setCombination(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));

        return HandRanking.FLUSH;
    }

    /**
     * Assigns the **Full House** combination to the given poker hand, setting the combination cards and kickers.
     * A Full House consists of three cards of one rank and two cards of another rank.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.FULL_HOUSE**
     */
    private HandRanking assignFullHouse(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setCombination(createCardList(rankCounts, RankFrequency.TRIPLE, pokerHand));
        pokerHand.setKickers(createCardList(rankCounts, RankFrequency.PAIR, pokerHand));

        return HandRanking.FULL_HOUSE;
    }

    /**
     * Assigns the **Four Of A Kind** combination to the given poker hand, setting the combination cards and kickers.
     * Four of a Kind consists of four cards of the same rank and one kicker card.
     *
     * @param pokerHand The poker hand to evaluate
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.FOUR_OF_A_KIND**
     */
    private HandRanking assignFourOfAKind(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setCombination(createCardList(rankCounts, RankFrequency.QUADRUPLE, pokerHand));
        pokerHand.setKickers(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));

        return HandRanking.FOUR_OF_A_KIND;
    }

    /**
     * Assigns the **Straight Flush** combination to the given poker hand, setting the combination cards.
     * A Straight Flush consists of five consecutive cards of the same suit.
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.STRAIGHT_FLUSH**
     */
    private HandRanking assignStraightFlush(PokerHand pokerHand, List<Integer> sequence, Map<Character, Integer> rankCounts) {
        List<Card> combination = createCardList(rankCounts, RankFrequency.SINGLE, pokerHand);

        if (sequence.equals(WHEEL_STRAIGHT_RANKS)) {
            sortWheelStraight(combination);
        }

        pokerHand.setCombination(combination);
        return HandRanking.STRAIGHT_FLUSH;
    }

    /**
     * Assigns the **Royal Flush** combination to the given poker hand, setting the combination cards.
     * (a straight flush from 10 to Ace).
     * All 5 cards are placed in the combination, with no kickers.
     *
     * @param pokerHand The poker hand
     * @param rankCounts A map of card ranks and their frequencies.
     * @return The **HandRanking.ROYAL_FLUSH**
     */
    private HandRanking assignRoyalFlush(PokerHand pokerHand, Map<Character, Integer> rankCounts) {
        pokerHand.setCombination(createCardList(rankCounts, RankFrequency.SINGLE, pokerHand));
        return HandRanking.ROYAL_FLUSH;
    }
}
