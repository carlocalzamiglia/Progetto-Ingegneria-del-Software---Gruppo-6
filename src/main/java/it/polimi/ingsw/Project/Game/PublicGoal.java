package it.polimi.ingsw.Project.Game;

public class PublicGoal {
    private String name;
    private String description;
    private String points;
    private int serialNumber;

    public PublicGoal (int serialNumber){
        switch (serialNumber) {
            case 1:
                this.name = "Colori diversi - Riga";
                this.description = "Righe senza colori ripetuti";
                this.serialNumber = 1;
                this.points="6";
                break;
            case 2:
                this.name = "Colori diversi - Colonna";
                this.description = "Colonne senza colori ripetuti";
                this.serialNumber = 2;
                this.points="5";
                break;
            case 3:
                this.name = "Sfumature diverse - Riga";
                this.description = "Righe senza sfumature ripetute";
                this.serialNumber = 3;
                this.points="5";
                break;
            case 4:
                this.name = "Sfumature diverse - Colonna";
                this.description = "Colonne senza sfumature ripetute";
                this.serialNumber = 4;
                this.points="4";
                break;
            case 5:
                this.name = "Sfumature Chiare";
                this.description = "Set di 1 & 2 ovunque";
                this.serialNumber = 5;
                this.points="2";
                break;
            case 6:
                this.name = "Sfumature Medie";
                this.description = "Set di 3 & 4 ovunque";
                this.serialNumber = 6;
                this.points="2";
                break;
            case 7:
                this.name = "Sfumature Scure";
                this.description = "Set di 5 & 6 ovunque";
                this.serialNumber = 7;
                this.points="2";
                break;
            case 8:
                this.name = "Sfumature Diverse";
                this.description = "Set di dadi di ogni valore ovunque";
                this.serialNumber = 8;
                this.points="5";
                break;
            case 9:
                this.name = "Diagonali Colorate";
                this.description = "Numero di dadi dello stesso colore diagonalmente adiacenti";
                this.serialNumber = 9;
                this.points="#";
                break;
            case 10:
                this.name = "Varietà di Colore";
                this.description = "Set di dadi di ogni colore ovunque";
                this.serialNumber = 10;
                this.points="4";
                break;
            default:
                break;

        }

    }

    public int getSerialNumber() {
        return serialNumber;
    }




    public String toString() {
        String s=new String();
        s=points+" "+name;
        s=s.concat("\n");
        s=s+description;
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
}
