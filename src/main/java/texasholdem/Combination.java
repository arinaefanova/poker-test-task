package texasholdem;

import java.util.TreeSet;

public class Combination {
    private final HandRanking handRanking;
    // The weight always consists of the combination weight + the highest card weight in the combination
    // (this is enough for most sorting and is done for code optimization)
    // However, if both the combination and the highest card are identical,
    // the comparison will continue by the rank within the combination, and then by the rank of the kickers.
    private final Integer weight;
    private final TreeSet<Card> combination;
    private final TreeSet<Card> kickers;

    public Combination(HandRanking handRanking, TreeSet<Card> combination, TreeSet<Card> kickers) {
        this.handRanking = handRanking;
        this.combination = combination;
        this.kickers = kickers;
        this.weight = handRanking.getWeight() +
                combination.stream()
                        .mapToInt(Card::getWeight)
                        .max()
                        .orElse(0);
    }

    public Integer getWeight() {
        return weight;
    }

    public TreeSet<Card> getCombination() {
        return combination;
    }

    public TreeSet<Card> getKickers() {
        return kickers;
    }

    public HandRanking getHandRanking() {
        return handRanking;
    }

    @Override
    public String toString() {
        return "CombinationAtHand{" +
                "handRanking=" + handRanking +
                ", weight=" + weight +
                ", combination=" + combination +
                ", kickers=" + kickers +
                '}';
    }
}
