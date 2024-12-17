package texasholdem;

import java.util.Objects;

public class Card {
    private final CardRank rank;
    private final CardSuit suit;

    public Card(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card + ". Each card must be 2 characters long.");
        }
        char rank = card.charAt(0);
        char suit = card.charAt(1);

        this.rank = CardRank.of(rank);
        this.suit = CardSuit.of(suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(rank, card.rank) &&
                Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardRank=" + rank +
                ", cardSuit=" + suit +
                '}';
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardRank getRank() {
        return rank;
    }
}
