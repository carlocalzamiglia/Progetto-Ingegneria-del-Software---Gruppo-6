package it.polimi.ingsw.Game;

public class Box {
    private String restrictionValue;
    private Colour restrictionColour;
    private Dice addedDice;

    public Box(String restrictionValue){
        this.restrictionValue=restrictionValue;
    }
    public Box(Colour restrictionColour){
        this.restrictionColour=restrictionColour;
    }
    public Box(){
       this.restrictionValue=null;
       this.restrictionColour=null;
    }

    //-----------------------------------------------Getters and setters------------------------------------------------
    public void setRestrictionValue(String restrictionValue){
        this.restrictionValue=restrictionValue;
    }
    public void setRestrictionColour(Colour restrictionColour){
        this.restrictionColour=restrictionColour;
    }
    public String getRestrictionValue() {
        return restrictionValue;
    }
    public Colour getRestrictionColour() {
        return restrictionColour;
    }

    //-----------------------------------------------Get a dice from the scheme-----------------------------------------
    public Dice getAddedDice() {
        if(addedDice!=null)
            return addedDice;
        else
            return null;
    }

    //-----------------------------------------------Set a dice in the scheme-------------------------------------------
    public void setAddedDice(Dice addedDice) {
        this.addedDice = addedDice;
    }

    //-----------------------------------------------Print methods------------------------------------------------------
    @Override
    public String toString() {
        if (addedDice != null)
            return addedDice.toString();
        else {
            if (restrictionValue != null) {
                return "[" + restrictionValue + "]";
            } else if (restrictionColour != null) {
                String escape = restrictionColour.escape();
                return "["+escape + "x" + Colour.RESET+"]";
            } else {
                return "[ ]";
            }
        }
    }
    public void dump() {
        System.out.print(this);
    }
}
