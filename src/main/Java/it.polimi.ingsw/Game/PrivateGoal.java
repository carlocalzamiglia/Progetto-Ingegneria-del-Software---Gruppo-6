package it.polimi.ingsw.Game;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class PrivateGoal {
    private String name;
    private String description;
    private int serialNumber;

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
