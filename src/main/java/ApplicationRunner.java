import texasholdem.PokerHand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationRunner {

//    первый символ — это номинал карты. Допустимые значения: 2, 3, 4, 5, 6, 7, 8, 9, T(en), J(ack), Q(ueen), K(ing), A(ce);
//    второй символ — масть. Допустимые значения: S(pades), H(earts), D(iamonds), C(lubs).

    public static void main(String[] args) {

        List<PokerHand> hands = new ArrayList<>();
     /*   PokerHand pokerHand = (new PokerHand("2C 5D 8H 9C KC"));
        System.out.println(pokerHand);*/


        // без комбинаций
        hands.add(new PokerHand("2C 5D 8H 9C KC"));
        hands.add(new PokerHand("2S 3D 5H 7C 9H"));
        hands.add(new PokerHand("2C 3D 5H 8C 9D"));
        hands.add(new PokerHand("2S 3C 4H 6D 7S"));
        hands.add(new PokerHand("2C 3D 5H 8S 9C"));
        hands.add(new PokerHand("2D 3S 4H 6C 7D"));
        hands.add(new PokerHand("3C 4D 6H 9S TC"));
        hands.add(new PokerHand("5H 6S 7D 8C 9H"));
        hands.add(new PokerHand("3S 5C 6D 7H 8D"));
        hands.add(new PokerHand("2H 3C 5S 7D 8H"));
        hands.add(new PokerHand("4S 5C 6H 8D 9S"));
        hands.add(new PokerHand("6D 7S 8C 9H TC"));
        hands.add(new PokerHand("2C 4H 5S 7D 9C"));
        hands.add(new PokerHand("3H 4D 6S 7C 9D"));

        // пара
        hands.add(new PokerHand("2C 2D JC QC AC"));
        hands.add(new PokerHand("2C 2D TC 9C 8C"));
        hands.add(new PokerHand("3C 3D JC QC AC"));
        hands.add(new PokerHand("TC TD QC JC AC"));

        // 2 пары
        hands.add(new PokerHand("3C 3D 5C 5D 9C"));
        hands.add(new PokerHand("4C 4D 9C 9D JC"));
        hands.add(new PokerHand("7C 7D TC TD AC"));

        // сеты
        hands.add(new PokerHand("2C 2D 2H JC QC"));
        hands.add(new PokerHand("3C 3D 3H QC JC"));
        hands.add(new PokerHand("KH KD KC JC QC"));

        // флеш
        hands.add(new PokerHand("2C 4C 6C 8C TC"));
        hands.add(new PokerHand("3C 5C 7C 9C KC"));
        hands.add(new PokerHand("TC JC QC KC AC"));

        // фулл-хаус
        hands.add(new PokerHand("2C 2D 2H 3C 3D"));
        hands.add(new PokerHand("KC KD KH AC AH"));

        // роял флеш
        hands.add(new PokerHand("AC KC QC JC TC"));

        // каре
        hands.add(new PokerHand("6C 6D 6H 6S 3C"));
        hands.add(new PokerHand("9C 9D 9H 9S 8C"));
        hands.add(new PokerHand("2C 2D 2H 2S 9C"));
        hands.add(new PokerHand("3C 3D 3H 3S JC"));
        hands.add(new PokerHand("KH KD KC KS JC"));

        // стриты
        hands.add(new PokerHand("2C 3C 4C 5C 6C"));
        hands.add(new PokerHand("3C 4C 5C 6C 7C"));
        hands.add(new PokerHand("9C TC JC QC KC"));
        hands.add(new PokerHand("AC 2C 3C 4C 5C"));

        // стрит флеш
        hands.add(new PokerHand("2C 3C 4C 5C 6C"));
        hands.add(new PokerHand("3C 4C 5C 6C 7C"));
        hands.add(new PokerHand("5C 6C 7C 8C 9C"));
        hands.add(new PokerHand("9C TC JC QC KC"));
        hands.add(new PokerHand("TC JC QC KC AC"));

    // Реализовать возможность сортировки рук по «силе» (рейтингу / рангу) от сильной к слабой:

        Collections.sort(hands);
        for (PokerHand hand:hands) {
            System.out.println(hand.toString());
        }

      /*  PokerHand highCard = new PokerHand("2C 4D 7H TS KC");
        System.out.println(highCard.toString());

        PokerHand onePair = new PokerHand("2C 4D AC 4C 8C");
        System.out.println(onePair.toString());

        PokerHand twoPairs = new PokerHand("9H 9C 4D 4S TH");
        System.out.println(twoPairs.toString());

        PokerHand set = new PokerHand("9H 9C 9D 4S TH");
        System.out.println(set.toString());


        PokerHand straight = new PokerHand("8C 7D 6H 5S 4C");
        System.out.println(straight.toString());


        PokerHand straight2 = new PokerHand("AC 2D 3H 4S 5C");
        System.out.println(straight2.toString());


        PokerHand flush  = new PokerHand("2H 5H 8H KH TH");
        System.out.println(flush.toString());


        PokerHand fullHouse  = new PokerHand("TS TD TC KH KD");
        System.out.println(fullHouse.toString());

        PokerHand fourOfAKind  = new PokerHand("9S 9H 9D 9C 4S");
        System.out.println(fourOfAKind.toString());

        PokerHand straightFlush  = new PokerHand("7H 6H 5H 4H 3H");
        System.out.println(straightFlush.toString());

        PokerHand straightFlush2  = new PokerHand("AC 2C 3C 4C 5C");
        System.out.println(straightFlush2.toString());

        PokerHand royalFlush = new PokerHand("AH KH QH JH TH");
        System.out.println(royalFlush.toString());*/
    }
}
