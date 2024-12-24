package texasholdem;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PokerHand implements Comparable<PokerHand> {

    private final List<Card> cardsAtHand;
    private final CombinationAtHand combinationAtHand;

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
        PokerHandEvaluator pokerHandEvaluator = new PokerHandEvaluator();
        this.combinationAtHand = pokerHandEvaluator.evaluate(this);
    }

    public int compareTo(PokerHand other) {
        PokerHandComparator comparator = new PokerHandComparator();
        return comparator.compare(this, other);
    }

    CardSuit getSuit(Card card) {
        return card.getSuit();
    }

    CardRank getRank(Card card) {
        return card.getRank();
    }

    private <E extends Enum<E>> Map<E, Integer> getCounts(Function<Card, E> function) {
        return cardsAtHand.stream()
                .map(function)
                .collect(Collectors.toMap(
                        key -> key,
                        value -> 1,
                        Integer::sum
                ));
    }

    Map<CardRank, Integer> getRankCounts() {
        return getCounts(this::getRank);
    }

    Map<CardSuit, Integer> getSuitCounts() {
        return getCounts(this::getSuit);
    }

    public List<Card> getCardsAtHand() {
        return cardsAtHand;
    }

    public CombinationAtHand getCombinationAtHand() {
        return combinationAtHand;
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
        return "PokerHand{" +
                "cardsAtHand=" + cardsAtHand +
                ", " + combinationAtHand +
                '}';
    }
}


