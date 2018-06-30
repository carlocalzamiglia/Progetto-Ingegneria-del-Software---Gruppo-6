package itpolimiingsw;

import itpolimiingsw.Game.*;

import java.util.ArrayList;

public class TestGreenCarpet {
    @org.junit.jupiter.api.Test
    public void tGreenCarpet(){
        Inventory inventory=new Inventory();
        Scheme scheme=new Scheme(2);
        GreenCarpet greenCarpet=new GreenCarpet(4);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));
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
        Inventory inventory=new Inventory();
        Scheme scheme=new Scheme(2);
        GreenCarpet greenCarpet=new GreenCarpet(4);
        DiceBucket diceBucket;
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));
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

}
