package mandelbrot;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;


public class MandelBrot extends JFrame implements RegionChooser, WindowListener, ActionListener {
    
    //stałe
    private static final int R = 2;
    //domyslny rozmiar obrazka
    private static final int IMG_WIDTH = 600;
    private static final int IMG_HEIGHT = 600;
    //domyslny zakres fraktala
    private static final double X_MIN = -2.0;
    private static final double X_MAX = 1.0;
    private static final double Y_MIN = -1.25;
    private static final double Y_MAX = 1.25;
	
	private ImagePanel imagePanel;
    private int width = IMG_WIDTH;
	private int height = IMG_HEIGHT;
    private final int maxIterations = 100; //max liczba iteracji przy określaniu zbieżności ciągu
    //dla określania czy dany punkt p należy do zbioru
    //Mandelbrota
    
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double scaleX = 1.0; //100%
    private double scaleY = 1.0; //100%
    
    //zakres wykresu (przedziały w których jest on rysowany)
    //domyślnie x= (-1.25, 1.25) y = (-2.0, 1.0)
    private double rangeX = X_MAX - X_MIN;
    private double rangeY = Y_MAX - Y_MIN;
    //przesuniecie punktów poczatkowy x_min, y_min
    private double x_min = X_MIN;
    private double y_min = Y_MIN;
    
    private int r = R;
    
    //komponenty
    JButton resetButton;
    JButton drawButton;
    JTextField xminField;
    JTextField yminField;
    JTextField xmaxField;
    JTextField ymaxField;
    JTextField widthField;
    JTextField heightField;
    JTextField rField;
	
	public MandelBrot() {
        
        super();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fraktal MandelBrota");
        setSize(600,700);
        setLocationRelativeTo(null);
        
        
         //ustawianie GridBagLayout jako menadżera layoutu
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
			
        imagePanel = new ImagePanel(this, width, height);
        imagePanel.setSize(500,500);
        imagePanel.setImage(mandelBrotImage());
		
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        getContentPane().add(imagePanel, constraints);
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        getContentPane().add(resetButton, constraints);
        
        drawButton = new JButton("Draw");
        drawButton.addActionListener(this);
        
        constraints.gridx = 0;
        constraints.gridy = 3;
        getContentPane().add(drawButton, constraints);
        
        JLabel xminLabel = new JLabel("x_min: ");
        constraints.gridx = 1;
        constraints.gridy = 2;
        getContentPane().add(xminLabel, constraints);
        
        xminField = new JTextField(Double.toString(X_MIN), 5);
        constraints.gridx = 2;
        constraints.gridy = 2;
        getContentPane().add(xminField, constraints);
        
        JLabel yminLabel = new JLabel("y_min: ");
        constraints.gridx = 1;
        constraints.gridy = 3;
        getContentPane().add(yminLabel, constraints);
        
        yminField = new JTextField(Double.toString(Y_MIN), 5);
        constraints.gridx = 2;
        constraints.gridy = 3;
        getContentPane().add(yminField, constraints);
        
        JLabel xmaxLabel = new JLabel("x_max: ");
        constraints.gridx = 3;
        constraints.gridy = 2;
        getContentPane().add(xmaxLabel, constraints);
        
        xmaxField = new JTextField(Double.toString(X_MAX), 5);
        constraints.gridx = 4;
        constraints.gridy = 2;
        getContentPane().add(xmaxField, constraints);
        
        JLabel ymaxLabel = new JLabel("y_max: ");
        constraints.gridx = 3;
        constraints.gridy = 3;
        getContentPane().add(ymaxLabel, constraints);
        
        ymaxField = new JTextField(Double.toString(Y_MAX), 5);
        constraints.gridx = 4;
        constraints.gridy = 3;
        getContentPane().add(ymaxField, constraints);
        
        JLabel widthLabel = new JLabel("width: ");
        constraints.gridx = 5;
        constraints.gridy = 2;
        getContentPane().add(widthLabel, constraints);
        
        widthField = new JTextField(Integer.toString(IMG_WIDTH), 5);
        constraints.gridx = 6;
        constraints.gridy = 2;
        getContentPane().add(widthField, constraints);
        
        JLabel heightLabel = new JLabel("height: ");
        constraints.gridx = 5;
        constraints.gridy = 3;
        getContentPane().add(heightLabel, constraints);
        
        heightField = new JTextField(Integer.toString(IMG_HEIGHT), 5);
        constraints.gridx = 6;
        constraints.gridy = 3;
        getContentPane().add(heightField, constraints);
        
        JLabel rLabel = new JLabel("r: ");
        constraints.gridx = 7;
        constraints.gridy = 2;
        getContentPane().add(rLabel, constraints);
        
        rField = new JTextField(Integer.toString(R), 5);
        constraints.gridx = 7;
        constraints.gridy = 3;
        getContentPane().add(rField, constraints);
        
        pack();
        setSize(600,700);
        setVisible(true);
	}

	
	
	public static void main(String[] args) { 
		
		new MandelBrot(); 
		
	}

