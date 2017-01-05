package messanger;
import java.io.Serializable;


public class Message implements Serializable
{
    static int ID = 1;
    private int id;
    private int sender_id;
    private int reciever_id;
    private transient int something; //nie jest serializowane
    private String message;
    
    public Message(int from_id, int to_id, String m)
    {
        id = ID++;
        sender_id = from_id;
        reciever_id = to_id;
        message = m;
        
    }
    
    public int getRecieverId() {
        return reciever_id;
    }
    
    public int getSenderId() {
        return sender_id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String toString() {
        return message;
    }
}

/**
 * Obiekty mozna wysyłać do strumienia -> ObjectOutputStream
 * przesyłanie obiektu przez sieć:
 * ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream()); 
 * oos.writeObject(m); 
 * Odczytywanie przesyłanego obiektu ze strumienia -> ObjectInputStream
 * ObjectInputStream ois = new ObjectInputStream(sock.getInputStream()); 
 * m = (Message) ois.readObject(); 
 **/