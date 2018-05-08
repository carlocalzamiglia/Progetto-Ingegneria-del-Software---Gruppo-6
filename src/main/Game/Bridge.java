package Game;

public class Bridge {
    private Colour colour;
    private int serialNumber;
    private Scheme scheme;

    public Bridge (int serialNumber){
        switch (serialNumber){
            case 1:
                this.colour=Colour.ANSI_RED;
                this.serialNumber=1;
                break;
            case 2:
                this.colour=Colour.ANSI_PURPLE;
                this.serialNumber=2;
                break;
            case 3:
                this.colour=Colour.ANSI_BLUE;
                this.serialNumber=3;
                break;
            case 4:
                this.colour=Colour.ANSI_GREEN;
                this.serialNumber=4;
                break;
            default:
                break;
        }

    }

    public Colour getColour() {
        return colour;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        String s=new String();
        s=s+colour.escape()+"VETRATA"+Colour.RESET+"\n";
        if(scheme!=null)
            s=s+scheme.toString();
        return s;
    }
    public void dump(){
        System.out.println(this);
    }
}
