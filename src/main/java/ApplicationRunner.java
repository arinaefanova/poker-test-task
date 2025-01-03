import texasholdem.PokerHand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationRunner {

    public static void main(String[] args) {
        List<PokerHand> hands = new ArrayList<>();

        PokerHand pokerHand = new PokerHand("2C 3D AC 4C 5C");
        hands.add(pokerHand);
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
