package mandelbrot;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.Math;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {

	private MandelBrot mandelbrot;
	private BufferedImage image;

    //współrzędne początkowego punktu zaznaczania prostokątnego obszaru powiększenia rysunku
    //ustawiane podczas zdarzenia mousePressed i zerowane podczas zdarzenia mouseReleased
    private int startSelectionX;
    private int startSelectionY;
    //współrzędne końcowego punktu zaznaczenia prostokątnego obszaru powiększenia rysunku
    //ustawiane podczas zdarzenia przeciągania myszy mouseDragged nad ImagePanel (ciągła aktualizacja)
    //po zdarzeniu mouseReleased zerowane podobnie jak współrzędne początkowe
    private int endSelectionX;
    private int endSelectionY;
    
	
	public ImagePanel(MandelBrot m, int width, int height) {
        
        mandelbrot = m;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++) {
                image.setRGB(x, y, new Color(237.0f/255, 237.0f/255, 237.0f/255).getRGB());
            }
        }
        
        addMouseMotionListener(this);
        addMouseListener(this);
	}
    
    public void setImage(BufferedImage bi) {
        image = bi;
        repaint();
    }


	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		//BufferedImage, posX, posY,...
		g.drawImage(image, 0,0, null);
	}
    
    public void paint(Graphics g) {
        super.paint(g);
        
        int rectWidth = (endSelectionX - startSelectionX);
        int rectHeight = (endSelectionY - startSelectionY);
        if(rectWidth != 0 && rectHeight !=0) {
            g.drawRect(startSelectionX, startSelectionY, rectWidth, rectHeight);
        }
    
    }
    
    @Override
	public void mouseDragged(MouseEvent e) {
        //System.out.println("Mouse Dragged Event");
        //aktualizowanie współrzędnych końca obszaru zaznaczenia podczas przeciągania myszy
        endSelectionX = e.getX();
        endSelectionY = e.getY();
        repaint();
	}
	
    @Override
	public void mouseMoved(MouseEvent e) {
        //System.out.println("Mouse Moved Event");
        
	}
    
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked Event");
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse Entered Event");
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse Exited Event");
    }
    
    @Override
	public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed Event");
        //ustawianie współrzędnych początku obszaru zaznaczenia podczas wcisnięcia myszy
        startSelectionX = e.getX();
        startSelectionY = e.getY();
	}
    
    @Override
	public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released Event");
        mandelbrot.regionChoosen(startSelectionX, startSelectionY, endSelectionX, endSelectionY);
        //zresetowanie współrzędnych początku i końca obszaru zaznaczenia
        startSelectionX = 0;
        startSelectionY = 0;
        endSelectionX = 0;
        endSelectionY = 0;
        repaint();
	}
    
}
