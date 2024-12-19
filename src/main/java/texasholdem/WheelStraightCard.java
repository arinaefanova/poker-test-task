package texasholdem;

public class WheelStraightCard extends Card {

    public WheelStraightCard(Card card) {
        super(card.toString());
    }

    @Override
    public CardRank getRank() {
        if (super.getRank() == CardRank.ACE) {
            return CardRank.TWO;
        }
        return super.getRank();
    }
}