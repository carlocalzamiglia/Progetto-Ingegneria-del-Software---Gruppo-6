package it.polimi.ingsw;


import it.polimi.ingsw.Game.Dice;
import it.polimi.ingsw.Game.DiceBucket;
import it.polimi.ingsw.Game.Inventory;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.Colour;
import it.polimi.ingsw.Game.Ruler;

public class TestRuler {
    @org.junit.jupiter.api.Test
    public void tRuler() {
        Inventory inventory = new Inventory();
        DiceBucket diceBucket = new DiceBucket();
        Dice dice = new Dice(Colour.ANSI_YELLOW);
        Player player1 = new Player("Cesna");
        player1.setScheme(inventory.getScheme(5));
        player1.setMarkers();
        player1.setBridge(inventory.getBridge(2));
        player1.setPrivateGoal(inventory.getPrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        Ruler ruler = new Ruler();
        boolean bool;
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(0, 2, dice,player1.getScheme());
        if (bool) {
            player1.getScheme().setBoxes(dice, 0, 2);
            player1.getScheme().dump();
        }
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(1, 3, dice,player1.getScheme());
        if (bool) {
            player1.getScheme().setBoxes(dice, 1, 3);
            player1.getScheme().dump();
        }
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(2, 4, dice,player1.getScheme());
        if (bool) {
            player1.getScheme().setBoxes(dice, 2, 4);
            player1.getScheme().dump();
        }
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(1, 4, dice,player1.getScheme());
        if (bool) {
            player1.getScheme().setBoxes(dice, 1, 4);
            player1.getScheme().dump();
        }
    }

}