import java.util.ArrayList;
import java.util.List;

public class ApplicationRunner {

//    первый символ — это номинал карты. Допустимые значения: 2, 3, 4, 5, 6, 7, 8, 9, T(en), J(ack), Q(ueen), K(ing), A(ce);
//    второй символ — масть. Допустимые значения: S(pades), H(earts), D(iamonds), C(lubs).

    public static void main(String[] args) {

        List<PokerHand> hands = new ArrayList<>();
        hands.add(new PokerHand("KS 2H 5C JD TD"));
        hands.add(new PokerHand("2C 3C AC 4C 5C"));

//        Collections.sort(hands);

//        for (PokerHand hand:hands) {
//            System.out.println(hand.toString());
//        }

        PokerHand pokerHand2 = new PokerHand("TC KC QC JC AC");
        System.out.println( pokerHand2.isRoyalFlush());
    }
}
