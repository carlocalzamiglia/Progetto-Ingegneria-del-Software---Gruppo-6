package it.polimi.ingsw.Project.Game;

public class Scheme {
    private Box boxes[][]=new Box[4][5];
    private int difficulty;
    private Boolean used;

    public Scheme(){
        boxes=new Box[4][5];
    }

    public Scheme setScheme(int i){
        switch (i) {
            case 1:
                boxes[0][0] = new Box("4");
                boxes[0][1] = new Box();
                boxes[0][2] = new Box("2");
                boxes[0][3] = new Box("5");
                boxes[0][4] = new Box(Colour.ANSI_GREEN);
                boxes[1][0] = new Box();
                boxes[1][1] = new Box();
                boxes[1][2] = new Box("6");
                boxes[1][3] = new Box(Colour.ANSI_GREEN);
                boxes[1][4] = new Box("2");
                boxes[2][0] = new Box();
                boxes[2][1] = new Box("3");
                boxes[2][2] = new Box(Colour.ANSI_GREEN);
                boxes[2][3] = new Box("4");
                boxes[2][4] = new Box();
                boxes[3][0] = new Box("5");
                boxes[3][1] = new Box(Colour.ANSI_GREEN);
                boxes[3][2] = new Box("1");
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                this.difficulty=5;
                break;
            case 2:
                boxes[0][0] = new Box();
                boxes[0][1] = new Box();
                boxes[0][2] = new Box();
                boxes[0][3] = new Box();
                boxes[0][4] = new Box();
                boxes[1][0] = new Box();
                boxes[1][1] = new Box();
                boxes[1][2] = new Box();
                boxes[1][3] = new Box();
                boxes[1][4] = new Box();
                boxes[2][0] = new Box();
                boxes[2][1] = new Box();
                boxes[2][2] = new Box();
                boxes[2][3] = new Box();
                boxes[2][4] = new Box();
                boxes[3][0] = new Box();
                boxes[3][1] = new Box();
                boxes[3][2] = new Box();
                boxes[3][3] = new Box();
                boxes[3][4] = new Box();
                break;
            default:
                break;
        }
        return this;

    }

    public void dump() {
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 5; j++) {
                boxes[i][j].dump();
            }
            System.out.println();
        }
    }
}
