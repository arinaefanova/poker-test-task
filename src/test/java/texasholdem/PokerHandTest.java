package texasholdem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static texasholdem.HandRanking.*;

public class PokerHandTest {

    @Test
    void compareSameCardsWithSameOrder() {
        PokerHand hand1 = new PokerHand("AS KS QS JS TS");
        PokerHand hand2 = new PokerHand("AS KS QS JS TS");

        assertEquals(hand1, hand2, "Hands with the same cards in the same order should be equal");
    }

    @Test
    void compareHashCodeOfSameCardsWithSameOrder() {
        PokerHand hand1 = new PokerHand("AS KS QS JS TS");
        PokerHand hand2 = new PokerHand("AS KS QS JS TS");

        assertEquals(hand1.hashCode(), hand2.hashCode(), "Hands with the same cards in the same order should be equal");
    }

    @Test
    void compareSameCardsWithDifferentOrder() {
        PokerHand hand1 = new PokerHand("9D KC 6H 8S 5C");
        PokerHand hand2 = new PokerHand("KC 9D 8S 6H 5C");

        assertEquals(hand1, hand2, "Hands with the same cards in the same order should be equal");
    }

    @Test
    void compareHashCodeOfSameCardsWithDifferentOrder() {
        PokerHand hand1 = new PokerHand("9D KC 6H 8S 5C");
        PokerHand hand2 = new PokerHand("KC 9D 8S 6H 5C");

        assertEquals(hand1.hashCode(), hand2.hashCode(), "Hands with the same cards in the same order should be equal");
    }

    @Test
    void compareDifferentCards() {
        PokerHand hand1 = new PokerHand("9D KC 6H 3S 5C");
        PokerHand hand2 = new PokerHand("KC TS 8S 6H 5C");

        assertNotEquals(hand1, hand2, "Hands with the same cards in the same order should be equal");
    }

    @Test
    void compareHashCodeOfDifferentCards() {
        PokerHand hand1 = new PokerHand("9D KC 6H 3S 5C");
        PokerHand hand2 = new PokerHand("KC TS 8S 6H 5C");

        assertNotEquals(hand1.hashCode(), hand2.hashCode(), "Hands with the same cards in the same order should be equal");
    }

    @Test
    void compareIdenticalHands() {
        PokerHand sameCard1 = new PokerHand("KC 9D 8S 6H 5C");
        PokerHand sameCard2 = new PokerHand("KC 9D 8S 6H 5C");

        assertEquals(0, sameCard1.compareTo(sameCard2), "Hands should be equal");
    }

    @Test
    void compareDifferentHandsWithoutCombination() {
        PokerHand withoutCombination1 = new PokerHand("KC 9D 8S 6H 5C");
        PokerHand withoutCombination2 = new PokerHand("AH 9D 8S 6H 5C");

        assertTrue(withoutCombination1.compareTo(withoutCombination2) > 0,
                "First hand should be lesser due to lower High Card (King vs Ace)");
        assertTrue(withoutCombination2.compareTo(withoutCombination1) < 0,
                "Second hand should be greater due to higher High Card (Ace vs King)");
    }

    @Test
    void compareSameHandsWithDifferentKickers() {
        PokerHand fourOfAKindHand1 = new PokerHand("KH KD KC KS JC");
        PokerHand fourOfAKindHand2 = new PokerHand("KH KD KC KS 3C");

        assertTrue(fourOfAKindHand2.compareTo(fourOfAKindHand1) > 0,
                "Hand with lower kicker (3) should rank lower than hand with higher kicker (Jack)");
        assertTrue(fourOfAKindHand1.compareTo(fourOfAKindHand2) < 0,
                "Hand with higher kicker (Jack) should rank higher than hand with lower kicker (3)");
    }

    @Test
    void compareFlushVsStraight() {
        PokerHand flushHand = new PokerHand("3C 5C 7C 9C KC");
        PokerHand straightHand = new PokerHand("9D TC JH QC KC");

        assertTrue(straightHand.compareTo(flushHand) > 0, "Straight hand should be lesser than Flush hand");
        assertTrue(flushHand.compareTo(straightHand) < 0, "Flush hand should be greater than Straight hand");
    }

    @Test
    void compareRoyalFlushVsFourOfAKind() {
        PokerHand royalFlushHand = new PokerHand("AC KC QC JC TC");
        PokerHand fourOfAKindHand = new PokerHand("7S 7H 7C 7D 2H");

        assertTrue(fourOfAKindHand.compareTo(royalFlushHand) > 0, "Four of a Kind should be lesser than Royal Flush");
        assertTrue(royalFlushHand.compareTo(fourOfAKindHand) < 0, "Royal Flush should be greater than Four of a Kind");
    }

