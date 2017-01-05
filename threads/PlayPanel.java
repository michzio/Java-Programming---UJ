package threads;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

enum PlayOption {
    A, B, C, D, E, F
}

public class PlayPanel extends JPanel implements MouseListener
{
    private List<Ball> balls;
    private Box box; //referencja do obiektu box'u
    private PlayOption option;
    
    public PlayPanel() {
        balls = new ArrayList<Ball>();
        option = PlayOption.A;
        
        addMouseListener(this);
        
    }
    
    public void setPlayOption(PlayOption o) {
        option = o;
        System.out.println("Wybrano opcje zabawy: " + option);
    }
    
    public PlayOption getPlayOption() {
        return option;
    }
    
    public void setBox(Box b) {
        box = b;
    }
    
    public Box getBox() {
        return box;
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
        g.fillRect(0,0, getWidth(), getHeight());
    }
    
    public void addBall(Ball b) {
        balls.add(b);
    }
    
    public boolean checkCollision(Ball b1) {
        
        double distance;
        
        for(Ball b2 : balls) {
            
            if(b1.equals(b2)) continue;
            
            distance = Math.sqrt( Math.pow((b1.centerX() - b2.centerX()), 2)
                                 + Math.pow((b1.centerY() - b2.centerY()), 2));
            if(distance <= Ball.diameter+5) {
                System.out.println("Ball Collision");
                
                if(b2.isRunning()) {
                    if(b1.getStep() == b2.getStep()) {
                        //kuli leca w ta sama strone tylko a sie zmienia
                        b1.bounceBall(1);
                        b2.bounceBall(1);
                    } else {
                        //kulki leca w przeciwna strne
                        b1.bounceBall(2);
                        b2.bounceBall(2);
                    }
                } else if(!b2.isRunning() && b1.isInsideBox()) {
                    if(b1.getStep() == b2.getStep()) {
                        //kuli leca w ta sama strone tylko a sie zmienia
                        b1.bounceBall(1);
                    } else {
                        //kulki leca w przeciwna strne
                        b1.bounceBall(2);
                    }

                } else {
                        //zatrzumujemy kulke b1 bo kulka b2 przylepiona do scianki
                    
                    if(option == PlayOption.C) {
                        
                        //pierwszy parametr oznacza ze kulka uderzyla w box
                        //drugi parametr ze kulka wyleciala z boxu
                        b1.optionCSynchronization(true, false);
                        /*
                         synchronized(box) {
                            try {
                                b1.isRunning(false);
                                if(!box.hasUserClicked())
                                    box.wait();
                                box.hasUserClicked(false);
                                while(!box.isEmpty())
                                    box.wait();
                                box.isEmpty(false);
                                
                                b1.isRunning(true);
                                System.out.println("kulka wpuszczone do boxu");
                                b1.isInsideBox(true);
                                
                                
                            } catch(InterruptedException ex) {
                                System.out.println("blokowanie kulki na scianie boxu przerwane");
                            }
                            
                        }
                         */
                    } else {
                        try {
                            b1.isRunning(false);
                            box.scheduleBallEntry();
                            b1.isRunning(true);
                            System.out.println("kulka wpuszczone do boxu");
                            b1.isInsideBox(true);
                        
                        
                        } catch(InterruptedException ex) {
                            System.out.println("blokowanie kulki na scianie boxu przerwane");
                        }
                    }
                }
                
                /* 
                 if((b1.getAngle() > 75 && b1.getAngle() < 115) ||
                 (b2.getAngle() > 75 && b2.getAngle() < 115) ||
                 b1.movingHorizontally() || b2.movingHorizontally()) {
                 b1.bounceBall(0);
                 b2.bounceBall(0);
                 } */
                
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //Wariant B - uzytkownik klika w box i wpuszamy kulke
        int x = e.getX();
        int y = e.getY();
        System.out.println("Kliknięto x:" + x + " y: " + y);
        
        for(Ball b : balls) {
            System.out.println("Kulka x: " + b.centerX() + " y: " + b.centerY());
            if(  Math.abs(x - b.centerX()) < Ball.diameter
                && Math.abs(y - b.centerY()) < Ball.diameter ) {
                
                System.out.println("***** Kulka kliknieta ******");
                
                
                if(SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
                    b.rightMouseClicked();
                    
                } else if(SwingUtilities.isLeftMouseButton(e)) {
                    b.leftMouseClicked();
                    
                }
            }
        }
        
        if( (x >= box.xMin() &&  x<= box.xMax())
           && (y >= box.yMin() && y <= box.yMax()) ) {
            System.out.println("Uzytkownik kliknal w box");
            if(option == PlayOption.B || option == PlayOption.D) {
                box.userClickedOnBox();
            } else if(option == PlayOption.C) {
                synchronized(box) {
                    box.hasUserClicked(true);
                    box.notify();
                }
            }
        }
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
}