/*
Author: Jason Ash
Professor: Dr. AL-Agha
Course: CSCI 2350, Programming and Data Structures, Summer E-Learn2
Date: 2026-07-28 to 2026-07-29
File: PriorityQueue.java
Description: Uses a custom MyArrayList that extends AbstractList class and both of them implement a List interface. In turn, MyArrayList is used to create MyHeap that extends comparable and implements heap. PriorityQueue implements queue and uses MyHeap to store objects. Book is a separate internal class that implements comparable by comparing if a Book's rating is less than another Book's rating by subtracting the other Book's rating from the first Book's rating. PriorityQueue creates four book object enqueues them on the priority queue that uses a heap as its implementation, and when they are dequeued, the books are listed in ascending order by title since that is how they are stored in the heap's tree structure. The program displays the heap/priorityQueue as a string before displaying the dequeued books.

Sources: My implementation for List, AbstractList, and MyArrayList closely follow what is in our textbook since they use generics but not comparable or iterator. I also followed a suggestion in the third chapter on sorted and unsorted lists in Object-Oriented Data Structures by N. Dale, D.T. Joyce, and C. Weems on returning a copy of the object that was gotten or removed from a list to ensure information hidding and better encapsulation.
*/

/* MyHeap is mostly based on LiveExample 23.9 in our textbook.
I was having trouble with the compiler since it could not find symbol Comparable or <E>, so I asked https://deepai.org/chat/ai-code#3297b278-b6dc-4357-8193-541a907b991b "why can't the javac compiler find the symbol Comparator and <E>?" and it suggested using <E extends Comparable<E>> at the top of the class (which you have in your template for PriorityQueue, so I applogize since I should have looked their first for that clue), and using Comparable<? super E> instead of Comparable<E> in the constructors. After asking it to "Please help me rewrite the constructor with proper constraints.", it next suggested to this.c = (e1, e2) -> e1.compareTo(e2)); in the constructors instead of the commented out line that it first suggested.
*/

import java.util.Comparator;
interface List<E>
{
	// Append an element to the end of the list
	void add(E e);

	// Insert elements at a specified index
	void add(int index, E e);

	// Remove all elements from the list
	void clear();

	// Check if element exists
	boolean contains(E e);

	// Retrieve element at specified index
	E get(int index);

	// Get index of specified element
	int indexOf(E e);
	
	// Check if list is empty
	boolean isEmpty();

	// Get last occurrence of element
	int lastIndexOf(E e);

	// Remove element
	boolean remove(E e);

	// Remove element at index
	E remove(int index);

	// Replace element
	Object set(int index, E e);

	// Get number of elements
	int size();
}

abstract class AbstractList<E> implements List<E>
{
	// Stores the number of elements
	protected int size = 0;

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean remove(E e)
	{
		if (indexOf(e) >= 0)
		{
			remove(indexOf(e));
			return true;
		}
		else
		{
			return false;
		}
	}

	// Append an element to the end of the list
	public abstract void add(E e);

	// Insert elements at a specified index
	public abstract void add(int index, E e);

	// Remove all elements from the list
	public abstract void clear();

	// Check if element exists
	public abstract boolean contains(E e);

	// Retrieve element at specified index
	public abstract E get(int index);

	// Get index of specified element
	public abstract int indexOf(E e);
	
	// Get last occurrence of element
	public abstract int lastIndexOf(E e);

	// Remove element at index
	public abstract E remove(int index);

	// Replace element
	public abstract Object set(int index, E e);
}

class MyArrayList<E> extends AbstractList<E> implements List<E>
{
	public static final int INITIAL_CAPACITY = 16;

	private E[] data = (E[])(new Object[INITIAL_CAPACITY]);

	// Number of elements in the list
	// private int current = 0;

	// Create an empty list with no-arg constructor
	public MyArrayList()
	{
	}

	// Create a list from an array of objects
	public MyArrayList(E[] e)
	{
		E item;
		for (int i = 0; i < e.length; i++)
		{
			item = e[i];
			// Warning: don't use super(objects)!
			add(item);
		}
	}

	// Add a new element at the specified index
	@Override
	public void add(int index, E e)
	{
		if (index < 0 || index > size)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}

		ensureCapacity();

		// Move the elements to the right after the specified index
		for (int i = size - 1; i >= index; i--)
		{
			data[i + 1] = data[i];
		}

		// Insert new element at data[index]
		E item = e;
		data[index] = item;

