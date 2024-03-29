package itpolimiingsw.GameTools;

import itpolimiingsw.GameItems.Dice;

import java.io.Serializable;

public class ToolCardsExecutor implements Serializable {


    /**
     * method that check if is there are enough markers to use the tool
     *
     * @param player      is the player that want to use the tool
     * @param greenCarpet the greencarpet
     * @param selection   index of the tool in the grrencarpet
     * @return true if the player have more markers than the cost of the tool card
     */
    private boolean checkCost(Player player, GreenCarpet greenCarpet, int selection) {
        boolean bool;
        int cost = 0;
        for (int i = 0; i < greenCarpet.getToolCards().length; i++)
            if (greenCarpet.getToolCards()[i].getSerialNumber() == selection) {
                cost = greenCarpet.getToolCards()[i].getCost();
            }
        bool = player.checkMarkers(cost);
        return bool;
    }

    /**
     * method for toolcards 1 and 10 usage
     *
     * @param player       the player designed
     * @param greenCarpet  the greencarpet
     * @param serialnumber is the serial number of the tool card
     * @param stockPos     is the index of the dice in the stock
     * @param choice       is the choice for the tool card
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean changeDiceCard(Player player, GreenCarpet greenCarpet, int serialnumber, int stockPos, int choice) {
        boolean bool = checkCost(player, greenCarpet, serialnumber);
        Dice dice;
        Ruler ruler = new Ruler();
        int face;
        if (bool) {
            if (stockPos > 0 && stockPos <= greenCarpet.getStock().size()) {
                dice = greenCarpet.getDiceFromStock(stockPos);
                if (serialnumber == 1) {                             //----------------tool 1----------------------------
                    face = ruler.stringtoInt(dice.getFace());
                    if (choice == 1 && face != 6)
                        face++;
                    else if (choice == 2 && face != 1)
                        face--;
                    else
                        bool = false;
                    dice.setFace(ruler.intToString(face));
                } else if (serialnumber == 10) {                        //------------------tool 10-------------------------
                    setOppositeFace(dice);
                }
                greenCarpet.setDiceInStock(dice);
            } else
                bool = false;
        }
        if (bool)
            player.useMarkers(greenCarpet, serialnumber);
        return bool;
    }

    /**
     * method for toolcards 5 usage
     *
     * @param player         the player designed.
     * @param greenCarpet    the greencarpet.
     * @param serialnumber   is the serial number of the tool card.
     * @param stockPos       is the index of the dice in the stock.
     * @param roundPathCoord is an array of coordinates of the dice in the roundpath
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean changeDiceCard(Player player, GreenCarpet greenCarpet, int serialnumber, int stockPos, int[] roundPathCoord) {
        boolean bool = checkCost(player, greenCarpet, serialnumber);
        int round = roundPathCoord[0];
        int dicePos = roundPathCoord[1];
        Dice dice;
        if (bool) {
            if (stockPos > 0 && stockPos <= greenCarpet.getStock().size()) {
                dice = greenCarpet.getDiceFromStock(stockPos);
                bool = greenCarpet.checkEmptyRoundpath();
                if (bool && dicePos > 0 && dicePos <= greenCarpet.getnPlayers() * 2 + 1 && round > 0 && round <= 10 && greenCarpet.getDiceFromRoundPath(dicePos, round) != null) {
                    greenCarpet.changeDiceFromRoundPath(dicePos, round, dice);
                } else {
                    bool = false;
                    greenCarpet.setDiceInStock(dice);
                }
            } else
                bool = false;
        }
        if (bool) {
            player.useMarkers(greenCarpet, serialnumber);
        }
        return bool;
    }

    /**
     * method for toolcards 7 usage
     *
     * @param player       the player designed.
     * @param greenCarpet  the greencarpet.
     * @param serialnumber is the serial number of the tool card.
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean changeDiceCard(Player player, GreenCarpet greenCarpet, int serialnumber) {
        boolean bool = checkCost(player, greenCarpet, serialnumber);
        if (bool) {
            greenCarpet.reRollStock();
            player.useMarkers(greenCarpet, serialnumber);
        }
        return bool;
    }

    /**
     * method for toolcards 2 and 3 usage
     *
     * @param player       the player designed.
     * @param greenCarpet  the greencarpet.
     * @param serialnumber is the serial number of the tool card.
     * @param coordinates  is an array of int where there are all the coordinates
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean useMovementCard(Player player, GreenCarpet greenCarpet, int serialnumber, int[] coordinates) {
        boolean bool = checkCost(player, greenCarpet, serialnumber);
        int row = coordinates[0];
        int col = coordinates[1];
        int newRow = coordinates[2];
        int newCol = coordinates[3];
        Dice dice;
        Ruler ruler = new Ruler();
        if (bool) {
            if (!player.getScheme().isEmpty()) {
                if (checkCoordinate(row, col) && player.getScheme().getBox(row, col).getAddedDice() != null) {
                    dice = player.getScheme().getBox(row, col).getAddedDice();
                    player.getScheme().setBoxes(null, row, col);
                    if (checkCoordinate(newRow, newCol) && player.getScheme().getBox(newRow, newCol).getAddedDice() == null) {
                        //----------------------Tool card number 2---------------------
                        if (serialnumber == 2) {
                            bool = ruler.checkNeighborsFaces(newRow, newCol, dice, player.getScheme())
                                    && (player.getScheme().getBox(newRow, newCol).getRestrictionValue() == null
                                    || player.getScheme().getBox(newRow, newCol).getRestrictionValue().equals(dice.faceToNo()));
                            //----------------------Tool card number 3---------------------
                        } else if (serialnumber == 3) {
                            bool = ruler.checkNeighborsColours(newRow, newCol, dice, player.getScheme())
                                    && (player.getScheme().getBox(newRow, newCol).getRestrictionColour() == null
                                    || player.getScheme().getBox(newRow, newCol).getRestrictionColour().equals(dice.getColour()));
                        }
                        //----------------------Tool card number 12 case 1---------------------
                        else if (serialnumber == 12) {
                            bool = ruler.checkCorrectPlacement(newRow, newCol, dice, player.getScheme());
                        }
                        if (!bool)
                            player.getScheme().setBoxes(dice, row, col);
                        else
                            player.getScheme().setBoxes(dice, newRow, newCol);
                    } else
                        bool = false;
                } else
                    bool = false;
            } else
                bool = false;
        }
        if (bool && serialnumber != 12) {
            player.useMarkers(greenCarpet, serialnumber);

        }
        return bool;
    }

    /**
     *method for toolcards 4 usage
     *
     * @param player       the player designed.
     * @param greenCarpet  the greencarpet.
     * @param serialnumber is the serial number of the tool card.
     * @param coord1Dice   is an array of int where there are the coordinates of the firs dice
     * @param coord2Dice   is an array of int where there are the coordinates of the firs dice
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean useMovementCard(Player player, GreenCarpet greenCarpet, int serialnumber, int[] coord1Dice, int[] coord2Dice) {
        boolean bool = checkCost(player, greenCarpet, serialnumber);
        int row1 = coord1Dice[0];
        int col1 = coord1Dice[1];
        int newRow1 = coord1Dice[2];
        int newCol1 = coord1Dice[3];
        int row2 = coord2Dice[0];
        int col2 = coord2Dice[1];
        int newRow2 = coord2Dice[2];
        int newCol2 = coord2Dice[3];
        Ruler ruler = new Ruler();
        if (bool) {
            if (ruler.schemeCount(player.getScheme()) >= 2) {
                if (checkCoordinate(row1, col1) && player.getScheme().getBox(row1, col1).getAddedDice() != null) {
                    if (row1 != row2 || col1 != col2) {
                        if (checkCoordinate(row2, col2) && player.getScheme().getBox(row2, col2).getAddedDice() != null) {
                            if (checkCoordinate(newRow1, newCol1) && player.getScheme().getBox(newRow1, newCol1).getAddedDice() == null) {
                                if (newRow1 != newRow2 || newCol1 != newCol2) {
                                    if (checkCoordinate(newRow2, newCol2) && (player.getScheme().getBox(newRow2, newCol2).getAddedDice() == null || (newRow2 == row1 && newCol2 == col1))) {
                                        Dice dice1 = player.getScheme().getBox(row1, col1).getAddedDice();
                                        Dice dice2 = player.getScheme().getBox(row2, col2).getAddedDice();
                                        player.getScheme().setBoxes(null, row1, col1);
                                        player.getScheme().setBoxes(null, row2, col2);
                                        bool = ruler.checkCorrectPlacement(newRow1, newCol1, dice1, player.getScheme());
                                        if (bool) {
                                            player.getScheme().setBoxes(dice1, newRow1, newCol1);
                                            bool = ruler.checkCorrectPlacement(newRow2, newCol2, dice2, player.getScheme());
                                            if (bool) {
                                                player.getScheme().setBoxes(dice2, newRow2, newCol2);
                                            } else {
                                                player.getScheme().setBoxes(null, newRow1, newCol1);
                                                player.getScheme().setBoxes(dice1, row1, col1);
                                                player.getScheme().setBoxes(dice2, row2, col2);
                                            }
                                        } else {
                                            player.getScheme().setBoxes(dice1, row1, col1);
                                            player.getScheme().setBoxes(dice2, row2, col2);
                                        }
                                    } else
                                        bool = false;
                                } else
                                    bool = false;
                            } else
                                bool = false;

                        } else
                            bool = false;

                    } else
                        bool = false;
                } else
                    bool = false;
            } else
                bool = false;
        }
        if (bool && serialnumber != 12) {
            player.useMarkers(greenCarpet, serialnumber);
        }
        return bool;
    }

    /**
     *method for toolcards 12 usage
     *
     * @param player       the player designed.
     * @param greenCarpet  the greencarpet.
     * @param serialnumber is the serial number of the tool card.
     * @param numdice      number of dice you want to move
     * @param allcoordinates array of cordinates
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int serialnumber,int numdice,int[] allcoordinates) {
        boolean bool = checkCost(player,greenCarpet,serialnumber);
        int[] coord1dice= new int[4];
        int[] coord2dice= new int[4];
        for (int k=0;k<allcoordinates.length;k++) {
            if (k<4)
             coord1dice[k] = allcoordinates[k];
            else if (k<8)
                coord2dice[k % 4] = allcoordinates[k];
        }
        int round=allcoordinates[9];
        int dicePos=allcoordinates[10];

        Dice dice= new Dice(null);
        if (dicePos>0 && dicePos<=greenCarpet.getnPlayers()*2+1 && round >0 && round <=10)
            dice=greenCarpet.getDiceFromRoundPath(dicePos,round);
        else
            bool=false;
        if (dice==null)
            bool=false;
        if(numdice!=1 && numdice!=2)
            bool=false;
        if (bool) {
            switch (numdice){
                case 1:
                   Dice dice11=player.getScheme().getBox(coord1dice[0],coord1dice[1]).getAddedDice();
                   if (dice11.getColour().equals(dice.getColour()))
                        bool=useMovementCard(player,greenCarpet,serialnumber,coord1dice);
                   else
                       bool=false;
                    break;
                case 2:
                    Dice dice1 = player.getScheme().getBox(coord1dice[0],coord1dice[1]).getAddedDice();
                    Dice dice2 = player.getScheme().getBox(coord2dice[0], coord2dice[1]).getAddedDice();
                    if(dice1.getColour().equals(dice.getColour())&& dice2.getColour().equals(dice.getColour())){
                        bool=useMovementCard(player,greenCarpet,serialnumber,coord1dice,coord2dice);
                    }
                    else
                        bool=false;

                    break;
            }

        }
        if(bool){
            player.useMarkers(greenCarpet,serialnumber);
        }
        return bool;

    }

    /**
     *method for toolcards 6 and 11 usage
     *
     * @param player       the player designed.
     * @param greenCarpet  the greencarpet.
     * @param stockPos  index of the dice position in the stock
     * @param serialnumber is the serial number of the tool card.
     * @param value value of the new faces
     * @return true if the tool card has been used correctly, false otherwise
     */
    public Dice usePlacementCard(Player player,GreenCarpet greenCarpet, int stockPos,int serialnumber, int value){
        Dice dice = null;
        Ruler ruler=new Ruler();
        boolean bool=checkCost(player,greenCarpet,serialnumber);

        if(bool) {
            if (stockPos > 0 && stockPos <= greenCarpet.getStock().size()) {
                dice = new Dice(greenCarpet.checkDiceFromStock(stockPos).getColour());
                if (serialnumber==6)
                  dice.roll();
                else if (serialnumber==11){
                    greenCarpet.getDiceBucket().insertDice(dice);
                    dice=greenCarpet.getDiceBucket().educe();
                    dice.setFace(ruler.intToString(value));
                }
                player.useMarkers(greenCarpet, serialnumber);
                return dice;
            }
        }else
            return null;
        return null;
    }

