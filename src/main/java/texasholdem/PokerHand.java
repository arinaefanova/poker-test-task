package texasholdem;

import java.util.*;
import java.util.function.Function;

public class PokerHand implements Comparable<PokerHand> {

    private final List<Card> cardsAtHand;
    private HandRankings handRanking;
    // The weight always consists of the combination weight + the highest card weight in the combination
    // (this is enough for most sorting and is done for code optimization)
    // However, if both the combination and the highest card are identical,
    // the comparison will continue by the rank within the combination, and then by the rank of the kickers.
    private Integer weight = -1;
    private List<Map.Entry<Character, Integer>> combination;
    private List<Map.Entry<Character, Integer>> kickers;
    // Cached result for whether this hand is a Flush.
    private Boolean isFlushCached = null;
    // Cached result for whether this hand is a Straight Flush.
    private Boolean isStraightCached = null;

    public PokerHand(String cardCombination) {
        if (cardCombination == null || cardCombination.isBlank()) {
            throw new IllegalArgumentException("Card combination cannot be null or empty.");
        }

        this.cardsAtHand = Arrays.stream(cardCombination.split("\\s+"))
                .map(Card::new)
                .toList();

        if (this.cardsAtHand.size() != 5) {
            throw new IllegalArgumentException("Poker Hand must contain exactly 5 cards.");
        }
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
        PokerHandService pokerHandService = PokerHandService.getPokerHandService();
        this.handRanking = pokerHandService.evaluate(this);
        this.weight = handRanking.getWeight();
        Optional<Map.Entry<Character, Integer>> maxEntry = combination.stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        maxEntry.ifPresent(characterIntegerEntry -> this.weight += characterIntegerEntry.getValue());
    }

    public boolean isAlreadyEstimated(){
        return weight != -1;
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

    public void setStraightCached(Boolean isStraight) {
        this.isStraightCached = isStraight;
    }

    public void setFlushCached(Boolean isFlush) {
        this.isFlushCached = isFlush;
    }

    public Boolean isFlushCached() {
        return isFlushCached;
    }

    public Boolean isStraightCached() {
        return isStraightCached;
    }

    char getSuit(Card card) {
        return card.getCardSuit().getSuit();
    }

    char getRank(Card card) {
        return card.getCardRank().getLetter();
    }

    private Map<Character, Integer> getCounts(Function<Card, Character> function) {
        Map<Character, Integer> counts = new HashMap<>();
        for (Card card : cardsAtHand) {
            char key = function.apply(card);
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }

    public List<Card> getCardsAtHand() {
        return cardsAtHand;
    }

    Map<Character, Integer> getRankCounts() {
        return getCounts(this::getRank);
    }

    Map<Character, Integer> getSuitCounts() {
        return getCounts(this::getSuit);
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
        return new HashSet<>(this.cardsAtHand).equals(new HashSet<>(pokerHand.cardsAtHand));
    }

    @Override
    public int hashCode() {
        return new HashSet<>(cardsAtHand).hashCode();
    }

    @Override
    public String toString() {
        return "utils.PokerHand{" +
                "cards=" + cardsAtHand.toString() +
                ", handRanking=" + handRanking +
                ", weight=" + weight +
                ", combination=" + combination +
                ", kickers=" + kickers +
                '}';
    }
}


