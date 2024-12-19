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
        assertEquals(HandRanking.HIGH_CARD, pokerHandEvaluator.evaluate(hand), "The hand should be High Card.");
    }

    @Test
    void evaluateOnePair() {
        hand = new PokerHand("AS AH 9S 8D 7H");
        assertEquals(HandRanking.ONE_PAIR, pokerHandEvaluator.evaluate(hand), "The hand should be One Pair.");
    }

    @Test
    void evaluateTwoPair() {
        hand = new PokerHand("AS AH 9S 9D 5H");
        assertEquals(HandRanking.TWO_PAIR, pokerHandEvaluator.evaluate(hand), "The hand should be Two Pair.");
    }

    @Test
    void evaluateSet() {
        hand = new PokerHand("AS AH AC 9D 5H");
        assertEquals(HandRanking.SET, pokerHandEvaluator.evaluate(hand), "The hand should be a Set.");
    }

    @Test
    void evaluateFlush() {
        hand = new PokerHand("2S 4S 6S 8S TS");
        assertEquals(HandRanking.FLUSH, pokerHandEvaluator.evaluate(hand), "The hand should be a Flush.");
    }

    @Test
    void evaluateStraight() {
        hand = new PokerHand("6S 7D 8H 9S TS");
        assertEquals(HandRanking.STRAIGHT, pokerHandEvaluator.evaluate(hand), "The hand should be Straight.");
    }


    @Test
    void evaluateFullHouse() {
        hand = new PokerHand("KS KH KC 9S 9H");
        assertEquals(HandRanking.FULL_HOUSE, pokerHandEvaluator.evaluate(hand), "The hand should be a Full House.");
    }

    @Test
    void evaluateFourOfAKind() {
        hand = new PokerHand("KS KH KC KD 9S");
        assertEquals(HandRanking.FOUR_OF_A_KIND, pokerHandEvaluator.evaluate(hand), "The hand should be Four of a Kind.");
    }

    @Test
    void evaluateStraightFlush() {
        hand = new PokerHand("5S 6S 7S 8S 9S");
        assertEquals(HandRanking.STRAIGHT_FLUSH, pokerHandEvaluator.evaluate(hand), "The hand should be a Straight Flush.");
    }

    @Test
    void evaluateRoyalFlush() {
        hand = new PokerHand("TS JS QS KS AS");
        assertEquals(HandRanking.ROYAL_FLUSH, pokerHandEvaluator.evaluate(hand), "The hand should be Royal Flush.");
    }
}
