package itpolimiingsw;

import itpolimiingsw.Game.ToolCards;

public class TestToolCard {
    @org.junit.jupiter.api.Test
    public void tToolCard() {
        for(int i=0;i<30;i++) {
            System.out.println("numero: "+i);
            ToolCards toolCards=new ToolCards(i);
            toolCards.dump();
        }
    }
}
