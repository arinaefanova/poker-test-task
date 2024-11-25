import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PokerHandTest {

    @Test
    void testNoCombination() {
        PokerHand randomHand = new PokerHand("KS 2H 5C JD TD");

        assertFalse(randomHand.isFullHouse(), "Poker Hand should not be a full house");
        assertFalse(randomHand.isFourOfAKind(), "Poker Hand should not be four of a kind");
    }

    @Test
    void testIsFullHouse() {
        PokerHand fullHouseHand = new PokerHand("3S 3H 3C 5D 5H");
        PokerHand notFullHouseHand = new PokerHand("3S 3H 4C 5D 5H");

        assertTrue(fullHouseHand.isFullHouse(), "Poker Hand should be a full house");
        assertFalse(notFullHouseHand.isFullHouse(), "Poker Hand should not be a full house");
    }

    @Test
    void testIsFourOfAKind() {
        PokerHand fourOfAKindHand = new PokerHand("7S 7H 7C 7D 2H");
        PokerHand notFourOfAKindHand = new PokerHand("7S 7H 7C 2D 2H");

        assertTrue(fourOfAKindHand.isFourOfAKind(), "Poker Hand should be four of a kind");
        assertFalse(notFourOfAKindHand.isFourOfAKind(), "Poker Hand should not be four of a kind");
    }


}
