package threads;

import java.lang.Thread;
import java.lang.InterruptedException;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class Box extends Thread
{
    
    private PlayPanel panel;
    private Graphics graph;
    
    //punkt początkowy Box'u
    private int x;
    private int y;
    
    //rozmiary Box'u
    private int boxWidth;
    private int boxHeight;
    
    private boolean isEmpty = true;
    private boolean userClicked = false;
    private boolean isBoxOpen = false;  //flaga otwierajaca box co 30 sek

    public Box(PlayPanel panel) {
        this.panel = panel;
        graph = panel.getGraphics();
        
        System.out.println("Panel width: " + panel.getWidth()
                           + " height: " + panel.getHeight());
        
        int width = panel.getWidth();
        int height = panel.getHeight();
        
        //wyznaczenie początku boxu w zaleznosci od wielkości panelu
        x = (int) (0.25*width);
        y = (int) (0.25*height);
        
        //wyznaczenie rozmiarów boxu w zaleznosci od wielkosci panelu
        boxWidth = (int) (0.5*width);
        boxHeight = (int) (0.5*height);
        
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
    
    public void isEmpty(boolean flag) {
        isEmpty = flag;
    }
    
    public boolean hasUserClicked() {
        return userClicked;
    }
    
    public void hasUserClicked(boolean flag) {
        userClicked = flag;
    }
    
    //metody zwracajace współrzędne ograniczające obszar boxu
    public int xMin() {
        return x;
    }
    
    public int xMax() {
        return x+boxWidth;
    }
    
    public int yMin() {
        return y;
    }
    
    public int yMax() {
        return y+boxHeight;
    }
    
    public void run() {
         //metoda watkowa...
        while(true) {
            drawBox(Color.WHITE);
            
            
            if(panel.getPlayOption() == PlayOption.E || panel.getPlayOption() == PlayOption.F) {
                try {
                    Thread.sleep(30000);
                    openBox();
                } catch(InterruptedException ex) {
                
                }
            }
        }
    }
    
    private synchronized void openBox() {
        isBoxOpen = true;
        System.out.println("Box otwart dla kulek");
        if(panel.getPlayOption() == PlayOption.F) {
            notifyAll();
        } else {
            notify();
        }

    }
    
    public synchronized void scheduleBallEntry() throws InterruptedException {
        
        if(panel.getPlayOption() == PlayOption.A) {
        
            while(!isEmpty)
                //if(!isEmpty)
                wait();
            isEmpty = false;
        
        } else if(panel.getPlayOption() == PlayOption.B) {
        
            while(!userClicked)
                wait();
            userClicked = false;
        } else if(panel.getPlayOption() == PlayOption.D) {
            if(!userClicked)
                wait();
            userClicked = false;
            
        } else if(panel.getPlayOption() == PlayOption.E) {
            while(!isBoxOpen)
                wait();
            isBoxOpen = false;
            
            //System.out.println("Box zamknięty dla kulek");
            
        } else if(panel.getPlayOption() == PlayOption.F) {
            if(!isBoxOpen)
                wait();
            isBoxOpen = false;
        }
    }
    
    public synchronized void hasBeenLeftByBall() {
        isEmpty = true;
        notify();
        drawBox(Color.WHITE);
    }
    
    public synchronized void userClickedOnBox() {
        userClicked = true;
        if(panel.getPlayOption() == PlayOption.D) {
            notifyAll();
        } else {
            notify();
        }
    }
    
    //funkcja rysuje box jako obramowaie prostokata kolorem wskazanym przez argument
    private void drawBox(Color c) {
        graph.setColor(c);
        graph.drawRect(x, y, boxWidth, boxHeight);
    }

}