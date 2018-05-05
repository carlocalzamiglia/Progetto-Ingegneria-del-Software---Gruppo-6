package it.polimi.ingsw;

import it.polimi.ingsw.Project.Game.*;

public class TestGreenCarpetAndPlayer {
    @org.junit.jupiter.api.Test
    public void test() {
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(2);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCards());

        for (int i=0;i<greenCarpet.getnPlayers()*2+1;i++) {
            Dice dice = inventory.getDiceBucket().educe();
            dice.roll();
            greenCarpet.setDiceInStock(dice);
        }

        Player player1=new Player("Cesna");
        player1.setScheme(inventory.getScheme(1));
        player1.setMarkers();
        player1.setBridge(inventory.getBridge(2));
        player1.setPrivateGoal(inventory.getPrivateGoal(4));
        player1.setOnline(true);
        Player player2=new Player("ElCcciarelloz");
        player2.setScheme(inventory.getScheme(2));
        player2.setMarkers();
        player2.setBridge(inventory.getBridge(1));
        player2.setPrivateGoal(inventory.getPrivateGoal(2));
        player2.setOnline(true);
        greenCarpet.dump();
        player1.dump();
        player2.dump();
        Box box=new  Box();
        box.setAddedDice(greenCarpet.getDiceFromStock(1));
        player1.getScheme().setBoxes(box,0,1);
        box.setAddedDice(greenCarpet.getDiceFromStock(1));
        player2.getScheme().setBoxes(box,1,1);
        greenCarpet.dump();
        player1.dump();
        player2.dump();
    }
}
