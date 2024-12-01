package texasholdem;

import java.util.function.Predicate;

public enum HandRankings {
    ROYAL_FLUSH(900, PokerHandUtils::isRoyalFlush),
    STRAIGHT_FLUSH(800, PokerHandUtils::isStraightFlush),
    FOUR_OF_A_KIND(700, PokerHandUtils::isFourOfAKind),
    FULL_HOUSE(600, PokerHandUtils::isFullHouse),
    FLUSH(500, PokerHandUtils::isFlush),
    STRAIGHT(400, PokerHandUtils::isStraight),
    SET(300, PokerHandUtils::isSet),
    TWO_PAIR(200, PokerHandUtils::isTwoPair),
    ONE_PAIR(100, PokerHandUtils::isOnePair),
    HIGH_CARD(0, PokerHandUtils::isHighCard);

    private final int weight;
    private final Predicate<PokerHand> condition;

    HandRankings(int weight, Predicate<PokerHand> condition) {
        this.weight = weight;
        this.condition = condition;
    }

    boolean matches(PokerHand hand) {
        return condition.test(hand);
    }

    public int getWeight() {
        return weight;
    }
}
