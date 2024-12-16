package texasholdem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokerHandComparatorTest {

    private PokerHandComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new PokerHandComparator();
    }

    @Test
    void compare_ShouldReturnNegativeWhenHand1IsHigher() {
        PokerHand hand1 = new PokerHand("AS KS QS JS TS");
        PokerHand hand2 = new PokerHand("KH QH JH TH 9H");

        int result = comparator.compare(hand1, hand2);

        assertTrue(result < 0, "Hand1 should be higher than Hand2");
    }

    @Test
    void compare_ShouldReturnPositiveWhenHand2IsHigher() {
        PokerHand hand1 = new PokerHand("AH KH QH JH 9H");
        PokerHand hand2 = new PokerHand("AS KS QS JS TS");

        int result = comparator.compare(hand1, hand2);

        assertTrue(result > 0, "Hand2 should be higher than Hand1");
    }

    @Test
    void compare_ShouldReturnZeroWhenHandsHaveSameWeightAndCombination() {
        PokerHand hand1 = new PokerHand("AS KS QS JS TS");
        PokerHand hand2 = new PokerHand("AH KH QH JH TH");

        int result = comparator.compare(hand1, hand2);

        assertEquals(0, result, "Hands should be considered equal");
    }

    @Test
    void compare_ShouldReturnNegativeWhenHand1HasHigherKicker() {
        PokerHand hand1 = new PokerHand("AS KS QS JS TS");
        PokerHand hand2 = new PokerHand("AS KS QS JS 9S");

        int result = comparator.compare(hand1, hand2);

        assertTrue(result < 0, "Hand1 should be higher than Hand2 based on kickers");
    }

    @Test
    void compare_ShouldReturnPositiveWhenHand2HasHigherKicker() {
        PokerHand hand1 = new PokerHand("AS KS QS JS 9S");
        PokerHand hand2 = new PokerHand("AS KS QS JS TS");

        int result = comparator.compare(hand1, hand2);

        assertTrue(result > 0, "Hand2 should be higher than Hand1 based on kickers");
    }
}
