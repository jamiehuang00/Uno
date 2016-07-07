
package uno;

import java.util.List;

/**
 * <p>A GameState object provides programmatic access to certain (legal)
 * aspects of an Uno game, so that interested players can take advantage of
 * that information. Note that not all aspects of a game's state
 * (<i>e.g.</i>, the direction of play, whose turn it is next, the actual
 * cards in each player's hand (!), etc.) are reflected in the GameState
 * object -- only those for which it makes sense for a player to have
 * access.</p>
 * @since 2.0
 */
public class GameState {

    private Game theGame;
    private int[] numCardsInHandsOfUpcomingPlayers;
    private UnoPlayer.Color[] mostRecentColorCalledByUpcomingPlayers;
    private int[] totalScoreOfUpcomingPlayers;

    /**
     * (Blank constructor, used only during testing.)
     */
    GameState() {
        numCardsInHandsOfUpcomingPlayers = new int[4];
        mostRecentColorCalledByUpcomingPlayers = new UnoPlayer.Color[4];
        totalScoreOfUpcomingPlayers = new int[4];
    }

    /**
     * Instantiate a new GameState object whose job it is to provide safe
     * access to the Game object passed.
     */
    GameState(Game game) {

        numCardsInHandsOfUpcomingPlayers =
            new int[game.scoreboard.getNumPlayers()];
        mostRecentColorCalledByUpcomingPlayers =
            new UnoPlayer.Color[game.scoreboard.getNumPlayers()];
        totalScoreOfUpcomingPlayers =
            new int[game.scoreboard.getNumPlayers()];

        if (game.direction == Game.Direction.FORWARDS) {
            for (int i=0; i<game.h.length; i++) {
                numCardsInHandsOfUpcomingPlayers[i] =
                    game.h[(game.currPlayer + i + 1) %
                        game.scoreboard.getNumPlayers()].size();
                totalScoreOfUpcomingPlayers[i] =
                    game.scoreboard.getScore((game.currPlayer + i + 1) %
                        game.scoreboard.getNumPlayers());
                mostRecentColorCalledByUpcomingPlayers[i] =
                    game.mostRecentColorCalled[(game.currPlayer + i + 1) %
                        game.scoreboard.getNumPlayers()];
            }
        }
        else {
            for (int i=0; i<game.h.length; i++) { // FIXTHIS
                numCardsInHandsOfUpcomingPlayers[i] =
                    game.h[(game.currPlayer - i - 1 +
                        game.scoreboard.getNumPlayers()) %
                        game.scoreboard.getNumPlayers()].size();
                totalScoreOfUpcomingPlayers[i] =
                    game.scoreboard.getScore((game.currPlayer - i - 1 +
                        game.scoreboard.getNumPlayers()) %
                        game.scoreboard.getNumPlayers());
                mostRecentColorCalledByUpcomingPlayers[i] =
                    game.mostRecentColorCalled[(game.currPlayer - i - 1 +
                        game.scoreboard.getNumPlayers()) %
                        game.scoreboard.getNumPlayers()];
            }
        }
        theGame = game;
    }

    /**
     * Return an array of ints indicating the number of cards each player
     * has remaining. The array is ordered so that index 0 has the count
     * for the player who (barring action cards that might change it) will
     * play next, index 1 the player who (barring action cards) will play
     * second, etc.
     */
    public int[] getNumCardsInHandsOfUpcomingPlayers() {
        return numCardsInHandsOfUpcomingPlayers;
    }

    /**
     * Return an array of ints indicating the total overall score each
     * player has. The array is ordered so that index 0 has the count
     * for the player who (barring action cards that might change it) will
     * play next, index 1 the player who (barring action cards) will play
     * second, etc.
     */
    public int[] getTotalScoreOfUpcomingPlayers() {
        return numCardsInHandsOfUpcomingPlayers;
    }

    /**
     * Return the color most recently "called" (after playing a wild) by
     * each opponent. If a given opponent has not played a wild card this
     * game, the value will be Color.NONE. The array is ordered so that
     * index 0 has the count for the player who (barring action cards that
     * might change it) will play next, index 1 the player who (barring
     * action cards) will play second, etc.
     */
    public UnoPlayer.Color[] getMostRecentColorCalledByUpcomingPlayers() {
        return mostRecentColorCalledByUpcomingPlayers;
    }

    /**
     * Return a list of <i>all</i> cards that have been played since the
     * last time the deck was remixed. This allows players to "card count"
     * if they choose.
     */
    public List<Card> getPlayedCards() {
        if (theGame != null) {
            return theGame.deck.getDiscardedCards();
        }
        else {
            // testing only
            return new java.util.ArrayList<Card>();
        }
    }
}
