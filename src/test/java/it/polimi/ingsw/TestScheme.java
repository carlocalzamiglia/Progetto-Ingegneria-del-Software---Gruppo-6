package it.polimi.ingsw;
import it.polimi.ingsw.Project.Game.Dice;
import it.polimi.ingsw.Project.Game.DiceBucket;
import it.polimi.ingsw.Project.Game.Scheme;

public class TestScheme {
    @org.junit.jupiter.api.Test


    public void test() {
        Scheme scheme=new Scheme();
        scheme=scheme.setScheme(1);
        scheme.dump();

    }
}

