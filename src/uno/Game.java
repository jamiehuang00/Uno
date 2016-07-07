
package uno;

import java.util.ArrayList;

/**
 * <p>A Game object represents a single game of Uno in an overall match (of
 * possibly many games). Games are instantiated by providing them with a
 * roster of players, including a Scoreboard object through which scores
 * can be accumulated. The play() method then kicks off the game, which
 * will proceed from start to finish and update the Scoreboard. Various
 * aspects of the game's state (<i>e.g.</i>, whether the direction of play
 * is currently clockwise or counterclockwise, whose player's turn is next)
 * can be accessed and controlled through methods on this class.</p>
 * <p>A GameState object can be obtained through the getGameState() method,
 * which allows UnoPlayers to selectively and legally examine certain
 * aspects of the game's state.</p>
 * @since 1.0
 */
public class Game {

    /**
     * The number of cards each player will be dealt at start of game.
     */
    static final int INIT_HAND_SIZE = 7;

    public enum Direction { FORWARDS, BACKWARDS };

    /**
     * An object representing the state of the game at any point in time.
     * Note that much of the "state" is represented in the Game object
     * itself, and that this object provides a double-dispatch vehicle
     * through which select methods can access that state.
     */
    private GameState state;

    /* package-visibility variables to simplify access between Game and
     * GameState classes */
    Deck deck;
    Hand h[];
    Card upCard;
    Direction direction;
    int currPlayer;
    UnoPlayer.Color calledColor;
    Scoreboard scoreboard;
    UnoPlayer.Color mostRecentColorCalled[];

    /**
     * Main constructor to instantiate a Game of Uno. Provided must be two
     * objects indicating the player roster: a Scoreboard, and a class
     * list. This constructor will deal hands to all players, select a
     * non-action up card, and reset all game settings so that play() can
     * be safely called.
     * @param scoreboard A fully-populated Scoreboard object that contains
     * the names of the contestants, in order.
     * @param playerClassList[] An array of Strings, each of which is a
     * fully-qualified package/class name of a class that implements the
     * UnoPlayer interface.
     */
    public Game(Scoreboard scoreboard, ArrayList<String> playerClassList) {
        this.scoreboard = scoreboard;
        deck = new Deck();
        h = new Hand[scoreboard.getNumPlayers()];
        mostRecentColorCalled =
            new UnoPlayer.Color[scoreboard.getNumPlayers()];
        try {
            for (int i=0; i<scoreboard.getNumPlayers(); i++) {
                h[i] = new Hand(playerClassList.get(i),
                    scoreboard.getPlayerList()[i]);
                for (int j=0; j<INIT_HAND_SIZE; j++) {
                    h[i].addCard(deck.draw());
                }
            }
            upCard = deck.draw();
            while (upCard.followedByCall()) {
                deck.discard(upCard);
                upCard = deck.draw();
            }
        }
        catch (Exception e) {
            System.out.println("Can't deal initial hands!");
            System.exit(1);
        }
        direction = Direction.FORWARDS;
        currPlayer =
            new java.util.Random().nextInt(scoreboard.getNumPlayers());
        calledColor = UnoPlayer.Color.NONE;
    }

    private void printState() {
        for (int i=0; i<scoreboard.getNumPlayers(); i++) {
            System.out.println("Hand #" + i + ": " + h[i]);
        }
    }

    /**
     * Return the number of the <i>next</i> player to play, provided the
     * current player doesn't jack that up by playing an action card.
     * @return An integer from 0 to scoreboard.getNumPlayers()-1.
     */
    public int getNextPlayer() {
        if (direction == Direction.FORWARDS) {
            return (currPlayer + 1) % scoreboard.getNumPlayers();
        }
        else {
            if (currPlayer == 0) {
                return scoreboard.getNumPlayers() - 1;
            }
            else {
                return currPlayer - 1;
            }
        }
    }

    /**
     * Go ahead and advance to the next player.
     */
    void advanceToNextPlayer() {
        currPlayer = getNextPlayer();
    }

    /**
     * Change the direction of the game from clockwise to counterclockwise
     * (or vice versa.)
     */
    void reverseDirection() {
        if (direction == Direction.FORWARDS) {
            direction = Direction.BACKWARDS;
        }
        else {
            direction = Direction.FORWARDS;
        }
    }

    /**
     * Play an entire Game of Uno from start to finish. Hands should have
     * already been dealt before this method is called, and a valid up card
     * turned up. When the method is completed, the Game's scoreboard object
     * will have been updated with new scoring favoring the winner.
     */
    public void play() {
        println("Initial upcard is " + upCard + ".");
        try {
            while (true) {
                //print("Hand #" + currPlayer + " (" + h[currPlayer] + ")");
                print(h[currPlayer].getPlayerName() +
                    " (" + h[currPlayer] + ")");
                Card playedCard = h[currPlayer].play(this);
                if (playedCard == null) {
                    Card drawnCard;
                    try {
                        drawnCard = deck.draw();
                    }
                    catch (Exception e) {
                        print("...deck exhausted, remixing...");
                        deck.remix();
                        drawnCard = deck.draw();
                    }
                    h[currPlayer].addCard(drawnCard);
                    if (h[currPlayer].isHuman())
                    	print(h[currPlayer].getPlayerName());
                    print(" has to draw (" + drawnCard + ").");
                    playedCard = h[currPlayer].play(this);
                }
                if (playedCard != null) {
                    if (h[currPlayer].isHuman())
                    	print(h[currPlayer].getPlayerName());
                    print(" plays " + playedCard + " on " + upCard + ".");
                    deck.discard(upCard);
                    upCard = playedCard;
                    if (upCard.followedByCall()) {
                        calledColor = h[currPlayer].callColor(this);
                        mostRecentColorCalled[currPlayer] = calledColor;
                        print(" (and calls " + calledColor +
                            ").");
                    }
                    else {
                        calledColor = UnoPlayer.Color.NONE;
                    }
                }
                if (h[currPlayer].isEmpty()) {
                    int roundPoints = 0;
                    for (int j=0; j<scoreboard.getNumPlayers(); j++) {
                        roundPoints += h[j].countCards();
                    }
                    println("\n" + h[currPlayer].getPlayerName() +
                        " wins! (and collects " + roundPoints + " points.)");
                    scoreboard.addToScore(currPlayer,roundPoints);
                    println("---------------\n" + scoreboard);
                    return;
                }
                if (h[currPlayer].size() == 1) {
                    print(" UNO!");
                }
                println("");
                if (playedCard != null) {
                    playedCard.performCardEffect(this);
                }
                else {
                    advanceToNextPlayer();
                }
            }
        }
        catch (EmptyDeckException e) {
            System.out.println("Deck exhausted! This game is a draw.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void print(String s) {
        if (UnoSimulation.PRINT_VERBOSE) {
            System.out.print(s);
        }
    }

    void println(String s) {
        if (UnoSimulation.PRINT_VERBOSE) {
            System.out.println(s);
        }
    }

    /**
     * Return the GameState object, through which the state of the game can
     * be accessed and safely manipulated.
     */
    public GameState getGameState() {

        return new GameState(this);
    }

    /**
     * Return the Card that is currently the "up card" in the game.
     */
    public Card getUpCard() {
        return upCard;
    }
}
