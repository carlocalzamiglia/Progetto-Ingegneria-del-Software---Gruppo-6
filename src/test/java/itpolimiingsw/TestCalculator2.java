package itpolimiingsw;

import itpolimiingsw.Game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;
import java.util.Random;

class TestCalculator2 {

    @Test
    public void test() {
        int n=1;
        int r=10-n;
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(1);
        ArrayList<Player> players=new ArrayList<Player>();
        players.add(new Player("cesna"));
        players.get(0).setScheme(inventory.getScheme(1));
        players.get(0).setPrivateGoal(inventory.getPrivateGoal(5));
        players.get(0).setMarkers();
        players.get(0).setBridge(inventory.getBridge(1));
        players.get(0).setOnline(true);

        Dice dice = new Dice(Colour.ANSI_RED);
        dice.setFace("\u2680");
        Dice dice1 = new Dice(Colour.ANSI_BLUE);
        dice1.setFace("\u2681");
        Dice dice2 = new Dice(Colour.ANSI_YELLOW);
        dice2.setFace("\u2682");
        Dice dice3 = new Dice(Colour.ANSI_GREEN);
        dice3.setFace("\u2683");
        Dice dice4 = new Dice(Colour.ANSI_PURPLE);
        dice4.setFace("\u2684");
        Dice dice5 = new Dice(Colour.ANSI_GREEN);
        dice5.setFace("\u2685");

        players.get(0).getScheme().setBoxes(dice, 0, 0);
        players.get(0).getScheme().setBoxes(dice1, 0, 1);
        players.get(0).getScheme().setBoxes(dice2, 0, 2);
        players.get(0).getScheme().setBoxes(dice3, 0, 3);
        players.get(0).getScheme().setBoxes(dice4, 0, 4);
        players.get(0).getScheme().setBoxes(dice1, 1, 0);
        players.get(0).getScheme().setBoxes(dice2, 2, 0);
        players.get(0).getScheme().setBoxes(dice3, 3, 0);
        players.get(0).getScheme().setBoxes(dice, 1, 1);
        players.get(0).getScheme().setBoxes(dice, 2, 2);
        players.get(0).getScheme().setBoxes(dice1, 1, 2);
        players.get(0).getScheme().setBoxes(dice5, 3, 4);






        Calculator calculator = new Calculator(players, greenCarpet);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(5),inventory.getPublicGoal(6),inventory.getPublicGoal(7));

        assertEquals(14, calculator.calculate(0));
        greenCarpet.setPublicGoals(inventory.getPublicGoal(8),inventory.getPublicGoal(9),inventory.getPublicGoal(10));
        calculator.setGreenCarpet(greenCarpet);
        assertEquals(17, calculator.calculate(0));
        greenCarpet.setPublicGoals(inventory.getPublicGoal(4),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        calculator.setGreenCarpet(greenCarpet);
        assertEquals(16, calculator.calculate(0));




    }

}