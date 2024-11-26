import java.util.*;
import java.util.function.Function;

public class PokerHand implements Comparable<PokerHand> {

    private final String[] cards;
    private HandRankings handRanking;
    private Integer weight;
    private List<Map.Entry<Character, Integer>> combination;
    private List<Map.Entry<Character, Integer>> kickers;


    public PokerHand(String cardCombination) {
        String[] splitCards = cardCombination.split("\\s");
        if (splitCards.length != 5) {
            throw new IllegalArgumentException("Poker Hand must contain exactly 5 cards.");
        }
        this.cards = splitCards;
        this.combination = new ArrayList<>();
        this.kickers = new ArrayList<>();
        calculateWeight();
    }

    public void calculateWeight() {
        this.handRanking = PokerHandUtils.evaluate(this);
        this.weight = handRanking.getWeight();
        Optional<Map.Entry<Character, Integer>> maxEntry = combination.stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        maxEntry.ifPresent(characterIntegerEntry -> this.weight += characterIntegerEntry.getValue());
    }

    public int compareTo(PokerHand other) {
        PokerHandComparator comparator = new PokerHandComparator();
        return comparator.compare(this, other);
    }

    @Override
    public String toString() {
        return "PokerHand{" +
                "cards=" + Arrays.toString(cards) +
                ", handRanking=" + handRanking +
                ", weight=" + weight +
                ", combination=" + combination +
                ", kickers=" + kickers +
                '}';
    }

    public void setKickers(List<Map.Entry<Character, Integer>> kickers) {
        this.kickers = kickers;
    }

    public void setCombination(List<Map.Entry<Character, Integer>> combination) {
        combination.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        this.combination = combination;
    }
    public String[] getCards() {
        return cards;
    }

    public List<Map.Entry<Character, Integer>> getKickers() {
        return kickers;
    }

    public char getSuit(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
        return card.charAt(1);
    }

    public char getRank(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
        return card.charAt(0);
    }

    public Integer getWeight() {
        return weight;
    }

    public List<Map.Entry<Character, Integer>> getCombination() {
        return combination;
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

}


