package texasholdem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PokerHandEvaluatorTest {

    private PokerHandEvaluator pokerHandEvaluator;
    private PokerHand hand;

    @BeforeEach
    void setUp() {
        pokerHandEvaluator = new PokerHandEvaluator();
    }

    @Test
    void evaluateHighCard() {
        hand = new PokerHand("AS KH 9S 2D 4H");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.HIGH_CARD, handRanking, "The hand should be High Card.");
    }

    @Test
    void evaluateOnePair() {
        hand = new PokerHand("AS AH 9S 8D 7H");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.ONE_PAIR, handRanking, "The hand should be One Pair.");
    }

    @Test
    void evaluateTwoPair() {
        hand = new PokerHand("AS AH 9S 9D 5H");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.TWO_PAIR, handRanking, "The hand should be Two Pair.");
    }

    @Test
    void evaluateSet() {
        hand = new PokerHand("AS AH AC 9D 5H");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.SET, handRanking, "The hand should be a Set.");
    }

    @Test
    void evaluateFlush() {
        hand = new PokerHand("2S 4S 6S 8S TS");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.FLUSH, handRanking, "The hand should be a Flush.");
    }

    @Test
    void evaluateStraight() {
        hand = new PokerHand("6S 7D 8H 9S TS");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.STRAIGHT, handRanking, "The hand should be Straight.");
    }


    @Test
    void evaluateFullHouse() {
        hand = new PokerHand("KS KH KC 9S 9H");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.FULL_HOUSE, handRanking, "The hand should be a Full House.");
    }

    @Test
    void evaluateFourOfAKind() {
        hand = new PokerHand("KS KH KC KD 9S");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRanking, "The hand should be Four of a Kind.");
    }

    @Test
    void evaluateStraightFlush() {
        hand = new PokerHand("5S 6S 7S 8S 9S");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRanking, "The hand should be a Straight Flush.");
    }

    @Test
    void evaluateRoyalFlush() {
        hand = new PokerHand("TS JS QS KS AS");
        HandRanking handRanking = pokerHandEvaluator.evaluate(hand).getHandRanking();
        assertEquals(HandRanking.ROYAL_FLUSH, handRanking, "The hand should be Royal Flush.");
    }
}
