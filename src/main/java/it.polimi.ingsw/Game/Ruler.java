package it.polimi.ingsw.Game;


import java.io.Serializable;

public class Ruler implements Serializable {
        private Scheme scheme;
        private Dice dice;


        //---------------------------Method that checks all the placement restriction of a dice-------------------------
        public boolean checkCorrectPlacement(int row, int col, Dice dice, Scheme scheme) {
            boolean isCorrect = true;
            this.scheme = scheme;
            this.dice = dice;
            if (row < 0 || row > 3 || col < 0 || col > 4) {
                isCorrect = false;
                return isCorrect;
            }
            if (scheme.isEmpty() == true) {
                if (row != 0 && row != 3 && col != 0 && col != 4) {
                    isCorrect = false;
                }
                else if (checkBox(row, col) == false) {
                    isCorrect = false;
                }
            } else {
                isCorrect = checkBox(row, col);
                if (isCorrect)
                    isCorrect = checkNeighbors(row, col);
            }
            return isCorrect;
        }

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

        //---------------------------Method that returns true if a dice can be placed in a specific box-----------------
        public boolean checkBox(int row, int col) {
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

        //---------------------------Method that controls neighborhood restriction of a dice in a specific box----------
        public boolean checkNeighbors(int row, int col) {
            boolean bool = true;
            int flag = 0;
            if(checkNeighborsColours(row,col,dice,scheme)&&checkNeighborsFaces(row,col,dice,scheme)&&!checkEmptyNeighbors(row,col,scheme));
            else
                bool=false;
            return bool;
        }

        //---------------------------Method that controls that a specific box has no neighbors--------------------------
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

        //---------------------------Method that controls neighborhood restriction (only numbers)-----------------------
        public boolean checkNeighborsFaces(int row, int col, Dice dice, Scheme scheme) {
            boolean bool = true;
            int flag = 0;
            if (scheme.isEmpty())
                return bool;
            if (col - 1 >= 0)
                if (scheme.getBox(row, col - 1).getAddedDice() != null) {
                    if (scheme.getBox(row, col - 1).getAddedDice().getFace().equals(dice.getFace()))
                        bool = false;
                    flag = 1;
                }
            if (col + 1 <= 4 && bool)
                if (scheme.getBox(row, col + 1).getAddedDice() != null) {
                    if (scheme.getBox(row, col + 1).getAddedDice().getFace().equals(dice.getFace()))
                        bool = false;
                    flag = 1;
                }
            if (row - 1 >= 0 && bool)
                if (scheme.getBox(row - 1, col).getAddedDice() != null) {
                    flag = 1;
                    if (scheme.getBox(row - 1, col).getAddedDice().getFace().equals(dice.getFace()))
                        bool = false;
                }
            if (row + 1 <= 3 && bool)
                if (scheme.getBox(row + 1, col).getAddedDice() != null) {
                    flag = 1;
                    if (scheme.getBox(row + 1, col).getAddedDice().getFace().equals(dice.getFace()))
                        bool = false;
                }
            if (col - 1 >= 0 && row - 1 >= 0 && bool)
                if (scheme.getBox(row - 1, col - 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (col - 1 >= 0 && row + 1 <= 3 && bool)
                if (scheme.getBox(row + 1, col - 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (col + 1 <= 4 && row - 1 >= 0 && bool)
                if (scheme.getBox(row - 1, col + 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (col + 1 <= 4 && row + 1 <= 3 && bool)
                if (scheme.getBox(row + 1, col + 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (flag == 0 && bool)
                bool = false;
            return bool;

        }

        //---------------------------Method that controls neighborhood restriction (only colors)------------------------
        public boolean checkNeighborsColours(int row, int col, Dice dice, Scheme scheme) {
            boolean bool = true;
            int flag = 0;
            if (scheme.isEmpty())
                return bool;
            if (col - 1 >= 0)
                if (scheme.getBox(row, col - 1).getAddedDice() != null) {
                    if (scheme.getBox(row, col - 1).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                    flag = 1;
                }
            if (col + 1 <= 4 && bool)
                if (scheme.getBox(row, col + 1).getAddedDice() != null) {
                    if (scheme.getBox(row, col + 1).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                    flag = 1;
                }
            if (row - 1 >= 0 && bool)
                if (scheme.getBox(row - 1, col).getAddedDice() != null) {
                    flag = 1;
                    if (scheme.getBox(row - 1, col).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                }
            if (row + 1 <= 3 && bool)
                if (scheme.getBox(row + 1, col).getAddedDice() != null) {
                    flag = 1;
                    if (scheme.getBox(row + 1, col).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                }
            if (col - 1 >= 0 && row - 1 >= 0 && bool)
                if (scheme.getBox(row - 1, col - 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (col - 1 >= 0 && row + 1 <= 3 && bool)
                if (scheme.getBox(row + 1, col - 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (col + 1 <= 4 && row - 1 >= 0 && bool)
                if (scheme.getBox(row - 1, col + 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (col + 1 <= 4 && row + 1 <= 3 && bool)
                if (scheme.getBox(row + 1, col + 1).getAddedDice() != null) {
                    flag = 1;
                }
            if (flag == 0 && bool)
                bool = false;
            return bool;
        }

        //---------------------------Returns the number of dices in the scheme------------------------------------------
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

        //---------------------------check if the stock respetc the restriction of the tool card number 9---------------
        public boolean checkRulesTool9(GreenCarpet greenCarpet,Player player) {
            boolean bool = false;
            Dice dice;
            for (int i = 0; i < greenCarpet.getStock().size()+1; i++) {
                dice = greenCarpet.getDiceFromStock(i+1);
                for (int k = 0; k <= 3 && !bool; k++)
                    for (int x = 0; x <= 4 && !bool; x++) {
                        if (checkEmptyNeighbors(k, x, player.getScheme())) {
                            if (player.getScheme().getBox(k, x).getRestrictionColour() != null)
                                bool = player.getScheme().getBox(k, x).getRestrictionColour().equals(dice.getColour());
                            else if (player.getScheme().getBox(k, x).getRestrictionValue() != null)
                                bool = player.getScheme().getBox(k, x).getRestrictionValue().equals(dice.faceToNo());
                            else
                                bool = true;
                        }
                    }
            }
            return bool;
        }

        public int stringtoInt(String face){
        int i=0;

        if(face.equals("\u2680")){
            i=1;
        }
        if(face.equals("\u2681")){
            i=2;
        }
        if(face.equals("\u2682")){
            i=3;
        }
        if(face.equals("\u2683")){
            i=4;
        }
        if(face.equals("\u2684")){
            i=5;
        }
        if(face.equals("\u2685")){
            i=6;
        }
        return i;
    }
        public String intToString(int i) {
            String s = new String();
            if (i == 1)
                s = "\u2680";
            if (i == 2)
                s = "\u2681";
            if (i == 3)
                s = "\u2682";
            if (i == 4)
                s = "\u2683";
            if (i == 5)
                s = "\u2684";
            if (i == 6)
                s = "\u2685";
            return s;
        }




    }
