package it.polimi.ingsw.Project.Game;

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

    public Dice getAddedDice() {
        return addedDice;
    }

    public void setAddedDice(Dice addedDice) {
        this.addedDice = addedDice;
    }
    public void dump() {
        System.out.print(this);
    }

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

}
