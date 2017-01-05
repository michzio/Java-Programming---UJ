
import java.lang.Enum;

enum Stan
{
    A, //private static final, nie moga to byc obiekty abstrakcyjne
    B;
    //mozna zdefiniowac metody i mozna uzywac konstruktorów

    //metody wlasne dziedziczone z java.lang.Enum
    //static values() - zablica wszystkich elementow
    //ordinal(elem);
    //toString();


}

//Stan.A.toString(); -> A
//for(Stan a : Stan.values()) //iterujemy po elementach
//switch(a) { case A: ; case B: ; default; }
//ochrona typu
//eliminuje koniecznosc stosowania przedrostkó np. w switch

enum Liczba {
    JEDEN(1), DWA(2), TRZY(3); //musi byc na poczatku

    int i; //pola w dowolnym juz miejscu

    Liczba(int i) {
        this.i= i;
    }

    int getNumber() {
        return this.i;
    }

}

//metody abstrakcyjne w typach wyliczeniowych
enum Operacja {
     DODAJ {
        int operacja(int a, int b) { return a+ b; }
        },
    POMNÓŻ {
        int operacja(int a, int b) { return a*b; }
    };

    abstract int operacja(int  a, int b);

}

int wynikDodaj = Operacja.DODAJ.operacja(1,2);
int wynikMnozenia = OPeracja.POMNÓŻ.operacja(1,2);




//TYPY GENERYCZNE - klasy i metody

// - algorytmy ktore konstruujemy sa ogólne
// - raz implementujemy liste a potem ona przechowuje obiekty dowolnego typu
// - kontrola typow juz na etapie kompilacji a nie w trakcie dzialania programu
// - brak potrzeby rzutowania - kompilator dodaje rzutowanie
// - List<E>

//ex. metoda statyczna na typach generycznych
public static <T extends Comparable<T>> T max(T a, T b) {
    if(a.compareTo(b) > 0) {
        return a;
    }

return b;

}
//extends oznacza ze T dziedzicy po klasie lub implementuje interfejsy w tym przypadku
//interfejst Comparable<T> -> metoda int compareTo(T obj);
//typ generyczny jest rozpoznawany automatycznie po argumentach
//uzycie metody
String result = max("A", "B");

java.util.*;


public class BinarySearchTree<E extends Comparable<? super E>>
                               implements Collection<E> {
    
                public BinarySearchTree(E elem) {
                                       
                }
                
                @Override
                public Iterator<E> iterator();
                                   
                //stworzyc klase BinaryIterator<E> implementujaca ten interfejs
                //next i hasNext metody
                                   
               //powinno dzialac dla elementami uporzadkowana para Pait<K,V>

}

public class Node<E implements Comparable<? super E>> {
    //mozna rozmiar przechowwyac size
    
    private E elem;
    private E leftChild; //kolejny wierzcholek po lewej stronie
    private E rightChild; //wierzcholek po prawej stronie (NULL gdy nie istnieje dziecko)
    private E parent; //wskazujemy rodzica
    
    //metody
    void add(Node<E> elem);
    E getValue();
    Node<E> leftmostElement(); //najbardziej na lewo zwraca
    //przyjmuje liste i ?
    public <T> void traverse(ArrayList<T> list); //metoda do przechodzenia
    if(left != null)  {
        left.traverse(list);
       
    }
    //zamiast dodawac mozna wypisac print
     list.add(elem);
    if(right != null) {
        right.traverse(list);
    }
    }
}

//struktura typu drzewiastego dodajemy elementy
//5 2 7 46 8 3 9 1
//elementy drzewa to obiekty klasy Node<E>
//nowy element mniejszy lub równy to dodajemy po lewej poddrzewie
//jezeli > to dodajemy w prawym poddrzewie

BinaryIterator implements Iterator<E> {
next() {
   // leftmostElement
    //czy sie da na prawo to na prawo
//potem parent
  //  potem jak sie da to na prawo
   // do rodzica az dojdziemy do niego z lewej strony nie dojdziemy to buu koniec
}
}
}