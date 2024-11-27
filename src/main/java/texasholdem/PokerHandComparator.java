package texasholdem;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PokerHandComparator implements Comparator<PokerHand> {

    @Override
    public int compare(PokerHand hand1, PokerHand hand2) {
        int compareByCombination = hand2.getWeight().compareTo(hand1.getWeight());
        if (compareByCombination != 0) {
            return compareByCombination;
        }
        return compareByHighCards(hand1, hand2);
    }

    private int compareByHighCards(PokerHand hand1, PokerHand hand2) {
        List<Map.Entry<Character, Integer>> combination1 = hand1.getCombination();
        List<Map.Entry<Character, Integer>> combination2 = hand2.getCombination();

        for (int i = 0; i < combination1.size(); i++) {
            int comparison = combination1.get(i).getValue().compareTo(combination2.get(i).getValue());
            if (comparison != 0) {
                return comparison;
            }
        }

        List<Map.Entry<Character, Integer>> kickers1 = hand1.getKickers();
        List<Map.Entry<Character, Integer>> kickers2 = hand2.getKickers();

        for (int i = 0; i < kickers1.size(); i++) {
            int comparison = kickers2.get(i).getValue().compareTo(kickers1.get(i).getValue());
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }
}
