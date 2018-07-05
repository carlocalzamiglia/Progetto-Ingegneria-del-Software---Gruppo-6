package itpolimiingsw;

import itpolimiingsw.Game.Calculator;
import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Inventory;
import itpolimiingsw.Game.Player;
import static org.junit.jupiter.api.Assertions.assertEquals;



import java.util.ArrayList;

public class TestCalculator {

    @org.junit.jupiter.api.Test
    public void tCalculator() {

        Calculator calculator;
        ArrayList<Player> players = new ArrayList<Player>();
        int punteggio1;
        int punteggio2;


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

        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,1);
        player1.dump();


        players.add(player1);


        calculator=new Calculator(players, greenCarpet);


        assertEquals(-14, calculator.calculate(0));


    }

}
