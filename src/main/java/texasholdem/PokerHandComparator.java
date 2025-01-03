package texasholdem;

import java.util.Comparator;
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
        int compareByCombination = hand2.getCombinationAtHand().getWeight().compareTo(hand1.getCombinationAtHand().getWeight());
        if (compareByCombination != 0) {
            return compareByCombination;
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
        List<Card> combination1 = hand1.getCombinationAtHand().getCombination();
        List<Card> combination2 = hand2.getCombinationAtHand().getCombination();

        for (int i = 0; i < combination1.size(); i++) {
            int comparison = combination1.get(i).compareTo(combination2.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }

        List<Card> kickers1 = hand1.getCombinationAtHand().getKickers();
        List<Card> kickers2 = hand2.getCombinationAtHand().getKickers();

        for (int i = 0; i < kickers1.size(); i++) {
            int comparison = kickers2.get(i).compareTo(kickers1.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }
}
