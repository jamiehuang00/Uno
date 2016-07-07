
package uno;

import java.util.List;

public class rcatahan_UnoPlayer implements UnoPlayer {

    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card. 
     * (Otherwise, it will be -1.)
     *
     * The upCard parameter works the same way, and tells you what the 
     * up card (in the middle of the table) is.
     *
     * The calledColor parameter only has meaning if the up card is a wild,
     * and tells you what color the player who played that wild card called.
     *
     * Finally, the state parameter is a GameState object on which you can 
     * invoke methods if you choose to access certain detailed information
     * about the game (like who is currently ahead, what colors each player
     * has recently called, etc.)
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state) {

        int i = 0;

        //Returns card for Wild calledColors
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( upCard.getColor ( ) == Color.NONE )
            {
                if ( hand.get ( i ).getColor ( ) == calledColor && hand.get ( i ).getNumber ( ) > 5 )
                {
                    return i;
                }
                if ( hand.get ( i ).getColor ( ) == calledColor && hand.get ( i ).getNumber ( ) <= 5 )
                {
                    return i;
                }
            }
        }
        //Returns Reverse, Skip, or Draw Two
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getRank ( ) == upCard.getRank ( ) && upCard.getRank ( ) != Rank.NUMBER &&
                 upCard.getColor ( ) != Color.NONE )
            {
                return i;
            }
        }
        //Returns higher number than upCard.getNumber ( )
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == upCard.getColor ( ) && hand.get ( i ).getNumber ( ) >
                 upCard.getNumber ( ) )
            {
                return i;
            }
        }
        //Returns same number than upCard.getNumber ( )
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getNumber ( ) == upCard.getNumber ( ) && hand.get ( i ).getNumber ( ) != -1 )
            {
                return i;
            }
        }
        //Returns same color as upCard.getColor ( )
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == upCard.getColor ( ) && hand.get ( i ).getNumber ( ) > 5 )
            {
                return i;
            }
            if ( hand.get ( i ).getColor ( ) == upCard.getColor ( ) && hand.get ( i ).getNumber ( ) <= 5 )
            {
                return i;
            }
        }
        //Returns Wild
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == Color.NONE )
            {
                return i;
            }
        }

        return -1;
    }


    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to 
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
    public Color callColor(List<Card> hand) {

        int i = 0;
        int numColor [ ] = new int [ 4 ];
        int highestColor = 0;
        Color calledColor = Color.RED;

        //Counts number of reds
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == Color.RED )
            {
                numColor [ 0 ]++;
            }
        }
        //Counts number of blues
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == Color.BLUE )
            {
                numColor [ 1 ]++;
            }
        }
        //Counts number of greens
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == Color.GREEN )
            {
                numColor [ 2 ]++;
            }
        }
        //Counts number of yellows
        for ( i = 0; i < hand.size ( ); i++ )
        {
            if ( hand.get ( i ).getColor ( ) == Color.YELLOW )
            {
                numColor [ 3 ]++;
            }
        }
        //Finds color with highest number of cards
        for ( i = 0; i < numColor.length; i++ )
        {
            if ( numColor [ i ] > highestColor )
            {
                highestColor = numColor [ i ];
            }
        }
        //Calls color with highest number of cards
        if ( highestColor == numColor [ 0 ] )
        {
            calledColor = Color.RED;
        }
        else if ( highestColor == numColor [ 1 ] )
        {
            calledColor = Color.BLUE;
        }
        else if ( highestColor == numColor [ 2 ] )
        {
            calledColor = Color.GREEN;
        }
        else if ( highestColor == numColor [ 3 ] )
        {
            calledColor = Color.YELLOW;
        }

        return calledColor;
    }
 
}