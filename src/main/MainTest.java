import Game.*;
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
        Dice[] diceStock = new Dice[7];
        Dice dice;
        for (int i = 0; i < 7; i++) {
            dice = diceBucket.educe();
            dice.roll();
            diceStock[i] = dice;
        }
        greenCarpet.setStock(diceStock);
        greenCarpet.setRoundPath(1,greenCarpet.getStock());
        greenCarpet.setPublicGoals(inventory.getPublicGoal(3), inventory.getPublicGoal(5), inventory.getPublicGoal(1));
        greenCarpet.setToolCards(inventory.getToolCards());
        greenCarpet.dump();
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,2);
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,3);
        player1.getScheme().dump();
        boolean goodEnding = toolCardsExecutor.executeToolCard(12, player1, greenCarpet, ruler, diceBucket);
        greenCarpet.dump();
        System.out.println(goodEnding);
        player1.getScheme().dump();


    }
}
