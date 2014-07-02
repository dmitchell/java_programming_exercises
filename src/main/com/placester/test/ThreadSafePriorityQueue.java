package com.placester.test;
// NOTE: we are aware that there is a PriorityQueue in
// java.util. PLease do not use this. 
// If you are doing this test at home, please do not use any containers from
// java.util in your solution, as this is a test of data
// structure knowledge, rather than a test of java library knowledge.
// If you are doing it in the office, please ask the person testing you if you are going to
// use any built in collections other than arrays.

/*
 * The task is as follows: implement this class as you see fit, and get the unit test in
 * src/test/com/placester/test/PriorityQueueTest to pass. This class
 * must allow dynamic resizing as elements are added. What the
 * strategy is to do this is entirely up to you modulo the previously
 * stated constraints.
 * 
 * Feel free to use anything from Java.util.Arrays (e.g., you don't need to implement
 * your own sort if you don't want to).
 */
public class ThreadSafePriorityQueue<X> implements SimpleQueue<Priority<X>>
{
    public ThreadSafePriorityQueue()
    {
        initialize();
    }
    
    
    public void initialize()
    {
        //TODO: put your initialization code here
    }
    
    
    @Override
    public int size()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean add(Priority<X> e)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Priority<X> poll()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Priority<X> peek()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains(Priority<X> x)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
