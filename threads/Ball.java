package threads;

import java.lang.Thread;
import java.lang.InterruptedException;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;
import java.lang.Math;


public class Ball extends Thread  {
    
    // stala okreslajaca spowolnienie ruchu kulek
    // im wieksza wartosc tym kulki wolniej sie przesuwaja
    private final int slowness = 10;
    
    //rozmiary kulki
    public static final int diameter = 50;
    
    private PlayPanel panel;
    private Graphics graph;
    //współrzędne początkowe kulki
    private int x, y;
    //określenie kierunku
    private int alfa; //kąt kierunku
    private double a; //współczynnik kierunkowy
    private int step; //krok ruchu kulki +1px lub -1px na współrzędnych x
    private boolean isVertical = false;
    
    private boolean insideBox = false; //kulka jest wewnatrz boxu
    private boolean isRunning = true; //kulka sie porusza
    
    private boolean terminate = false;
    private boolean suspended = false;
    
    public Ball(PlayPanel panel) {
        
        //dodajemy panel na którym kulka ma się rysować
        this.panel = panel;
        graph = panel.getGraphics();
        
        System.out.println("Panel width: " + panel.getWidth() + " height: " + panel.getHeight());
        int width = panel.getWidth();
        int height = panel.getHeight();
        
        //wygenerowanie poczatkowych współrzednych (x, y)
        x = randomNumber(0, width-diameter);
        y = randomNumber(0, height-diameter);
        //upewnienie sie aby poczatkowe polozenie kulki bylo poza boxem!
        Box box = panel.getBox();
        while( (x + diameter) >= box.xMin() && x <= box.xMax()) {
            x = randomNumber(0, width-diameter);
        }
        while( (y + diameter) >= box.yMin() && y <= box.yMax()) {
            y = randomNumber(0, height-diameter);
        }
        //wygenerowanie kąta
        alfa = randomNumber(0, 180);
        //uncomment to test:
        //alfa = 90;
        System.out.println("Kat: " + alfa);
        if(alfa == 90) {
            isVertical = true;
            a = 0;
        } else {
            a = Math.tan( Math.toRadians(alfa));
        }
        if(randomNumber(0, 1) == 1
           ) {
            step = 1;
        } else {
            step = -1;
        }
        
    }
    
    public int centerX() {
        return x + diameter/2;
    }
    
    public int centerY() {
        return y + diameter/2;
    }
    
    public int getStep() {
        return step;
    }
    
