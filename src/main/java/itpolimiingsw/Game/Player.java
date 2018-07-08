package itpolimiingsw.Game;

import java.io.Serializable;
import java.util.ArrayList;


public class Player implements Serializable {

    private String nickname;
    private Bridge bridge;
    private PrivateGoal privateGoal;
    private Scheme scheme;
    private ArrayList<Markers> markers;
    private boolean online;
    private int points;
    private boolean secondTurn;


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
    public boolean getSecondTurn() {
        return secondTurn;
    }
    public void setSecondTurn(boolean secondTurn) {
        this.secondTurn = secondTurn;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getPoints() {
        return points;
    }
    public String getNickname() {
        return nickname;
    }


    /**
     * Use a marker if i use a toolcard and set the cost of the tool to 2 from 1 with the firs use
     *
     * @param greenCarpet is the greencarpet that contains the tool cards
     * @param serialnumber is the serial number of the tool i want to use
     */
    public void useMarkers(GreenCarpet greenCarpet,int serialnumber) {
        int cost =0;
        for(int j=0; j<greenCarpet.getToolCards().length;j++) {
            if (greenCarpet.getToolCards()[j].getSerialNumber() == serialnumber) {
                for (int i = 0; i < greenCarpet.getToolCards()[j].getCost(); i++)
                    markers.remove(0);
                if (greenCarpet.getToolCards()[j].getCost() == 1)
                    greenCarpet.getToolCards()[j].setCost(2);

            }
        }
    }

    /**
     * check if i can use a toolcard
     *
     * @param cost the cost of the tool card
     * @return true if i can use the tool, false if i can't
     */
    public boolean checkMarkers(int cost){
        boolean bool;
        if (markers.size()>= cost) {
            bool = true;
        }
        else
            bool=false;
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
