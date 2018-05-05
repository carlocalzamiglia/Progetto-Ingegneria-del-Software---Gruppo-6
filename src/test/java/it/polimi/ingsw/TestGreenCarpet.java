package it.polimi.ingsw;

import it.polimi.ingsw.Project.Game.*;

public class TestGreenCarpet {
    @org.junit.jupiter.api.Test
    public void test() {
        Inventory inventory=new Inventory();
        Scheme scheme=new Scheme(2);
        GreenCarpet greenCarpet=new GreenCarpet(4);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCards());

        for (int i=0;i<6;i++) {
            Dice dice = inventory.getDiceBucket().educe();
            dice.roll();
            greenCarpet.setDiceInStock(dice);
        }

        greenCarpet.dump();
        greenCarpet.setRoundPath(1,greenCarpet.getStock());
        greenCarpet.dump();
        scheme.dump();
        Box box=new Box();
        box.setAddedDice(greenCarpet.getDiceFromStock(2));
        scheme.setBoxes(box,0,3);
        greenCarpet.setRoundPath(2,greenCarpet.getStock());
        greenCarpet.dump();
        scheme.dump();
    }
}
