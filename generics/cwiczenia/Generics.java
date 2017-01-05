package generics;

public class Generics {
    //ex. metoda statyczna na typach generycznych
    public static <T extends Comparable<T>> T max(T a, T b) {
        if(a.compareTo(b) > 0) {
            return a;
        }
        
        return b;
        
    }
    
    public static void main(String[] args) {
        String result = max("A", "B");
        System.out.println(result);
        
        int result2 = max(1, 5);
        System.out.println(result2);
    }

}