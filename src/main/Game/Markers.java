package it.polimi.ingsw.Project.Game;

public class Markers {
    private final int value = 1;

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        String s="(X)";
        return s;
    }
    public void dump(){
        System.out.println(this);
    }

}