    public int getAngle() {
        return alfa;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void isRunning(boolean flag) {
        isRunning = flag;
    }
    
    public boolean isInsideBox() {
        return insideBox;
    }
    
    public void isInsideBox(boolean flag) {
        insideBox = true;
    }
    
    public boolean movingHorizontally() {
       if(Math.round(a) == 0)
           return true;
        return false;
    }
    
    public void bounceBall(int type) {
       
        if(type == 0) {
            step = -step; 
        } else if(type == 1) {
            a = -a;
        } else if(type == 2) {
             //a = -a;
            step = -step;
        }
        
    }

    public void run() {
        
        graph.setColor(Color.WHITE);
        graph.fillOval(x, y, 50, 50);
        
        System.out.println("Kulka jako osobny watek");
        
        int oldx, oldy;
        
        while(!terminate) {
            
            synchronized(this) {
                if(suspended) {
                    try{
                        wait();
                    } catch(InterruptedException e) { }
                }
            }
            
            try {
                int adjustedSlowness;
                if(Math.round(a) != 0) {
                    adjustedSlowness = slowness*((int)Math.round(Math.abs(a)) + 1);
                } else {
                    adjustedSlowness = slowness;
                }
                Thread.sleep(adjustedSlowness);
            } catch(InterruptedException e) {
                System.out.println("ball sleep interrupted");
            }
            oldx = x;
            oldy = y;
            
            if(isVertical) {
                y += step;
            } else {
                x += step;
                y += Math.round(a)*step;
            }
            
            checkToBounce(x,y);
            panel.checkCollision(this);
            
            //sprawdzenie czy kulka nie napotkala na scianke BOXu
            checkBallHitsBoxWall(x, y);
            
            graph.setColor(Color.BLACK);
            graph.fillOval(oldx, oldy, diameter, diameter);
            graph.setColor(Color.WHITE);
            graph.fillOval(x, y, diameter, diameter);
        
        }
    }
    
    //generuje losową liczbę całkowitoliczbową z przedziału [min, max]
    private int randomNumber(int min, int max) {
        
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
    
    //metoda sprawdzajaca czy należy odbić kulke
    private void checkToBounce(int x, int y) {
        if(isVertical) {
            if(y < 0 || y > (panel.getHeight() - diameter) ) {
                step = -step;
            }
        } else {
            
            if(x<0 || x > (panel.getWidth() -diameter)) {
                step = -step;
                a = -a;
            }
            
            if(y<0 || y > (panel.getHeight() - diameter)) {
                a = -a;
            }
        }
    }
    
    //metoda sprawdzajaca czy kulka aby nie napotkała na sciane boxu
    private void checkBallHitsBoxWall(int x, int y) {
        
       Box box = panel.getBox();
       boolean wallCollision = false;
       boolean hasLeftBox = false;
        
       /* if(step > 0 && (x + diameter) == box.xMin() &&
           ((y+diameter) >= box.yMin() && y <= box.yMax())) {
             //kulka uderzyła w lewą ściankę boxu
            wallCollision = true;
        }
        
        if(step < 0 && x == box.xMax() &&
           ((y+diameter) >= box.yMin() && y <= box.yMax())) {
            //kulka uderzyła w prawa scianke boxu
            wallCollision = true;
        }
        
        if( Math.round(a)*step > 0 && (y+diameter) >= box.yMin()  && y < (box.yMin() + 50) &&
           ((x+diameter) >=  box.xMin() && x <= box.xMax()) ) {
           //kulka uderzyła w górna scianke boxu
            wallCollision = true;
        }
        
        if( Math.round(a)*step < 0 && y <= box.yMax() && y > (box.yMax() - 50) &&
           ((x+diameter) >=  box.xMin() && x <= box.xMax()) ) {
            //kulka uderzyła w dolna scianke boxu
            wallCollision = true;
        } */
        
        
        if(((x+diameter) >=  box.xMin() && x <= box.xMax())
             && ((y+diameter) >= box.yMin() && y <= box.yMax()) ) {
            //kulka uderzyła w jedną z pionowych scian BOX'u
            if(!insideBox) {
                wallCollision = true;
            } else {
              //kulka porusza się wewnątrz boxu
            }
        } else {
            //kulka poza boxem
            if(insideBox) {
               //kulka byla w boxie i wlasnie z niego wyleciala
                hasLeftBox = true;
            } else {
                //kulka porusza sie poza boxem
            }
        }
        
        
        if(panel.getPlayOption() == PlayOption.C) {
            //przypadek C
            //z uzyciem bloku synchronized(box) {...}
            
            optionCSynchronization(wallCollision, hasLeftBox);
            
            /* KOD PRZENIESIONY DO METODY optionCSynchronization
             synchronized(box) {
                
             if(wallCollision) {

                try {
                    isRunning = false;
                    if(!box.hasUserClicked())
                        box.wait();
                    box.hasUserClicked(false);
                    while(!box.isEmpty())
                        box.wait();
                    box.isEmpty(false);
                    
                    isRunning = true;
                    System.out.println("kulka wpuszczone do boxu");
                    insideBox = true;
                    
                    
                } catch(InterruptedException ex) {
                    System.out.println("blokowanie kulki na scianie boxu przerwane");
                }
             }
                
              if(hasLeftBox) {
                  insideBox = false;
                  box.isEmpty(true);
                  box.notify();
              }
                
            } */
           
            
        } else {
        
            if(wallCollision) {
            
                try {
                    isRunning = false;
                    box.scheduleBallEntry();
                    isRunning = true;
                    System.out.println("kulka wpuszczone do boxu");
                    insideBox = true;
                
                } catch(InterruptedException ex) {
                    System.out.println("blokowanie kulki na scianie boxu przerwane");
                }
            }
        
            if(hasLeftBox) {
                insideBox = false;
                box.hasBeenLeftByBall();
            }
        }
        
        
    }
    
    public void optionCSynchronization(boolean wallCollision, boolean hasLeftBox) {
        //przypadek C
        //z uzyciem bloku synchronized(box) {...}
        Box box = panel.getBox();
        
        synchronized(box) {
            
            if(wallCollision) {
                
                try {
                    isRunning = false;
                    if(!box.hasUserClicked())
                        box.wait();
                    box.hasUserClicked(false);
                    while(!box.isEmpty())
                        box.wait();
                    box.isEmpty(false);
                    
                    isRunning = true;
                    System.out.println("kulka wpuszczone do boxu");
                    insideBox = true;
                    
                    
                } catch(InterruptedException ex) {
                    System.out.println("blokowanie kulki na scianie boxu przerwane");
                }
            }
            
            if(hasLeftBox) {
                insideBox = false;
                box.isEmpty(true);
                box.notify();
            }
            
        }
    }
    
    public void rightMouseClicked() {
        System.out.println("Kliknieto kulke prawym przyciskiem myszy");
        terminate = true;
     
        graph.setColor(Color.BLACK);
        graph.fillOval(x, y, diameter, diameter);
        
        
    }
    
    public boolean isSuspended() {
        return suspended;
    }
    
    public void leftMouseClicked() {
        System.out.println("Kliknieto kulke lewym przyciksiem myszy");
        if(suspended) {
            synchronized(this) {
                suspended = false;
                notify();
            }
        } else {
            suspended = true;
        }
    }
    
}