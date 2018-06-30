package itpolimiingsw;

import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Scheme;
import itpolimiingsw.Game.Inventory;

public class TestGreenCarpet {
    @org.junit.jupiter.api.Test
    public void tGreenCarpet(){
        Inventory inventory=new Inventory();
        Scheme scheme=new Scheme(2);
        GreenCarpet greenCarpet=new GreenCarpet(4);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));
        greenCarpet.setStock((4*2+1));
        greenCarpet.dump();

        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();
        greenCarpet.dump();
        /*
        greenCarpet.setRoundPath(1,greenCarpet.getStock());
        greenCarpet.dump();
        scheme.dump();
        scheme.setBoxes(greenCarpet.getDiceFromStock(2),0,3);
        greenCarpet.setRoundPath(2,greenCarpet.getStock());
        greenCarpet.dump();
        scheme.dump();*/
    }
}
