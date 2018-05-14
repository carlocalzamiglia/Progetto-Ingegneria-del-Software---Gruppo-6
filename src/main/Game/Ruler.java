package Game;


    public class Ruler {
        private Scheme scheme;
        private Dice dice;

        public Ruler(Player player) {
            this.scheme = player.getScheme();
        }

        public boolean checkCorrectPlacement(int row, int col,Dice dice) {
            boolean isCorrect = true;
            this.dice=dice;
            if (scheme.isEmpty() == true) {
                if (row != 0 && row != 3 && col != 0 && col != 4)
                    isCorrect = false;
                 else
                    if (checkBox(row, col) == false)
                        isCorrect = false;
            }
            else {
                isCorrect=checkBox(row,col);
                if (isCorrect)
                    isCorrect=checkNeighbors(row,col);
            }
            if(!isCorrect)
                System.out.println(isCorrect);
        return isCorrect;
        }

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
                    String num = faceToNo(dice.getFace());
                    if (scheme.getBox(row, col).getRestrictionValue().equals(num))
                        bool = true;
                    else
                        bool = false;
                }
            } else
                bool = false;
            return bool;
        }

        public boolean checkNeighbors(int row, int col) {
            boolean bool=true;
            int flag=0;
            if (col-1>=0)
                if(scheme.getBox(row,col-1).getAddedDice()!=null) {
                    bool = checkWest(row, col);
                    flag = 1;
                }
            if (col+1<=4 && bool)
                if(scheme.getBox(row,col+1).getAddedDice()!=null){
                    bool=checkEast(row,col);
                    flag=1;
                }
            if(row-1>=0 && bool)
                if (scheme.getBox(row-1,col).getAddedDice()!=null){
                flag=1;
                bool=checkNorth(row,col);
                }
            if (row+1<=3 && bool)
                if (scheme.getBox(row+1,col).getAddedDice()!=null){
                flag=1;
                bool=checkSouth(row,col);
                }
            if (col-1>=0 && row-1>=0 && bool)
                if (scheme.getBox(row-1,col-1).getAddedDice()!=null){
                flag=1;
                }
            if (col-1>=0 && row+1<=3 && bool)
                if (scheme.getBox(row+1,col-1).getAddedDice()!=null){
                    flag=1;
                }
            if (col+1<=4 && row-1>=0 && bool)
                if (scheme.getBox(row-1,col+1).getAddedDice()!=null){
                    flag=1;
                }
            if (col+1<=4 && row+1<=3 && bool)
                if (scheme.getBox(row+1,col+1).getAddedDice()!=null){
                    flag=1;
                }
                if(flag==0 && bool)
                    bool=false;
            return bool;
        }

        public boolean checkWest(int row, int col) {
            boolean bool = true;
                    if (scheme.getBox(row, col - 1).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                    if(scheme.getBox(row, col - 1).getAddedDice().getFace().equals(dice.getFace()))
                        bool=false;
            return bool;
        }
        public boolean checkEast(int row, int col) {
            boolean bool = true;
                    if (scheme.getBox(row, col + 1).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                    if(scheme.getBox(row, col + 1).getAddedDice().getFace().equals(dice.getFace()))
                        bool=false;
            return bool;
        }
        public boolean checkNorth(int row, int col) {
            boolean bool = true;
                    if (scheme.getBox(row-1, col ).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                    if(scheme.getBox(row-1, col ).getAddedDice().getFace().equals(dice.getFace()))
                        bool=false;
                    return bool;
        }
        public boolean checkSouth(int row, int col) {
            boolean bool = true;
                    if (scheme.getBox(row+1, col ).getAddedDice().getColour().equals(dice.getColour()))
                        bool = false;
                    if(scheme.getBox(row+1, col ).getAddedDice().getFace().equals(dice.getFace()))
                        bool=false;
            return bool;
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
    }
