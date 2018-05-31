package it.polimi.ingsw.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class ToolCardsExecutor implements Serializable {



    //--------------------------------------Method that execute a tool card(requires input from keyboard)---------------
   public boolean checkCost(Player player,GreenCarpet greenCarpet,int selection) {
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
    //---------------------------------------Tool number 2/3------------------------------------------------------------
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int choose,int row,int col,int newRow,int newCol) {
        boolean bool = checkCost(player,greenCarpet,choose);
        Dice dice;
        Ruler ruler = new Ruler();
        int serialnumber = choose;
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

    //-----------------------------------------------tool number 4------------------------------------------------------
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int choose,int row1,int col1,int newRow1,int newCol1,int row2,int col2,int newRow2,int newCol2) {
        boolean bool = checkCost(player,greenCarpet,choose);
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
        if(bool && choose!=12){
            player.useMarkers(greenCarpet,choose);
        }
        return bool;

    }

    //-----------------------------------------------tool number 12-----------------------------------------------------
    public boolean useMovementCard(Player player,GreenCarpet greenCarpet,int choose,int numdice,int row1,int col1,int newRow1,int newCol1,int row2,int col2,int newRow2,int newCol2,int round, int d) {
        boolean bool = checkCost(player,greenCarpet,choose);
        Dice dice= new Dice(null);
        if (d>0 && d<=greenCarpet.getnPlayers()*2+1 && round >0 && round <=10)
            dice=greenCarpet.getDiceFromRoundPath(d,round);
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
                        bool=useMovementCard(player,greenCarpet,choose,row1,col1,newRow1,newCol1);
                   else
                       bool=false;
                    break;
                case 2:
                    Dice dice1 = player.getScheme().getBox(row1, col1).getAddedDice();
                    Dice dice2 = player.getScheme().getBox(row2, col2).getAddedDice();
                    if(dice1.getColour().equals(dice.getColour())&& dice2.getColour().equals(dice.getColour())){
                        bool=useMovementCard(player,greenCarpet,choose,row1,col1,newRow1,newCol1,row2,col2,newRow2,newCol2);
                    }
                    else
                        bool=false;

                    break;
            }

        }
        if(bool){
            player.useMarkers(greenCarpet,choose);
        }
        return bool;

    }
    public boolean checkCoordinate(int row,int col){
        if(row >= 0 && row <= 3 && col >= 0 && col <= 4)
            return true;
        return false;
    }
    //-----------------------------------------------Conversion methods-------------------------------------------------
    /*private int stringtoInt(String face){ For use: call it from ruler!
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
    */
    private String intToString(int i){
       String s=new String();
       if (i==1)
           s="\u2680";
       if (i==2)
           s="\u2681";
       if (i==3)
           s="\u2682";
       if(i==4)
           s="\u2683";
       if (i==5)
           s="\u2684";
       if (i==6)
           s="\u2685";
       return s;
    }
   /* private String faceToNo(String face) { For use: call it from the dice itself!
        String s= null ;

        if (face == "\u2680") {
            s = "1";
        }
        if (face == "\u2681") {
            s = "2";
        }
        if (face == "\u2682") {
            s = "3";
        }
        if (face == "\u2683") {
            s = "4";
        }
        if (face == "\u2684") {
            s = "5";
        }
        if (face == "\u2685") {
            s= "6";
        }
        return s;
    }
    public int chooseInt(int n){
       return n;
    }
    */
}