	public void regionChoosen(int x1, int y1, int x2, int y2) {
        //wypisanie współrzędnych początku i końca obszaru zaznaczenia podczas ostatniego zaznaczania
        System.out.println("startX: " + x1 + ", startY: " + y1 + ", endX: " + x2 + ", endY: " + y2 + ".");
        //ustawienie nowego obrazka z wykresem mandelBrota
        
        offsetX += (double) x1/width * scaleX;
        offsetY += (double) y1/width * scaleX;
        scaleX *= (double) (x2-x1)/width;
        //obliczamy tez przyblizenie w kierunku pionowym, ale nie wykorzystujemy
        //poniewaz wtedy fraktal nie zachowuje proporcji
        scaleY *= (double) (y2-y1)/height;
        
        imagePanel.setImage(mandelBrotImage());
	}
    
    private BufferedImage mandelBrotImage() {
        
        BufferedImage image;
        
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int x =0; x<width; x++) {
			for( int y=0; y<height; y++) {
                
				image.setRGB(x, y, colorMandelBrotPixel(x,y));
			}
		}
        
        return image;
        
    }
    
    private int colorMandelBrotPixel(int x, int y) {
        
        //(x,y) to punkt na płaszczyźnie zespolonej dla którego
        //sprawdzamy w sposób przybliżony czy należy do zbioru Mandelbrota
        //jeżeli tak to kolorujemy pixel na czarno jeżeli nie do w zależności
        //od szybkości ucieczki określamy kolor jako odpowiednio jaśniejszy
        //lub ciemniejszy odcień
        //punkt (x, y) należy do zbioru Mandelbrota jezeli Vn |z_n| < 2
        //wzór rekurencyjny to zn+1 = zn + p gdzie p to pkt płaszczyny
        //zespolonej u nas (x,y)
        double r = 0.0, g = 0.0, b = 0.0; //domyśna wartość jak dla pkt należącego
        //do zbioru Mandelbrota
        //skalowanie współrzędnych tak by leżały w przedziale x -> (-2.5, 1)
        //y -> (-1,1)
        double re =  scaleX * rangeX * x/width  + (x_min + offsetX *3.0);
        double im =  scaleX * rangeY * y/height  + (y_min + offsetY *2.5);
        
        int escapeValue = getEscapeValueForPoint(new Complex(re, im));
        
        if(escapeValue < maxIterations) {
            r = Math.log(escapeValue)/Math.log(maxIterations);
            g = Math.log(escapeValue)/Math.log(maxIterations);
            b = 1 - Math.log(escapeValue)/Math.log(maxIterations);
        }
        
        return new Color((float)r, (float)g, (float)b).getRGB();
        
    }
    
    
    //jeżeli wartość zwracana (int) i == maxIterations tzn. że p należy do
    //zbioru Mandelbrota bo dla każdego i należąceg do (0, maxIterations)
    // |z_n| < 2 !   -> dla takich wartości i kolorujemy pixel p=(x,y) na czarno
    // jeżeli i < maxIterations tzn., że pkt p=(x,y) nie należy do zbioru
    // Mandelbrota (ciąg rozbieżny) kolorujemy wybranym kolorem o odcieniu
    // uzalżnionym od wartości i
    private int getEscapeValueForPoint(Complex p) {
        
        Complex z_n = new Complex(0,0); //liczba zespolona (0,0)
        int i;
        
        for(i=0; i<maxIterations && z_n.Abs() < r; i++) {
            z_n.Mul(z_n).Add(p); //z_n+1 = z_n^2 + p
            /* Complex z_n1 = new Complex(0,0);
             z_n1.setRe(z_n.Re()*z_n.Re() - z_n.Im()*z_n.Im() + p.Re());
             z_n1.setIm(z_n.Im()*z_n.Re() + z_n.Re()*z_n.Im() + p.Im());
             z_n = z_n1;*/
        }
        
        return i; //zwracamy wartość którą udało się osiągnąć
    }

    
    /***** WindowListener interface methods *******/
    public void windowActivated(WindowEvent e) {
        
    }
    
    public void windowClosed(WindowEvent e) {
        
    }
    
    public void windowClosing(WindowEvent e) {
        
    }
    
    public void windowDeactivated(WindowEvent e) {
    
    }
    
    public void windowDeiconified(WindowEvent e) {
        
    }
    
    public void windowIconified(WindowEvent e) {
        
    }
    
    public void windowOpened(WindowEvent e) {
        
    }
    
    /***** ActionListener interface methods ******/
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == drawButton) {
            System.out.println("Draw Button");
            
            x_min = Double.parseDouble(xminField.getText());
            y_min = Double.parseDouble(yminField.getText());
            
            rangeX = Double.parseDouble(xmaxField.getText()) - x_min;
            rangeY = Double.parseDouble(ymaxField.getText()) - y_min;
            
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
            
            r = Integer.parseInt(rField.getText()); 
            
        } else if(e.getSource() == resetButton) {
            System.out.println("Reset Button");
            x_min = X_MIN;
            xminField.setText(Double.toString(x_min));
            y_min = Y_MIN;
            yminField.setText(Double.toString(y_min));
            rangeX = X_MAX - X_MIN;
            xmaxField.setText(Double.toString(X_MAX));
            rangeY = Y_MAX - Y_MIN;
            ymaxField.setText(Double.toString(Y_MAX));
            
            width = IMG_WIDTH;
            widthField.setText(Integer.toString(width));
            height = IMG_HEIGHT;
            heightField.setText(Integer.toString(height));
            
            r = R;
            rField.setText(Integer.toString(r));
            
            offsetX = 0.0; offsetY = 0.0; scaleX = 1.0; scaleY = 1.0;
            
        }
        
         imagePanel.setImage(mandelBrotImage());
        
    }
}
