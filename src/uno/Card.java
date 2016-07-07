package uno;

/**
 * <p>A Card in an Uno deck. Each Card knows its particular type, which is
 * comprised of a 3-tuple (color, rank, number). Not all of these values
 * are relevant for every particular type of card, however; for instance,
 * wild cards have no color (getColor() will return Color.NONE) or number
 * (getNumber() will return -1).</p>
 * <p>A Card knows its forfeit cost (<i>i.e.</i>, how many points it counts
 * against a loser who gets stuck with it) and how it should act during
 * game play (whether it permits the player to change the color, what
 * effect it has on the game state, etc.)</p>
 * @since 1.0
 */
public class Card {

    /**
     * For terminals that support it, setting PRINT_IN_COLOR to true will
     * annotate toString() calls with ANSI color codes. This is known to
     * work on Ubuntu Linux, and known not to work in DOS terminal and
     * NetBeans.
     */
    public static final boolean PRINT_IN_COLOR = false;

    private UnoPlayer.Color color;
    private UnoPlayer.Rank rank;
    private int number;

    /**
     * Constructor for non-number cards (skips, wilds, etc.)
     */
    public Card(UnoPlayer.Color color, UnoPlayer.Rank rank) {
        this.color = color;
        this.rank = rank;
        this.number = -1;
    }

    /**
     * Constructor for number cards.
     */
    public Card(UnoPlayer.Color color, int number) {
        this.color = color;
        this.rank = UnoPlayer.Rank.NUMBER;
        this.number = number;
    }

    /**
     * Constructor to explicitly set entire card state.
     */
    public Card(UnoPlayer.Color color, UnoPlayer.Rank rank, int number) {
        this.color = color;
        this.rank = rank;
        this.number = number;
    }

    /**
     * Render this Card object as a string. Whether the string comes out
     * with ANSI color codes is controlled by the PRINT_IN_COLOR static
     * class variable.
     */
    public String toString() {
        String retval = "";
        if (PRINT_IN_COLOR) {
            switch (color) {
                case RED:
                    retval += "\033[31m";
                    break;
                case YELLOW:
                    retval += "\033[33m";
                    break;
                case GREEN:
                    retval += "\033[32m";
                    break;
                case BLUE:
                    retval += "\033[34m";
                    break;
                case NONE:
                    retval += "\033[1m";
                    break;
            }
        }
        else {
            switch (color) {
                case RED:
                    retval += "R";
                    break;
                case YELLOW:
                    retval += "Y";
                    break;
                case GREEN:
                    retval += "G";
                    break;
                case BLUE:
                    retval += "B";
                    break;
                case NONE:
                    retval += "";
                    break;
            }
        }
        switch (rank) {
            case NUMBER:
                retval += number;
                break;
            case SKIP:
                retval += "S";
                break;
            case REVERSE:
                retval += "R";
                break;
            case WILD:
                retval += "W";
                break;
            case DRAW_TWO:
                retval += "+2";
                break;
            case WILD_D4:
                retval += "W4";
                break;
        }
        if (PRINT_IN_COLOR) {
            retval += "\033[37m\033[0m";
        }
        return retval;
    }

    /**
     * Returns the number of points this card will count against a player
     * who holds it in his/her hand when another player goes out.
     */
    public int forfeitCost() {
        if (rank == UnoPlayer.Rank.SKIP || rank == UnoPlayer.Rank.REVERSE ||
            rank == UnoPlayer.Rank.DRAW_TWO) {
            return 20;
        }
        if (rank == UnoPlayer.Rank.WILD || rank == UnoPlayer.Rank.WILD_D4) {
            return 50;
        }
        if (rank == UnoPlayer.Rank.NUMBER) {
            return number;
        }
        System.out.println("Illegal card!!");
        return -10000;
    }

    /**
     * Returns true only if this Card can legally be played on the up card
     * passed as an argument. The second argument is relevant only if the
     * up card is a wild.
     * @param c An "up card" upon which the current object might (or might
     * not) be a legal play.
     * @param calledColor If the up card is a wild card, this parameter
     * contains the color the player of that color called.
     */ 
    public boolean canPlayOn(Card c, UnoPlayer.Color calledColor) {
        if (rank == UnoPlayer.Rank.WILD ||
            rank == UnoPlayer.Rank.WILD_D4 ||
            color == c.color ||
            color == calledColor ||
            (rank == c.rank && rank != UnoPlayer.Rank.NUMBER) ||
            number == c.number && rank == UnoPlayer.Rank.NUMBER && c.rank == UnoPlayer.Rank.NUMBER)
        {
            return true;
        }
        return false;
    }

    /**
     * Returns true only if playing this Card object would result in the
     * player being asked for a color to call. (In the standard game, this
     * is true only for wild cards.)
     */
    public boolean followedByCall() {
        return rank == UnoPlayer.Rank.WILD || rank == UnoPlayer.Rank.WILD_D4;
    }

    /**
     * This method should be called immediately after a Card is played,
     * and will trigger the effect peculiar to that card. For most cards,
     * this merely advances play to the next player. Some special cards
     * have other effects that modify the game state. Examples include a
     * Skip, which will advance <i>twice</i> (past the next player), or a
     * Draw Two, which will cause the next player to have to draw cards.
     * @param game The Game being played, whose state may be modified by
     * this card's effect.
     * @throws EmptyDeckException Thrown only in very exceptional cases
     * when a player must draw as a result of this card's effect, yet the
     * draw cannot occur because of un-shufflable deck exhaustion.
     */
    void performCardEffect(Game game) throws EmptyDeckException {
        switch (rank) {
            case SKIP:
                game.advanceToNextPlayer();
                game.advanceToNextPlayer();
                break;
            case REVERSE:
                game.reverseDirection();
                game.advanceToNextPlayer();
                break;
            case DRAW_TWO:
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                game.advanceToNextPlayer();
                game.advanceToNextPlayer();
                break;
            case WILD_D4:
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                game.advanceToNextPlayer();
                game.advanceToNextPlayer();
                break;
            default:
                game.advanceToNextPlayer();
                break;
        }
    }

    private void nextPlayerDraw(Game game) throws EmptyDeckException {
        int nextPlayer = game.getNextPlayer();
        Card drawnCard;
        try {
            drawnCard = game.deck.draw();
        }
        catch (EmptyDeckException e) {
            game.print("...deck exhausted, remixing...");
            game.deck.remix();
            drawnCard = game.deck.draw();
        }
        game.h[nextPlayer].addCard(drawnCard);
        //game.println("  Player #" + nextPlayer + " draws " + drawnCard + ".");
        game.println("  " + game.h[nextPlayer].getPlayerName() + " draws " +
            drawnCard + ".");
    }

    /**
     * Returns the color of this card, which is Color.NONE in the case of
     * wild cards.
     */
    public UnoPlayer.Color getColor() {
        return color;
    }

    /**
     * Returns the rank of this card, which is Rank.NUMBER in the case of
     * number cards (calling getNumber() will retrieve the specific
     * number.)
     */
    public UnoPlayer.Rank getRank() {
        return rank;
    }

    /**
     * Returns the number of this card, which is guaranteed to be -1 for
     * non-number cards (cards of non-Rank.NUMBER rank.)
     */
    public int getNumber() {
        return number;
    }
    
    public boolean isTheSameCard(Card card) {
    	return (color == card.getColor() &&
    			rank == card.getRank() &&
    			number == card.getNumber());
    }
}
