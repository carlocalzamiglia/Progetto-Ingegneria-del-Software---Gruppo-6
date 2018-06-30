package itpolimiingsw.Game;

import java.io.Serializable;
import java.util.ArrayList;

public class Calculator implements Serializable {

    private  ArrayList<Player> players;
    private  GreenCarpet greenCarpet;


    //-----------------------------------------------Constructor--------------------------------------------------------
    public Calculator(ArrayList<Player> players, GreenCarpet greenCarpet) {
        this.players = players;
        this.greenCarpet = greenCarpet;
    }

    public void setGreenCarpet(GreenCarpet greenCarpet) {
        this.greenCarpet = greenCarpet;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    //---------------------------------Returns the points of the player-------------------------------------------------
    public int calculate(int i) {
        int sum = 0;
        sum = sum + checkPrivate(players.get(i));
        sum = sum - checkEmpty(players.get(i));
        sum = sum + checkMarkers(players.get(i));
        sum = sum + checkPublic(players.get(i), greenCarpet);
        return sum;

    }

    //---------------------------------It calculates the point given by the Private Goal--------------------------------
    private int checkPrivate(Player player) {
        int sumPrivate = 0;
        Ruler ruler = new Ruler();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (player.getScheme().getBox(i, j).getAddedDice() != null)
                    if (player.getScheme().getBox(i, j).getAddedDice().getColour().equals(player.getPrivateGoal().getColour()))
                        sumPrivate = sumPrivate + ruler.stringtoInt(player.getScheme().getBox(i, j).getAddedDice().getFace());


        return sumPrivate;
    }

    //---------------------------------It returns the number of the remaining Markers-----------------------------------
    private int checkMarkers(Player player) {
        return player.getMarkers().size();
    }

    //---------------------------------It returns the number of empty boxes in the scheme-------------------------------
    private int checkEmpty(Player player) {
        int e = 0;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (player.getScheme().getBox(i, j).getAddedDice() == null)
                    e++;
        return e;

    }

    //---------------------------------It calculates the point given by the Public Goal---------------------------------
    private int checkPublic(Player player, GreenCarpet greenCarpet) {
        int sumPublic = 0;
        int serialNumber;
        for (int i = 0; i < 3; i++) {
            serialNumber = greenCarpet.getPublicGoal(i).getSerialNumber();
            if (serialNumber >= 1 && serialNumber <= 4)
                sumPublic = sumPublic + checkRowOrCol(serialNumber, player.getScheme(), (serialNumber / 3) + 1);
            else if (serialNumber >= 5 && serialNumber <= 7)
                sumPublic = sumPublic + countPoints(((serialNumber - 5) * 2) + 1, countOccurrences(player.getScheme()));
            else if (serialNumber == 8) {
                int min = findMin(countOccurrences(player.getScheme()));
                sumPublic = sumPublic + (min * 5);
            } else if (serialNumber == 9)
                sumPublic = sumPublic + countPublicN9(player.getScheme());
            else if (serialNumber == 10)
                sumPublic = sumPublic + countPublicN10(player.getScheme());
        }
        return sumPublic;
    }

    //-----------method that checks whether a row or a column has a repeated color or a repeated face-------------------
    private int checkRowOrCol(int serialnumber, Scheme scheme, int code) {
        boolean flag;
        int limit = 0;
        int reps = 0;
        int sumPublic = 0;
        int code2 = (serialnumber % 2);
        int counter1;
        int counter2;
        int counter3;
        int[] j = new int[2];
        int z;
        int k;
        if (serialnumber == 1 || serialnumber == 3) {
            reps = 4;
            limit = 5;
        } else if (serialnumber == 2 || serialnumber == 4) {
            reps = 5;
            limit = 4;
        }
        for (counter1 = 0; counter1 < reps; counter1++) {           //rows
            flag = true;
            for (counter2 = 0; counter2 < limit && flag; counter2++) {//columns
                if (code2 == 0) {
                    j[0] = counter2;
                    k = counter1;
                } else {
                    j[0] = counter1;
                    j[1] = counter1;
                    k = counter2;
                }
                if (scheme.getBox(j[0], k).getAddedDice() == null)
                    flag = false;
                for (counter3 = counter2 + 1; counter3 < limit && flag; counter3++) {                //colors
                    if (code2 == 0) {
                        j[1] = counter3;
                        z = counter1;
                    } else
                        z = counter3;
                    if (scheme.getBox(j[1], z).getAddedDice() == null)
                        flag = false;
                    else {
                        if (code == 1) {
                            if ((scheme.getBox(j[0], k).getAddedDice().getColour().equals(scheme.getBox(j[1], z).getAddedDice().getColour())))
                                flag = false;
                        } else if (code == 2) {
                            if ((scheme.getBox(j[0], k).getAddedDice().getFace().equals(scheme.getBox(j[1], z).getAddedDice().getFace())))
                                flag = false;
                        }
                    }
                }
            }
            if (flag && serialnumber == 1)
                sumPublic = sumPublic + 6;
            else if (flag && (serialnumber == 2 || serialnumber == 3))
                sumPublic = sumPublic + 5;
            else if (flag && serialnumber == 4)
                sumPublic = sumPublic + 4;
        }
        return sumPublic;
    }

    //---------------------------------returns the occurrences of every dice in the scheme------------------------------
    private int[] countOccurrences(Scheme scheme) {
        int[] values = new int[6];
        Dice d=new Dice(null);
        String[] faces=d.getFaces();
        for (int i = 0; i < 6; i++)
            values[i] = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 5; k++) {
                if (scheme.getBox(j, k).getAddedDice() != null)
                    for(int z=0;z<faces.length;z++)
                        if (scheme.getBox(j, k).getAddedDice().getFace().equals(faces[z]))
                            values[z]++;
            }
        }
        return values;
    }

    //-------------------------------return the points assigned by the public goals 5,6,7-------------------------------
    private int countPoints(int code, int[] values) {
        int points;
        if (values[code - 1] >= values[code])
            points = (values[code] * 2);
        else
            points = (values[code - 1] * 2);

        return points;
    }

    //---------------------------------------returns the minimum value of an array--------------------------------------
    private int findMin(int[] occurrences) {
        int min = occurrences[0];
        for (int z = 1; z < occurrences.length; z++) {
            if (min >= occurrences[z])
                min = occurrences[z];
        }
        return min;
    }

    //--------------------------------------Implementation of the rule of public goal n9--------------------------------
    private int countPublicN9(Scheme scheme) {
        Scheme testScheme = new Scheme(0);
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 5; k++) {
                if (k == 0 && scheme.getBox(j, k).getAddedDice() != null)
                    checkRight(testScheme, scheme, j, k);
                else if (k == 4 && scheme.getBox(j, k).getAddedDice() != null)
                    checkLeft(testScheme, scheme, j, k);
                else if (scheme.getBox(j, k).getAddedDice() != null) {
                    checkRight(testScheme, scheme, j, k);
                    checkLeft(testScheme, scheme, j, k);
                }
            }
        }
        return schemeCount(testScheme);
    }

    //--------------------------------------Implementation of the rule of public goal n10-------------------------------
    private int countPublicN10(Scheme scheme) {
        int[] nColors = new int[5];
        int minimum;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 5; k++) {
                if (scheme.getBox(j, k).getAddedDice() == null) ;
                else if (scheme.getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_PURPLE)) {
                    nColors[0]++;
                } else if (scheme.getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_BLUE)) {
                    nColors[1]++;
                } else if (scheme.getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_YELLOW)) {
                    nColors[2]++;
                } else if (scheme.getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_RED)) {
                    nColors[3]++;
                } else if (scheme.getBox(j, k).getAddedDice().getColour().equals(Colour.ANSI_GREEN)) {
                    nColors[4]++;
                }
            }
        }
        minimum = findMin(nColors);
        return (minimum * 4);

    }

    //---------------------------------Check color of the right box-----------------------------------------------------
    private void checkRight(Scheme testScheme, Scheme playerScheme, int row, int col) {
        if (playerScheme.getBox(row + 1, col + 1).getAddedDice() != null) {
            if (playerScheme.getBox(row, col).getAddedDice().getColour().equals(playerScheme.getBox(row + 1, col + 1).getAddedDice().getColour())) {
                testScheme.setBoxes(playerScheme.getBox(row, col).getAddedDice(), row, col);
                testScheme.setBoxes(playerScheme.getBox(row + 1, col + 1).getAddedDice(), row + 1, col + 1);
            }
        }
    }

    //---------------------------------Check color of the left box------------------------------------------------------
    private void checkLeft(Scheme testScheme, Scheme playerScheme, int row, int col) {
        if (playerScheme.getBox(row + 1, col - 1).getAddedDice() != null) {
            if (playerScheme.getBox(row, col).getAddedDice().getColour().equals(playerScheme.getBox(row + 1, col - 1).getAddedDice().getColour())) {
                testScheme.setBoxes(playerScheme.getBox(row, col).getAddedDice(), row, col);
                testScheme.setBoxes(playerScheme.getBox(row + 1, col - 1).getAddedDice(), row + 1, col - 1);
            }
        }
    }

    //---------------------------------Method that returns the number of dices in the scheme----------------------------
    private int schemeCount(Scheme testScheme) {

        int sum = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 5; k++) {
                if (testScheme.getBox(j, k).getAddedDice() != null)
                    sum = sum + 1;

            }
        }

        return sum;
    }

}