package texasholdem;

public class Card {

    private final CardSuits cardSuit;
    private final CardRanks cardRank;

    public Card(CardSuits cardSuit, CardRanks cardRank) {
        this.cardSuit = cardSuit;
        this.cardRank = cardRank;
    }

    public CardSuits getCardSuit() {
        return cardSuit;
    }

    public CardRanks getCardRank() {
        return cardRank;
    }
}
