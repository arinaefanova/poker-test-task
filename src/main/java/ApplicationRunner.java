import texasholdem.PokerHand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationRunner {

    public static void main(String[] args) {
        List<PokerHand> hands = new ArrayList<>();

        PokerHand pokerHand = new PokerHand("2C 3D AC 4C 5C");
        PokerHand fourOfAKindHand1 = new PokerHand("KH KD KC KS JC");
        PokerHand fourOfAKindHand2 = new PokerHand("KH KD KC KS 3C");
        PokerHand flushHand1 = new PokerHand("2H 4H 6H 8H TH");
        PokerHand flushHand2 = new PokerHand("3D 5D 7D 9D JD");
        PokerHand flushHand3 = new PokerHand("3D 5D 7D 8D JD");
        PokerHand flushHand4 = new PokerHand("3D 5D 6D 8D JD");
        PokerHand royalFlush1 = new PokerHand("TH JH QH KH AH");
        PokerHand royalFlush2 = new PokerHand("TD JD QD KD AD");
        PokerHand straightFlush1 = new PokerHand("9H 8H 7H 6H 5H");
        hands.add(pokerHand);
        hands.add(fourOfAKindHand1);
        hands.add(fourOfAKindHand2);
        hands.add(flushHand1);
        hands.add(flushHand2);
        hands.add(flushHand3);
        hands.add(flushHand4);
        hands.add(royalFlush1);
        hands.add(royalFlush2);
        hands.add(straightFlush1);
        hands.add(new PokerHand("KC QC AC JC TC"));
        hands.add(new PokerHand("2C 3C 6C 4C 5C"));
        hands.add(new PokerHand("KS 2H 5C JD TD"));
        hands.add(new PokerHand("2C 3C AC 4C 5C"));
        hands.add(new PokerHand("3S 2H 5C 9D TD"));


        Collections.sort(hands);
        for (PokerHand hand : hands) {
            System.out.println(hand);
        }
    }
}
