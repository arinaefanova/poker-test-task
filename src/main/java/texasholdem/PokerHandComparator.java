package texasholdem;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PokerHandComparator implements Comparator<PokerHand> {

    /**
     * Compares two poker hands based on their overall ranking (weight).
     *
     * The method first compares the hands by their hand ranking (weight).
     * If the rankings are different, it returns the result of the comparison.
     * If the rankings are the same, it proceeds to compare the hands by high cards.
     *
     * @param hand1 the first poker hand to compare
     * @param hand2 the second poker hand to compare
     * @return a negative value if hand1 is higher, a positive value if hand2 is higher,
     *         or 0 if both hands have the same weight and high cards.
     */
    @Override
    public int compare(PokerHand hand1, PokerHand hand2) {
        int compareByCombination = hand2.getWeight().compareTo(hand1.getWeight());
        if (compareByCombination != 0) {
            return compareByCombination;
        }
        boolean compareRoyalFlash = hand1.isCombination(HandRanking.ROYAL_FLUSH);
        if (compareRoyalFlash) {
            return 0;
        }
        return compareByHighCards(hand1, hand2);
    }

    /**
     * Compares two poker hands based on their high cards and kickers.
     *
     * This method compares the main cards (combination) of both hands. If the combinations are the same,
     * it then compares the extra cards (kickers). The comparison is done starting with the highest card value.
     *
     * @param hand1 the first poker hand to compare
     * @param hand2 the second poker hand to compare
     * @return a negative value if hand1 is higher, a positive value if hand2 is higher,
     *         or 0 if both hands are the same based on their high cards and kickers.
     */
    private int compareByHighCards(PokerHand hand1, PokerHand hand2) {
        List<Card> combination1 = hand1.getCombinationCards();
        List<Card> combination2 = hand2.getCombinationCards();

        int comparison = compareCombinationsByIterator(combination2, combination1);
        if (comparison != 0) return comparison;

        List<Card> kickers1 = hand1.getKickerCards();
        List<Card> kickers2 = hand2.getKickerCards();

        return compareCombinationsByIterator(kickers2, kickers1);
    }

    private Integer compareCombinationsByIterator(List<Card> combination1, List<Card> combination2) {
        Iterator<Card> it1 = combination1.iterator();
        Iterator<Card> it2 = combination2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            int comparison = it1.next().compareTo(it2.next());
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }
}
