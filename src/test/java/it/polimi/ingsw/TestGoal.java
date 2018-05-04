package it.polimi.ingsw;


import it.polimi.ingsw.Project.Game.PrivateGoal;
import it.polimi.ingsw.Project.Game.PublicGoal;

public class TestGoal {
    @org.junit.jupiter.api.Test
    public void test() {
        PrivateGoal privateGoals[]=new PrivateGoal[5];
        for(int i=0;i<5;i++){
            privateGoals[i]=new PrivateGoal(i+1);
            privateGoals[i].dump();
        }
        PublicGoal publicGoals[]=new PublicGoal[10];
        for(int i=0;i<10;i++){
            publicGoals[i]=new PublicGoal(i+1);
            publicGoals[i].dump();
        }
    }
}
