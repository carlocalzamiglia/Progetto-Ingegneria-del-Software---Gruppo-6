package it.polimi.ingsw.Game;

public class Markers {
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