    @Test
    void compareHandWithNull() {
        PokerHand hand = new PokerHand("KH JD 9C 3S 2D");

        assertThrows(NullPointerException.class, () -> hand.compareTo(null),
                "Comparing with null should throw NullPointerException");
    }

    @Test
    void detectHighCard() {
        PokerHand highCard = new PokerHand("KS 2H 5C JD TD");
        PokerHand notHighCard = new PokerHand("3C 3D 5C 5D 9C");

        assertEquals(HIGH_CARD, highCard.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be high card");
        assertNotEquals(HIGH_CARD, notHighCard.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be high card");
    }

    @Test
    void detectOnePair() {
        PokerHand onePair = new PokerHand("3C 3D JC QC AC");
        PokerHand notOnePair = new PokerHand("4S 5C 6H 8D 9S");

        assertEquals(ONE_PAIR, onePair.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be one pair");
        assertNotEquals(ONE_PAIR, notOnePair.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be one pair");
    }

    @Test
    void detectTwoPair() {
        PokerHand twoPair = new PokerHand("4C 4D 9C 9D JC");
        PokerHand notTwoPair = new PokerHand("2C 4C 6C 8C TC");

        assertEquals(TWO_PAIR, twoPair.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be two pair");
        assertNotEquals(TWO_PAIR, notTwoPair.getCombinationAtHand().getHandRanking(),  "Expected hand ranking to not be two pair");
    }

    @Test
    void detectSet() {
        PokerHand set = new PokerHand("KH KD KC JC QC");
        PokerHand notSet = new PokerHand("3C 5C 7C 9C KC");

        assertEquals(SET, set.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be a set");
        assertNotEquals(SET, notSet.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be a set");
    }

    @Test
    void detectStraight() {
        PokerHand regularStraight = new PokerHand("9D TC JH QC KC");
        PokerHand wheelStraight = new PokerHand("AC 2C 3D 4C 5S");
        PokerHand notStraight = new PokerHand("KC KD KH AC AS");

        assertEquals(STRAIGHT, regularStraight.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be straight");
        assertEquals(STRAIGHT, wheelStraight.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be straight");
        assertNotEquals(STRAIGHT, notStraight.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be straight");
    }

    @Test
    void detectFlush() {
        PokerHand flush = new PokerHand("3C 5C 7C 9C KC");
        PokerHand notFlush = new PokerHand("5H 6S 7D 8C 9H");

        assertEquals(FLUSH, flush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be a flush");
        assertNotEquals(FLUSH, notFlush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be a flush");
    }

    @Test
    void detectStraightFlush() {
        PokerHand regularStraightFlush = new PokerHand("9C TC JC QC KC");
        PokerHand wheelStraightFlush = new PokerHand("AS 2S 3S 4S 5S");
        PokerHand notStraightFlush = new PokerHand("5H 6S 7D 8C 9H");

        assertEquals(STRAIGHT_FLUSH, regularStraightFlush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be a straight flush");
        assertEquals(STRAIGHT_FLUSH, wheelStraightFlush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be a straight flush");
        assertNotEquals(STRAIGHT_FLUSH, notStraightFlush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be a straight flush");
    }

    @Test
    void detectFullHouse() {
        PokerHand fullHouseHand = new PokerHand("3S 3H 3C 5D 5H");
        PokerHand notFullHouseHand = new PokerHand("3S 3H 4C 5D 5H");

        assertEquals(FULL_HOUSE, fullHouseHand.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be a full house");
        assertNotEquals(FULL_HOUSE, notFullHouseHand.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be a full house");
    }

    @Test
    void detectFourOfAKind() {
        PokerHand fourOfAKindHand = new PokerHand("7S 7H 7C 7D 2H");
        PokerHand notFourOfAKindHand = new PokerHand("7S 7H 7C 2D 2H");

        assertEquals(FOUR_OF_A_KIND, fourOfAKindHand.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be four of a kind");
        assertNotEquals(FOUR_OF_A_KIND, notFourOfAKindHand.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be four of a kind");
    }

    @Test
    void detectRoyalFlush() {
        PokerHand royalFlush = new PokerHand("AC KC QC JC TC");
        PokerHand notRoyalFlush = new PokerHand("AC KC QC JD TC");

        assertEquals(ROYAL_FLUSH, royalFlush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to be a royal flush");
        assertNotEquals(ROYAL_FLUSH, notRoyalFlush.getCombinationAtHand().getHandRanking(), "Expected hand ranking to not be a royal flush");
    }
}