		// Increase size by 1
		size++;
	}

	@Override
	public void add(E e)
	{
		ensureCapacity();
		E item = e;
		data[size] = item;
		size++;
	}

	// A helper method that creates a new larger array by doubling the current size + 1
	private void ensureCapacity()
	{
		if(size >= data.length)
		{
			E[] newData = (E[])(new Object[size * 2 + 1]);
			copy(data, newData);
			data = newData;
		}
	}

	private void copy(E[] myData, E[] resizedData)
	{	
		// Avoid ArrayIndexOutOfBounds error
		int maxIndex = 0;
		maxIndex = (myData.length <= resizedData.length) ? myData.length : resizedData.length;

		for(int i = 0; i < maxIndex; i++)
		{
			resizedData[i] = data[i]; 
		} // end for loop
	} // end copy()

	// Clear the list. Dangerous: use only if intended
	@Override
	public void clear()
	{
		data = (E[])(new Object[INITIAL_CAPACITY]);
		size = 0;
	}

	// Return true if this list contains the element
	@Override
	public boolean contains(E e)
	{
		for(int i = 0; i < size; i++)
		{
			if (e.equals(data[i]))
			{
				return true;
			}
		}
		return false;
	}

	// Return the element at the specified index
	@Override
	public E get(int index)
	{
		checkIndex(index);
		return data[index];
	}

	// Helper method that throws an IndexOutOfBoundsException if the index is out-of-bounds
	private void checkIndex(int index)
	{
		if (index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}

	// Return the index of the first matching element in this list. Return -1 if there is no match.
	@Override
	public int indexOf(E e)
	{
		for (int i = 0; i < size; i++)
		{
			if (e.equals(data[i]))
			{
				return i;
			}
		}

		return -1;
	}

	// Return the index of the last matching element in this list. Return -1 if there is no match
	@Override
	public int lastIndexOf(E e)
	{
		for(int i = size - 1; i >=0; i--)
		{
			if(e.equals(data[i]))
			{
				return i;
			}
		}

		return -1;
	}

	// Remove the element at the specified position in this list. Shift any subsequent elements to the left. Return the element that was removed from the list.
	@Override
	public E remove(int index)
	{
		checkIndex(index);
		E e = data[index];
		
		// Shift data to the left
		for(int j = index; j < size - 1; j++)
		{
			data[j] = data[j + 1];
		}

		// This element is now null
		data[size - 1] = null;

		// Decrement size
		size--;
		// current--;
		
		return e;
	}

	// Replace the element at the specified position in this list with the specified element
	@Override
	public Object set(int index, E e)
	{
		checkIndex(index);
		ensureCapacity();
		E old = data[index];
		data[index] = e;
		return old;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder("[");

		for (int i = 0; i < size; i++)
		{
			result.append(String.valueOf(data[i]));
			if (i < size - 1)
			{
				result.append(", ");
			}
		}
		return result.toString() + "]";
	}

	// Trims the capacity to current size
	public void trimToSize()
	{
		if(size != data.length)
		{
			E[] newData = (E[])(new Object[size]);
			copy(data, newData);
			data = newData;
		}
	}
}

// A Heap Interface to serve as a contract for heaps

interface Heap<E>
{
	// Add a new object into the heap
	void add(E objects);

	// Remove the root from the heap
	E remove();

	// Get the number of nodes in the tree
	int getSize();

	// Return true if the heap is empty.
	boolean isEmpty();

}

class MyHeap<E extends Comparable<E>> implements Heap<E>
{
	MyArrayList<E> list= new MyArrayList<>();
	private java.util.Comparator<? super E> c;
	
	// Create a default heap using a natural order of comparison
	public MyHeap()
	{
		// The following line is a combination of what is in the textbook and what AI first suggested before it suggested the second line used below it that actually works.
		// this.c = (e1, e2) -> ((Comparable<? super E>e1).compareTo(e2));
		this.c = (e1, e2) -> e1.compareTo(e2);
	}
	
	// Create a heap with a specified comparator
	public MyHeap (Comparator<? super E> c)
	{
		this.c = c;
	}

	// Create a heap from an array of objects
	public MyHeap (E[] objects)
	{
		this.c = (e1, e2) -> e1.compareTo(e2);
		for (int i = 0; i < objects.length; i++)
		{
			add(objects[i]);
		}
	}

	// Add a new object into the heap
	@Override
	public void add(E Object)
	{
		// Append to the heap
		list.add(Object);

		// The index of the last node in the heap
		int currentIndex = list.size() - 1;
		
		while (currentIndex > 0)
		{
			int parentIndex = (currentIndex - 1) / 2;
			
			// Swap if the current object is less than its parent.
			if (c.compare(list.get(currentIndex), list.get(parentIndex)) < 0)
			{
				E temp = (E)list.get(currentIndex);
				list.set(currentIndex, list.get(parentIndex));
				list.set(parentIndex, temp);
			}
			else
			{
				// The tree is a heap now.
				break;
			}
			currentIndex = parentIndex;
		}

	}

