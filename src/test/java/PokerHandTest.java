import org.junit.jupiter.api.Test;
import texasholdem.PokerHand;

import static org.junit.jupiter.api.Assertions.*;
import static texasholdem.HandRankings.*;

public class PokerHandTest {

    @Test
    void testCompareToIdenticalHands() {
        PokerHand sameCard1 = new PokerHand("KC 9D 8S 6H 5C");
        PokerHand sameCard2 = new PokerHand("KC 9D 8S 6H 5C");

        assertEquals(0, sameCard1.compareTo(sameCard2), "Hands should be equal");
    }

    @Test
    void testCompareToDifferentHandsSameRank() {
        PokerHand withoutCombination1 = new PokerHand("KC 9D 8S 6H 5C");
        PokerHand withoutCombination2 = new PokerHand("AH 9D 8S 6H 5C");

        assertTrue(withoutCombination1.compareTo(withoutCombination2) > 0, "First hand should be greater");
        assertTrue(withoutCombination2.compareTo(withoutCombination1) < 0, "Second hand should be lesser");
    }

    @Test
    void testCompareToDifferentHandsDifferentRanks() {
        PokerHand flushHand = new PokerHand("3C 5C 7C 9C KC");
        PokerHand straightHand = new PokerHand("9D TC JH QC KC");

        assertTrue(straightHand.compareTo(flushHand) > 0, "Straight hand should be lesser than Flush hand");
        assertTrue(flushHand.compareTo(straightHand) < 0, "Flush hand should be greater than Straight hand");
    }

    @Test
    void testCompareToRoyalFlushVsFourOfAKind() {
        PokerHand royalFlushHand = new PokerHand("AC KC QC JC TC");
        PokerHand fourOfAKindHand = new PokerHand("7S 7H 7C 7D 2H");

        assertTrue(fourOfAKindHand.compareTo(royalFlushHand) > 0, "Four of a Kind should be lesser than Royal Flush");
        assertTrue(royalFlushHand.compareTo(fourOfAKindHand) < 0, "Royal Flush should be greater than Four of a Kind");
    }

    @Test
    void testCompareToWithNull() {
        PokerHand hand = new PokerHand("KH JD 9C 3S 2D");

        assertThrows(NullPointerException.class, () -> hand.compareTo(null), "Comparing with null should throw NullPointerException");
    }

    @Test
    void testNoCombination() {
        PokerHand highCard = new PokerHand("KS 2H 5C JD TD");
        PokerHand notHighCard = new PokerHand("3C 3D 5C 5D 9C");

        assertEquals(highCard.getHandRanking(), HIGH_CARD, "Expected hand ranking to be high card");
        assertNotEquals(notHighCard.getHandRanking(), HIGH_CARD, "Expected hand ranking to not be high card");
    }

    @Test
    void testIsOnePair() {
        PokerHand onePair = new PokerHand("3C 3D JC QC AC");
        PokerHand notOnePair = new PokerHand("4S 5C 6H 8D 9S");

        assertEquals(onePair.getHandRanking(), ONE_PAIR, "Expected hand ranking to be one pair");
        assertNotEquals(notOnePair.getHandRanking(), ONE_PAIR, "Expected hand ranking to not be one pair");
    }

    @Test
    void testIsTwoPair() {
        PokerHand twoPair = new PokerHand("4C 4D 9C 9D JC");
        PokerHand notTwoPair = new PokerHand("2C 4C 6C 8C TC");

        assertEquals(twoPair.getHandRanking(), TWO_PAIR, "Expected hand ranking to be two pair");
        assertNotEquals(notTwoPair.getHandRanking(), TWO_PAIR, "Expected hand ranking to not be two pair");
    }

    @Test
    void testIsSet() {
        PokerHand set = new PokerHand("KH KD KC JC QC");
        PokerHand notSet = new PokerHand("3C 5C 7C 9C KC");

        assertEquals(set.getHandRanking(), SET, "Expected hand ranking to be a set");
        assertNotEquals(notSet.getHandRanking(), SET, "Expected hand ranking to not be a set");
    }

    @Test
    void testIsStraight() {
        PokerHand regularStraight = new PokerHand("9D TC JH QC KC");
        PokerHand wheelStraight = new PokerHand("AC 2C 3D 4C 5S");
        PokerHand notStraight = new PokerHand("KC KD KH AC AS");

        assertEquals(regularStraight.getHandRanking(), STRAIGHT, "Expected hand ranking to be straight");
        assertEquals(wheelStraight.getHandRanking(), STRAIGHT, "Expected hand ranking to be straight");
        assertNotEquals(notStraight.getHandRanking(), STRAIGHT, "Expected hand ranking to not be straight");
    }

    @Test
    void testIsFlush() {
        PokerHand flush = new PokerHand("3C 5C 7C 9C KC");
        PokerHand notFlush = new PokerHand("5H 6S 7D 8C 9H");

        assertEquals(flush.getHandRanking(), FLUSH, "Expected hand ranking to be a flush");
        assertNotEquals(notFlush.getHandRanking(), FLUSH, "Expected hand ranking to not be a flush");
    }

    @Test
    void testIsStraightFlush() {
        PokerHand regularStraightFlush = new PokerHand("9C TC JC QC KC");
        PokerHand wheelStraightFlush = new PokerHand("AS 2S 3S 4S 5S");
        PokerHand notStraightFlush = new PokerHand("5H 6S 7D 8C 9H");

        assertEquals(regularStraightFlush.getHandRanking(), STRAIGHT_FLUSH, "Expected hand ranking to be a straight flush");
        assertEquals(wheelStraightFlush.getHandRanking(), STRAIGHT_FLUSH, "Expected hand ranking to be a straight flush");
        assertNotEquals(notStraightFlush.getHandRanking(), STRAIGHT_FLUSH, "Expected hand ranking to not be a straight flush");
    }

    @Test
    void testIsFullHouse() {
        PokerHand fullHouseHand = new PokerHand("3S 3H 3C 5D 5H");
        PokerHand notFullHouseHand = new PokerHand("3S 3H 4C 5D 5H");

        assertEquals(fullHouseHand.getHandRanking(), FULL_HOUSE, "Expected hand ranking to be a full house");
        assertNotEquals(notFullHouseHand.getHandRanking(), FULL_HOUSE, "Expected hand ranking to not be a full house");
    }

    @Test
    void testIsFourOfAKind() {
        PokerHand fourOfAKindHand = new PokerHand("7S 7H 7C 7D 2H");
        PokerHand notFourOfAKindHand = new PokerHand("7S 7H 7C 2D 2H");

        assertEquals(fourOfAKindHand.getHandRanking(), FOUR_OF_A_KIND, "Expected hand ranking to be four of a kind");
        assertNotEquals(notFourOfAKindHand.getHandRanking(), FOUR_OF_A_KIND, "Expected hand ranking to not be four of a kind");
    }

    @Test
    void testIsRoyalFlush() {
        PokerHand royalFlush = new PokerHand("AC KC QC JC TC");
        PokerHand notRoyalFlush = new PokerHand("AC KC QC JD TC");

        assertEquals(royalFlush.getHandRanking(), ROYAL_FLUSH, "Expected hand ranking to be a royal flush");
        assertNotEquals(notRoyalFlush.getHandRanking(), ROYAL_FLUSH, "Expected hand ranking to not be a royal flush");
    }
}

