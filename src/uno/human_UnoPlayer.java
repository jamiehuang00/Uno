package uno;

import java.util.List;

import java.util.Scanner;
import uno.UnoPlayer.Color;
import uno.UnoPlayer.Rank;

public class human_UnoPlayer implements UnoPlayer {

    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state) {
    	
    	Scanner keyboard = new Scanner(System.in);
		int j = -1;
		
    	do {
        	System.out.print("\nEnter the card you'd like to play (or \"draw\" to draw a card) : ");
        	
        	String cardName = keyboard.nextLine();
        	if (cardName.equalsIgnoreCase("draw"))
        		return -1;
        	
        	Card card = cardName2Card(cardName);

    		for (int i=0; i<hand.size(); i++) {
    			if (hand.get(i).isTheSameCard(card))
    				j = i;
    		}
    		if (j==-1) {
    			System.out.println("This card does not appear in your hand!");
    		}
    		else if (canPlayOn(hand.get(j), upCard, calledColor)) {
        		return j;
        	}
        	else {
        		System.out.println("This card is not valid!");
        		j = -1;
        	}
    	} while (j == -1);
    				
        return -1;
    }

    public Color callColor(List<Card> hand) {
    	Scanner keyboard = new Scanner(System.in);

    	do {
    		System.out.print("Choose the color (R Y G B) : ");
    		String color = keyboard.nextLine();
    		
    		switch (color.charAt(0)) {
    		case 'R':
    			return UnoPlayer.Color.RED;
    		case 'Y':
    			return UnoPlayer.Color.YELLOW;
    		case 'G':
    			return UnoPlayer.Color.GREEN;
    		case 'B':
    			return UnoPlayer.Color.BLUE;
    		default:
    			System.out.println("Invalid color!");
    		}
    	} while (true);
    	
    }
    
    public Card cardName2Card(String cardName) {
    	UnoPlayer.Color color = UnoPlayer.Color.NONE;
    	UnoPlayer.Rank rank = UnoPlayer.Rank.NUMBER;
    	int	number = -1;
    	
    	char ch = cardName.charAt(0);
    	switch (ch) {
    		case 'R' :
    			color = UnoPlayer.Color.RED;
    			break;
    		case 'Y' :
    			color = UnoPlayer.Color.YELLOW;
    			break;
    		case 'G' :
    			color = UnoPlayer.Color.GREEN;
    			break;
    		case 'B' :
    			color = UnoPlayer.Color.BLUE;
    			break;
    		case 'W' :
    			color = UnoPlayer.Color.NONE;
    			number = -1;
    			if (cardName.length() == 2)
    				rank = UnoPlayer.Rank.WILD_D4;
    			else
    				rank = UnoPlayer.Rank.WILD;
    			break;
    	}
    	if (color != UnoPlayer.Color.NONE) {
    		ch = cardName.charAt(1);
    		if (Character.isDigit(ch)) {
    			rank = UnoPlayer.Rank.NUMBER;
    			number = ch - '0';
    		}
    		else if (ch == 'R') {
    			rank = UnoPlayer.Rank.REVERSE;
    			number = -1;
    		}
    		else if (ch == 'S') {
    			rank = UnoPlayer.Rank.SKIP;
    			number = -1;
    		}
    		else if (ch == '+' && cardName.charAt(2) == '2') {
    			rank = UnoPlayer.Rank.DRAW_TWO;
    			number = -1;
    		}
    	}
    	
    	return ((number == -1) ? new Card(color, rank) : new Card(color, number));
    }
    
    public boolean canPlayOn(Card card, Card upCard, Color calledColor) {

        if (card.getRank() == Rank.WILD ||
            card.getRank() == Rank.WILD_D4 ||
            card.getColor() == upCard.getColor() ||
            card.getColor() == calledColor ||
            (card.getRank() == upCard.getRank() &&
                card.getRank() != Rank.NUMBER) ||
            card.getNumber() == upCard.getNumber() &&
                card.getRank() == Rank.NUMBER &&
                upCard.getRank() == Rank.NUMBER) {
            return true;
        }
        return false;
    }

}
