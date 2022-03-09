package calculator.util;

import java.util.Iterator;

public class Stack<T> implements Iterable<T> {
	private StackNode<T> firstNode;
	private StackNode<T> lastNode;

	public Stack() {
		firstNode = lastNode = null;
	}

	public T push(T item) {
		StackNode<T> newNode = new StackNode<T>(item, lastNode);

		if (isEmpty()) {
			firstNode = lastNode = new StackNode<T>(item);
		} else if (firstNode == lastNode) {
			lastNode.nextNode = newNode;
			lastNode = lastNode.nextNode;
			lastNode.prevNode = firstNode;
			firstNode.nextNode = lastNode;
		} else {
			lastNode.nextNode = newNode;
			lastNode = lastNode.nextNode;
		}

		return item;
	}

	public T pop() {
		T removedItem = null;

		if (isEmpty()) {
			return null;
		} else if (firstNode == lastNode) {
			removedItem = lastNode.item;
			firstNode = lastNode = null;
		} else {
			removedItem = lastNode.item;
			lastNode = lastNode.prevNode;
			lastNode.nextNode = null;
		}

		return removedItem;
	}

	public T peek() {
		return lastNode.item;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			StackNode<T> current = Stack.this.firstNode;
			
			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				T data = current.item;
				current = current.nextNode;
				return data;
			}
		};
	}
	
	public boolean contains(T o) {
		Iterator<T> iterator = this.iterator();
		
		while (iterator.hasNext()) {
			if (iterator.next().equals(o)) {
				return true;
			}
		}
		
		return false;
	}

	public int size() {
		if (isEmpty()) {
			return 0;
		} 
		
		StackNode<T> currNode = firstNode;
		int i = 0;
		
		while (currNode != null) {
			currNode = currNode.nextNode;
			i++;
		}
		
		return i;
	}

	public boolean isEmpty() {
		return firstNode == null;
	}
	
	public void clear() {
		firstNode = lastNode = null;
	}
	
	@Override
	public String toString() {
		StackNode<T> currNode = firstNode;
		String s = "[";
		
		while (currNode != null) {
			s += currNode.nextNode == null ? currNode.item + "]" : currNode.item + ", ";
			currNode = currNode.nextNode;
		}
		
		return isEmpty() ? "[]" : s;
	}

}

class StackNode<T> {
	T item;
	StackNode<T> prevNode;
	StackNode<T> nextNode;

	StackNode(T item) {
		this(item, null, null);
	}

	StackNode(T item, StackNode<T> prevNode) {
		this(item, prevNode, null);
	}

	StackNode(T item, StackNode<T> prevNode, StackNode<T> nextNode) {
		this.item = item;
		this.prevNode = prevNode;
		this.nextNode = nextNode;
	}
}
