package messanger;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Runnable;
import java.lang.Integer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.util.*;


public class Server implements Runnable
{
    private static final int PORT = 8875;
    private static final int MAX_CONNECTIONS = 50;
    private static final Map<Integer, String> contacts;
    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();
        aMap.put(1, "Tomek");
        aMap.put(2, "Bartek");
        aMap.put(3, "Iwona");
        aMap.put(4, "Mateusz");
        contacts = Collections.unmodifiableMap(aMap);
    }
    private static Map<Integer, Socket> sockets;
    private static Map<Integer, ObjectOutputStream> outputs;
    private Socket sock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public Server(Socket sock)
    {
        this.sock = sock;
        
        //tworzymy strumień wejściowy obiektów z gniazda klienta
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
            
            
        }  catch(IOException e)
        {
            System.out.println("Przechwycono IOException w konstruktorze Servera");
        }
        
        setupConnectionStreams();
    }
    
    private synchronized void setupConnectionStreams()
    {
        //pobranie identyfikatora int podłączonego klienta
        //i zapisanie w statycznej mapie dla tego identyfikatora
        //obiekt gniazda
        try {
            //wysyłanie klientowi listy kontaktów
            oos.writeObject(contacts);
            oos.flush();
            //odczytywanie identyfikatora podłączanego klienta
            int client_id = ois.readInt();
            sockets.put(client_id, sock);
            outputs.put(client_id, oos);

        } catch(IOException e) {
            System.out.println("Przechwycono IOException w konstruktorze Severa");
        }
    }
    
    @Override
    public void run()
    {
        
      try {
          
        //odebranie wiadomości od klienta
        //odczytyjemy wiadomość
        while(true) {
            System.out.println("Odczytywanie wiadomości na serwerze...");
            Message m = (Message)ois.readObject();
            //wysyłamy wiadomość do odbiorcy
            //gniazdo wyjściowe do drugiego klienta (odbiorcy wiadomości)
            int reciever_id = m.getRecieverId();
            /*Socket outSock = sockets.get(reciever_id);
            if(outSock == null) {
                System.out.println("null");
            }*/
            ObjectOutputStream outputToReciever = outputs.get(reciever_id);
            if(outputToReciever == null) {
                System.out.println(outputs);
                System.out.println("ObjectOutputStream to massege Receiver is null!");
            }
            outputToReciever.writeObject(m);
        }
      } catch(IOException e)
      {
          System.out.println("Przechwycono IOException w metodzi run() Servera");
      } catch(ClassNotFoundException e)
      {
          System.out.println("Przechwycono ClassNotFoundException w metodzi run() Servera");
      }
    }
    
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(PORT, MAX_CONNECTIONS);
        //utworzenie statycznego HashMap<Integer, String>
        //w k†órym będą przechowywane gniazda sieciowe z którymi
        //serwer nawiązał połączenie
        sockets = new HashMap<Integer, Socket>();
        //utworzenie statycznego HashMap<Integer, String>
        //w którym będą przechowywane obiekty ObjectOutputStream
        //odpowiadające poszczególnym klientą
        outputs = new HashMap<Integer, ObjectOutputStream>();
        while(true) {
            Socket sock = serverSocket.accept();
            if(sock != null) {
                new Thread(new Server(sock)).start();
            }
        }
    }


}