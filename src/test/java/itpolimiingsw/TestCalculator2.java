package itpolimiingsw;

import itpolimiingsw.Game.Dice;
import itpolimiingsw.Game.GreenCarpet;
import itpolimiingsw.Game.Inventory;
import itpolimiingsw.Game.Player;
import itpolimiingsw.Game.Calculator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

class TestCalculator2 {

    @Test
    public void test() {
        int n=1;
        int r=10-n;
        Random random= new Random();
        Inventory inventory=new Inventory();
        GreenCarpet greenCarpet=new GreenCarpet(1);
        ArrayList<Player> players=new ArrayList<Player>();
        players.add(new Player("cesna"));
        players.get(0).setScheme(inventory.getScheme(1));
        players.get(0).setPrivateGoal(inventory.getPrivateGoal(5));
        players.get(0).setMarkers();
        players.get(0).setBridge(inventory.getBridge(1));
        players.get(0).setOnline(true);

        int a = random.nextInt(r)+n;
        int b=random.nextInt(r)+n;
        int c=random.nextInt(r)+n;

        while(b==a || b==c || a==c) {
            if (b == a)
                b=random.nextInt(r)+n;
            if(a==c)
                c=random.nextInt(r)+n;
            if(b==c)
                c=random.nextInt(r)+n;
        }


            greenCarpet.setPublicGoals(inventory.getPublicGoal(5), inventory.getPublicGoal(6), inventory.getPublicGoal(7));
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    Dice d = inventory.getDiceBucket().educe();
                    d.roll();
                    players.get(0).getScheme().setBoxes(d, i, j);
                }
            }
            greenCarpet.dump();
            players.get(0).dump();
            Calculator calculator = new Calculator(players, greenCarpet);
            int pointPlayerCesna = calculator.calculate(0);
            System.out.println(pointPlayerCesna);

    }

}