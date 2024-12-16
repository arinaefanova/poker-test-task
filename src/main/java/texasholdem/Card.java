package texasholdem;

import java.util.Objects;

public class Card {
    private final CardRanks cardRank;
    private final CardSuits cardSuit;

    public Card(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card + ". Each card must be 2 characters long.");
        }
        char rank = card.charAt(0);
        char suit = card.charAt(1);

        if (!CardRanks.isValidRank(rank)) {
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
        if (!CardSuits.isValidSuit(suit)) {
            throw new IllegalArgumentException("Invalid card suit: " + suit);
        }
        this.cardRank = CardRanks.getByLetter(rank);
        this.cardSuit = CardSuits.getByLetter(suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardRank, card.cardRank) &&
                Objects.equals(cardSuit, card.cardSuit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardRank, cardSuit);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardRank=" + cardRank +
                ", cardSuit=" + cardSuit +
                '}';
    }

    public CardSuits getCardSuit() {
        return cardSuit;
    }

    public CardRanks getCardRank() {
        return cardRank;
    }
}
