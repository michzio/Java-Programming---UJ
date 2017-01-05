package fifo;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Iterator;
import java.io.*;
import java.util.Random;

public class CircularPanel {
    
    //in order to test serialization
    private static ByteArrayOutputStream bout;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException {
        //creating circural buffer with capacity of 12 elements
       BlockingQueue circularBuffer = new Circular(12);
        
        new Thread(new Putter(circularBuffer)).start();
        new Thread(new Getter(circularBuffer)).start();
       
        
        Thread.sleep(5000);
        
        System.out.println("AFTER Putter/Getter");
        System.out.println(circularBuffer.toString());
        
        circularBuffer.put("a");
        circularBuffer.put("b");
        circularBuffer.put("c");
        
        for(Object s : circularBuffer)
            System.out.println(s);
        System.out.println("AFTER PUTTING a,b,c ON MAIN THREAD");
        System.out.println(circularBuffer.toString());
        
        //testing serialisation
        bout = new ByteArrayOutputStream();
        
        //stream saving Circular object into ByteArrayOutputStream
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject((Circular) circularBuffer);
        out.flush();
        
        //stream reading Circular object from ByteArrayInputStream
        ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bout.toByteArray()));
        
        BlockingQueue circularBuffer2 =
        (Circular) in.readObject();
        
        System.out.println("DESERIALIZED CIRCULAR BUFFER");
        System.out.println(circularBuffer2.toString());
        
        
        Iterator itr = circularBuffer.iterator();
        while(itr.hasNext()) {
            itr.next();
            itr.remove();
        }
         System.out.println("AFTER ITR.REMOVE()");
        System.out.println(circularBuffer.toString());
    }
    
    //thread saving data to Circular Buffer
    static class Putter extends Thread
    {
        private BlockingQueue circularBuffer;
        
        
        public Putter(BlockingQueue circularBuffer)
        {
            this.circularBuffer = circularBuffer;
        }
        
        public void run() {
             Random rand = new Random(2000);
            try {
                for(int i =0; i<100; i++) {
                    circularBuffer.put(i);
                    System.out.println("Dodane w Putter: " + i);
                    Thread.sleep((rand.nextInt(10)+1)*300);
                
                    
                }
        
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    //thread reading data from Circural Buffer
    static class Getter extends Thread
    {
        private BlockingQueue circularBuffer;
        
        public Getter(BlockingQueue circularBuffer)
        {
            this.circularBuffer = circularBuffer;
        }
        
        public void run() {
            try {
                 Random rand = new Random(1000);
                for(int i =0; i<100; i++) {
                    System.out.println("Odczytano w Getter: " + circularBuffer.take());
                    Thread.sleep((rand.nextInt(10)+1)*300);
                    
                    
                }
    
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}