package it.polimi.ingsw.Game;

import java.io.Serializable;

public class Markers implements Serializable {
    private final int value = 1;

    public int getValue() {
        return value;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    @Override
    public String toString() {
        String s="(X)";
        return s;
    }
    public void dump(){
        System.out.println(this);
    }

}
