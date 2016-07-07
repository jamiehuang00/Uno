package uno;

import java.util.List;

public class CallMost_UnoPlayer extends Dumb_UnoPlayer {

    public Color callColor(List<Card> hand) {
        return colorWithMost(hand);
    }

}
