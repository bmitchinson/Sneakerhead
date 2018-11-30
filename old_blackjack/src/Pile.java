import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Pile class represents a pile of standard playing cards. It holds information
 * for each set of possible cards, and has the capability to shuffle it's contents
 * using {@link #shufflePile()} and remove amounts of cards from the pile
 * using a "pop" functionality in {@link #removeFromPile(int)}.
 *
 * @see #shufflePile()
 * @see #removeFromPile(int)
 */
public class Pile {

    private Card[] cardsInPile;
    // Hearts, Diamonds, Spades, Clubs
    private char[] suits = {'H', 'D', 'S', 'C'};
    //T means 10
    private char[] values = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
    private int insertIndex = 0;

    /**
     * When a pile is created using the "deckMode" constructor, it's aromatically
     * filled with 52 standard playing cards, and shuffled using {@link #shufflePile()}
     *
     * @param deckMode if deckMode should be enabled as detailed above.
     * @see #shufflePile()
     */
    Pile(Boolean deckMode) {
        cardsInPile = new Card[52];
        if (deckMode) {
            for (char suit : suits) {
                for (char value : values) {
                    addToPile(new Card(suit, value));
                }
            }
            shufflePile();
        }
    }

    /**
     * A constructor to enable a pile with a default card array given in parameters
     *
     * @param incomingCards the cards that the pile should hold
     */
    Pile(Card[] incomingCards) {
        cardsInPile = new Card[52];
        addToPile(incomingCards);
    }

    /**
     * Unused, but prints the contents of what's currently held in the pile
     */
    public void printPile() {
        int i = 0;
        for (Card card : cardsInPile) {
            if (card != null) {
                System.out.println("Pos:" + i +
                        " S:" + card.getSuit() +
                        " V:" + card.getValue());
                i++;
            }
        }
    }

    /**
     * Returns the currently held cards represented as an Array of strings.
     *
     * @return array of strings used for message delivery in {@link Dealer}
     */
    public String[] pileAsStrings() {
        String[] result = new String[insertIndex];
        for (int i = 0; i < insertIndex; i++) {
            result[i] = "" + cardsInPile[i].getSuit()
                    + cardsInPile[i].getValue();
        }
        return result;
    }

    /**
     * Uses the {@link Collections} library to shuffle all cards currently held
     * in the pile
     */
    public void shufflePile() {
        List<Card> temp = Arrays.asList(cardsInPile);
        Collections.shuffle(temp);
        cardsInPile = (Card[]) temp.toArray();
    }

    /**
     * Adds cards to the pile if within range of 52 cap
     *
     * @param cardsToAdd cards to be added to the pile
     */
    public void addToPile(Card[] cardsToAdd) {
        if (Array.getLength(cardsToAdd) + insertIndex > 52) {
            System.out.println("Cannot add cards, would overflow.");
        } else {
            for (Card card : cardsToAdd) {
                cardsInPile[insertIndex] = card;
                insertIndex++;
            }
        }
    }

    /**
     * Uses {@link #addToPile(Card[])} to add a single card to the pile
     *
     * @param cardToAdd single card to be added to pile
     */
    public void addToPile(Card cardToAdd) {
        Card[] singleCardArray = {cardToAdd};
        addToPile(singleCardArray);
    }

    /**
     * Uses {@link #addToPile(Card[])} to add the contents of another Pile to
     * this pile.
     *
     * @param pileToAdd pile to be added to this pile
     */
    public void addToPile(Pile pileToAdd) {
        String[] pileToAddAsString = pileToAdd.pileAsStrings();
        Card[] pileToAddAsCards = new Card[pileToAddAsString.length];
        int i = 0;
        for (String card : pileToAddAsString) {
            pileToAddAsCards[i] = new Card(card.charAt(0), card.charAt(1));
        }
        addToPile(pileToAddAsCards);
    }

    /**
     * Method to remove a select amount of cards from the pile, and return them
     * as an array. A "pop" method.
     *
     * @param amount amount of cards to be "popped"
     * @return array of cards "popped" from top of Pile
     */
    public Pile removeFromPile(int amount) {
        Card[] removed = new Card[amount];
        if (amount > insertIndex) {
            System.out.println(insertIndex + " Cards in pile, " +
                    "can't remove " + amount);
            return null;
        } else {
            for (int i = 0; i < amount; i++) {
                insertIndex--;
                removed[i] = cardsInPile[insertIndex];
                cardsInPile[insertIndex] = null;
            }
            Pile returnPile = new Pile(false);
            returnPile.addToPile(removed);
            return returnPile;
        }
    }

    /**
     * Calculate the current total of cards according to the rules of Blackjack.
     * Accounts for if an ace is held, to lower it's value to 1 instead of 11.
     *
     * @return Blackjack total of cards held in pile
     */
    public int getBlackjackTotal() {
        int total = 0;
        boolean hasAce = false;
        for (Card card : cardsInPile) {
            if (card != null) {
                char value = card.getValue();
                if (value == 'A') {
                    total += 11;
                    hasAce = true;
                } else if (value == 'T' || value == 'K' ||
                        value == 'Q' || value == 'J') {
                    total += 10;
                } else {
                    total += Character.getNumericValue(value);
                }
            }
        }
        if (hasAce && total > 21) {
            total -= 10;
        }
        return total;
    }
}