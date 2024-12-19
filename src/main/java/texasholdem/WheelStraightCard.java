package texasholdem;

public class WheelStraightCard extends Card {

    public WheelStraightCard(Card card) {
        super(card.toString());
    }

    @Override
    public int getWeight() {
        if (this.getRank() == CardRank.ACE) {
            return 1;
        }
        return super.getWeight();
    }
}