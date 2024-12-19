package texasholdem;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PokerHand implements Comparable<PokerHand> {

    private final List<Card> cardsAtHand;
    private HandRanking handRanking;
    // The weight always consists of the combination weight + the highest card weight in the combination
    // (this is enough for most sorting and is done for code optimization)
    // However, if both the combination and the highest card are identical,
    // the comparison will continue by the rank within the combination, and then by the rank of the kickers.
    private Integer weight;
    private List<Card> combination;
    private List<Card> kickers;

    public PokerHand(String cardCombination) {
        if (cardCombination == null || cardCombination.isBlank()) {
            throw new IllegalArgumentException("Card combination cannot be null or empty.");
        }

        this.cardsAtHand = Arrays.stream(cardCombination.split(" "))
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
        PokerHandEvaluator pokerHandEvaluator = new PokerHandEvaluator();
        this.handRanking = pokerHandEvaluator.evaluate(this);
        this.weight = handRanking.getWeight();

        Optional<Card> maxCard = combination.stream()
                .max(Comparator.comparingInt(card -> card.getRank().getWeight()));

        maxCard.ifPresent(card -> this.weight += card.getRank().getWeight());
    }

    public int compareTo(PokerHand other) {
        PokerHandComparator comparator = new PokerHandComparator();
        return comparator.compare(this, other);
    }

    void setCombination(List<Card> combination) {
        this.combination = combination;
        combination.sort(Comparator.reverseOrder());
    }

    void setKickers(List<Card> kickers) {
        this.kickers = kickers;
    }

    char getSuit(Card card) {
        return card.getSuit().getSuit();
    }

    char getRank(Card card) {
        return card.getRank().getLetter();
    }

    private Map<Character, Integer> getCounts(Function<Card, Character> function) {
        return cardsAtHand.stream()
                .map(function)
                .collect(Collectors.toMap(
                        key -> key,
                        value -> 1,
                        Integer::sum
                ));
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

    public List<Card> getKickers() {
        return kickers;
    }

    public Integer getWeight() {
        return weight;
    }

    public List<Card> getCombination() {
        return combination;
    }

    public HandRanking getHandRanking() {
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


