package itpolimiingsw;

import itpolimiingsw.GameCards.PrivateGoal;
import itpolimiingsw.GameCards.PublicGoal;
import itpolimiingsw.GameCards.Scheme;
import itpolimiingsw.GameCards.ToolCards;
import itpolimiingsw.GameTools.Calculator;
import itpolimiingsw.GameTools.GreenCarpet;
import itpolimiingsw.GameTools.Player;
import itpolimiingsw.GameItems.*;

import static org.junit.jupiter.api.Assertions.assertEquals;



import java.util.ArrayList;

public class TestCalculator {

    @org.junit.jupiter.api.Test
    public void tCalculator() {

        Calculator calculator;
        ArrayList<Player> players = new ArrayList<Player>();
        int punteggio1;
        int punteggio2;


        GreenCarpet greenCarpet=new GreenCarpet(2);
        greenCarpet.setPublicGoals(new PublicGoal(1),new PublicGoal(2),new PublicGoal(3));
        greenCarpet.setToolCards(new ToolCards(1),new ToolCards(2), new ToolCards(3));



        greenCarpet.setStock((2*2+1));

        Player player1=new Player("Cesna");
        player1.setScheme(new Scheme(1));
        player1.setMarkers();
        player1.setBridge(new Bridge(2));
        player1.setPrivateGoal(new PrivateGoal(4));
        player1.setOnline(true);

        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,1);
        player1.dump();


        players.add(player1);


        calculator=new Calculator(players, greenCarpet);


        assertEquals(-14, calculator.calculate(0));


    }

}
