package itpolimiingsw;

import itpolimiingsw.Game.PrivateGoal;
import itpolimiingsw.Game.PublicGoal;

public class TestGoal {
    @org.junit.jupiter.api.Test
    public void tPrivateGoal() {
        for(int i=0;i<30;i++) {
            System.out.println("numero: "+i);
            PrivateGoal privateGoal=new PrivateGoal(i);
            privateGoal.dump();
        }

    }
    @org.junit.jupiter.api.Test
    public void tPublicGoal() {
        for(int i=0;i<30;i++) {
            System.out.println("numero: "+i);
            PublicGoal publicGoal = new PublicGoal(i);
            publicGoal.dump();
        }
    }
}
