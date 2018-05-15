package it.polimi.ingsw;

import it.polimi.ingsw.Game.PrivateGoal;
import it.polimi.ingsw.Game.PublicGoal;

public class TestGoal {
    @org.junit.jupiter.api.Test
    public void tPrivateGoal() {
        PrivateGoal privateGoals[]=new PrivateGoal[5];
        for(int i=0;i<5;i++){
            privateGoals[i]=new PrivateGoal(i+1);
            privateGoals[i].dump();
        }

    }
    @org.junit.jupiter.api.Test
    public void tPublicGoal() {
        PublicGoal publicGoals[]=new PublicGoal[10];
        for(int i=0;i<10;i++){
            publicGoals[i]=new PublicGoal(i+1);
            publicGoals[i].dump();
        }
    }
}
