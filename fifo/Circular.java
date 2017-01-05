package fifo;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.lang.reflect.Array;
import java.lang.UnsupportedOperationException;
import java.lang.IllegalStateException;
import java.lang.NullPointerException;
import java.util.NoSuchElementException;
import java.lang.InterruptedException;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.Iterator;
import java.io.*;


/**
 * because of CircularE> implements BlocingQueue<E> it
 * also implements Collection<E>, Iterable<E>, Queue<E>
 * BlockingQueue<E> - A Queue that additionally supports operations 
 * that wait for the queue to become non-empty when retrieving an 
 * element, and wait for space to become available in the queue when 
 * storing an element.
 **/
public class Circular<E extends Serializable>
    implements Serializable, BlockingQueue<E>
{
    private static final int N = 7;
    //buffor nie bedzie automatycznie serializowany (użycie słówka transien)
    //zserializujemy go sami serializując jedynie aktywne obiekty
    private transient E[] buffer;
    //store number determining buffer size
    private int size;
    //store current number of elements in circular buffer
    private int count = 0;
    
    //indexes pointing to current head and tail position
    private int head = 0;
    private int tail = 0;
    
    public Circular() {
        //creating array of size 20 elements which will
        //be a circular buffer
        size = N;
        buffer = (E[]) new Serializable[size];
        
    }
    
    public Circular(int size) {
        
        this.size = size;
        buffer = (E[]) new Serializable[size];
    }
    
    /**
     * THROWS EXCEPTION
     * Insert the specified element into this queue if it is possible
     * to do so immediately wihout violating capacity restrictions,
     * returning true upon success and throwing an
     * IllegalStateException if no space is currently available
     **/
    @Override
    public boolean add(E element) throws IllegalStateException {
        if(!offer(element))
            throw new IllegalStateException("Could not add element to circular buffor");
        return true;
    }
    
    /**
     * returns true if this queue contains the specified element
     **/
    @Override
    public synchronized boolean contains(Object o)
    {
        for(int i = head, j=0; j<count; j++)
        {
            if(o.equals(buffer[i]))
                return true;
            i= ++i %size;
        }
        return false;
    }
    
    /**
     * removes all available elements from this queue and adds
     * them to the given collection
     **/
    @Override
    public int drainTo(Collection<? super E> c) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * removes at moset the given number of available elements
     * from this queue and adds them to the given collection
     **/
    @Override
    public int drainTo(Collection<? super E> c, int maxElements)
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Inserts the specified element into this queue if it is possible
     * to do so immediately without violating capacity restrictions,
     * returning true upon success and false if no space is currently
     * available.
     **/
    public synchronized boolean offer(E element) throws NullPointerException {
        //only not Null elements can be inserted to this queue
        if(element == null) throw new NullPointerException();
        //if there isn't free space to insert new element return false
        if(count == size) return false;
        //else insert element in the position indicated by tail idx
        buffer[tail] = element;
        //step forward with tail index
        //using modulo operator % if we reach circular queue size
        tail = ++tail % size;
        //increment count counter
        count++;
        //notify all methods waiting for poll()
        if(count > 0)
            notifyAll();
        return true;
    }
    
    /**
     * Inserts the specified element into this queue, waiting up 
     * to the specified wait time if necessary for space to become 
     * available.
     **/
    @Override
    public synchronized boolean offer(E element, long timeout, TimeUnit unit)
    throws NullPointerException, InterruptedException
    {
        if(element == null) throw new NullPointerException();
        
        if(remainingCapacity() == 0)
        {
            wait(timeout);
        }
        
        return offer(element);
    }
    
    @Override
    public synchronized E poll()
    {
        if(count == 0)
            return null;
        E result = buffer[head];
        buffer[head] = null;
        //step forword with head index
        head = ++head %size;
        //decreasing number of elements in circular queue
        --count;
        //notify all methods waiting for offer
        if(count < size)
            notifyAll();
        
        return result;
    }
    
    /**
     * Retrieves and removes the head of this queue, waiting up to 
     * the specified wait time if necessary for an element to 
     * become available.
     **/
    @Override
    public synchronized E poll(long timeout, TimeUnit unit) throws InterruptedException {
        if(count == 0)
            wait(timeout);
        E result = poll();
        return result;
    }

    /**
     * Inserts the specified element into this queue, 
     * waiting if necessary for space to become available.
     **/
    @Override
    public synchronized void put(E element) throws InterruptedException {
        while(remainingCapacity() == 0) {
            wait();
        }
        offer(element);
    }
    
    /**
     * Retrieves, but does not remove, the head of this queue, 
     * or returns null if this queue is empty.
     **/
    @Override
    public synchronized E peek() {
        if(count > 0) {
            //returns but not remove element
            //count doesn't change
            //head doesn't move forward
            return buffer[head];
        }
        return null;
    }
    
    /**
     *  Retrieves, but does not remove, the head of this queue.
     **/
    @Override
    public E element() {
        E result = peek();
        if( result == null) throw new IllegalStateException();
        return result;
    }

    /**
     * Removes a single instance of the specified element from this
     * queue, if it is present.
     **/
    @Override
    public synchronized boolean remove(Object o) throws NullPointerException {

        if(o == null) throw new NullPointerException();

        for(int i=head, j=0; j< count; j++)
        {
                if(o.equals(buffer[i])) {
                    if(i == head) {
                        buffer[head] = null;
                        head = ++head %size;
                    } else {
                        //przesuwamy elementy o jeden wstecz
                        //poczawszy od pozycji i + 1
                        for(int nextIdx = (i + 1)%size;
                            nextIdx != tail;
                            nextIdx = ++nextIdx %size)
                        {
                            buffer[i] = buffer[nextIdx];
                            i = nextIdx;
                        }
                        buffer[i] = null;
                        tail = i;
                    }
                    --count;
                    if(count < size) notifyAll();
                    return true;
                }
                i = ++i % size;
        }
        return false;
    }
    
    /**
     * Retrieves and removes the head of this queue.
     **/
    @Override
    public E remove() {
        E result = poll();
        if( result == null) throw new IllegalStateException();
        return result;
    }

    /**
     * Retrieves and removes the head of this queue, waiting 
     * if necessary until an element becomes available.
     **/
    public synchronized E take() throws InterruptedException {
        while(count == 0) {
            wait();
        }
        return poll();
    }

    public synchronized int remainingCapacity()
    {
        return size-count;
    }
    
    public synchronized <T> T[] toArray(T[] a)
    {
        //if passed array has size lower than needed
        if(a.length < count)
        {
            a = (T[])Array.newInstance(a.getClass().getComponentType(), count);
        }

        for(int i = head, j=0; j < count; j++) {
            a[j] = (T) buffer[i];
            i = ++i % size;
        }

        if(a.length > count)
            a[count] = null;

        return a;
    }
    
    public synchronized String toString() {
        
        String s = "";

        for(int i=0; i < buffer.length; i++) {
            s += Integer.toString(i) + " " + Integer.toString(i-head);
            if(buffer[i] == null) {
                s += " EMPTY \n";
            } else {
                s +=  " " + buffer[i].getClass().getSimpleName();
                s +=  " " + buffer[i].toString() + "\n";
            }
        }

        return s;
    }
    
    /**
     * method should be implemented
     **/
    public synchronized Iterator<E> iterator()
    {
        return new CircularIterator();
    }
    
    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isEmpty()
    {
        if(count >0) return false;
        return true;
    }
    
    @Override
    public int size()
    {
        return count;
    }

    //metody te nie stanowią części interfejsu Serializable który jest pusty
    //są one wywoływane przez ObjectOutputStream/InputStream za pomocą
    //mechanizmów refleksji (RTTI?), stąd są one prywatne podczas gdy
    //każda metoda zadeklarowane interfejsie powinna być publiczna
    //dodatkowo kiedy ObjectOutputStream/ObjectInputStream je odnajdzie
    //można używa ich zamiast domyślnego mechanizmu serializacji
    //+ możemy użyć defaultWriteObject()/defaultReadObject()
    //w celu domyślnego zapisu odczytu
    //Uwaga! transient - oznaczamy pole które ma nie być domyślnie serializowane!
    //
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        //mozna tez zapisac np
        out.writeInt(count);
        
        for(int i=head; i != tail; i = ++i %size)
        {
            //przechodzimy po wszystkich elementach bufora
            //poczynajac na head a kończąc na indeksie tail
            //bufor jest cykliczny wiec i = ++i %size zawiera
            //operacje modulo % aby dochodzac do elementu size przejsc
            //na element 0
            out.writeObject(buffer[i]);
        }
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        count = (int) in.readInt();
        buffer = (E[]) new Serializable[size];
        //i teraz wypełniamy buffor odpowiednio obiektami
        for(int i = head; i != tail; i = ++i %size)
        {
            buffer[i] = (E)in.readObject();
        }
    }
    
    private void readObjectNoData()
    {
            throw new UnsupportedOperationException();
    }
    
    
    
    //definition of private inner class CircularIterator that implements Iterator<E>
    //objects of this class are returned by iterator() method on Circular class
    private class CircularIterator implements Iterator<E> {
        
        private int nextIdx; //index of next element
        private int returnIdx; //index lastly return element
        private E nextElement; //next element
        
        private CircularIterator()
        {
            if(count > 0) {
                nextIdx = head;
                nextElement = buffer[head];
            } else {
                nextIdx = -1;
            }
            returnIdx = -1; //at the begining there isn't any return element
        }
        
        /**
         * Returns true if the iteration has more elements.
         **/
        public boolean hasNext()
        {
            return nextIdx >= 0;
        }
        /**
         * Returns true if the iteration has more elements.
         **/
        public E next()
        {
            //using synchronized block on OuterClass in order to synchronize access to elements
            synchronized(Circular.this) {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                //temporary storing nextElement
                E element = nextElement;
                returnIdx = nextIdx;
                //setting nextIdx and nextElement
                nextIdx = ++nextIdx % size;
                if(nextIdx != tail)
                {
                    nextElement = buffer[nextIdx];
                    if(nextElement == null)
                        nextIdx = -1;
                } else {
                    nextIdx = -1;
                    nextElement = null;
                }
                //returning current nextElement stored in element var
                return element;
            }
            
        }
        
        /**
         * Removes from the underlying collection the last element
         * returned by this iterator (optional operation).
         **/
        public void remove()
        {
            //synchronization on OuterClass object
            synchronized(Circular.this) {
                
                //checking returnIdx is greater or equal than 0
                if(returnIdx < 0)
                    throw new IllegalStateException();
                
                //reseting returnIdx
                int removeIdx = returnIdx;
                returnIdx = -1;
                
                //keeping indexes for further seting nextIdx
                int keepRmvIdx = removeIdx;
                int keepHeadIdx = head;
                
                //removing element from circular buffer
                if(removeIdx == head) {
                    buffer[head] = null;
                    head = ++head%size;
                } else {
                    for(int i=(removeIdx + 1)%size;
                        i != tail;
                        i = ++i %size) {
                        buffer[removeIdx] = buffer[i];
                        removeIdx = i;
                    }
                    
                    buffer[removeIdx] = null;
                    tail = removeIdx;
                }
                //decreasing number of elements and notifying about it
                --count;
                Circular.this.notifyAll();
                
                nextIdx = (keepRmvIdx == keepHeadIdx) ? head : keepRmvIdx;
                
                if(nextIdx != tail) {
                    nextElement = buffer[nextIdx];
                    if(nextElement == null)
                        nextIdx = -1;
                } else {
                    nextIdx = -1;
                    nextElement = null;
                }
            }
        }
    }
}