import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PokerHand implements Comparable {
    private final String[] cards;

    public PokerHand(String cardCombination) {
        String[] splitCards = cardCombination.split("\\s");
        if (splitCards.length != 5) {
            throw new IllegalArgumentException("Poker Hand must contain exactly 5 cards.");
        }
        this.cards = splitCards;
    }

    // масть
    private char getSuit(String card) {
        if (card.length() < 2) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
        return card.charAt(1);
    }

    private char getRank(String card) {
        if (card.isEmpty()) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
        return card.charAt(0);
    }

    private Map<Character, Integer> getCounts(Function<String, Character> function) {
        Map<Character, Integer> counts = new HashMap<>();
        for (String card : cards) {
            char key = function.apply(card);
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
        return counts;
    }

    public Map<Character, Integer> getRankCounts() {
        return getCounts(this::getRank);
    }

    public Map<Character, Integer> getSuitCounts() {
        return getCounts(this::getSuit);
    }

    // Две карты одного ранга. (пара)
    public boolean isOnePair(){
        Map<Character, Integer> rankCounts = getRankCounts();
        return rankCounts.size() == 4 &&
                rankCounts.containsValue(2);
    }

    // Две разные пары. (2 пары)
    public boolean isTwoPair(){
        Map<Character, Integer> rankCounts = getRankCounts();
        return rankCounts.size() == 3 &&
                rankCounts.containsValue(2);
    }

    // Три карты одного ранга. (сет)
    public boolean isSet(){
        Map<Character, Integer> rankCounts = getRankCounts();
        return  rankCounts.size() == 3 &&
                getRankCounts().containsValue(3);
    }

    // Пять последовательных по рангу карт разных мастей. (стрит)
    // *Для упрощения считать, что туз в комбинациях стрит или стрит-флэш может быть только наивысшей картой.
    public boolean isStraight(){
        List<Integer> positions = Arrays.stream(cards)
                .map(card -> CardPositions.cardPositions.get(getRank(card)))
                .sorted().toList();

        boolean regularStraight = true;
        for (int i = 1; i < positions.size(); i++) {
            if (positions.get(i) - positions.get(i - 1) != 1) {
                regularStraight = false;
                break;
            }
        }

        boolean wheelStraight = positions.equals(List.of(2, 3, 4, 5, 14));
        return regularStraight || wheelStraight;

    }

    // Пять карт одной масти, не обязательно по порядку.(флеш)
    public boolean isFlush(){
        return getSuitCounts().containsValue(5);
    }

    // Три карты одного ранга и две карты другого.(фулл-хаус)
    public boolean isFullHouse(){
        Map<Character, Integer> rankCounts = getRankCounts();

        return rankCounts.size() == 2 &&
                rankCounts.containsValue(3) &&
                rankCounts.containsValue(2);
    }

    // Четыре карты одного ранга (каре)
    public boolean isFourOfAKind(){
        Map<Character, Integer> rankCounts = getRankCounts();

        return rankCounts.size() == 2 &&
                rankCounts.containsValue(4);
    }

    // Пять последовательных по рангу карт одной масти (стрит-флеш)
    // *Для упрощения считать, что туз в комбинациях стрит или стрит-флэш может быть только наивысшей картой.
    public boolean isStraightFlush(){
       return isStraight() && isFlush();
    }

    // стрит-флеш от десятки до туза одной масти (роял-флеш)
    public boolean isRoyalFlush(){
        Set<Character> cardSet = Arrays.stream(cards)
                .map(this::getRank)
                .collect(Collectors.toSet());
        Set<Character> royalRanks = Set.of('A', 'K', 'Q', 'J', 'T');
        return isStraightFlush() && cardSet.containsAll(royalRanks);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
