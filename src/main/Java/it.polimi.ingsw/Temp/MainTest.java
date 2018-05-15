
package it.polimi.ingsw.Temp;


import it.polimi.ingsw.Game.DiceBucket;
import it.polimi.ingsw.Game.Inventory;
import it.polimi.ingsw.Game.Player;
import it.polimi.ingsw.Game.ToolCardsExecutor;
import it.polimi.ingsw.Game.Ruler;
import it.polimi.ingsw.Game.GreenCarpet;

public class MainTest {
    public static void main(String[] args)

    {

        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        Inventory inventory = new Inventory();
        DiceBucket diceBucket = new DiceBucket();
        Player player1 = new Player("Cesna");
        player1.setScheme(inventory.getScheme(5));
        player1.setMarkers();
        player1.setBridge(inventory.getBridge(2));
        player1.setPrivateGoal(inventory.getPrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        Ruler ruler = new Ruler();
        GreenCarpet greenCarpet = new GreenCarpet(3);

        greenCarpet.setStock(7,diceBucket);
        greenCarpet.setRoundPath(1,greenCarpet.getStock());
        greenCarpet.setPublicGoals(inventory.getPublicGoal(3), inventory.getPublicGoal(5), inventory.getPublicGoal(1));
        greenCarpet.setToolCards(inventory.getToolCard(1),inventory.getToolCard(2),inventory.getToolCard(3));
        greenCarpet.dump();
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,2);
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,3);
        player1.getScheme().dump();
        boolean goodEnding = toolCardsExecutor.executeToolCard(12, player1, greenCarpet, ruler, diceBucket);
        greenCarpet.dump();
        System.out.println(goodEnding);
        player1.dump();


    }
}
