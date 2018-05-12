import Game.*;

public class TestRuler {
    @org.junit.jupiter.api.Test
    public void tRuler() {
        Inventory inventory = new Inventory();
        DiceBucket diceBucket = new DiceBucket();
        Dice dice = new Dice(Colour.ANSI_YELLOW);
        Player player1 = new Player("Cesna");
        player1.setScheme(inventory.getScheme(1));
        player1.setMarkers();
        player1.setBridge(inventory.getBridge(2));
        player1.setPrivateGoal(inventory.getPrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        Ruler ruler = new Ruler(player1);
        boolean bool;
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(0, 1, dice);
        System.out.println(bool);
        if (bool) {
            player1.getScheme().setBoxes(dice, 0, 1);
            player1.getScheme().dump();
        }
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(1, 1, dice);
        if (bool) {
            player1.getScheme().setBoxes(dice, 1, 1);
            player1.getScheme().dump();
        }
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(1, 0, dice);
        if (bool) {
            player1.getScheme().setBoxes(dice, 1, 0);
            player1.getScheme().dump();
        }
        dice = diceBucket.educe();
        dice.roll();
        dice.dump();
        bool = ruler.checkCorrectPlacement(3, 3, dice);
        if (bool) {
            player1.getScheme().setBoxes(dice, 3, 3);
            player1.getScheme().dump();
        }
    }

}
