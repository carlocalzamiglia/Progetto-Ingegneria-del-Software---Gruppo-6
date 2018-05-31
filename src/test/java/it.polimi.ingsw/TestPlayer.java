package it.polimi.ingsw;


import it.polimi.ingsw.Game.Inventory;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.GreenCarpet;

public class TestPlayer {
    @org.junit.jupiter.api.Test
    public void tPlayer() {
        Inventory inventory=new Inventory();
        Player player1=new Player("Cesna");
        player1.setScheme(inventory.getScheme(1));
        player1.setMarkers();
        player1.setBridge(inventory.getBridge(2));
        player1.setPrivateGoal(inventory.getPrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        //player1.useMarkers(g);
        player1.dump();
    }
    @org.junit.jupiter.api.Test
    public void tPlayerAndGreenCarpet() {
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(2);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));

        greenCarpet.setStock((2*2+1));

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
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,1);
        player2.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),1,1);
        greenCarpet.dump();
        player1.dump();
        player2.dump();
    }
}
