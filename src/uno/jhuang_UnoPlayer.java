
package uno;

import java.util.List;

public class jhuang_UnoPlayer implements UnoPlayer {

    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state) {

        int i = 0;

        //Returns card for Wild calledColors
        for ( i = 0; i < hand.size(); i++ ) {
            if ( upCard.getColor() == Color.NONE ) {
                if ( hand.get(i).getColor() == calledColor && hand.get(i).getNumber() > 5 ) {
                    return i;
                }
                if ( hand.get(i).getColor() == calledColor && hand.get(i).getNumber() <= 5 ) {
                    return i;
                }
            }
        }
        //Returns Reverse, Skip, or Draw Two
        for ( i = 0; i < hand.size(); i++ )
        {
            if ( hand.get(i).getRank() == upCard.getRank() && upCard.getRank() != Rank.NUMBER &&
                 upCard.getColor() != Color.NONE ) {
                return i;
            }
        }
        //Returns higher number than upCard.getNumber()
        for ( i = 0; i < hand.size(); i++ )
        {
            if ( hand.get(i).getColor() == upCard.getColor() && hand.get(i).getNumber() >
                 upCard.getNumber())
            {
                return i;
            }
        }
        //Returns same number than upCard.getNumber()
        for ( i = 0; i < hand.size(); i++ )
        {
            if ( hand.get(i).getNumber() == upCard.getNumber() && hand.get(i).getNumber() != -1 )
            {
                return i;
            }
        }
        //Returns same color as upCard.getColor()
        for ( i = 0; i < hand.size(); i++ )
        {
            if ( hand.get(i).getColor() == upCard.getColor() && hand.get(i).getNumber() > 5 )
            {
                return i;
            }
            if ( hand.get(i).getColor() == upCard.getColor() && hand.get(i).getNumber() <= 5 )
            {
                return i;
            }
        }
        //Returns Wild
        for ( i = 0; i < hand.size(); i++ )
        {
            if ( hand.get(i).getColor() == Color.NONE )
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
        int numColor [ ] = new int[4];
        int highestColor = 0;
        Color calledColor = Color.RED;

        //Counts number of reds
        for ( i = 0; i < hand.size(); i++)
        {
            if ( hand.get(i).getColor() == Color.RED)
            {
                numColor[0]++;
            }
        }
        //Counts number of blues
        for ( i = 0; i < hand.size(); i++)
        {
            if ( hand.get(i).getColor() == Color.BLUE)
            {
                numColor[1]++;
            }
        }
        //Counts number of greens
        for ( i = 0; i < hand.size(); i++)
        {
            if ( hand.get(i).getColor() == Color.GREEN)
            {
                numColor[2]++;
            }
        }
        //Counts number of yellows
        for ( i = 0; i < hand.size(); i++)
        {
            if ( hand.get(i).getColor() == Color.YELLOW)
            {
                numColor[3]++;
            }
        }
        //Finds color with highest number of cards
        for ( i = 0; i < numColor.length; i++)
        {
            if ( numColor[i] > highestColor )
            {
                highestColor = numColor[i];
            }
        }
        //Calls color with highest number of cards
        if ( highestColor == numColor[0])
        {
            calledColor = Color.RED;
        }
        else if ( highestColor == numColor[1])
        {
            calledColor = Color.BLUE;
        }
        else if ( highestColor == numColor[2])
        {
            calledColor = Color.GREEN;
        }
        else if ( highestColor == numColor[3])
        {
            calledColor = Color.YELLOW;
        }

        return calledColor;
    }
 
}