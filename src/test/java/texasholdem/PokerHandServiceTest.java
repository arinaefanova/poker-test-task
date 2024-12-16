package texasholdem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PokerHandServiceTest {

    private PokerHandService pokerHandService;
    private PokerHand hand;

    @BeforeEach
    void setUp() {
        pokerHandService = PokerHandService.getPokerHandService();
    }

    @Test
    void testCheckHandBeforeWhenAlreadyEstimated() {
        hand = new PokerHand("AS KS QS JS TS");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pokerHandService.evaluate(hand));

        assertEquals("This card was already estimated.", exception.getMessage());
    }

    @Test
    void testEvaluateHighCard() {
        hand = new PokerHand("AS KH 9S 2D 4H");
        assertEquals(HandRankings.HIGH_CARD, hand.getHandRanking(), "The hand should be High Card.");
    }

    @Test
    void testEvaluateOnePair() {
        hand = new PokerHand("AS AH 9S 8D 7H");
        assertEquals(HandRankings.ONE_PAIR, hand.getHandRanking(), "The hand should be One Pair.");
    }

    @Test
    void testEvaluateTwoPair() {
        hand = new PokerHand("AS AH 9S 9D 5H");
        assertEquals(HandRankings.TWO_PAIR, hand.getHandRanking(), "The hand should be Two Pair.");
    }

    @Test
    void testEvaluateSet() {
        hand = new PokerHand("AS AH AC 9D 5H");
        assertEquals(HandRankings.SET, hand.getHandRanking(), "The hand should be a Set.");
    }

    @Test
    void testEvaluateFlush() {
        hand = new PokerHand("2S 4S 6S 8S TS");
        assertEquals(HandRankings.FLUSH, hand.getHandRanking(), "The hand should be a Flush.");
    }

    @Test
    void testEvaluateStraight() {
        hand = new PokerHand("6S 7D 8H 9S TS");
        assertEquals(HandRankings.STRAIGHT, hand.getHandRanking(), "The hand should be Straight.");
    }


    @Test
    void testIsFullHouse() {
        hand = new PokerHand("KS KH KC 9S 9H");
        assertEquals(HandRankings.FULL_HOUSE, hand.getHandRanking(), "The hand should be a Full House.");
    }

    @Test
    void testIsFourOfAKind() {
        hand = new PokerHand("KS KH KC KD 9S");
        assertEquals(HandRankings.FOUR_OF_A_KIND, hand.getHandRanking(), "The hand should be Four of a Kind.");
    }

    @Test
    void testIsStraightFlush() {
        hand = new PokerHand("5S 6S 7S 8S 9S");
        assertEquals(HandRankings.STRAIGHT_FLUSH, hand.getHandRanking(), "The hand should be a Straight Flush.");
    }

    @Test
    void testEvaluateRoyalFlush() {
        hand = new PokerHand("TS JS QS KS AS");
        assertEquals(HandRankings.ROYAL_FLUSH, hand.getHandRanking(), "The hand should be Royal Flush.");
    }
}
