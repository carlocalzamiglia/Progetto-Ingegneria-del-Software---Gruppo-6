package it.polimi.ingsw;

import it.polimi.ingsw.Game.ToolCards;

public class TestToolCard {
    @org.junit.jupiter.api.Test
    public void tToolCard() {
        ToolCards toolCards[]=new ToolCards[13];
        for (int i = 0; i < 13; i++) {
            toolCards[i] = new ToolCards(i+1);
            toolCards[i].dump();
        }
    }
}
