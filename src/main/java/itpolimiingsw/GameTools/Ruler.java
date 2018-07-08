package itpolimiingsw.GameTools;


import itpolimiingsw.GameItems.Dice;
import itpolimiingsw.GameCards.Scheme;

import java.io.Serializable;

public class Ruler implements Serializable {
    private Scheme scheme;
    private Dice dice;


    /**
     * Method that checks all the placement restriction of a dice
     *
     * @param row    the row of the scheme
     * @param col    the row of the scheme
     * @param dice   the dice i want to check the correct placement
     * @param scheme the scheme of the player
     * @return true if the dice can be placed in the scheme, false otherwise
     */
    public boolean checkCorrectPlacement(int row, int col, Dice dice, Scheme scheme) {
        boolean isCorrect = true;
        this.scheme = scheme;
        this.dice = dice;
        if (row < 0 || row > 3 || col < 0 || col > 4) {
            return false;
        }
        if (scheme.isEmpty()) {
            if (row != 0 && row != 3 && col != 0 && col != 4)
                isCorrect = false;
            else
                isCorrect = checkBox(row, col);
        } else {
            isCorrect = checkBox(row, col);
            if (isCorrect)
                isCorrect = checkNeighbors(row, col);
        }
        return isCorrect;
    }

    /**
     * Method that checks if ther is at least one dice that can be placed into the scheme
     *
     * @param greenCarpet the greencarpet
     * @param scheme scheme of the player
     * @return true if there is at least one dice can be placed into the scheme, false otherwise
     */
    public boolean checkAvailable(GreenCarpet greenCarpet, Scheme scheme) {
        boolean bool = false;
        for (int i = 0; i < greenCarpet.getStock().size(); i++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 5; col++) {
                    bool = checkCorrectPlacement(row, col, greenCarpet.checkDiceFromStock(i + 1), scheme);
                    if (bool)
                        return bool;
                }
            }
        }
        return bool;
    }

    /**
     * method that check if ther is at least one position where i can fit the dice in the scheme
     *
     * @param dice the dice i want to check
     * @param scheme the player scheme
     * @return true if threre is at least one position to place the dice in the scheme, false otherwise
     */
    public boolean checkAvailableDice(Dice dice, Scheme scheme) {
        boolean bool = false;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                bool = checkCorrectPlacement(row, col, dice, scheme);
                if (bool)
                    return bool;
            }
        }
        return bool;

    }

    /**
     * Method that returns true if a dice can be placed in a specific box
     *
     * @param row the row in the scheme
     * @param col the row in the scheme
     * @return true if i can put the dice in the box, false otherwise
     */
    private boolean checkBox(int row, int col) {
        boolean bool = true;
        if (scheme.getBox(row, col).getAddedDice() == null) {
            if (scheme.getBox(row, col).getRestrictionColour() != null) {
                if (scheme.getBox(row, col).getRestrictionColour().equals(dice.getColour()))
                    bool = true;
                else
                    bool = false;
            }
            if (scheme.getBox(row, col).getRestrictionValue() != null) {
                String num = dice.faceToNo();
                if (scheme.getBox(row, col).getRestrictionValue().equals(num))
                    bool = true;
                else
                    bool = false;
            }
        } else
            bool = false;
        return bool;
    }

    /**
     * Method that controls neighborhood restriction of a dice in a specific box
     *
     * @param row the row in the scheme
     * @param col the row in the scheme
     * @return true if i can put the dice in the box, false otherwise
     */
    private boolean checkNeighbors(int row, int col) {
        return (checkNeighborsColours(row, col, dice, scheme) && checkNeighborsFaces(row, col, dice, scheme) && !checkEmptyNeighbors(row, col, scheme));
    }

    /**
     *Method that controls that a specific box has no neighbors
     *
     * @param row the row of the scheme
     * @param col the row of the scheme
     * @param scheme the scheme of the player
     * @return true if the box hasn't dice placed around
     */
    public boolean checkEmptyNeighbors(int row, int col, Scheme scheme) {
        boolean bool = true;
        if (col - 1 >= 0)
            if (scheme.getBox(row, col - 1).getAddedDice() != null) {
                bool = false;
            }
        if (col + 1 <= 4 && bool)
            if (scheme.getBox(row, col + 1).getAddedDice() != null) {
                bool = false;
            }
        if (row - 1 >= 0 && bool)
            if (scheme.getBox(row - 1, col).getAddedDice() != null) {
                bool = false;
            }
        if (row + 1 <= 3 && bool)
            if (scheme.getBox(row + 1, col).getAddedDice() != null) {
                bool = false;
            }
        if (col - 1 >= 0 && row - 1 >= 0 && bool)
            if (scheme.getBox(row - 1, col - 1).getAddedDice() != null) {
                bool = false;
            }
        if (col - 1 >= 0 && row + 1 <= 3 && bool)
            if (scheme.getBox(row + 1, col - 1).getAddedDice() != null) {
                bool = false;
            }
        if (col + 1 <= 4 && row - 1 >= 0 && bool)
            if (scheme.getBox(row - 1, col + 1).getAddedDice() != null) {
                bool = false;
            }
        if (col + 1 <= 4 && row + 1 <= 3 && bool)
            if (scheme.getBox(row + 1, col + 1).getAddedDice() != null) {
                bool = false;
            }
        return bool;
    }

    /**
     * Method that controls neighborhood restriction values
     *
     * @param row the row in the scheme
     * @param col the row in the scheme
     * @param dice is the dice that i want to compare
     * @param scheme the scheme of the player
     * @return if the dice is can be placed in the scheme
     */
    public boolean checkNeighborsFaces(int row, int col, Dice dice, Scheme scheme) {
        boolean bool = true;
        int flag = 0;
        if (scheme.isEmpty()) {
            if (row != 0 && row != 3 && col != 0 && col != 4)
                return false;
            else
                return true;
        }
        if (col - 1 >= 0 && scheme.getBox(row, col - 1).getAddedDice() != null) {
            if (scheme.getBox(row, col - 1).getAddedDice().getFace().equals(dice.getFace()))
                bool = false;
            flag = 1;
        }
        if (col + 1 <= 4 && bool && scheme.getBox(row, col + 1).getAddedDice() != null) {
            if (scheme.getBox(row, col + 1).getAddedDice().getFace().equals(dice.getFace()))
                bool = false;
            flag = 1;
        }
        if (row - 1 >= 0 && bool && scheme.getBox(row - 1, col).getAddedDice() != null) {
            flag = 1;
            if (scheme.getBox(row - 1, col).getAddedDice().getFace().equals(dice.getFace()))
                bool = false;
        }
        if (row + 1 <= 3 && bool && scheme.getBox(row + 1, col).getAddedDice() != null) {
            flag = 1;
            if (scheme.getBox(row + 1, col).getAddedDice().getFace().equals(dice.getFace()))
                bool = false;
        }
        if (flag == 0)
            flag = checkDiagonallyNeighbors(scheme, row, col);
        if (flag == 0)
            bool = false;
        return bool;

    }

    /**
     * Method that controls neighborhood restriction colors
     *
     * @param row the row in the scheme
     * @param col the row in the scheme
     * @param dice is the dice that i want to compare
     * @param scheme the scheme of the player
     * @return if the dice is can be placed in the scheme
     */
    public boolean checkNeighborsColours(int row, int col, Dice dice, Scheme scheme) {
        boolean bool = true;
        int flag = 0;
        if (scheme.isEmpty()) {
            if (row != 0 && row != 3 && col != 0 && col != 4)
                return false;
            else
                return true;
        }
        if (col - 1 >= 0 && scheme.getBox(row, col - 1).getAddedDice() != null) {
            if (scheme.getBox(row, col - 1).getAddedDice().getColour().equals(dice.getColour()))
                bool = false;
            flag = 1;
        }
        if (col + 1 <= 4 && bool && scheme.getBox(row, col + 1).getAddedDice() != null) {
            if (scheme.getBox(row, col + 1).getAddedDice().getColour().equals(dice.getColour()))
                bool = false;
            flag = 1;
        }
        if (row - 1 >= 0 && bool && scheme.getBox(row - 1, col).getAddedDice() != null) {
            flag = 1;
            if (scheme.getBox(row - 1, col).getAddedDice().getColour().equals(dice.getColour()))
                bool = false;
        }
        if (row + 1 <= 3 && bool && scheme.getBox(row + 1, col).getAddedDice() != null) {
            flag = 1;
            if (scheme.getBox(row + 1, col).getAddedDice().getColour().equals(dice.getColour()))
                bool = false;
        }
        if (flag == 0)
            flag = checkDiagonallyNeighbors(scheme, row, col);
        if (flag == 0)
            bool = false;
        return bool;
    }

    /**
     *
     * @param scheme the scheme of the player
     * @param row the row in the scheme
     * @param col the row in the scheme
     * @return 1 if there is a dice diagonally adjacent, 0 otherwise
     */
    private int checkDiagonallyNeighbors(Scheme scheme, int row, int col) {
        int flag = 0;
        if (col - 1 >= 0 && row - 1 >= 0 && scheme.getBox(row - 1, col - 1).getAddedDice() != null)
            flag = 1;
        if (col - 1 >= 0 && row + 1 <= 3 && scheme.getBox(row + 1, col - 1).getAddedDice() != null)
            flag = 1;
        if (col + 1 <= 4 && row - 1 >= 0 && scheme.getBox(row - 1, col + 1).getAddedDice() != null)
            flag = 1;
        if (col + 1 <= 4 && row + 1 <= 3 && scheme.getBox(row + 1, col + 1).getAddedDice() != null)
            flag = 1;
        return flag;
    }

    /**
     * methods that count the dices in the scheme
     *
     * @param testScheme the scheme of the player
     * @return the sum of the dices inside the scheme
     */
    public int schemeCount(Scheme testScheme) {
        int sum = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 5; k++) {
                if (testScheme.getBox(j, k).getAddedDice() != null)
                    sum = sum + 1;
            }
        }
        return sum;
    }

    /**
     * converts the face of a dice into an integer
     *
     * @param face the face of the dice
     * @return the integer parsed of the dice
     */
    public int stringtoInt(String face) {
        int i = 0;
        Dice d = new Dice(null);
        String[] faces = d.getFaces();
        for (int j = 0; j < faces.length; j++)
            if (face.equals(faces[j]))
                i = j + 1;
        return i;
    }

    /**
     * converts an integer into a face of a dice
     *
     * @param i the integer of the dice's face
     * @return the string of the dice's face
     */
    public String intToString(int i) {
        if (i != 0) {
            Dice d = new Dice(null);
            String[] faces = d.getFaces();
            return faces[i - 1];
        } else
            return null;
    }
}
