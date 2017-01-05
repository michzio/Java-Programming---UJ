//package start;
import javax.swing.JApplet;
import javax.swing.JLabel; 

public class HelloWorldApplet extends JApplet {
    
   	private static final long serialVersionUID = 1L;
    
	public void init() {
		this.setContentPane(new JLabel("Hello World"));
	}
}
