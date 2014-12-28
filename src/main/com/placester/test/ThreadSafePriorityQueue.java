package com.placester.test;
// NOTE: we are aware that there is a PriorityQueue in
// java.util. Please do not use this. 
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
/**
 * I'm assuming ordering is strictly by priority and ignores item. I looked for a formal
 * definition of the ordering and didn't find one. The implication is that n elements with
 * the same priority are ordered arbitrarily.
 * 
 * Rather than reimplement Vector which optimizes for read given knowledge of index at
 * the expense of write and lookup, I'll attempt to do this using a binary tree. I
 * believe Vector synchronizes on the whole whereas I can probably impl a
 * binary tree to lock on the subtree. The tradeoff is that lookup by index will be O(log_n)
 * rather than O(1), but lookup by key will also be O(log_n) rather than O(n). Insert
 * will be O(n log_n) rather than O(n), but I believe the O(n) doesn't take synchronized
 * wait time into account? A downside of this approach is that size takes O(n) rather than O(1).
 *
 * @param <X>
 */
public class ThreadSafePriorityQueue<X> implements SimpleQueue<Priority<X>>
{
	/*
	 * I don't see how to create an array to support generic elements
	 * without using Collections; so, it appears I need to impl my own linked list.
	 * My failure on this may be due to my Java rustiness.
	 */
	/**
	 * A list of Priority<X> with the same priority value.
	 */
	private PriorityList<X> localEles = null;
	private int localSize = 0;

	// leftChild are priority < this.priority. rightChild.priority > this.priority
	private ThreadSafePriorityQueue<X> leftChild, rightChild;
	
    public ThreadSafePriorityQueue()
    {
        initialize();
    }
    
    
    public void initialize()
    {
    	this.leftChild = null;
    	this.rightChild = null;
    }
    
    
    @Override
    public int size()
    {
    	// assuming localSize won't overflow, but could do same safe add
    	int result = this.localSize;
    	if (this.leftChild != null) {
    		result = this.addSize(result, this.leftChild.size());
    	}
    	if (this.rightChild != null) {
    		result = this.addSize(result, this.rightChild.size());
    	}
        return result;
    }

    @Override
    public boolean isEmpty()
    {
    	return (this.localEles == null && this.leftChild == null && this.rightChild == null);
    }

    @Override
    public synchronized void clear()
    {
    	this.localEles = null;
    	this.localSize = 0;
    	this.leftChild = null;
    	this.rightChild = null;
    }

    @Override
    public boolean add(Priority<X> e)
    {
    	if (this.localEles == null) {
    		return this.initLocalEle(e);
    	}
    	if (this.localEles.value.priority == e.priority) {
    		return this.insertLocalEle(e);
    	}
    	if (e.priority < this.localEles.value.priority) {
    		if (this.leftChild == null) {
    			this.leftChild = new ThreadSafePriorityQueue<X>();
    		}
    		return this.leftChild.add(e);
    	}
    	else {
    		if (this.rightChild == null) {
    			this.rightChild = new ThreadSafePriorityQueue<X>();
    		}
    		return this.rightChild.add(e);
    	}
    }
    
    @Override
	public Priority<X> poll()
	{
		if (this.leftChild != null) {
			Priority<X> result = this.leftChild.poll();
			if (this.leftChild.isEmpty()) {
				this.removeLeftChild();
			}
			return result;
		}
		else {
			return this.syncPoll();
		}
	}


	@Override
	public Priority<X> peek()
	{
		if (this.leftChild != null) {
			return this.leftChild.poll();
		}
		else if (this.localEles != null) {
			return this.localEles.value;
		}
		else {
			return null;
		}
	}


	@Override
	public boolean contains(Priority<X> x)
	{
		// Do you really want contains(null) to return true ever? I'm not coding
		// for that assuming you don't.
		if (this.localEles == null) {
			return false;
		}
		if (x.priority == this.localEles.value.priority) {
			// find in this.localEles
			for (PriorityList<X> head = this.localEles; head != null; head = head.next) {
				if (head.value == x) {
					return true;
				}
			}
			return false;
		}
		if (x.priority < this.localEles.value.priority) {
			return this.leftChild.contains(x);
		}
		return this.rightChild.contains(x);
	}


	private synchronized boolean initLocalEle(Priority<X> e) {
		this.localEles = new PriorityList<X>(e);
		this.localSize = 1;
		return true;
    }
    
    private synchronized boolean insertLocalEle(Priority<X> e) {
		PriorityList<X> newEntry = new PriorityList<X>(e);
		newEntry.next = this.localEles;
		this.localEles = newEntry;
		this.localSize++;
		return true;
    }

    /**
     * remove first ele and return it
     */
    private synchronized Priority<X> syncPoll() {
		if (this.localEles != null) {
			PriorityList<X> element = this.localEles;
			this.localEles = element.next;
			this.localSize--;
			if (this.localEles == null && this.rightChild != null) {
				// the right children are next, bring them up
		    	this.localEles = this.rightChild.localEles;
		    	this.localSize = this.rightChild.localSize;
		    	this.rightChild = null;
			}
			return element.value;
		}
		return null;
	}


    /**
     * Remove the now-empty left child (a separate method to get the sync lock)
     */
    private synchronized void removeLeftChild() {
    	this.leftChild = null;
	}

	/**
     * catch size overflow
     */
    private int addSize(int result, int subtree_size) {
		if (subtree_size > Integer.MAX_VALUE - result) {
			return Integer.MAX_VALUE;
		}
		return result + subtree_size;
    }
}
