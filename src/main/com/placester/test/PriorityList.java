package com.placester.test;

/**
 * A simple linked list to store all of the priority elements with the same priority value
 * as I don't see how to create an Array of generics w/o using Collections (I must be missing
 * something). A linked list is no problem anyway as there's no cause for random access (access
 * by index); so, it's no problem.
 *
 * @param <X>
 */
public class PriorityList<X> {
	public Priority<X> value;
	public PriorityList<X> next;

	public PriorityList(Priority<X> e) {
		this.next = null;
		this.value = e;
	}
}
