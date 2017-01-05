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

public class ClientMessageFrame extends JFrame implements ActionListener
{
    //lista wiadomosci
    private JList msgList;
    //pole tekstowe do wprowadzania wiadomosci
    private JTextField textField;
    //obiekt Client przekazany do konstruktora
    private Client client;
    private int recieverId;
    private DefaultListModel listModel;
    
    public ClientMessageFrame(Client client, int recieverId)
    {
        super();
        
        this.client = client;
        this.recieverId = recieverId;
        
        createView();
    }
    
    private void createView()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                client.closeFrameFor(recieverId);
            }
        } );
        setTitle("Rozmowa z uzytkownikiem: " + recieverId);
        setSize(500,500);
        setLocationRelativeTo(null);
        //ustawienie GridBagLayout jako menadżera layoutu
        setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
       
        //tworzymy domyślny listModel umożliwia późniejsze dodawanie elementów
        //do listy to jest tresci wiadomosci
        listModel = new DefaultListModel();
        msgList = new JList(listModel);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        getContentPane().add(msgList, constraints);
        
        textField = new JTextField();
        constraints.gridy = 2;
        constraints.weighty = (double)1/3;
        getContentPane().add(textField, constraints);
        
        //dodanie obsługi zdarzenia klikniecia w kontrolkę JTextField
        textField.addActionListener(this);
        
        //wyświetlenie komponentów
        pack();
        setSize(500,500);
        setVisible(true);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //obsluga zdarzenia
        System.out.println("Kilknięto ENTER w TextField.");
        String message = ((JTextField)e.getSource()).getText();
        System.out.println("Wpisano: " + message);
        ((JTextField)e.getSource()).setText("");
        
        listModel.addElement(message);
        
        Message msgObject = new Message(client.getClientId(), recieverId, message);
        try {
            client.getObjectOutputStream().writeObject(msgObject);
            System.out.println("Wysłano wiadomość do " + recieverId);
        } catch(IOException ex) {
            System.out.println("Przechwycono IOException podczas wysylania wiadomosci w ClientMessageFrame.");
        } catch(Exception ex) {}
    }
    
    public void addMessage(Message m) {
        //wypisanie wiadomosci metoda .toString()
        listModel.addElement(m);
    }

}