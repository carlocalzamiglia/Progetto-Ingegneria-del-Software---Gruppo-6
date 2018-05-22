package it.polimi.ingsw.Game;

import java.util.ArrayList;
import java.util.Scanner;

public class ToolCardsExecutor {


   public boolean executeToolCard(int serialnumber,Player player,GreenCarpet greenCarpet,Ruler ruler,DiceBucket diceBucket){
           boolean bool=true;
           Scanner scan = new Scanner(System.in);
           int cost= greenCarpet.getToolCard(serialnumber).getCost();
           bool=player.useMarkers(cost);
           String s;
           if (bool) {
               if (cost == 1)
                   greenCarpet.getToolCard(serialnumber).setCost(2);
               int row = 0;
               int col = 0;
               boolean correct = false;
               boolean todo = true;
               switch (serialnumber) {
                   case 1:
                       int d = 0;
                       int num=0;
                       Dice dice;
                       int choose = 0;
                       boolean try1=true;
                       while(try1) {
                           System.out.println("Scegli un dado \n");
                           d = scan.nextInt();
                           if (d < greenCarpet.getStock().size()+1 && d>0)
                               try1 = false;
                           if(try1)
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                           dice = greenCarpet.getDiceFromStock(d);
                           num = stringtoInt(dice.getFace());
                       while (todo) {
                           System.out.println("1 per aumentare 2 per diminuire");
                           choose = scan.nextInt();
                           if (choose == 1) {
                               if (num != 6) {
                                   num = num + 1;
                                   todo = false;
                               }

                           } else if (choose == 2) {
                               if (num != 1) {
                                   num = num - 1;
                                   todo = false;
                               }

                           } else {
                               System.out.println("Invalid Selection \n Retry...");
                           }
                       }
                       dice.setFace(intToString(num));
                       while (!correct) {
                           System.out.println("Scegli le coordinate di inserimento");
                           row = scan.nextInt();
                           col = scan.nextInt();
                           if (row>=0&&row<=3&&col>=0&&col<=4) {
                               correct = ruler.checkCorrectPlacement(row, col, dice, player.getScheme());
                               if (!correct)
                                   System.out.println("Coordinate non valide");
                           }
                           else
                               System.out.println("Coordinate non valide");
                       }
                       player.getScheme().setBoxes(dice, row, col);

                       break;
                   case 2:
                       Dice dice2=null ;
                       int rowTmp=7;
                       int colTmp=7;
                       if(!player.getScheme().isEmpty()){
                           while (!correct) {
                               System.out.println("Scegli le coordinate del dado da spostare");
                               row = scan.nextInt();
                               col = scan.nextInt();
                               rowTmp=row;
                               colTmp=col;
                               if (row>=0&&row<=3&&col>=0&&col<=4 && player.getScheme().getBox(row,col).getAddedDice()!=null) {
                                   dice2=player.getScheme().getBox(row,col).getAddedDice();
                                   System.out.println("Scegli nuove coordinate di inserimento");
                                   row = scan.nextInt();
                                   col = scan.nextInt();
                                   if (row>=0&&row<=3&&col>=0&&col<=4 && player.getScheme().getBox(row,col).getAddedDice()==null){
                                       correct=ruler.checkNeighborsFaces(row,col,player.getScheme().getBox(rowTmp,colTmp).getAddedDice(),player.getScheme())&&(player.getScheme().getBox(row,col).getRestrictionValue().equals(faceToNo(dice2.getFace())) || player.getScheme().getBox(row,col).getRestrictionValue()==null);
                                       if(!correct)
                                           System.out.println("Coordinate non valide");
                                   }
                                   else
                                       System.out.println("Operazione non valida");
                               }
                               else
                                   System.out.println("Coordinate non valide");
                           }
                           if(dice2!=null && rowTmp!=7 && colTmp!=7 ) {
                               player.getScheme().getBox(row, col).setAddedDice(dice2);
                               player.getScheme().setBoxes(null,rowTmp,colTmp);
                           }


                       }
                       else
                           System.out.println("Schema vuoto. Carta non utilizzabile");

                       break;
                   case 3:
                       Dice dice3=null ;
                       int rowTmp3=7;
                       int colTmp3=7;
                       if(!player.getScheme().isEmpty()){
                           while (!correct) {
                               System.out.println("Scegli le coordinate del dado da spostare");
                               row = scan.nextInt();
                               col = scan.nextInt();
                               rowTmp3=row;
                               colTmp3=col;
                               if (row>=0&&row<=3&&col>=0&&col<=4 && player.getScheme().getBox(row,col).getAddedDice()!=null) {
                                   dice3=player.getScheme().getBox(row,col).getAddedDice();
                                   System.out.println("Scegli nuove coordinate di inserimento");
                                   row = scan.nextInt();
                                   col = scan.nextInt();
                                   if (row>=0&&row<=3&&col>=0&&col<=4 && player.getScheme().getBox(row,col).getAddedDice()==null){
                                       correct=ruler.checkNeighborsColours(row,col,player.getScheme().getBox(rowTmp3,colTmp3).getAddedDice(),player.getScheme())&&(player.getScheme().getBox(row,col).getRestrictionColour().equals(dice3.getColour()) || player.getScheme().getBox(row,col).getRestrictionColour()==null);
                                       if(!correct)
                                           System.out.println("Coordinate non valide");
                                   }
                                   else
                                       System.out.println("Operazione non valida");
                               }
                               else
                                   System.out.println("Coordinate non valide");
                           }
                           if(dice3!=null && rowTmp3!=7 && colTmp3!=7 ) {
                               player.getScheme().getBox(row, col).setAddedDice(dice3);
                               player.getScheme().setBoxes(null,rowTmp3,colTmp3);
                           }


                       }
                       else
                           System.out.println("Schema vuoto. Carta non utilizzabile");
                       break;
                   case 4:
                       int row41=0;
                       int row42=0;
                       int col41=0;
                       int col42=0;
                       int rowTmp41=0;
                       int rowTmp42=0;
                       int colTmp41=0;
                       int colTmp42=0;
                       while (!correct) {
                           System.out.println("Scegli le coordinate del primo dado da spostare");
                           row41 = scan.nextInt();
                           col41 = scan.nextInt();
                           if (row41 >= 0 && row41 <= 3 && col41 >= 0 && col41 <= 4 && player.getScheme().getBox(row41, col41).getAddedDice() != null) {
                               System.out.println("Scegli le coordinate del secondo dado da spostare");
                               row42 = scan.nextInt();
                               col42 = scan.nextInt();
                               if (row41 != row42 || col41 != col42) {
                                   if (row42 >= 0 && row42 <= 3 && col42 >= 0 && col42 <= 4 && player.getScheme().getBox(row42, col42).getAddedDice() != null) {
                                       System.out.println("Scegli le nuove coordinate del primo dado");
                                       rowTmp41 = scan.nextInt();
                                       colTmp41 = scan.nextInt();
                                       if (rowTmp41 >= 0 && rowTmp41 <= 3 && colTmp41 >= 0 && colTmp41 <= 4 && player.getScheme().getBox(rowTmp41, colTmp41).getAddedDice() == null) {
                                           System.out.println("Scegli le nuove coordinate del secondo dado");
                                           rowTmp42 = scan.nextInt();
                                           colTmp42 = scan.nextInt();
                                           if (rowTmp41 != rowTmp42 || colTmp41 != colTmp42) {
                                               if (rowTmp42 >= 0 && rowTmp42 <= 3 && colTmp42 >= 0 && colTmp42 <= 4 && (player.getScheme().getBox(rowTmp42, colTmp42).getAddedDice() == null || (rowTmp42 == row41 && colTmp42 == col41))) {
                                                   Dice dice41 = player.getScheme().getBox(row41, col41).getAddedDice();
                                                   Dice dice42 = player.getScheme().getBox(row42, col42).getAddedDice();
                                                   player.getScheme().setBoxes(null, row41, col41);
                                                   player.getScheme().setBoxes(null, row42, col42);
                                                   correct = ruler.checkCorrectPlacement(rowTmp41, colTmp41, dice41, player.getScheme());
                                                   if (correct) {
                                                       player.getScheme().setBoxes(dice41,rowTmp41,colTmp41);
                                                       correct=ruler.checkCorrectPlacement(rowTmp42, colTmp42, dice42, player.getScheme());
                                                       if(correct){
                                                           player.getScheme().setBoxes(dice42,rowTmp42,colTmp42);
                                                       }
                                                       else{
                                                           player.getScheme().setBoxes(null,rowTmp41,colTmp41);
                                                           player.getScheme().setBoxes(dice41, row41, col41);
                                                           player.getScheme().setBoxes(dice42, row42, col42);
                                                       }
                                                   }
                                               }
                                               if (!correct) {
                                                   System.out.println("operazione non valida");
                                               }
                                           } else
                                               System.out.println("scelta la stessa posizione");

                                       } else
                                           System.out.println("Nuove cordinate del primo dado non valide");
                                   }
                                   else
                                       System.out.println("cordinate secondo dado non valide");
                               }
                               else
                                   System.out.println("scelto lo stesso dado");
                           }
                           else
                               System.out.println("cordinate primo dado non valide");
                       }
                       break;
                   case 5:
                       boolean try5=true;
                       int d5 = 0;
                       Dice dice5 ;
                       while(try5) {
                           System.out.println("Scegli un dado \n");
                           d5 = scan.nextInt();
                           if (d5 < greenCarpet.getStock().size()+1 && d5>0)
                               try5 = false;
                           if(try5)
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       dice5 = greenCarpet.getDiceFromStock(d5);
                       while(!try5){
                           System.out.println("scegli dado dal Roundpath");
                           row=scan.nextInt();
                           col=scan.nextInt();
                           if(row>=1 && row<=9 && col>=1 && col<=9 && greenCarpet.getDiceFromRoundPath(row,col)!=null   ){
                               try5=true;
                           }
                           else
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       dice5=greenCarpet.changeDiceFromRoundPath(row,col,dice5);
                       while (!correct) {
                           System.out.println("Scegli le coordinate di inserimento");
                           row = scan.nextInt();
                           col = scan.nextInt();
                           if (row >= 0 && row <= 3 && col >= 0 && col <= 4) {
                               correct = ruler.checkCorrectPlacement(row, col, dice5, player.getScheme());
                               if (!correct)
                                   System.out.println("Coordinate non valide");
                           } else
                               System.out.println("Coordinate non valide");
                       }
                       player.getScheme().setBoxes(dice5, row, col);
                       break;
                   case 6:
                       boolean try6=true;
                       int d6 = 0;
                       Dice dice6 ;
                       while(try6) {
                           System.out.println("Scegli un dado \n");
                           d6 = scan.nextInt();
                           if (d6 < greenCarpet.getStock().size()+1 && d6>0)
                               try6 = false;
                           if(try6)
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       dice6 = greenCarpet.getDiceFromStock(d6);
                       dice6.roll();
                       dice6.dump();
                       boolean keepGoing6=true;
                       for (int i=0;i<4 && keepGoing6 ;i++) {
                           for (int j = 0; j < 5 && keepGoing6; j++) {
                               if (ruler.checkCorrectPlacement(i, j, dice6, player.getScheme())) {
                                   keepGoing6 = false;
                               }
                           }
                       }
                       if (keepGoing6)
                           greenCarpet.setDiceInStock(dice6);
                       else {
                           while (!keepGoing6) {
                               System.out.println("Scegli le coordinate di inserimento");
                               row = scan.nextInt();
                               col = scan.nextInt();
                               if (row>=0&&row<=3&&col>=0&&col<=4) {
                                   keepGoing6 = ruler.checkCorrectPlacement(row, col, dice6, player.getScheme());
                                   if (!keepGoing6)
                                       System.out.println("Coordinate non valide");
                               }
                               else
                                   System.out.println("Coordinate non valide");
                           }
                           player.getScheme().setBoxes(dice6, row, col);
                       }

                       break;
                   case 7:
                       //-------------------------------ricordarsi i turni---------------------------------------------
                       for (Dice dice7:greenCarpet.getStock())
                           dice7.roll();
                   break;
                   case 9:
                       boolean try9=true;
                       int d9 = 0;
                       Dice dice9;
                       while(try9) {
                           System.out.println("Scegli un dado \n");
                           d9 = scan.nextInt();
                           if (d9 < greenCarpet.getStock().size()+1 && d9>0)
                               try9 = false;
                           if(try9)
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       dice9 = greenCarpet.getDiceFromStock(d9);
                       while(!correct){
                           System.out.println("Scegli le coordinate di inserimento");
                           row = scan.nextInt();
                           col = scan.nextInt();
                           if (row>=0 && row<=3 && col>=0 && col<=4) {
                               correct = ruler.checkEmptyNeighbors(row, col, player.getScheme());
                               if (!correct)
                                   System.out.println("Coordinate non valide");
                               else {
                                   if(player.getScheme().getBox(row,col).getRestrictionColour()!=null){
                                       correct=player.getScheme().getBox(row,col).getRestrictionColour().equals(dice9.getColour());
                                   }
                                   else if(player.getScheme().getBox(row,col).getRestrictionValue()!=null) {
                                       correct = player.getScheme().getBox(row, col).getRestrictionValue().equals(faceToNo(dice9.getFace()));
                                   }
                               }

                           }
                           else
                               System.out.println("Coordinate non valide");
                       }
                       player.getScheme().setBoxes(dice9,row,col);
                       break;
                   case 10:
                       boolean try10=true;
                       int d10 = 0;
                       Dice dice10 ;
                       while(try10) {
                           System.out.println("Scegli un dado \n");
                           d10 = scan.nextInt();
                           if (d10 < greenCarpet.getStock().size()+1 && d10>0)
                               try10 = false;
                           if(try10)
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       dice10 = greenCarpet.getDiceFromStock(d10);
                       if (dice10.getFace().equals("\u2680"))
                           dice10.setFace("\u2685");
                       else if (dice10.getFace().equals("\u2681"))
                           dice10.setFace("\u2684");
                       else if (dice10.getFace().equals("\u2682"))
                           dice10.setFace("\u2683");
                       else if (dice10.getFace().equals("\u2683"))
                           dice10.setFace("\u2682");
                       else if (dice10.getFace().equals("\u2684"))
                           dice10.setFace("\u2681");
                       else if (dice10.getFace().equals("\u2685"))
                           dice10.setFace("\u2680");
                       dice10.dump();
                       while(!correct){
                           System.out.println("Scegli le coordinate di inserimento");
                           row = scan.nextInt();
                           col = scan.nextInt();
                           if (row>=0 && row<=3 && col>=0 && col<=4) {
                               correct = ruler.checkCorrectPlacement(row, col, dice10, player.getScheme());
                               if (!correct)
                                   System.out.println("Coordinate non valide");
                           }
                           else
                               System.out.println("Coordinate non valide");
                       }
                       player.getScheme().setBoxes(dice10, row, col);
                       break;
                   case 11:
                       boolean try11=true;
                       int d11 = 0;
                       while(try11) {
                           System.out.println("Scegli un dado \n");
                           d11 = scan.nextInt();
                           if (d11 < greenCarpet.getStock().size()+1 && d11>0)
                               try11 = false;
                           if(try11)
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       diceBucket.insertDice(greenCarpet.getDiceFromStock(d11));
                       Dice dice11=diceBucket.educe();
                       System.out.println("Scegli il numero da piazzare");
                       dice11.setFace(intToString(scan.nextInt()));
                       dice11.dump();
                       while(!correct){
                           System.out.println("Scegli le coordinate di inserimento");
                           row = scan.nextInt();
                           col = scan.nextInt();
                           if (row>=0&&row<=3 &&col>=0&&col<=4) {
                               correct = ruler.checkCorrectPlacement(row, col, dice11, player.getScheme());
                               if (!correct)
                                   System.out.println("Coordinate non valide");

                           }
                           else
                               System.out.println("Coordinate non valide");
                       }
                       player.getScheme().setBoxes(dice11, row, col);
                       break;
                   case 12:
                       boolean try12 = false;

                       while(!try12){
                           System.out.println("scegli dado dal Roundpath");
                           col=scan.nextInt();
                           row=scan.nextInt();
                           if(row>=1 && row<=9 && col>=1 && col<=9 && greenCarpet.getDiceFromRoundPath(row,col)!=null   ){
                               for(int i12=0;i12<4;i12++)
                                   for(int j12=0;j12<5;j12++)
                                       if(player.getScheme().getBox(i12,j12).getAddedDice()!=null)
                                           if(player.getScheme().getBox(i12,j12).getAddedDice().getColour().equals(greenCarpet.getDiceFromRoundPath(row,col).getColour()))
                                              try12=true;
                               if(!try12)
                                   System.out.println("non hai dadi di questo colore \n Retry...");
                               }
                           else
                               System.out.println("Scelta non valida \n Riprova...");
                       }
                       Colour colour=greenCarpet.getDiceFromRoundPath(row,col).getColour();
                       int d12=0;
                       int row121=0;
                       int row122=0;
                       int col121=0;
                       int col122=0;
                       int rowTmp121=0;
                       int rowTmp122=0;
                       int colTmp121=0;
                       int colTmp122=0;
                       while (!correct) {
                           System.out.println("Quanti dadi vuoi spostare");
                           d12=scan.nextInt();
                           switch (d12){
                               case 1:
                                   System.out.println("Scegli le coordinate del dado da spostare");
                                   row121 = scan.nextInt();
                                   col121 = scan.nextInt();
                                   if (row121 >= 0 && row121 <= 3 && col121 >= 0 && col121 <= 4 && player.getScheme().getBox(row121, col121).getAddedDice() != null && player.getScheme().getBox(row121, col121).getAddedDice().getColour() == colour) {
                                       System.out.println("Scegli le nuove coordinate del dado");
                                       rowTmp121 = scan.nextInt();
                                       colTmp121 = scan.nextInt();
                                       if (rowTmp121 >= 0 && rowTmp121 <= 3 && colTmp121 >= 0 && colTmp121 <= 4 && player.getScheme().getBox(rowTmp121, colTmp121).getAddedDice() == null){
                                           Dice dice121 = player.getScheme().getBox(row121, col121).getAddedDice();
                                           player.getScheme().setBoxes(null, row121, col121);
                                           correct = ruler.checkCorrectPlacement(rowTmp121, colTmp121, dice121, player.getScheme());
                                           if(correct)
                                               player.getScheme().setBoxes(dice121, rowTmp121, colTmp121);
                                           else {
                                               player.getScheme().setBoxes(dice121, row121, col121);
                                               System.out.println("operazione non valida");
                                           }

                                       }
                                       else
                                           System.out.println("nuove coordinate non valide");
                                   }
                                   else
                                        System.out.println("coordinate non valide");
                                   break;
                               case 2:
                                   System.out.println("Scegli le coordinate del primo dado da spostare");
                                   row121 = scan.nextInt();
                                   col121 = scan.nextInt();
                                   if (row121 >= 0 && row121 <= 3 && col121 >= 0 && col121 <= 4 && player.getScheme().getBox(row121, col121).getAddedDice() != null && player.getScheme().getBox(row121, col121).getAddedDice().getColour() == colour) {
                                       System.out.println("Scegli le coordinate del secondo dado da spostare");
                                       row122 = scan.nextInt();
                                       col122 = scan.nextInt();
                                       if (row121 != row122 || col121 != col122) {
                                           if (row122 >= 0 && row122 <= 3 && col122 >= 0 && col122 <= 4 && player.getScheme().getBox(row122, col122).getAddedDice() != null && player.getScheme().getBox(row122, col122).getAddedDice().getColour() == colour) {
                                               System.out.println("Scegli le nuove coordinate del primo dado");
                                               rowTmp121 = scan.nextInt();
                                               colTmp121 = scan.nextInt();
                                               if (rowTmp121 >= 0 && rowTmp121 <= 3 && colTmp121 >= 0 && colTmp121 <= 4 && player.getScheme().getBox(rowTmp121, colTmp121).getAddedDice() == null) {
                                                   System.out.println("Scegli le nuove coordinate del secondo dado");
                                                   rowTmp122 = scan.nextInt();
                                                   colTmp122 = scan.nextInt();
                                                   if (rowTmp121 != rowTmp122 || colTmp121 != colTmp122) {
                                                       if (rowTmp122 >= 0 && rowTmp122 <= 3 && colTmp122 >= 0 && colTmp122 <= 4 && (player.getScheme().getBox(rowTmp122, colTmp122).getAddedDice() == null || (rowTmp122 == row121 && colTmp122 == col121))) {
                                                           Dice dice121 = player.getScheme().getBox(row121, col121).getAddedDice();
                                                           Dice dice122 = player.getScheme().getBox(row122, col122).getAddedDice();
                                                           player.getScheme().setBoxes(null, row121, col121);
                                                           player.getScheme().setBoxes(null, row122, col122);
                                                           correct = ruler.checkCorrectPlacement(rowTmp121, colTmp121, dice121, player.getScheme());
                                                           if (correct) {
                                                               player.getScheme().setBoxes(dice121, rowTmp121, colTmp121);
                                                               correct = ruler.checkCorrectPlacement(rowTmp122, colTmp122, dice122, player.getScheme());
                                                               if (correct) {
                                                                   player.getScheme().setBoxes(dice122, rowTmp122, colTmp122);
                                                               } else {
                                                                   player.getScheme().setBoxes(null, rowTmp121, colTmp121);
                                                                   player.getScheme().setBoxes(dice121, row121, col121);
                                                                   player.getScheme().setBoxes(dice122, row122, col122);
                                                               }
                                                           }
                                                       }
                                                   } else
                                                       System.out.println("stesse coordinate inserite");
                                               } else
                                                   System.out.println("nuove coordinate primo dado non corretto");
                                           } else
                                               System.out.println("secondo dado non corretto");
                                       } else
                                           System.out.println("stesso dado selezionato");
                                   } else
                                       System.out.println("primo dado non corretto");
                               break;
                           }
                       }
                       break;
               }
           }
           return bool;
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
    private String faceToNo(String face) {
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
}
