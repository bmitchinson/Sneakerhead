/**
 * A simple class to represent the suit and value help in a standard playing
 * card. Held collectively in {@link Pile} objects.
 *
 * @see Pile
 */
public class Card {
    private char suit;
    private char value;

    /**
     * Initalize a card with a suit and value.
     *
     * @param suit  suit of standard playing card represented
     * @param value value of standard playing card represented
     */
    Card(char suit, char value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * @return get the suit of the card
     */
    public char getSuit() {
        return suit;
    }

    /**
     * @return get the value of the card
     */
    public char getValue() {
        return value;
    }

    /**
     * Print the suit and value of the card
     */
    public void printCard() {
        System.out.println("S:" + suit + " V:" + value);
    }


}