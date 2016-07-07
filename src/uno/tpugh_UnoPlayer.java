package uno;

import java.util.List;


public class tpugh_UnoPlayer implements UnoPlayer {

    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The handColors, handRanks, and handNumbers parameters tell you what's
     * in your hand. The lengths of these arrays will all be the same, one
     * element for each card. (For instance, if handColors.length is 5, then
     * handRanks.length and handNumbers.length will also be 5, and this means
     * you have 5 cards in your hand.)
     *
     * The values of these three arrays at each index tell you what card that
     * is. handColors will either be the color of the card, or else the value
     * "Color.NONE" if the card at that position is a wild card. handRanks
     * will be "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * handNumbers at a particular index only has meaning if the card is a
     * numbered card, in which case it indicates the number. Otherwise, it
     * will be -1.
     *
     * Examples:
     *
     * Suppose handColors[0] is Color.RED, handRanks[0] is Rank.NUMBER, and
     * handNumbers[0] is 6. This means that the first card in your hand is a
     * red 6.
     *
     * Suppose handColors[1] is Color.GREEN, handRanks[1] is Rank.SKIP, and
     * handNumbers[1] is -1. This means that the next card in your hand is a
     * green Skip.
     *
     * Suppose handColors[2] is Color.NONE, handRanks[2] is Rank.WILD_D4, and
     * handNumbers[2] is -1. This means that the next card in your hand is a
     * Wild Draw 4.
     *
     * The upCard.getColor(), upCardRank, and upCard.getNumber() parameters work the same
     * way, and tell you what card is turned up in the middle of the table.
     *
     * Example:
     *
     * Suppose upCard.getColor() is Color.BLUE, upCardRank is Rank.NUMBER, and
     * upCard.getNumber() is 8. This means that the up card is a Blue 8.
     *
     * Finally, the calledColor parameter only has meaning if the up card is
     * a wild, and tells you what color the player who played that wild card
     * called.
     *
     * Example:
     *
     * Suppose upCard.getColor() is Color.NONE, upCardRank is Rank.WILD,
     * upCard.getNumber() is -1, and calledColor is Color.YELLOW. This means that
     * the up card is a regular wild card, and the person who played it
     * called "yellow" as the color.
     *
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
	
	/*  int cardPlayed = jup.play(hand, 
	            new Card(UnoPlayer.Color.RED, UnoPlayer.Rank.NUMBER, 7),
	            UnoPlayer.Color.RED, new GameState()); */
	/*  public int play(Color[] handColors, Rank[] handRanks, int[] handNumbers,
		        Color upCard.getColor(), Rank upCardRank, int upCard.getNumber(),
		        Color calledColor)
	*/
	

    public int play(List<Card> hand, Card upCard,  Color calledColor,GameState state) {

        //Tallying your color of cards
       int blueAmt = 0;
       int redAmt = 0;
       int greenAmt = 0;
       int yellowAmt = 0;
       for( int i = 0; i < hand.size(); i++){
           if (hand.get(i).getColor().equals(Color.BLUE))
                   ++blueAmt;
           else if (hand.get(i).getColor().equals(Color.GREEN))
                   ++greenAmt;
           else if (hand.get(i).getColor().equals(Color.RED))
                   ++redAmt;
           else if (hand.get(i).getColor().equals(Color.YELLOW))
                    ++yellowAmt;
      }
       int[] allColor = {blueAmt, redAmt, greenAmt, yellowAmt};
        int bestColor = 0;
        
        for (int i=0; i<allColor.length; i++) {
            if (allColor[i] > bestColor) {
                bestColor = allColor[i];
      }
            }
        //Eliminate Wild Draw 4 cards
        for (int i = 0; i < hand.size(); i++){
            if(hand.get(i).getRank().equals(Rank.WILD_D4)) 
                return i;
            }

        //Eliminate Wild cards
       for (int i = 0; i < hand.size(); i++) {
           if((hand.get(i).getRank().equals(Rank.WILD))) {
               return i;
           }
        }

        //Eliminate Draw Two cards
       for (int i = 0; i < hand.size(); i++) {
           if((hand.get(i).getRank().equals(Rank.DRAW_TWO))&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor) || (hand.get(i).getRank() == upCard.getRank())))) {
               return i;
           }
       }
        //Eliminate Skip Cards
       for (int i = 0; i < hand.size(); i++) {
           if ((hand.get(i).getRank().equals(Rank.SKIP)) && (((hand.get(i).getColor() == upCard.getColor()) || (hand.get(i).getColor() == calledColor)) || (hand.get(i).getRank() == upCard.getRank()))){
               return i;
            }
       }
       for (int i = 0; i < hand.size(); i++) {
           if ((hand.get(i).getRank().equals(Rank.REVERSE)) && ((hand.get(i).getColor() == upCard.getColor()) || (hand.get(i).getColor() == calledColor || (hand.get(i).getRank() == upCard.getRank()))))
                   return i;
           }
        
        // Match best color to number
       for (int i = 0; i < hand.size(); i++) {
           if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.RED)) && (redAmt == bestColor)) {
               return i;
           }
            else if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.BLUE)) && (blueAmt == bestColor)) {
                return i ;
            }
            else if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.GREEN)) && (greenAmt == bestColor)) {
                return i ;
                
            }
            else if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.YELLOW)) && (yellowAmt == bestColor)){
             return i ;
            }
               
       }
      
       // Match color, top #'s released first
       
        
             for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 9)&& (hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 8)&& (hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 7)&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 6) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))) {
                     return i;}
                 }
            
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 5) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))) {
                     return i; }
                 }
            
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 4)&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 3)&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 2) && ((hand.get(i).getColor().equals(upCard.getColor()) || (hand.get(i).getColor().equals(calledColor))))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++)
                  if((hand.get(i).getNumber() == 1) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;}
                 
            
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 0) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
        

        

       //Match number
        for(int i = 0; i < hand.size(); i++) {
            if((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1)){
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
 //Tallying your color of cards
	 int blueAmt = 0;
     int redAmt = 0;
     int greenAmt = 0;
     int yellowAmt = 0;
     for( int i = 0; i < hand.size(); i++){
         if (hand.get(i).equals(Color.BLUE))
                 ++blueAmt;
         else if (hand.get(i).equals(Color.GREEN))
                 ++greenAmt;
         else if (hand.get(i).equals(Color.RED))
                 ++redAmt;
         else if (hand.get(i).equals(Color.YELLOW))
                  ++yellowAmt;
    }
     int[] allColor = {blueAmt, redAmt, greenAmt, yellowAmt};
      int bestColor = 0;
      
      for (int i=0; i<allColor.length; i++) {
          if (allColor[i] > bestColor) {
              bestColor = allColor[i];
    }
          }
      if(bestColor == blueAmt)
          return Color.BLUE;
      else if (bestColor == redAmt)
          return Color.RED;
      else if (bestColor == greenAmt)
          return Color.GREEN;
      else if (bestColor == yellowAmt)
          return Color.YELLOW;
  
         return Color.YELLOW;
    
    }




/*	@Override
	public int play(List<Card> hand, Card upCard, Color calledColor,
			GameState state) {
		// TODO Auto-generated method stub
		return 0;
	}
*/


/*
	@Override
	public Color callColor(List<Card> hand) {
		// TODO Auto-generated method stub
		return null;
	}
*/



}
