package itpolimiingsw;
import itpolimiingsw.GameController.Game;
import itpolimiingsw.GameController.Matches;
import itpolimiingsw.GameTools.Player;
import itpolimiingsw.UsersDatabase.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestMatches {
    @Test
    public void TestMatchesSizeTrue(){
        Matches matches = new Matches();
        assertEquals(0, matches.size());
    }

    @Test
    public void TestMatchesSizeFalse() throws IOException, InterruptedException {
        Matches matches = new Matches();
        User user = new User("A", "A");
        matches.addUser(user);
        assertEquals(1, matches.size());
    }

    @Test
    public void TestDeleteGameTrue() throws IOException, InterruptedException {
        Matches matches = new Matches();
        User user = new User("A", "A");
        user.setName("A");
        matches.addUser(user);
        Game game = matches.getGame("A");
        matches.deleteGame(game);
        assertEquals(0, matches.size());
    }

    @Test
    public void TestGetUser() throws IOException, InterruptedException {
        Matches matches = new Matches();
        User user = new User("A", "A");
        User user1 = new User("B", "B");
        user.setName("A");
        matches.addUser(user);
        matches.addUser(user1);
        assertEquals(user, matches.getUser("A"));
    }

    @Test
    public void TestGetPlayer() throws IOException, InterruptedException {
        Matches matches = new Matches();
        User user = new User("A", "A");
        User user1 = new User("B", "B");
        user.setName("A");
        matches.addUser(user);
        matches.addUser(user1);
        Game game = matches.getGame("A");
        Player player = new Player("C");
        game.setPlayer(player);
        assertEquals(player, matches.getPlayer("C"));
    }
}
