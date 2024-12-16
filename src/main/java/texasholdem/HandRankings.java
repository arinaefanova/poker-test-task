package texasholdem;

import java.util.function.Predicate;

public enum HandRankings {
    ROYAL_FLUSH(900, hand -> PokerHandService.getPokerHandService().isRoyalFlush(hand)),
    STRAIGHT_FLUSH(800, hand -> PokerHandService.getPokerHandService().isStraightFlush(hand)),
    FOUR_OF_A_KIND(700, hand -> PokerHandService.getPokerHandService().isFourOfAKind(hand)),
    FULL_HOUSE(600, hand -> PokerHandService.getPokerHandService().isFullHouse(hand)),
    FLUSH(500, hand -> PokerHandService.getPokerHandService().isFlush(hand)),
    STRAIGHT(400, hand -> PokerHandService.getPokerHandService().isStraight(hand)),
    SET(300,  hand -> PokerHandService.getPokerHandService().isSet(hand)),
    TWO_PAIR(200, hand -> PokerHandService.getPokerHandService().isTwoPair(hand)),
    ONE_PAIR(100, hand -> PokerHandService.getPokerHandService().isOnePair(hand)),
    HIGH_CARD(0, hand -> PokerHandService.getPokerHandService().isHighCard(hand));


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
