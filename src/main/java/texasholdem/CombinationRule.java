package texasholdem;

import java.util.function.Predicate;

public record CombinationRule(Predicate<PokerHand> condition, HandRankings result) {

}
