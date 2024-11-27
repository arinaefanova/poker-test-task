package texasholdem;
import java.util.*;
import java.util.stream.Collectors;

class PokerHandUtils {

     static HandRankings evaluate(PokerHand hand) {
        return Arrays.stream(HandRankings.values())
                .filter(type -> type.matches(hand))
                .findFirst()
                .orElse(HandRankings.HIGH_CARD);
    }

    static List<Map.Entry<Character, Integer>> createCombination(Map<Character, Integer> rankCounts, Integer value) {
        return rankCounts.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(entry -> Map.entry(entry.getKey(), CardValidator.CARD_POSITIONS.get(entry.getKey())))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    static List<Map.Entry<Character, Integer>> createKickers(Map<Character, Integer> rankCounts, Integer value){
        return rankCounts.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(entry -> Map.entry(entry.getKey(), CardValidator.CARD_POSITIONS.get(entry.getKey())))
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .toList();
    }
    static boolean isHighCard(PokerHand pokerHand) {
        pokerHand.setKickers(createKickers(pokerHand.getRankCounts(), 1));
        return true;
    }

    // Одна пара. (1 пара)
    // 2 карты поместить в комбинацию, 3 карты в кикеры
     static boolean isOnePair(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasOnePair = rankCounts.size() == 4 && rankCounts.containsValue(2);

        if (hasOnePair) {
            pokerHand.setCombination(createCombination(rankCounts, 2));
            pokerHand.setKickers(createKickers(rankCounts, 1));
        }

        return hasOnePair;
    }

    // Две разные пары. (2 пары)
    // 4 карты поместить в комбинацию, 1 карту в кикеры
     static boolean isTwoPair(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasTwoPair = rankCounts.size() == 3 && rankCounts.containsValue(2);

        if (hasTwoPair) {
            pokerHand.setCombination(createCombination(rankCounts, 2));
            pokerHand.setKickers(createKickers(rankCounts, 1));
        }

        return hasTwoPair;
    }

    // Три карты одного ранга. (сет)
    // 3 карты поместить в комбинацию, 2 карты в кикеры
     static boolean isSet(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasSet = rankCounts.size() == 3 && pokerHand.getRankCounts().containsValue(3);

        if (hasSet) {
            pokerHand.setCombination(createCombination(rankCounts, 3));
            pokerHand.setKickers(createKickers(rankCounts, 1));
        }

        return hasSet;
    }

    // Пять последовательных по рангу карт разных мастей. (стрит)
    // 5 карт поместить в комбинацию, кикеры - пустые
     static boolean isStraight(PokerHand pokerHand) {
        List<Integer> positions = Arrays.stream(pokerHand.getCards())
                .map(card -> CardValidator.CARD_POSITIONS.get(pokerHand.getRank(card)))
                .sorted().toList();

        boolean regularStraight = true;
        for (int i = 1; i < positions.size(); i++) {
            if (positions.get(i) - positions.get(i - 1) != 1) {
                regularStraight = false;
                break;
            }
        }
        boolean wheelStraight = positions.equals(List.of(2, 3, 4, 5, 14));

        if (regularStraight || wheelStraight) {
            Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
            List<Map.Entry<Character, Integer>> combination = createCombination(rankCounts, 1);
            if (wheelStraight) {
                combination.stream()
                        .filter(entry -> entry.getKey() == 'A')
                        .findFirst()
                        .ifPresent(entry -> combination.replaceAll(e -> e.getKey() == 'A' ? new AbstractMap.SimpleEntry<>(e.getKey(), 1) : e));
            }
            pokerHand.setCombination(combination);
        }
        return regularStraight || wheelStraight;
    }

    // Пять карт одной масти, не обязательно по порядку.(флеш)
    // 5 карт поместить в комбинацию, кикеры - пустые
     static boolean isFlush(PokerHand pokerHand) {
        Map<Character, Integer> suitCounts = pokerHand.getSuitCounts();
        boolean hasFlush = suitCounts.containsValue(5);

        if (hasFlush) {
            Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
            pokerHand.setCombination(createCombination(rankCounts, 1));
        }
        return hasFlush;
    }

    // Три карты одного ранга и две карты другого.(фулл-хаус)
    // 3 карты поместить в комбинацию, 2 карты в кикеры
     static boolean isFullHouse(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasFullHouse  =
                rankCounts.size() == 2 &&
                rankCounts.containsValue(3) &&
                rankCounts.containsValue(2);

        if (hasFullHouse){
            pokerHand.setCombination(createCombination(rankCounts, 3));
            pokerHand.setKickers(createKickers(rankCounts, 2));
        }
        return hasFullHouse;
    }

    // Четыре карты одного ранга (каре)
    // 4 карты поместить в комбинацию, 1 карту в кикеры
     static boolean isFourOfAKind(PokerHand pokerHand) {
        Map<Character, Integer> rankCounts = pokerHand.getRankCounts();
        boolean hasFourOfAKind = rankCounts.size() == 2 && rankCounts.containsValue(4);

        if (hasFourOfAKind){
            pokerHand.setCombination(createCombination(rankCounts, 4));
            pokerHand.setKickers(createKickers(rankCounts, 1));
        }

        return hasFourOfAKind;
    }

    // Пять последовательных по рангу карт одной масти (стрит-флеш)
    // 5 карт поместить в комбинацию, кикеры - пустые
    // todo на заметку, что порядок вызовов этих функций важен, тк они переопределяют комбинацию
    static boolean isStraightFlush(PokerHand pokerHand) {
        return isFlush(pokerHand) && isStraight(pokerHand);
    }

    // стрит-флеш от десятки до туза одной масти (роял-флеш)
    // 5 карт поместить в комбинацию, кикеры - пустые
    // работает только если вызвать перед стрит флэш
     static boolean isRoyalFlush(PokerHand pokerHand) {
        Set<Character> cardSet = Arrays.stream(pokerHand.getCards())
                .map(pokerHand::getRank)
                .collect(Collectors.toSet());
        System.out.println("from royal flash: " + cardSet);
        Set<Character> royalRanks = Set.of('A', 'K', 'Q', 'J', 'T');
        return isFlush(pokerHand) && cardSet.containsAll(royalRanks);
    }
}
