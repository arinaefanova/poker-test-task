package texasholdem;

import java.util.*;
import java.util.function.Function;

public class PokerHand implements Comparable<PokerHand> {

    private final String[] cards;
    private HandRankings handRanking;
    // The weight always consists of the combination weight + the highest card weight in the combination
    // (this is enough for most sorting and is done for code optimization)
    // However, if both the combination and the highest card are identical,
    // the comparison will continue by the rank within the combination, and then by the rank of the kickers.
    private Integer weight;
    private List<Map.Entry<Character, Integer>> combination;
    private List<Map.Entry<Character, Integer>> kickers;
    // Cached result for whether this hand is a Flush.
    private Boolean isFlushCached = null;
    // Cached result for whether this hand is a Straight Flush.
    private Boolean isStraightCached = null;

    public PokerHand(String cardCombination) {
        this.cards = CardValidator.validatePokerHand(cardCombination);
        this.combination = new ArrayList<>();
        this.kickers = new ArrayList<>();
        calculateWeight();
    }

    /**
     * Calculates the weight of the poker hand based on its ranking and highest card.
     *
     * This method first evaluates the hand's ranking (e.g., Full House, High Card) and gets its weight.
     * Then, it finds the card with the highest value in the combination and adds its value to the hand's weight.
     */
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

    public Boolean isFlushCached() {
        return isFlushCached;
    }

    public void setFlushCached(Boolean isFlush) {
        this.isFlushCached = isFlush;
    }

    public Boolean isStraightCached() {
        return isStraightCached;
    }

    public void setStraightCached(Boolean isStraight) {
        this.isStraightCached = isStraight;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokerHand pokerHand = (PokerHand) o;
        Set<String> set1 = new HashSet<>(Arrays.asList(cards));
        Set<String> set2 = new HashSet<>(Arrays.asList(pokerHand.cards));
        return set1.equals(set2);
    }

    @Override
    public int hashCode() {
        return new HashSet<>(Arrays.asList(cards)).hashCode();
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


