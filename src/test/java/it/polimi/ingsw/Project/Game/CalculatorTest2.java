package it.polimi.ingsw.Project.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest2 {

    @org.junit.jupiter.api.Test
    public void test() {
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(1);
        ArrayList<Player> players=new ArrayList<Player>();
        players.add(new Player("cesna"));
        players.get(0).setScheme(inventory.getScheme(1));
        players.get(0).setPrivateGoal(inventory.getPrivateGoal(5));
        players.get(0).setMarkers();
        players.get(0).setBridge(inventory.getBridge(1));
        players.get(0).setOnline(true);

        greenCarpet.setPublicGoals(inventory.getPublicGoal(4),inventory.getPublicGoal(9),inventory.getPublicGoal(2));

        for(int i=0;i<4;i++) {
            for (int j = 0; j < 3; j++) {
                Dice d = inventory.getDiceBucket().educe();
                d.roll();
                players.get(0).getScheme().setBoxes(d, i, j);
            }
        }
        greenCarpet.dump();
        players.get(0).dump();
        Calculator calculator=new Calculator(players,greenCarpet);
        int pointPlayerCesna=calculator.calculate(0);
        System.out.println(pointPlayerCesna);

    }

}