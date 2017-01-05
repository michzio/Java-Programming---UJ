package start; 
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HelloWorldFrame extends JFrame {

    public HelloWorldFrame() {
        super();
        JLabel l = new JLabel(new ImageIcon("start/image.png"));
        add(l);
    }
    public static void main(String[] args) {
        HelloWorldFrame frame = new HelloWorldFrame();
        frame.setTitle("Pierwsze Okno");
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
