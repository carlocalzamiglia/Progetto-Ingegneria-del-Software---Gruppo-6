package it.polimi.ingsw;

import it.polimi.ingsw.Project.Game.Inventory;
import it.polimi.ingsw.Project.Game.Player;

public class TestPlayer {
    @org.junit.jupiter.api.Test
    public void test() {
        Inventory inventory=new Inventory();
        Player player1=new Player("Cesna");
        player1.setScheme(inventory.getScheme(1));
        player1.setMarkers();
        player1.setBridge(inventory.getBridge(2));
        player1.setPrivateGoal(inventory.getPrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        player1.useMarkers(1);
        player1.dump();
    }
}