	// Remove the root from the heap
	@Override
	public E remove()
	{
		if (list.size() == 0)
		{
			return null;
		}

		// Store the root in the variable removed object and move the last node to the root to start rebuilding the heap
		E removedObject = (E)list.get(0);
		list.set(0, list.get(list.size() - 1));
		list.remove(list.size() - 1);

		int currentIndex = 0;

		while(currentIndex < list.size())
		{
			int leftChildIndex = (2 * currentIndex) + 1;
			int rightChildIndex = (2 * currentIndex) + 2;

			// Find the minimum between two children
			if (leftChildIndex >= list.size())
			{
				// The tree is a heap
				break;
			}
			int minIndex = leftChildIndex;
			if (rightChildIndex < list.size())
			{
				if (c.compare(list.get(minIndex), list.get(rightChildIndex)) > 0)
				{
					minIndex = rightChildIndex;
				}
			}

			// Swap if the current node is greater than the minimum
			if (c.compare(list.get(currentIndex), list.get(minIndex)) > 0)
			{
				E temp = (E)list.get(minIndex);
				list.set(minIndex, list.get(currentIndex));
				list.set(currentIndex, temp);
				currentIndex = minIndex;
			}
			else
			{
				// Thr tree is a heap
				break;
			}
		}
		return removedObject;
	}

	// Get the number of nodes in the tree
	@Override
	public int getSize()
	{
		return list.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public String toString()
	{
		return list.toString();
	}
} // end class Heap

// Note that I removed peek() from this version of the interface since it does not apply.
interface Queue<E>
{
	// Add an item to the back of the list.
	public void enqueue(E item);
	
	// Remove the first item from the list.
	public E dequeue();

	// Get the size of the queue
	public int getSize();

	// Return true if the queue is empty or false if otherwise
	public boolean isEmpty();
}

class Book implements Comparable<Book>
{
	private String title;
	private int rating;

	public Book(String title, int rating)
	{
		this.title = title;
		this.rating = rating;
	}

	@Override
	public int compareTo(Book other)
	{
		return (this.rating - other.rating);
	}

	@Override
	public String toString()
	{
		return title + " (" + rating + ")";
	}
}

public class PriorityQueue<E extends Comparable<E>> implements Queue<E>
{
   private MyHeap<E> heap = new MyHeap<>();

	@Override
    	public void enqueue(E item)
	{
		// Heap always keeps things in order
        	heap.add(item);
	}

	@Override
	public E dequeue()
	{
        	// Remove the highest priority item.
		if(heap.getSize() > 0)
		{
        		return heap.remove();
		}
		else
		{
			return null;
		}
      	}

	@Override
	public boolean isEmpty()
	{
		return heap.isEmpty();
	}


    	@Override
	public int getSize()
	{
        	return heap.getSize();
    	}

	@Override
	public String toString()
	{
		return heap.toString();
	}

	public static void main(String[] args)
	{
		PriorityQueue priorityQueue = new PriorityQueue();
		System.out.println("Is the priority queue empty to begin with? " + priorityQueue.isEmpty());
		System.out.println("The size of the priority queue to start with is " + priorityQueue.getSize());
		priorityQueue.enqueue(new Book("Lord of the Flies", 3));
		priorityQueue.enqueue(new Book("To Kill a Mockingbird", 4));
		priorityQueue.enqueue(new Book("1984", 5));
		priorityQueue.enqueue(new Book("Eye of Argon", 2));
		System.out.println("The size of the priority queue is now " + priorityQueue.getSize());
		System.out.println("The priority queue contains: " + priorityQueue.toString());

		while(!priorityQueue.isEmpty())
		{
			System.out.println("Dequeuing: " + priorityQueue.dequeue().toString());
		}
		System.out.println("Is the priority queue now empty? " + priorityQueue.isEmpty());
		System.out.println("The size of the priority queue is now " + priorityQueue.getSize());
	}
/*
Works Cited:
Dale, Nell, Joyce, Daniel T., and Weems, Chip. Object-Oriented Data Structures Using Java. Jones and Bartlett Learning, 2002. 
Liang, Y. Daniel. Introduction to Java Programming and Data Structures. 13th ed., Pearson Education Limited, 2024.
And, the https://deepai.org/chat/ai-code chat cited at the beginning of the program to help with the top of the MyHeap class and its constructors.
*/
}
