package uno;

import java.util.List;

public class Dumb_UnoPlayer extends StephenUnoPlayer {

    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state) {

        for (int i=0; i<hand.size(); i++) {
            if (canPlayOn(hand.get(i), upCard, calledColor)) {
                return i;
            }
        }
        return -1;
    }

    public Color callColor(List<Card> hand) {
        return Color.RED;
    }

}
