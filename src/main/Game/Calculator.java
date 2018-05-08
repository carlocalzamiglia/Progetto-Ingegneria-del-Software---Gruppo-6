package it.polimi.ingsw.Project.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Calculator {

    private final ArrayList<Player> players;
    private final GreenCarpet greenCarpet;

    public Calculator(ArrayList<Player> players, GreenCarpet greenCarpet){
        this.players=players;
        this.greenCarpet=greenCarpet;
    }



    public int calculate(int i){
        int sum=0;
            sum=sum+checkPrivate(players.get(i));
            sum=sum-checkEmpty(players.get(i));
            sum=sum+checkMarkers(players.get(i));
            sum=sum+checkPublic(players.get(i),greenCarpet);


            return sum;

    }





    private int checkPrivate(Player player){

        int serialNumber = player.getPrivateGoal().getSerialNumber();
        int sumPrivate = 0;
        int z=0;

        switch (serialNumber) {
            case 1:
                for(int i=0;i<4;i++)
                    for(int j=0; j<5;j++)
                        if(player.getScheme().getBox(i, j).getAddedDice()!=null)
                            if(player.getScheme().getBox(i, j).getAddedDice().getColour().equals(Colour.ANSI_RED))
                                sumPrivate=sumPrivate+stringtoInt(player.getScheme().getBox(i, j).getAddedDice().getFace());
                break;
            case 2:
                for(int i=0;i<4;i++)
                    for(int j=0; j<5;j++)
                        if(player.getScheme().getBox(i, j).getAddedDice()!=null)
                            if(player.getScheme().getBox(i, j).getAddedDice().getColour().equals(Colour.ANSI_YELLOW))
                                sumPrivate = sumPrivate + stringtoInt(player.getScheme().getBox(i, j).getAddedDice().getFace());
                break;
            case 3:
                for(int i=0;i<4;i++)
                    for(int j=0; j<5;j++)
                        if(player.getScheme().getBox(i, j).getAddedDice()!=null)
                            if(player.getScheme().getBox(i, j).getAddedDice().getColour().equals(Colour.ANSI_GREEN))
                                sumPrivate=sumPrivate+stringtoInt(player.getScheme().getBox(i, j).getAddedDice().getFace());
                break;
            case 4:
                for(int i=0;i<4;i++)
                    for(int j=0; j<5;j++)
                        if(player.getScheme().getBox(i, j).getAddedDice()!=null)
                            if(player.getScheme().getBox(i, j).getAddedDice().getColour().equals(Colour.ANSI_BLUE))
                                sumPrivate = sumPrivate + stringtoInt(player.getScheme().getBox(i, j).getAddedDice().getFace());
                break;
            case 5:
                for(int i=0;i<4;i++)
                    for(int j=0; j<5;j++)
                        if(player.getScheme().getBox(i, j).getAddedDice()!=null)
                            if(player.getScheme().getBox(i, j).getAddedDice().getColour().equals(Colour.ANSI_PURPLE))
                                sumPrivate = sumPrivate + stringtoInt(player.getScheme().getBox(i, j).getAddedDice().getFace());
                break;
            default:
                break;

        }

    return sumPrivate;
    }


    private int checkMarkers(Player player){
        return player.getMarkers().size();
    }

    private int checkEmpty(Player player){
        int e=0;

        for(int i=0;i<4;i++)
            for(int j=0; j<5;j++)
                if(player.getScheme().getBox(i, j).getAddedDice()==null)
                    e++;
        return e;

    }



    private int checkPublic(Player player, GreenCarpet greenCarpet) {
        int sumPublic = 0;
        int serialNumber = 0;

        for (int i = 0; i < 3; i++) {
            int flag = 0;
            serialNumber = greenCarpet.getPublicGoal(i).getSerialNumber();
            switch (serialNumber) {
                case 1:                 //Colori diversi - Riga

                    for (int j = 0; j < 4; j++) {            //righe
                        flag = 0;
                        for (int k = 0; k < 5 && flag == 0; k++) {       //colonne
                            if (player.getScheme().getBox(j, k).getAddedDice() == null)
                                flag = 1;
                            for (int z = k + 1; z < 5 && flag == 0; z++) {                //colori
                                if (player.getScheme().getBox(j, z).getAddedDice() == null)
                                    flag = 1;
                                else {
                                    if (!(player.getScheme().getBox(j, k).getAddedDice().getColour().equals(player.getScheme().getBox(j, z).getAddedDice().getColour()))) {
                                    } else
                                        flag = 1;
                                }
                            }
                        }
                        if (flag == 0)
                            sumPublic = sumPublic + 6;
                    }
                    break;
                case 2:                     //Colori diversi - Colonna

                    for (int j = 0; j < 5; j++) {            //colonne
                        flag = 0;
                        for (int k = 0; k < 4 && flag == 0; k++) {       //righe
                            if (player.getScheme().getBox(k, j).getAddedDice() == null)
                                flag = 1;
                            for (int z = k + 1; z < 4 && flag == 0; z++) {                //colori
                                if (player.getScheme().getBox(z, j).getAddedDice() == null)
                                    flag = 1;
                                else {
                                    if (!(player.getScheme().getBox(k, j).getAddedDice().getColour().equals(player.getScheme().getBox(z, j).getAddedDice().getColour()))) {
                                    } else
                                        flag = 1;
                                }
                            }
                        }
                        if (flag == 0)
                            sumPublic = sumPublic + 5;
                    }

                    break;
                case 3:

                    for (int j = 0; j < 4; j++) {            //righe
                        flag = 0;
                        for (int k = 0; k < 5 && flag == 0; k++) {       //colonne
                            if (player.getScheme().getBox(j, k).getAddedDice() == null)
                                flag = 1;
                            for (int z = k + 1; z < 5 && flag == 0; z++) {                //colori
                                if (player.getScheme().getBox(j, z).getAddedDice() == null)
                                    flag = 1;
                                else {
                                    if (!(player.getScheme().getBox(j, k).getAddedDice().getFace().equals(player.getScheme().getBox(j, z).getAddedDice().getFace()))) {
                                    } else
                                        flag = 1;
                                }
                            }
                        }
                        if (flag == 0)
                            sumPublic = sumPublic + 5;
                    }
                    break;
                case 4:


                    for (int j = 0; j < 5; j++) {            //colonne
                        flag = 0;
                        for (int k = 0; k < 4 && flag == 0; k++) {       //righe
                            if (player.getScheme().getBox(k, j).getAddedDice() == null)
                                flag = 1;
                            for (int z = k + 1; z < 4 && flag == 0; z++) {                //colori
                                if (player.getScheme().getBox(z, j).getAddedDice() == null)
                                    flag = 1;
                                else {
                                    if (!(player.getScheme().getBox(k, j).getAddedDice().getFace().equals(player.getScheme().getBox(z, j).getAddedDice().getFace()))) {
                                    } else
                                        flag = 1;
                                }
                            }
                        }
                        if (flag == 0)
                            sumPublic = sumPublic + 4;
                    }
                    break;
                case 5:
                    int nOne = 0;
                    int nTwo = 0;

                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2680"))
                                nOne++;
                            else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2681"))
                                nTwo++;
                        }
                    }
                    if (nOne >= nTwo)
                        sumPublic = sumPublic + nTwo * 2;
                    else
                        sumPublic = sumPublic + nOne * 2;
                    break;
                case 6:

                    int nThree = 0;
                    int nFour = 0;

                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2682"))
                                nThree++;
                            else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2683"))
                                nFour++;
                        }
                    }
                    if (nThree >= nFour)
                        sumPublic = sumPublic + nFour * 2;
                    else
                        sumPublic = sumPublic + nThree * 2;
                    break;
                case 7:

                    int nFive = 0;
                    int nSix = 0;

                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2684"))
                                nFive++;
                            else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2685"))
                                nSix++;
                        }
                    }
                    if (nFive >= nSix)
                        sumPublic = sumPublic + nSix * 2;
                    else
                        sumPublic = sumPublic + nFive * 2;
                    break;
                case 8:
                    int noOne = 0;
                    int noTwo = 0;
                    int noThree = 0;
                    int noFour = 0;
                    int noFive = 0;
                    int noSix = 0;
                    int[] values = new int[6];
                    int min;

                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2680")) {
                                noOne++;
                                values[0] = noOne;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2681")) {
                                noTwo++;
                                values[1] = noTwo;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2682")) {
                                noThree++;
                                values[2] = noThree;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2683")) {
                                noFour++;
                                values[3] = noFour;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2684")) {
                                noFive++;
                                values[4] = noFive;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getFace().equals("\u2685")) {
                                noSix++;
                                values[5] = noSix;
                            }
                        }
                    }

                    min = values[0];

                    for (int z = 1; z < 6; z++) {
                        if (min >= values[z])
                            min = values[z];
                    }

                    sumPublic = sumPublic + min * 5;
                    break;
                case 9:

                    Scheme testScheme = new Scheme(0);

                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (k == 0 && player.getScheme().getBox(j, k).getAddedDice()!=null)
                                checkRight(testScheme, player.getScheme(), j, k);
                            else if (k == 4 && player.getScheme().getBox(j, k).getAddedDice()!=null)
                                checkLeft(testScheme, player.getScheme(), j, k);
                            else if (player.getScheme().getBox(j, k).getAddedDice()!=null) {
                                checkRight(testScheme, player.getScheme(), j, k);
                                checkLeft(testScheme, player.getScheme(), j, k);
                            }
                        }
                    }
                    sumPublic = sumPublic + schemeCount(testScheme);
                    break;
                case 10:

                    int nReds = 0;
                    int nGreens = 0;
                    int nYellows = 0;
                    int nBlues = 0;
                    int nPurples = 0;
                    int[] nColors = new int[5];
                    int minimum;

                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 5; k++) {
                            if (player.getScheme().getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_PURPLE)) {
                                nPurples++;
                                nColors[0] = nPurples;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_BLUE)) {
                                nBlues++;
                                nColors[1] = nBlues;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_YELLOW)) {
                                nYellows++;
                                nColors[2] = nYellows;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_RED)) {
                                nReds++;
                                nColors[3] = nReds;
                            } else if (player.getScheme().getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_GREEN)) {
                                nGreens++;
                                nColors[4] = nGreens;
                            }
                        }
                    }

                    minimum = nColors[0];

                    for (int z = 1; z < 5; z++) {
                        if (minimum >= nColors[z])
                            minimum = nColors[z];
                    }

                    sumPublic = sumPublic + minimum * 4;
                    break;
                default:
                    break;

            }
        }

        return sumPublic;
    }


    private int stringtoInt(String face){
        int i=0;

        if(face=="\u2680"){
            i=1;
        }
        if(face=="\u2681"){
            i=2;
        }
        if(face=="\u2682"){
            i=3;
        }
        if(face=="\u2683"){
            i=4;
        }
        if(face=="\u2684"){
            i=5;
        }
        if(face=="\u2685"){
            i=6;
        }
        return i;
    }


    private void checkRight(Scheme testScheme, Scheme playerScheme, int row, int col){
        if(playerScheme.getBox(row+1, col+1).getAddedDice()!=null) {
            if (playerScheme.getBox(row, col).getAddedDice().getColour().equals(playerScheme.getBox(row + 1, col + 1).getAddedDice().getColour())) {
                testScheme.setBoxes(playerScheme.getBox(row, col).getAddedDice(), row, col);
                testScheme.setBoxes(playerScheme.getBox(row + 1, col + 1).getAddedDice(), row + 1, col + 1);
            }
        }
    }

    private void checkLeft(Scheme testScheme, Scheme playerScheme, int row, int col){
        if(playerScheme.getBox(row+1, col-1).getAddedDice()!=null) {
            if (playerScheme.getBox(row, col).getAddedDice().getColour().equals(playerScheme.getBox(row + 1, col - 1).getAddedDice().getColour())) {
                testScheme.setBoxes(playerScheme.getBox(row, col).getAddedDice(), row, col);
                testScheme.setBoxes(playerScheme.getBox(row + 1, col - 1).getAddedDice(), row + 1, col - 1);
            }
        }
    }

    private int schemeCount(Scheme testScheme){

        int sum=0;
        for(int j=0;j<4;j++) {
            for (int k = 0; k < 5; k++) {
                if(testScheme.getBox(j,k).getAddedDice()!=null)
                    sum=sum+1;

            }
        }

        return sum;
    }



}
