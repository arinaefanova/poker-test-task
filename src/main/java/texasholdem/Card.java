package texasholdem;

import java.util.Objects;

public class Card implements Comparable<Card> {
    private final CardRank rank;
    private final CardSuit suit;

    public Card(String card) {
        if (card.length() != 2) {
            throw new IllegalArgumentException("Invalid card format: " + card + ". Each card must be 2 characters long.");
        }
        this.rank = CardRank.of(card.charAt(0));
        this.suit = CardSuit.of(card.charAt(1));
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
        return "" + rank.getLetter() + suit.getSuit();
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardRank getRank() {
        return rank;
    }

    public int getWeight() {
        return this.rank.getWeight();
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.getWeight(), o.getWeight());
    }
}
