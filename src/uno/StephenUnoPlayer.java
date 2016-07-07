
package uno;

import java.util.List;

abstract public class StephenUnoPlayer implements UnoPlayer {

    public UnoPlayer.Color colorWithMost(List<Card> hand) {

        int numCardsOfColor[] = new int[4];
        for (int i=0; i<4; i++) {
            numCardsOfColor[i] = 0;
        }
        for (int i=0; i<hand.size(); i++) {
            if (hand.get(i).getColor() == UnoPlayer.Color.RED) {
                numCardsOfColor[0]++;
            }
            if (hand.get(i).getColor() == UnoPlayer.Color.YELLOW) {
                numCardsOfColor[1]++;
            }
            if (hand.get(i).getColor() == UnoPlayer.Color.GREEN) {
                numCardsOfColor[2]++;
            }
            if (hand.get(i).getColor() == UnoPlayer.Color.BLUE) {
                numCardsOfColor[3]++;
            }
        }
        int mostCardColor = 0;
        for (int i=0; i<4; i++) {
            if (numCardsOfColor[i] > numCardsOfColor[mostCardColor]) {
                mostCardColor = i;
            }
        }
        switch (mostCardColor) {
            case 0:
                return UnoPlayer.Color.RED;
            case 1:
                return UnoPlayer.Color.YELLOW;
            case 2:
                return UnoPlayer.Color.GREEN;
            case 3:
                return UnoPlayer.Color.BLUE;
            default:
                System.out.println("Non-existent color!");
                return UnoPlayer.Color.NONE;
        }
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
