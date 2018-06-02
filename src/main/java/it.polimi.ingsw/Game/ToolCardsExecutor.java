package it.polimi.ingsw.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class ToolCardsExecutor implements Serializable {



    //--------------------------------------Method that execute a tool card(requires input from keyboard)---------------
    private boolean checkCost(Player player,GreenCarpet greenCarpet,int selection) {
       boolean bool;
       int cost=0;
       for(int i =1;i<greenCarpet.getToolCards().length;i++)
           if(greenCarpet.getToolCards()[i].getSerialNumber()==selection) {
               cost = greenCarpet.getToolCards()[i].getCost();
           }
       bool = player.checkMarkers(cost);
       /*if (bool) {
           player.useMarkers(cost);
           if (cost == 1)
               greenCarpet.getToolCard(selection).setCost(2);
           int serialnumber = greenCarpet.getToolCard(selection).getSerialNumber();
           switch (serialnumber) {
           }

       }*/
       return bool;
   }

    //-----------------------------------------------Tool methods--------------------------------------------------------
    //--------------------------------------Tool card 1,10------------------------------------------------------------
    public boolean changeDiceCard (Player player,GreenCarpet greenCarpet,int serialnumber,int stockPos,int choice){
       boolean bool=checkCost(player,greenCarpet,serialnumber);
       Dice dice;
       Ruler ruler= new Ruler();
       int face;
       if (bool) {
           if (stockPos > 0 && stockPos <= greenCarpet.getStock().size()) {
               dice = greenCarpet.getDiceFromStock(stockPos);
               if (serialnumber == 1) {//----------------tool 1---------------------------------------------------------
                   face = ruler.stringtoInt(dice.getFace());
                   if (choice == 1 && face != 6)
                       face++;
                   else if (choice == 2 && face != 1)
                       face--;
                   else
                       bool = false;
                   dice.setFace(ruler.intToString(face));
               }else if (serialnumber==10) {//-----------tool 10(at the invocation choice can be any)-------------------
                   setOppositeFace(dice);
               }
               greenCarpet.setDiceInStock(dice);
           }else
               bool=false;
       }
        if(bool)
            player.useMarkers(greenCarpet,serialnumber);
       return bool;
    }

    //---------------------------------------Tool card 5----------------------------------------------------------------
    public boolean changeDiceCard (Player player,GreenCarpet greenCarpet,int serialnumber,int stockPos,int round,int dicePos){
       boolean bool=checkCost(player,greenCarpet,serialnumber);
       Dice dice;
       if (bool){
           if (stockPos > 0 && stockPos <= greenCarpet.getStock().size()) {
               dice=greenCarpet.getDiceFromStock(stockPos);
               bool=greenCarpet.checkEmptyRoundpath();
               if (bool && dicePos>0 && dicePos<=greenCarpet.getnPlayers()*2+1 && round >0 && round <=10 && greenCarpet.getDiceFromRoundPath(dicePos,round)!=null){
                  greenCarpet.changeDiceFromRoundPath(dicePos,round,dice);
               }else{
                   bool=false;
                   greenCarpet.setDiceInStock(dice);
               }
           }else
               bool=false;
       }
       if (bool){
           player.useMarkers(greenCarpet,serialnumber);
       }
       return bool;
    }

    //---------------------------------------Tool card 7----------------------------------------------------------------
    public boolean changeDiceCard (Player player,GreenCarpet greenCarpet,int serialnumber){
        boolean bool=checkCost(player,greenCarpet,serialnumber);
        if (bool) {
            greenCarpet.reRollStock();
            player.useMarkers(greenCarpet, serialnumber);
        }
        return bool;
    }

    //---------------------------------------Tool Card 2 and 3----------------------------------------------------------
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int serialnumber,int row,int col,int newRow,int newCol) {
        boolean bool = checkCost(player,greenCarpet,serialnumber);
        Dice dice;
        Ruler ruler = new Ruler();
        if (bool) {
            if (!player.getScheme().isEmpty()) {
                if (checkCoordinate(row,col) && player.getScheme().getBox(row, col).getAddedDice() != null) {
                    dice = player.getScheme().getBox(row, col).getAddedDice();
                    player.getScheme().setBoxes(null, row, col);
                    if (checkCoordinate(newRow,newCol) && player.getScheme().getBox(newRow, newCol).getAddedDice() == null) {
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
                        else if(serialnumber==12){
                            bool=ruler.checkCorrectPlacement(newRow,newCol,dice,player.getScheme());
                        }
                        if (!bool)
                            player.getScheme().setBoxes(dice, row, col);
                        else
                            player.getScheme().setBoxes(dice,newRow,newCol);
                    } else
                        bool = false;
                } else
                    bool = false;
            } else
                bool = false;
        }
        if(bool && serialnumber!=12){
            player.useMarkers(greenCarpet,serialnumber);

        }
        return bool;
    }

    //---------------------------------------Tool card 4----------------------------------------------------------------
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int serialnumber,int row1,int col1,int newRow1,int newCol1,int row2,int col2,int newRow2,int newCol2) {
        boolean bool = checkCost(player,greenCarpet,serialnumber);
        Ruler ruler = new Ruler();
        if (bool) {
            if (ruler.schemeCount(player.getScheme()) >= 2) {
                if (checkCoordinate(row1,col1) && player.getScheme().getBox(row1, col1).getAddedDice() != null) {
                    if (row1 != row2 || col1 != col2) {
                        if (checkCoordinate(row2,col2) && player.getScheme().getBox(row2, col2).getAddedDice() != null) {
                            if (checkCoordinate(newRow1,newCol1) && player.getScheme().getBox(newRow1, newCol1).getAddedDice() == null) {
                                if (newRow1 != newRow2 || newCol1 != newCol2) {
                                    if (checkCoordinate(newRow2,newCol2) && (player.getScheme().getBox(newRow2, newCol2).getAddedDice() == null || (newRow2 == row1 && newCol2 == col1))) {
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
                                        }
                                        else {
                                            player.getScheme().setBoxes(dice1, row1, col1);
                                            player.getScheme().setBoxes(dice2, row2, col2);
                                        }
                                    }
                                    else
                                        bool=false;
                                }
                                else
                                    bool=false;
                            }
                            else
                                bool=false;

                        }
                        else
                            bool=false;

                    }
                    else
                        bool=false;
                }
                else
                    bool=false;
            }
            else
                bool=false;
        }
        if(bool && serialnumber!=12){
            player.useMarkers(greenCarpet,serialnumber);
        }
        return bool;

    }

    //---------------------------------------Tool card 12---------------------------------------------------------------
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int serialnumber,int numdice,int row1,int col1,int newRow1,int newCol1,int row2,int col2,int newRow2,int newCol2,int round, int dicePos) {
        boolean bool = checkCost(player,greenCarpet,serialnumber);
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
                   Dice dice11=player.getScheme().getBox(row1,col1).getAddedDice();
                   if (dice11.getColour().equals(dice.getColour()))
                        bool=useMovementCard(player,greenCarpet,serialnumber,row1,col1,newRow1,newCol1);
                   else
                       bool=false;
                    break;
                case 2:
                    Dice dice1 = player.getScheme().getBox(row1, col1).getAddedDice();
                    Dice dice2 = player.getScheme().getBox(row2, col2).getAddedDice();
                    if(dice1.getColour().equals(dice.getColour())&& dice2.getColour().equals(dice.getColour())){
                        bool=useMovementCard(player,greenCarpet,serialnumber,row1,col1,newRow1,newCol1,row2,col2,newRow2,newCol2);
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

    //----------------------------------tool card 6 and 11--------------------------------------------------------------
    public Dice usePlacementCard(Player player,GreenCarpet greenCarpet, int stockPos,int serialnumber, int value){
        Dice dice = null;
        Ruler ruler=new Ruler();
        boolean bool=checkCost(player,greenCarpet,serialnumber);
        if(bool) {
            if (stockPos > 0 && stockPos <= greenCarpet.getStock().size()) {
                dice = greenCarpet.getDiceFromStock(stockPos);
                if (serialnumber==6)
                  dice.roll();
                else if (serialnumber==11){
                    greenCarpet.getDiceBucket().insertDice(dice);
                    dice=greenCarpet.getDiceBucket().educe();
                    dice.setFace(ruler.intToString(value));
                }
                player.useMarkers(greenCarpet, serialnumber);
            }
        }
        return dice;
    }

    //----------------------------------tool card 8 and 9---------------------------------------------------------------
    public boolean usePlacementCard(Player player,GreenCarpet greenCarpet, int stockPos,int serialnumber,int row,int col){
       boolean bool=checkCost(player,greenCarpet,serialnumber);
       Dice dice;
       Ruler ruler=new Ruler();
       if (bool){
           if (stockPos > 0 && stockPos <= greenCarpet.getStock().size() && checkCoordinate(row,col)) {
               dice=greenCarpet.getDiceFromStock(stockPos);
               if(serialnumber==9) {
                   bool = ruler.checkEmptyNeighbors(row, col, player.getScheme());
                   if (bool && player.getScheme().getBox(row, col).getAddedDice() != null && player.getScheme().getBox(row, col).getRestrictionColour() != null) {
                       bool = dice.getColour().equals(player.getScheme().getBox(row, col).getRestrictionColour());
                   } else if (bool && player.getScheme().getBox(row, col).getAddedDice() != null && player.getScheme().getBox(row, col).getRestrictionValue() != null) {
                       bool = dice.faceToNo().equals(player.getScheme().getBox(row, col).getRestrictionValue());
                   }
               }else if(serialnumber==8){
                   bool=ruler.checkCorrectPlacement(row,col,dice,player.getScheme());
               }
               if (bool){
                   if (serialnumber==8)
                       player.setSecondTurn(false);
                   player.getScheme().setBoxes(dice,row,col);
                   player.useMarkers(greenCarpet,serialnumber);
               }else
                   greenCarpet.setDiceInStock(dice);
           }
       }
       return bool;
    }

    //---------------------------------------Returns true if the row and col are in the scheme--------------------------
    private boolean checkCoordinate(int row,int col){
        if(row >= 0 && row <= 3 && col >= 0 && col <= 4)
            return true;
        return false;
    }
    //-----------------------------------------------Conversion methods-------------------------------------------------
    private void setOppositeFace(Dice dice){
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
