package messanger;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.lang.Runnable;


public class Client extends JFrame implements ActionListener, ListSelectionListener, Runnable
{
    private final String host = "localhost"; //localhost
    private final int port = 8875;
    //identyfikator clienta przekazany podczas wywołania programu
    private int clientId;
    private Socket sock;
    private JList contactList;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private static Map<Integer, String> contacts;
    private Map<Integer, ClientMessageFrame> msgFrames;
    
    public Client(int clientId) throws UnknownHostException, IOException
    {
        super();
        
        this.clientId = clientId;
        msgFrames = new HashMap<Integer, ClientMessageFrame>();
        
        sock = new Socket(java.net.InetAddress.getByName(host).getHostName(), port);
        try {
          //utworzenie strumieni wejsciowych i wyjsciowych
          oos = new ObjectOutputStream(sock.getOutputStream());
          ois = new ObjectInputStream(sock.getInputStream());
          
         //odczytanie obiektu Map<Integer, String> zawierajacy liste kontaktów
            //z serwera za pomocą obiektu ObjectInputStream
            contacts = (Map<Integer, String>) ois.readObject();
            //wypisanie do strumienia (do serwera) identyfikatora klienta, który
            //aktualnie nawiązał połączenie z serwerem
            oos.writeInt(clientId);
            oos.flush();
         
        } catch(IOException e)
        {
            System.out.println("Przechwycono IOException w konstruktorze Client");
        } catch(ClassNotFoundException e)
        {
            System.out.println("Przechwycono ClassNotFoundException w konstruktorze Client");
        }
        
        createView();
    }
    
    private void createView()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Witaj uzytkowniku: " + clientId);
        setSize(300, 700);
        setLocationRelativeTo(null);
        //ustawienie GridBagLayout jako menadżera layoutu
        setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        //pobranie nazw użytkowników w postaci tablicy stringów
        System.out.println(contacts);
        String[] contactNames = (String[]) (contacts.values().toArray(new String[0]));
        //utworzenie kontrolki JList wyświetlajacej liste kontaktów do wyboru
        contactList = new JList(contactNames);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        getContentPane().add(contactList, constraints);
        
        //list.element.addActionListener(ActionListener l);
        contactList.addListSelectionListener(this);
        
        //wyswietlenie komponentów
        pack();
        setSize(300,700);
        setVisible(true);

        
    }
    
    @Override
    public void run()
    {
        System.out.println("Rozpoczęcie wątku run() w obiekcie Client o id: " + clientId);
        
        try {
            
            while(true) {
                //odbieranie wiadomości z serwera
                System.out.println("Klient " + clientId + " odbieranie wiadomości...");
                Message m = (Message) ois.readObject();
                System.out.println("Odebrano od " + m.getSenderId() + ": " + m);
                if(msgFrames.containsKey(m.getSenderId())) {
                    System.out.println("Okienko rozmowy z tą osobą jest już otwarte");
                    //pobieramy obiekt okienka i dodajemy nową wiadomość
                    //do kontrolki JList
                    ClientMessageFrame  frame = msgFrames.get(m.getSenderId());
                    frame.addMessage(m);
                } else {
                    //utworzenie nowego okienka komunikacji z osoba od której dostaliśmy wiadomość
                    ClientMessageFrame frame = new ClientMessageFrame(this, m.getSenderId());
                    //wstawienie okienka do hashmap zawierajace liste otwartych okienek
                    msgFrames.put(m.getSenderId(), frame);
                    //dodaje do JList utworzonego okienka nowa otrzymana wiadmosc
                    frame.addMessage(m);
                }
                
            }
        } catch(IOException e)
        {
            System.out.println("Przechwycono IOException w metodzi run() Clienta");
        } catch(ClassNotFoundException e)
        {
            System.out.println("Przechwycono ClassNotFoundException w metodzi run() Clienta");
        }

        
    }
    
    public void closeFrameFor(int reciever_id)
    {
        //usuwanie obiektu okna z HashMap po jego zamknieciu
        msgFrames.remove(reciever_id);
    }
    
    public int getClientId()
    {
        return clientId;
    }
    
    public static void main(String[] args) throws LackOfClientIdException, UnknownHostException, IOException
    {
        if(args.length !=1)
            throw new LackOfClientIdException("Nie podano identyfikatora klienta podczas uruchamiania programu.");
        
        new Thread(new Client(Integer.parseInt(args[0]))).start();
        
    }
    
    public Socket getSocket()
    {
        return sock;
    }
    
    public ObjectOutputStream getObjectOutputStream(){
        return oos;
    }
    
    public ObjectInputStream getObjectInputStream() {
        return ois;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
            //obsluga zdarzenia
    }
    
    @Override
    public void valueChanged(ListSelectionEvent event) {
        
        boolean adjusting = event.getValueIsAdjusting();
        System.out.println("Dostosowywanie: " + adjusting);
        if(adjusting) return;
        
        int selected = ((JList)event.getSource()).getSelectedIndex();
        System.out.println("Wybrano: " + selected);
        
        int listIdx = selected; //should be derived from Map<Integer, String>
        //integer key where corresponding value is equal to selected value
        //open new windows with receiver_id passed to constructor
        //with messages etc.
        Integer reciever_id = ((Integer[])contacts.keySet().toArray(new Integer[0]))[listIdx];
        System.out.println("Otwieranie okna rozmowy z odbiorcą: " + reciever_id);
        if(msgFrames.containsKey(reciever_id)) {
            System.out.println("Okienko rozmowy z tą osobą jest już otwarte");
        } else {
            ClientMessageFrame frame = new ClientMessageFrame(this, reciever_id);
            msgFrames.put(reciever_id, frame);
        }
        
    }
    
    static class LackOfClientIdException extends Exception
    {
        private String msg;
        
        LackOfClientIdException(String msg) {
            this.msg = msg;
        }
        
        @Override
        public String getMessage() {
            return msg;
        }
    }
    
   
}

