package start;
import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.lang.*; //contains Exception classed ex. java.lang.NumberFormatException

public class Summator extends JFrame implements ActionListener
{
  private JButton button; 
  private JLabel label;
  private JPanel panel;
  private JTextField textField1; 
  private JTextField textField2; 
  private static final long serialVersionUID = 1L;
   
  private Summator() {
  	super("Summator"); 
	init();
  }

  public static void main(String[] args) { 
	 new Summator();
  } 
  
  @Override 
  public void actionPerformed(ActionEvent evt) {
      try {
          Double sumOfTextFields = Double.parseDouble(textField1.getText()) + Double.parseDouble(textField2.getText());
          label.setText(Double.toString(sumOfTextFields));
      } catch(NumberFormatException e) {
          label.setText("W polach tekstowych wpisano niepoprawne dane.");
      }
      
	
  }

  private void init() { 
	panel = new JPanel(); 
    panel.setLayout(new GridLayout(0,1));
    
    textField1 = new JTextField(20);
	textField1.setHorizontalAlignment(JTextField.RIGHT);
	panel.add(textField1);
    
    textField2 = new JTextField(20);
	textField2.setHorizontalAlignment(JTextField.RIGHT);
	panel.add(textField2);
	
    button = new JButton();
	button.setText("Dodaj liczby"); 
	button.addActionListener(this);
	panel.add(button);
    
    label = new JLabel();
	label.setHorizontalAlignment(SwingConstants.RIGHT);
	panel.add(label);
	
    getContentPane().add(panel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
	setVisible(true);
  }
}
