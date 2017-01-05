package mandelbrot;
import java.lang.*;
import java.util.regex.*;


public class Complex {

    private double real;
    private double imaginary;
    
    //constructors
    public Complex() {
        this(0.0,0.0);
    }
    
    public Complex(double real) {
        this(real, 0.0);
    }
    
    public Complex(double re, double im) {
        real = re;
        imaginary = im;
    }
    
    public Complex(Complex number) {
        this(number.Re(), number.Im());
    }
    
    public Complex(String str) throws WrongComplexNumberFormatException {
        this(Complex.valueOf(str).Re(), Complex.valueOf(str).Im());
    }
    
    //static methods
    //Zwraca sumę dwóch liczb zespolonych nie modyfikująć przekaznych liczb
    public static Complex Add(Complex num1, Complex num2) {
     
        return new Complex(num1).Add(num2);
    }
    //Zwraca różnicę dwóch liczb zespolonych
    public static Complex Sub(Complex num1, Complex num2) {
        return new Complex(num1).Sub(num2);
    }
    //Zwraca iloczyn dwóch liczb zespolonych
    public static Complex Mul(Complex num1, Complex num2) {
        return new Complex(num1).Mul(num2);
    }
    //Zwraca iloraz dwóch liczb zespolonych
    public static Complex Div(Complex num1, Complex num2) {
        return new Complex(num1).Div(num2);
    }
    //Zwraca moduł liczby zespolonej
    public static double Abs(Complex num) {
        return num.Abs();
    }
    
    //Zwraca fazę liczby zespolonej
    public static double Phase(Complex num) {
        return num.Phase();
    }
    
    //Zwraca kwadrat modułu liczby zespolonej
    public static double SqrAbs(Complex num) {
        return num.SqrAbs();
    }
    
    //Zwraca część rzeczywistą liczby zespolonej
    public static double Re(Complex num) {
        return num.Re();
    }
    
    //Zwraca część urojoną liczby zespolonej
    public static double Im(Complex num) {
        return num.Im();
    }
    
    //object methods
    //Poniższe metody modyfikują aktualny stan obiektu i zwracają wynik poprzez
    //referencję 'this'
    
    //Dodaje liczbe zespolonej do aktualnego obiektu liczby zespolonej
    public Complex Add(Complex num) {
        
        double re = Re() + num.Re();
        double im = Im() + num.Im();
        
        setRe(re);
        setIm(im); 
        
        return this;
    }
    
    //Odejmuje liczbę zespoloną od aktualnego obiektu liczby zespolonej
    public Complex Sub(Complex num) {
        double re = Re() - num.Re();
        double im = Im() - num.Im();
        
        setRe(re);
        setIm(im);
        
        return this;
    }
    
    //Mnoży aktualny liczby zespolonej przez przekazywaną liczbę zespoloną
    public Complex Mul(Complex num) {
       
    double re = Re() * num.Re() - Im() * num.Im();
    double im = Im() * num.Re() + Re() * num.Im();
       // setRe(re);
       // setRe(Re() * num.Re() - Im() * num.Im());
       //setIm(im);
        //setIm(Im() * num.Re() + Re() * num.Im());
        
        return this;
    }
    
    //Dzielenie aktualnej liczby zespolonej przez przekazywaną liczbę zepoloną
    public Complex Div(Complex num) {
        
        double denominator = num.Re()*num.Re() + num.Im()*num.Im();
        double re =  (Re() * num.Re() + Im() * num.Im())/denominator;
        double im = (Im() * num.Re() - Re() * num.Im())/denominator;
        
        setRe(re);
        setIm(im);
     
        return this;
    }
    
    public double Abs() {
        return Math.sqrt( this.SqrAbs() );  //sqrt(a^2 + b^2)
    }
    
    public double SqrAbs() {
        return Re()*Re() + Im()*Im(); //a^2 + b^2
    }
    
    public double Phase() {
        return Math.atan(Im()/Re()) % 2*Math.PI; //argument z zakresu 0 - 2TT
    }
    
    public double Re() {
        return real;
    }
    
    public double Im() {
        return imaginary;
    }
    
    //Zwraca reprezentacje liczby zespolonej jako łańcuch znaków "-1.23+4.55i"
    public String toString()  {
        
        StringBuilder builder = new StringBuilder();
        if(Re() < 0) { builder.append("-"); } else { builder.append("+"); }
        builder.append( Double.toString(Math.abs(Re())) );
        if(Im() < 0) { builder.append("-"); } else { builder.append("+"); }
        builder.append( Double.toString(Math.abs(Im())) );
        builder.append("i");
                       
        return builder.toString();
    }
    
    //Zwraca liczbę zespoloną o wartości podanej w argumencie w formacie "-1.23+4.56i"
    public static Complex valueOf(String str) throws WrongComplexNumberFormatException {

	   Complex number = new Complex(); 
            
            Pattern pattern = Pattern.compile("([+-]?\\d*\\.\\d+)([+-]?\\d*\\.\\d+)i",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(str);
            if(matcher.matches()) {
                double re, im;
                re = Double.parseDouble(matcher.group(1));
                im = Double.parseDouble(matcher.group(2));
                System.out.println("real:" + re);
                System.out.println("imaginary:" + im);
                number.setRe(re); 
                number.setIm(im); 
                
            } else {
                System.out.println("String doesn't represent complex number");
                throw new WrongComplexNumberFormatException( str );
            }

            return number; 
    }
                       
    static class WrongComplexNumberFormatException extends Exception {
        
        String format;
        
        public WrongComplexNumberFormatException(String format) {
            this.format = format;
        }
        
        public String getMessage() {
            return "Podany ciąg znaków: " + format + " nie jest poprawnym formatem liczby zespolonej.";
        }
    };
    
       //przypisuje podaną w argumencie wartość jako część rzeczywistą liczby zespolonej
        public void setRe(double re) {
            
            this.real = re;
        }
                       
        //przypisuje podaną w argumencie wartość jako część urojoną liczby zespolonej
        public void setIm(double im) {
            this.imaginary = im;
        }
                       
        //przypisuje do aktualnej liczby zespolonej liczbę przekazana w argumencie
        public void setVal(Complex num) {
            this.setRe(num.Re());
            this.setIm(num.Im());
        }
}
