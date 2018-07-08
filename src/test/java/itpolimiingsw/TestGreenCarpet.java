package itpolimiingsw;

import itpolimiingsw.GameCards.PublicGoal;
import itpolimiingsw.GameCards.Scheme;
import itpolimiingsw.GameCards.ToolCards;
import itpolimiingsw.GameTools.*;
import itpolimiingsw.GameItems.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class TestGreenCarpet {
    @org.junit.jupiter.api.Test
    public void tGreenCarpet(){
        Scheme scheme=new Scheme(2);
        GreenCarpet greenCarpet=new GreenCarpet(4);
        greenCarpet.setPublicGoals(new PublicGoal(1),new PublicGoal(2),new PublicGoal(3));
        greenCarpet.setToolCards(new ToolCards(1),new ToolCards(2),new ToolCards(3));
        greenCarpet.setStock((4*2+1));
        greenCarpet.dump();

        greenCarpet.setRndPublicGoals();
        greenCarpet.setRndToolCards();
        greenCarpet.dump();
        ToolCards[] toolCards=greenCarpet.getToolCards();
        for (int i=0;i<toolCards.length;i++)
            toolCards[i].dump();
        ArrayList<Dice> stock=greenCarpet.getStock();
        greenCarpet.setRoundPath(3);
        Player player=new Player("aa");
        Player player1=new Player("aaa");
        ArrayList<Player> players=new ArrayList<>();
        players.add(player);
        players.add(player1);
        greenCarpet.setPlayer(players);
        greenCarpet.dump();
        players=greenCarpet.getPlayer();
        Player player2=new Player("ccc");
        players.add(player2);
        greenCarpet.setPlayer(players);
        greenCarpet.setStock(9);
        Dice dice=stock.get(4);
        greenCarpet.dump();
        greenCarpet.changeDiceFromRoundPath(2,3,dice);
        greenCarpet.dump();
        System.out.println(greenCarpet.playersToString(1));
    }




    @org.junit.jupiter.api.Test
    public void testGreenCarpet2(){
        Scheme scheme=new Scheme(2);
        GreenCarpet greenCarpet=new GreenCarpet(4);
        DiceBucket diceBucket;
        greenCarpet.setPublicGoals(new PublicGoal(1),new PublicGoal(2),new PublicGoal(3));
        greenCarpet.setToolCards(new ToolCards(1),new ToolCards(2), new ToolCards(3));
        greenCarpet.setStock((4*2+1));
        greenCarpet.dump();
        greenCarpet.setRoundPath(1);
        Dice dice=greenCarpet.getDiceFromRoundPath(2,1);
        greenCarpet.setStock(8);
        dice=greenCarpet.getDiceFromStock(3);
        dice.dump();
        dice=greenCarpet.checkDiceFromStock(4);
        greenCarpet.dump();
        greenCarpet.setDiceInStock(dice);
        greenCarpet.dump();
        ToolCards toolCards=greenCarpet.getToolCard(2);
        toolCards.dump();
        int n=greenCarpet.getnPlayers();
        diceBucket=greenCarpet.getDiceBucket();
        greenCarpet.dump();
        greenCarpet.reRollStock();
        greenCarpet.dump();
        greenCarpet.setRound(4);
        greenCarpet.setTurn(2);
        int round=greenCarpet.getRound();
        int turn=greenCarpet.getTurn();
        System.out.println(n+"   "+round+"  "+turn);
        boolean bool=greenCarpet.checkEmptyRoundpath();
        System.out.println(greenCarpet.stockToString());



    }

    @Test
    public void checkEmptyRPTrue(){
        GreenCarpet greenCarpet = new GreenCarpet(4);
        assertEquals(false, greenCarpet.checkEmptyRoundpath());
    }

    @Test
    public void checkEmptyRPFalse(){
        GreenCarpet greenCarpet = new GreenCarpet(4);
        greenCarpet.setStock(4);
        greenCarpet.setRoundPath(1);
        assertEquals(true, greenCarpet.checkEmptyRoundpath());
    }

    @Test
    public void toolIsInFalse(){
        GreenCarpet greenCarpet = new GreenCarpet(4);
        greenCarpet.setToolCards(new ToolCards(1), new ToolCards(2), new ToolCards(3));
        assertEquals(false, greenCarpet.toolIsIn(4));
    }

    @Test
    public void toolIsInTrue(){
        GreenCarpet greenCarpet = new GreenCarpet(4);
        greenCarpet.setToolCards(new ToolCards(1), new ToolCards(2), new ToolCards(3));
        assertEquals(true, greenCarpet.toolIsIn(1));
    }

}
