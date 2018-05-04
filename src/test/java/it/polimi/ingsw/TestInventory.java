package it.polimi.ingsw;

import it.polimi.ingsw.Project.Game.*;

public class TestInventory {
    @org.junit.jupiter.api.Test
    public void test() {
        Bridge bridges[];
        PrivateGoal privateGoals[];
        PublicGoal publicGoals[];
        Scheme schemes[];
        Markers markers[];
        ToolCards toolCards[];
        DiceBucket diceBucket;
        Inventory inventory=new Inventory();
        bridges=inventory.getBridges();
        diceBucket=inventory.getDiceBucket();
        markers=inventory.getMarkers();
        privateGoals=inventory.getPrivateGoals();
        publicGoals=inventory.getPublicGoals();
        schemes=inventory.getSchemes();
        toolCards=inventory.getToolCards();
        for(int i=0;i<4;i++){
            bridges[i].dump();
        }
        diceBucket.dump();
        for(int i=0;i<24;i++){
            markers[i].dump();
        }
        for(int i=0;i<12;i++){
            toolCards[i].dump();
        }
        for(int i=0;i<5;i++){
            privateGoals[i].dump();
        }
        for(int i=0;i<10;i++){
            publicGoals[i].dump();
        }
        for(int i=0;i<24;i++){
            schemes[i].dump();
        }
    }
}
