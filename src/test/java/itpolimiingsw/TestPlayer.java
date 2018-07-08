package itpolimiingsw;


import itpolimiingsw.GameCards.PrivateGoal;
import itpolimiingsw.GameCards.PublicGoal;
import itpolimiingsw.GameCards.Scheme;
import itpolimiingsw.GameCards.ToolCards;
import itpolimiingsw.GameTools.*;
import itpolimiingsw.GameItems.*;

public class TestPlayer {
    @org.junit.jupiter.api.Test
    public void tPlayer() {
        Player player1=new Player("Cesna");
        player1.setScheme(new Scheme(1));
        player1.setMarkers();
        player1.setBridge(new Bridge(2));
        player1.setPrivateGoal(new PrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        //player1.useMarkers(g);
        player1.dump();
    }
    @org.junit.jupiter.api.Test
    public void tPlayerAndGreenCarpet() {
        GreenCarpet greenCarpet=new GreenCarpet(2);
        greenCarpet.setPublicGoals(new PublicGoal(1),new PublicGoal(2),new PublicGoal(3));
        greenCarpet.setToolCards(new ToolCards(1),new ToolCards(2),new ToolCards(3));
        greenCarpet.setStock((2*2+1));
        Player player1=new Player("Cesna");
        player1.setScheme(new Scheme(1));
        player1.setMarkers();
        player1.setBridge(new Bridge(2));
        player1.setPrivateGoal(new PrivateGoal(4));
        player1.setOnline(true);
        Player player2=new Player("ElCcciarelloz");
        player2.setScheme(new Scheme(2));
        player2.setMarkers();
        player2.setBridge(new Bridge(1));
        player2.setPrivateGoal(new PrivateGoal(2));
        player2.setOnline(true);
        greenCarpet.dump();
        player1.dump();
        player2.dump();
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,1);
        player2.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),1,1);
        player1.useMarkers(greenCarpet,greenCarpet.getToolCard(1).getSerialNumber());
        Bridge bridge=player1.getBridge();
        bridge.dump();
        player1.setOnline(false);
        boolean bool=player1.isOnline();
        player1.setSecondTurn(bool);
        player1.setSecondTurn(true);
        bool=player1.getSecondTurn();
        Ruler ruler=new Ruler();
        bool=ruler.checkAvailable(greenCarpet,player1.getScheme());
        System.out.println(bool);
        Dice dice=new Dice(Colour.ANSI_BLUE);
        dice.roll();
        bool=ruler.checkAvailableDice(dice,player1.getScheme());
        greenCarpet.dump();
        player1.dump();
        player2.dump();
    }
}
