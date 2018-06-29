
package itpolimiingsw.Temp;




import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Player;
import itpolimiingsw.Game.Ruler;
import itpolimiingsw.Game.ToolCardsExecutor;
import itpolimiingsw.Game.Bridge;
import itpolimiingsw.Game.Scheme;
import itpolimiingsw.Game.PrivateGoal;
import itpolimiingsw.Game.ToolCards;
import itpolimiingsw.Game.PublicGoal;

public class MainTest {
    public static void main(String[] args)

    {

        ToolCardsExecutor toolCardsExecutor = new ToolCardsExecutor();
        Player player1 = new Player("Cesna");
        GreenCarpet greenCarpet=new GreenCarpet(2);
        player1.setScheme(new Scheme(5));
        player1.setMarkers();
        player1.setBridge(new Bridge(2));
        player1.setPrivateGoal(new PrivateGoal(4));
        player1.setOnline(true);
        player1.dump();
        Ruler ruler = new Ruler();

        //greenCarpet.setStock(3);
        greenCarpet.setRoundPath(1);
        greenCarpet.setPublicGoals(new PublicGoal(3), new PublicGoal(5), new PublicGoal(1));
        greenCarpet.setToolCards(new ToolCards(2),new ToolCards(3),new ToolCards(4));
        greenCarpet.dump();
        //greenCarpet.setStock(2);
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,2);
        player1.getScheme().setBoxes(greenCarpet.getDiceFromStock(1),0,3);
        player1.dump();
        //toolCardsExecutor.useMovementCard(player1,greenCarpet,1,0,2,1,3);

        player1.dump();

        /*Game game=new Game(1);
        User user1=new User("Cesna","nickname");
        //User user2=new User("Ciccia","Rello");
        //User user3 =new User("Carlo","Carli");
        game.addUser(user1);
        //game.addUser(user2);
        //game.addUser(user3);
        game.startGame();*/



    }
}
