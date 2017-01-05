package generics;

public enum Mieszadlo {
    ADD { //mieszanie addytywne
        Kolor mieszaj(Kolor a, Kolor b) {
            double red = a.red() + b.red();
            double green = a.green() + b.green();
            double blue = a.blue() + b.blue();

            if(red > 1.0) red = 1.0;
            if(green > 1.0) green = 1.0;
            if(blue > 1.0) blue = 1.0;

            return Kolor.nazwij(red, green, blue);
        }
    },
    MUL { //mieszanie multiplikatywne
        Kolor mieszaj(Kolor a, Kolor b) {

            return Kolor.nazwij( a.red() * b.red(),
                                 a.green() * b.green(),
                                  a.blue() * b.blue() );
        }
    },
    AVER { //średnia
        Kolor mieszaj(Kolor a, Kolor b) {

            return Kolor.nazwij(  (a.red() + b.red())/2,
                                  (a.green() + b.green())/2,
                                  (a.blue() + b.blue())/2 );
        }
    };

        //abstrakcyjna metoda typu wyliczeniowego realizujaca operacje
        //mieszania ze soba dwóch klorów (przedmioty typu wyliczeniowego Kolor)
        //metoda jest zaimplementowana w kazdym z przedmiotów (reprezentujacych
        //odopowiednie operacje mieszania kolorów) biezacego typu wyliczeniowego
        abstract Kolor mieszaj(Kolor a, Kolor b);


    public static void main(String[] args) {
            System.out.println("****** PROGRAM MIESZADŁO ******");

            Kolor mixedKolor;
            //wypisanie wszystkich mozliwosci mieszania
            //tj. kazdy kolor z kazdym kolorem (z typu wyliczeniowego Kolor)
            //realizujac kazdy mozliwy przypadek mieszania z typu Mieszadlo
            for(Kolor kolor1 : Kolor.values()) {
                for(Kolor kolor2 : Kolor.values()) {

                    //mieszanie addytywne kolorow
                    mixedKolor = ADD.mieszaj(kolor1, kolor2);
                    System.out.println(kolor1 + " ADD " + kolor2 + " = " + mixedKolor);
                    //mieszanie multiplikatywne kolorow
                    mixedKolor = MUL.mieszaj(kolor1, kolor2);
                    System.out.println(kolor1 + " MUL " + kolor2 + " = " + mixedKolor);
                    //mieszanie usredniajace
                    mixedKolor = AVER.mieszaj(kolor1, kolor2);
                    System.out.println(kolor1 + " AVER " + kolor2 + " = " + mixedKolor);

                    System.out.println("");

                }
            }
    }

}