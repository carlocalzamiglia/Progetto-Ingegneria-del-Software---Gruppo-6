package it.polimi.ingsw.Game;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Bridge bridge;
    private PrivateGoal privateGoal;
    private Scheme scheme;
    private ArrayList<Markers> markers;
    private boolean online;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public Player(String nickname){
        this.nickname=nickname;
    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }
    public void setPrivateGoal(PrivateGoal privateGoal) {
        this.privateGoal = privateGoal;
    }
    public void setScheme(Scheme scheme) {
        this.scheme = scheme;

    }
    public void setMarkers() {
        markers=new ArrayList<Markers>(scheme.getDifficulty());
        for(int i=0;i<scheme.getDifficulty();i++)
            markers.add(new Markers());
    }
    public void setOnline(Boolean bool){
        this.online=bool;
    }
    public Bridge getBridge() {
        return bridge;
    }
    public PrivateGoal getPrivateGoal() {
        return privateGoal;
    }
    public Scheme getScheme() {
        return scheme;
    }
    public ArrayList<Markers> getMarkers() {
        return markers;
    }
    public boolean isOnline() {
        return online;
    }

    public String getNickname() {
        return nickname;
    }

    //----------------------------------Method that consume markers when the player uses a toolcard---------------------
    public boolean useMarkers(int cost) {
        boolean bool=true;
        if (markers.size()>cost) {
            bool = true;
            for (int i = 0; i < cost; i++)
                markers.remove(i);
        }
        else {
            bool = false;
            System.out.println("You don't have enough markers");
        }
        return bool;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    @Override
    public String toString() {
        String s=new String();
        s=s+nickname+ "             ";
        if(online)
            s=s+"              online";
        else
            s=s+"              offline";
        s=s+"\n";
        s=s+privateGoal+"\n"+bridge+scheme+"\n"+markers+"\n";
        return s;

    }
    public void dump(){
        System.out.println(this);
    }
}
