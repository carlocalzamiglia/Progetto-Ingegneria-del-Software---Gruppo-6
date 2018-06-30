package itpolimiingsw;

import itpolimiingsw.Game.*;
import org.junit.jupiter.api.Test;


public class TestToolExecutor {
    @org.junit.jupiter.api.Test
    public void tToolCardExe1and10() {
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(2);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));
        greenCarpet.setStock((2*2+1));
        Player player1=new Player("cecio");
        player1.setScheme(new Scheme(2));
        Player player2=new Player("aaa");
        player2.setScheme(new Scheme(3));
        player1.setMarkers();
        player2.setMarkers();
        ToolCardsExecutor executor=new ToolCardsExecutor();
        System.out.println(greenCarpet.checkDiceFromStock(3));
        System.out.println(greenCarpet.checkDiceFromStock(2));
        System.out.println(executor.changeDiceCard(player1,greenCarpet,greenCarpet.getToolCard(1).getSerialNumber(),3,1));
        System.out.println(executor.changeDiceCard(player2,greenCarpet,greenCarpet.getToolCard(1).getSerialNumber(),2,2));
        Dice dice=new Dice(Colour.ANSI_BLUE);
        dice.setFace("\u2680");
        executor.setOppositeFace(dice);
        System.out.println(dice);
        executor.setOppositeFace(dice);
        System.out.println(dice);
        dice.setFace("\u2681");
        executor.setOppositeFace(dice);
        System.out.println(dice);
        executor.setOppositeFace(dice);
        System.out.println(dice);
        dice.setFace("\u2682");
        executor.setOppositeFace(dice);
        System.out.println(dice);
        executor.setOppositeFace(dice);
        System.out.println(dice);
    }
    @org.junit.jupiter.api.Test
    public void tToolCardExe2and3(){
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(2);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1),inventory.getPublicGoal(2),inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));
        greenCarpet.setStock((2*2+1));
        Player player1=new Player("cecio");
        player1.setScheme(new Scheme(2));
        Player player2=new Player("aaa");
        player2.setScheme(new Scheme(10));
        player1.setMarkers();
        player2.setMarkers();
        Dice dice=new Dice(Colour.ANSI_YELLOW);
        dice.setFace("\u2682");
        player2.getScheme().setBoxes(dice,0,0);
        ToolCardsExecutor executor=new ToolCardsExecutor();
        int[] coord=new int[4];
        coord[0]=0;
        coord[1]=0;
        coord[2]=0;
        coord[3]=2;
        System.out.println(executor.useMovementCard(player2,greenCarpet,2,coord));
        player2.getScheme().dump();
        coord[1]=2;
        coord[2]=1;
        coord[3]=0;
        System.out.println(executor.useMovementCard(player2,greenCarpet,3,coord));
        player2.getScheme().dump();
    }
    @org.junit.jupiter.api.Test
    public void tToolCardExe7() {
        Inventory inventory = new Inventory();
        GreenCarpet greenCarpet = new GreenCarpet(2);
        greenCarpet.setPublicGoals(inventory.getPublicGoal(1), inventory.getPublicGoal(2), inventory.getPublicGoal(3));
        greenCarpet.setToolCards(inventory.getToolCard(1), inventory.getToolCard(2), inventory.getToolCard(3));
        greenCarpet.setStock((2 * 2 + 1));
        Player player1 = new Player("cecio");
        player1.setScheme(new Scheme(2));
        Player player2 = new Player("aaa");
        player2.setScheme(new Scheme(10));
        player1.setMarkers();
        player2.setMarkers();
        System.out.println(greenCarpet.stockToString());
        ToolCardsExecutor executor=new ToolCardsExecutor();
        System.out.println(executor.changeDiceCard(player1,greenCarpet,7));
        System.out.println(greenCarpet.stockToString());






    }

}
