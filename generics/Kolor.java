package generics;

import java.lang.*;

//Poszczególny kolor reprezentowanyw modelu RGB
//2 konstruktory jeden przyjmujacy wartosci typu double z zakresu 0.0 - 1.0
//oraz drugi przyjmujacy wartosci typu int z zakresu 0 - 255
enum Kolor {
    //poszczególne przedmioty w typie wyliczeniowym
    BLACK(0,0,0),
    WHITE(255, 255, 255),
    LIGHT_GREY(235, 235, 235),
    GREY(194, 194, 194),
    DARK_GREY(149,149,149),
    RED(255,0,0),
    GREEN(0,255,0),
    BLUE(0,0,255),
    YELLOW(255,255,0),
    CYAN(0,255,255),
    MAGENTA(255,0,255);

    //stała definiujaca dopuszczalmy zakres dla składowych koloru w modelu RGB
    private static final int rgbRange = 255;

    //pola prywatne przechowujace wartosci poszczególnych składowych w modelu RGB
    //ustawiane podczas konstruowania przedmiotyu typu wyliczeniowego
    private double red;
    private double green;
    private double blue;

    //konstruktory przedmiotów - Kolor typu wyliczeniowego
    Kolor(double r, double g, double b) throws OutOfRangeException {
        if(r > 1.0 || r < 0.0) throw new OutOfRangeException("Składowa r koloru poza zakresem");
        if(g > 1.0 || g < 0.0) throw new OutOfRangeException("Składowa g koloru poza zakresem");
        if(b > 1.0 || b < 0.0) throw new OutOfRangeException("Składowa b koloru poza zakresem");
    //ustawienie pól typu wyliczeniowego
    red = r; green = g; blue = b;

    }

    Kolor(int r, int g, int b) throws OutOfRangeException {
        //wywolanie konstruktora z parametrami typu double
        //zamiana liczb int z zakresu 0-255 na typ double z zakresu 0.0-1.0
        this((double)r/rgbRange, (double)g/rgbRange, (double)b/rgbRange);
    }

    //metody wykonywane na przedmiocie
    public boolean porownaj(double r, double g, double b) {
            if(red != r) return false;
            if(green != g) return false;
            if(blue != b) return false;
            //wszystkie argumenty były dokładnie takie jak składowe aktualnego koloru w polach red, green, blue typu wyliczeniowego
            return true;
    }
    //metody zwracajace składowe koloru w modelu RGB
    public double red() {
        return red;
    }
    public double green() {
        return green;
    }
    public double blue() {
        return blue;
    }

    //metoda statyczna zwracajaca odpowiedni PRZEDMIOT (Kolor) który dokładnie
    //odpowiada składowym r,g,b
    public static Kolor nazwij(double r, double g, double b) {
            for(Kolor element : values()) {
                if(element.porownaj(r,g,b)) return element;
            }

            return null;
    }

    //wyjatek wyrzucany dla przypadku składowej koloru w modelu RGB
    //spoza dopuszczalnego zakresu 0-255 lub 0.0-1.0
    static class OutOfRangeException extends ExceptionInInitializerError {
        
        private String msg;
        
        OutOfRangeException(String msg) {
            this.msg = msg;
        }
        
        @Override
        public String getMessage() {
            return msg;
        }
    };

}

