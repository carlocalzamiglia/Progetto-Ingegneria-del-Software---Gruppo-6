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
                return false;
            }
            if (scheme.isEmpty()) {
                if (row != 0 && row != 3 && col != 0 && col != 4)
                    isCorrect = false;
                else
                    isCorrect = checkBox(row,col);
            }
            else {
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

        //---------------------------Method that controls neighborhood restriction of a dice in a specific box----------
        private boolean checkNeighbors(int row, int col) {
            /*boolean bool = true;
            if(checkNeighborsColours(row,col,dice,scheme)&&checkNeighborsFaces(row,col,dice,scheme)&&!checkEmptyNeighbors(row,col,scheme));
            else
                bool=false;*/
            return (checkNeighborsColours(row,col,dice,scheme)&&checkNeighborsFaces(row,col,dice,scheme)&&!checkEmptyNeighbors(row,col,scheme));
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
                return true;
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
            if (row - 1 >= 0 && bool && scheme.getBox(row - 1, col).getAddedDice() != null){
                flag = 1;
                if (scheme.getBox(row - 1, col).getAddedDice().getFace().equals(dice.getFace()))
                    bool = false;
            }
            if (row + 1 <= 3 && bool && scheme.getBox(row + 1, col).getAddedDice() != null) {
                flag = 1;
                if (scheme.getBox(row + 1, col).getAddedDice().getFace().equals(dice.getFace()))
                    bool = false;
            }
            if (flag==0)
                  flag=checkDiagonallyNeighbors(scheme,row,col);
            if (flag == 0)
                bool = false;
            return bool;

        }

        //---------------------------Method that controls neighborhood restriction (only colors)------------------------
        public boolean checkNeighborsColours(int row, int col, Dice dice, Scheme scheme) {
            boolean bool = true;
            int flag = 0;
            if (scheme.isEmpty())
                return true;
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
            if (flag==0)
                flag=checkDiagonallyNeighbors(scheme,row,col);
            if (flag == 0)
                bool = false;
            return bool;
        }

        //---------------------------------check if there are diagonally adjacent dice----------------------------------
        private int checkDiagonallyNeighbors(Scheme scheme,int row,int col){
            int flag=0;
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

        //--------------------------------converts the face of a dice into an integer-----------------------------------
        public int stringtoInt(String face){
        int i=0;
        Dice d=new Dice(null);
        String[] faces= d.getFaces();
        for (int j=0;j<faces.length;j++)
            if (face.equals(faces[j]))
                i=j+1;
        return i;
    }

    //--------------------------------converts an integer into a face of a dice-----------------------------------------
        public String intToString(int i) {
            Dice d= new Dice(null);
            String[] faces= d.getFaces();
            return faces[i-1];
        }

    }
