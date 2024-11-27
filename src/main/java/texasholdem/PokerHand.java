package texasholdem;

import java.util.*;
import java.util.function.Function;

public class PokerHand implements Comparable<PokerHand> {

    private final String[] cards;
    private HandRankings handRanking;
    private Integer weight;
    private List<Map.Entry<Character, Integer>> combination;
    private List<Map.Entry<Character, Integer>> kickers;

    public PokerHand(String cardCombination) {
        this.cards = CardValidator.validatePokerHand(cardCombination);
        this.combination = new ArrayList<>();
        this.kickers = new ArrayList<>();
        calculateWeight();
    }

    private void calculateWeight() {
        this.handRanking = PokerHandUtils.evaluate(this);
        this.weight = handRanking.getWeight();
        Optional<Map.Entry<Character, Integer>> maxEntry = combination.stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        maxEntry.ifPresent(characterIntegerEntry -> this.weight += characterIntegerEntry.getValue());
    }

    public int compareTo(PokerHand other) {
        PokerHandComparator comparator = new PokerHandComparator();
        return comparator.compare(this, other);
    }

    void setCombination(List<Map.Entry<Character, Integer>> combination) {
        this.combination = combination;
        combination.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
    }

    void setKickers(List<Map.Entry<Character, Integer>> kickers) {
        this.kickers = kickers;
    }

    char getSuit(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
        return card.charAt(1);
    }
    char getRank(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
        return card.charAt(0);
    }

    private Map<Character, Integer> getCounts(Function<String, Character> function) {
        Map<Character, Integer> counts = new HashMap<>();
        for (String card : cards) {
            char key = function.apply(card);
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }

    Map<Character, Integer> getRankCounts() {
        return getCounts(this::getRank);
    }

    Map<Character, Integer> getSuitCounts() {
        return getCounts(this::getSuit);
    }

    public String[] getCards() {
        return cards;
    }

    public List<Map.Entry<Character, Integer>> getKickers() {
        return kickers;
    }

    public Integer getWeight() {
        return weight;
    }

    public List<Map.Entry<Character, Integer>> getCombination() {
        return combination;
    }

    public HandRankings getHandRanking() {
        return handRanking;
    }

    @Override
    public String toString() {
        return "utils.PokerHand{" +
                "cards=" + Arrays.toString(cards) +
                ", handRanking=" + handRanking +
                ", weight=" + weight +
                ", combination=" + combination +
                ", kickers=" + kickers +
                '}';
    }
}


