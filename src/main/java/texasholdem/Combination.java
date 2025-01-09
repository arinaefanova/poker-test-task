package texasholdem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Combination {
    private final HandRanking handRanking;
    // The weight always consists of the combination weight + the highest card weight in the combination
    // (this is enough for most sorting and is done for code optimization)
    // However, if both the combination and the highest card are identical,
    // the comparison will continue by the rank within the combination, and then by the rank of the kickers.
    private final Integer weight;
    private final List<Card> combination;
    private final List<Card> kickers;

    public Combination(HandRanking handRanking, List<Card> combination, List<Card> kickers) {
        this.handRanking = handRanking;
        this.combination = filterCardsForHandCombination(combination);
        this.kickers = kickers;
        this.weight = handRanking.getWeight() +
                combination.stream()
                        .mapToInt(Card::getWeight)
                        .max()
                        .orElse(0);
    }

    private List<Card> filterCardsForHandCombination(List<Card> cards) {
        return switch (handRanking) {
            case ROYAL_FLUSH -> Collections.emptyList();

            case STRAIGHT_FLUSH, STRAIGHT -> cards.stream()
                    .max(Card::compareTo)
                    .map(List::of)
                    .orElse(List.of());

            case FOUR_OF_A_KIND, SET, ONE_PAIR -> cards.stream()
                    .findAny()
                    .map(List::of)
                    .orElse(List.of());

            case FULL_HOUSE, TWO_PAIR -> cards.stream()
                    .collect(Collectors.groupingBy(Card::getWeight))
                    .values().stream()
                    .map(group -> group.get(0))
                    .limit(2)
                    .toList();

            default -> cards;
        };
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
