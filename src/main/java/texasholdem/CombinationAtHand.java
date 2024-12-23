package texasholdem;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CombinationAtHand {

    private final HandRanking handRanking;
    // The weight always consists of the combination weight + the highest card weight in the combination
    // (this is enough for most sorting and is done for code optimization)
    // However, if both the combination and the highest card are identical,
    // the comparison will continue by the rank within the combination, and then by the rank of the kickers.
    private Integer weight;
    private final List<Card> combination;
    private final List<Card> kickers;

    public CombinationAtHand(HandRanking handRanking, List<Card> combination, List<Card> kickers) {
        this.handRanking = handRanking;
        this.combination = combination;
        this.kickers = kickers;
        this.weight = handRanking.getWeight();

        Optional<Card> maxCard = combination.stream()
                .max(Comparator.comparingInt(Card::getWeight));
        maxCard.ifPresent(card -> this.weight += card.getWeight());
    }

    public Integer getWeight() {
        return weight;
    }

    public List<Card> getCombination() {
        return combination;
    }

    public List<Card> getKickers() {
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
