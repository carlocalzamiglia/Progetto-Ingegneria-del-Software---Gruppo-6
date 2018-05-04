package it.polimi.ingsw;
import it.polimi.ingsw.Project.Game.Box;
import it.polimi.ingsw.Project.Game.Colour;
import it.polimi.ingsw.Project.Game.Dice;
import it.polimi.ingsw.Project.Game.Scheme;

public class TestInsertDiceOnScheme {
    @org.junit.jupiter.api.Test
    public void test() {
        Scheme scheme=new Scheme(1);
        Dice d=new Dice(Colour.ANSI_BLUE);
        d.roll();
        Box box = new Box();
        box.setAddedDice(d);
        scheme.setBoxes(box,1,0);
        scheme.dump();
    }


}
