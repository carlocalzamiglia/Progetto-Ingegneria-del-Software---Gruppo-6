package it.polimi.ingsw.Project.Game;

public class Inventory {
    private PrivateGoal[] privateGoals;
    private PublicGoal[] publicGoals;
    private Bridge[] bridges;
    private Scheme[] schemes;
    private Markers[] markers;
    private  ToolCards[] toolCards;
    private DiceBucket diceBucket;

    public Inventory(){
        privateGoals=new PrivateGoal[5];
        publicGoals=new PublicGoal[10];
        bridges=new Bridge[4];
        schemes=new Scheme[24];
        markers=new Markers[24];
        toolCards=new ToolCards[12];
        diceBucket=new DiceBucket();
        setBridges();
        setPrivateGoals();
        setPublicGoals();
        setToolCards();
        setSchemes();
        setMarkers();

    }

    public void setBridges() {
        for(int i=0;i<4;i++) {
            bridges[i] = new Bridge(i + 1);
        }
    }
    public void setPrivateGoals(){
        for(int i=0;i<5;i++) {
            privateGoals[i] = new PrivateGoal(i+1);
        }
    }
    public void setPublicGoals(){
        for (int i=0;i<10;i++) {
            publicGoals[i] = new PublicGoal(i+1);
        }
    }
    public void setSchemes(){
        for (int i=0;i<24;i++) {
            schemes[i] = new Scheme(i+1);
        }
    }
    public void setMarkers(){
        for (int i=0;i<24;i++) {
            markers[i] = new Markers();
        }
    }
    public void setToolCards(){
        for (int i=0;i<12;i++) {
            toolCards[i] = new ToolCards(i+1);
        }
    }

    public Bridge[] getBridges() {
        return bridges;
    }
    public Markers[] getMarkers() {
        return markers;
    }
    public PrivateGoal[] getPrivateGoals() {
        return privateGoals;
    }
    public PublicGoal[] getPublicGoals() {
        return publicGoals;
    }
    public Scheme[] getSchemes() {
        return schemes;
    }
    public ToolCards[] getToolCards() {
        return toolCards;
    }
    public DiceBucket getDiceBucket() {
        return diceBucket;
    }

}