    /**
     *method for toolcards 8 and 9 usage
     * @param player       the player designed.
     * @param greenCarpet  the greencarpet.
     * @param stockPos  index of the dice position in the stock
     * @param serialnumber is the serial number of the tool card.
     * @param row
     * @param col
     * @return true if the tool card has been used correctly, false otherwise
     */
    public boolean usePlacementCard(Player player,GreenCarpet greenCarpet, int stockPos,int serialnumber,int row,int col){
       boolean bool=checkCost(player,greenCarpet,serialnumber);
       Dice dice;
       Ruler ruler=new Ruler();
       if (bool){
           if (stockPos > 0 && stockPos <= greenCarpet.getStock().size() && checkCoordinate(row,col)) {
               dice = greenCarpet.checkDiceFromStock(stockPos);
               if (serialnumber == 9) {
                   if (player.getScheme().isEmpty())
                       bool = ruler.checkCorrectPlacement(row, col, dice, player.getScheme());
                   else {
                       bool = ruler.checkEmptyNeighbors(row, col, player.getScheme());
                       if (bool && player.getScheme().getBox(row, col).getAddedDice() != null && player.getScheme().getBox(row, col).getRestrictionColour() != null) {
                           bool = dice.getColour().equals(player.getScheme().getBox(row, col).getRestrictionColour());
                       } else if (bool && player.getScheme().getBox(row, col).getAddedDice() != null && player.getScheme().getBox(row, col).getRestrictionValue() != null) {
                           bool = dice.faceToNo().equals(player.getScheme().getBox(row, col).getRestrictionValue());
                       }
                   }
               } else if (serialnumber == 8) {
                   bool = ruler.checkCorrectPlacement(row, col, dice, player.getScheme());
               }
               if (bool) {
                   if (serialnumber == 8)
                       player.setSecondTurn(false);
                   dice = greenCarpet.getDiceFromStock(stockPos);
                   player.getScheme().setBoxes(dice, row, col);
                   player.useMarkers(greenCarpet, serialnumber);
               }
           }else
             return false;
       }
       return bool;
    }

    /**
     * method that chek if two coordinates row and col are inside a scheme
     *
     * @param row
     * @param col
     * @return true if the row and col are in the scheme
     */
    private boolean checkCoordinate(int row,int col){
        return (row >= 0 && row <= 3 && col >= 0 && col <= 4);
    }

    /**
     * set a face of a dice with the opposite face
     *
     * @param dice the dice i want to change
     */
    public void setOppositeFace(Dice dice){
        if (dice.getFace().equals("\u2680"))
            dice.setFace("\u2685");
        else if (dice.getFace().equals("\u2681"))
            dice.setFace("\u2684");
        else if (dice.getFace().equals("\u2682"))
            dice.setFace("\u2683");
        else if (dice.getFace().equals("\u2683"))
            dice.setFace("\u2682");
        else if (dice.getFace().equals("\u2684"))
            dice.setFace("\u2681");
        else if (dice.getFace().equals("\u2685"))
            dice.setFace("\u2680");
    }
}
