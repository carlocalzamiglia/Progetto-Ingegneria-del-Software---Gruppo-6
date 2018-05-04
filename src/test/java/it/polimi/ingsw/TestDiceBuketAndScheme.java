package it.polimi.ingsw;
import it.polimi.ingsw.Project.Game.*;

import java.io.BufferedReader;
import java.util.Scanner;

public class TestDiceBuketAndScheme {
    @org.junit.jupiter.api.Test
    public void test(){
        Scanner input = new Scanner(System.in);

        Bridge bridge=new Bridge(1);
        DiceBucket bag=new DiceBucket();
        Scheme scheme = new Scheme(1);
        bridge.setScheme(scheme);
        Dice diceArray[]=new Dice[6];
        for(int i=0;i<6;i++){
            diceArray[i]=bag.educe();
            diceArray[i].roll();
            System.out.print(i+1+") "+diceArray[i]+" ");
        }
        System.out.println();
        System.out.println(bridge);



    }

}
