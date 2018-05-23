package it.polimi.ingsw.Game;


import java.util.Random;

public class PrivateGoal {
    private String name;
    private String description;
    private int serialNumber;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public PrivateGoal (int serialNumber) {
        switch (serialNumber) {
            case 1:
                this.name = "Sfumature Rosse";
                this.description = "Somma dei valori su tutti i dadi rossi";
                this.serialNumber = 1;
                break;
            case 2:
                this.name = "Sfumature Gialle";
                this.description = "Somma dei valori su tutti i dadi gialli";
                this.serialNumber = 2;
                break;
            case 3:
                this.name = "Sfumature Verdi";
                this.description = "Somma dei valori su tutti i dadi verdi";
                this.serialNumber = 3;
                break;
            case 4:
                this.name = "Sfumature Blu";
                this.description = "Somma dei valori su tutti i dadi blu";
                this.serialNumber = 4;
                break;
            case 5:
                this.name = "Sfumature Viola";
                this.description = "Somma dei valori su tutti i dadi viola";
                this.serialNumber = 5;
                break;
            default:
                break;

        }

    }

    //-----------------------------------------------Getters and Setters------------------------------------------------
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getSerialNumber() {
        return serialNumber;
    }

    public PrivateGoal[] getRndPrivateGoals(int numPlayer){
        Random rnd=new Random();
        PrivateGoal [] privateGoals=new PrivateGoal[numPlayer];
        int index[]=new int[4];
        index[0]=rnd.nextInt(4)+1;
        index[1]=rnd.nextInt(4)+1;
        index[2]=rnd.nextInt(4)+1;
        index[3]=rnd.nextInt(4)+1;
        while(index[0]==index[1] || index[1]==index[2] || index[0]==index[2] || index[0]==index[3] || index[1]==index[3] || index[3]==index[2] ){
            index[0]=rnd.nextInt(4)+1;
            index[1]=rnd.nextInt(4)+1;
            index[2]=rnd.nextInt(4)+1;
            index[3]=rnd.nextInt(4)+1;
        }
        for(int i=0;i<numPlayer;i++) {
            privateGoals[i] = new PrivateGoal(index[i]);
        }
        return privateGoals;
    }

    //-----------------------------------------------Print methods------------------------------------------------------

    @Override
    public String toString() {
        String s=new String();
        s=name.concat(" • Privata");
        s=s.concat("\n");
        s=s.concat(description);
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
}
