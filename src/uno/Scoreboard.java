
package uno;

/**
 * <p>A Scoreboard is a simple container for player names and their current
 * scores. It can do the obvious things like increment someone's score,
 * find the score for a particular player, and determine the winner at the
 * end of the game.</p>
 * @since 1.0
 */
public class Scoreboard {

    private String playerList[];
    private int scores[];

    /**
     * Instantiate a new Scoreboard object, given an array of player names.
     */
    public Scoreboard(String playerList[]) {
        scores = new int[playerList.length];
        for (int i=0; i<playerList.length; i++) {
            scores[i] = 0;
        }
        this.playerList = playerList;
    }

    /**
     * Award points to a particular player.
     * @param player The zero-based player number who just won a game.
     * @param points The number of points to award.
     */
    public void addToScore(int player, int points) {
        scores[player] += points; 
    }

    /**
     * Obtain the score of a particular player.
     * @param player The zero-based player number whose score is desired.
     */
    public int getScore(int player) {
        return scores[player];
    }

    /**
     * Render the Scoreboard as a string for display during game play.
     */
    public String toString() {
        String retval = "";
        for (int i=0; i<scores.length; i++) {
            retval += "Player #" + i + ": " + scores[i] + "\n";
        }
        return retval;
    }

    /**
     * Return the list of player names.
     */
    public String[] getPlayerList() {
        return playerList;
    }

    /**
     * Return the number of players in the game.
     */
    public int getNumPlayers() {
        return playerList.length; 
    }

    /**
     * Return the zero-based number of the player who has won the game,
     * <i>presuming someone has.</i> (This method should only be called
     * once the end of the entire match has been detected by some other
     * means, and returns the number of the player with the highest score.)
     */
    public int getWinner() {
        int winner = 0;
        int topScore = scores[0];
    
        for (int i=1; i<scores.length; i++) {
            if (scores[i] > topScore) {
                topScore = scores[i];
                winner = i;
            }
        }
        return winner;
    }
}
