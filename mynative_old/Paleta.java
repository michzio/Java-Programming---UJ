package mynative;

public class Paleta
{
    //?
    static {
        System.loadLibrary("Paleta");
    }
    
    public native void printText(Kolor c);
    
    public static void main(String[] args) {
        Paleta picasso = new Paleta();
        picasso.printText(Kolor.BLUE);
    }
}