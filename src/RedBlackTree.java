/*
 * Name:		John Dixon
 * Class:		CS 3345
 * Section:		004
 * Semester:	Spring 2018
 * Project:		Project 4 - an implementation of a Red-Black BST
 */

import java.util.StringJoiner;

/**
 * RedBlackTree
 * 
 * This is a generic implementation of a Red-Black Binary Search Tree.
 * Within the class, the constant RED is equivalent to false and the constant
 * BLACK is equivalent to true. Each node in the tree has a boolean data member
 * that is set to true (BLACK) or false (RED) to indicate what type of node it is.
 * This implementation provides insert, contains, and toString as public methods and
 * defines several private helper methods as well.
 * 
 * @author John Dixon
 *
 * @param <E> - the type parameter E must extend the Comparable<E> interface
 */
public class RedBlackTree<E extends Comparable<E>> {
	
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	private Node<E> root = null;
	private int numNodes = 0;
	
	/**
	 * Checks if the tree contains the given object
	 * 
	 * @param object - object that is searched for; it must extend
	 * 					the Comparable<E> interface
	 * @return true if object is found in the tree, false if the object
	 * 			is null or is not found
	 */
	public boolean contains(Comparable<E> object) {
		if (object == null)
			return false;
		else
			return contains(object, root);		
	}
	
	/**
	 * Inserts an element into the tree if the element is not a duplicate
	 * 
	 * @param element - instance of type E to be inserted
	 * @return true if the element is inserted into the tree, false if the
	 * 			element is a duplicate
	 * @throws NullPointerException if element is null
	 */
	public boolean insert(E element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("Error - Cannot insert a value of null");
		int count = numNodes;
		insert(element, root);
		return numNodes > count;
	}
	
	/**
	 * Creates a string representation of the tree. Overrides
	 * Object.toString()
	 * 
	 * @return a string listing every element in the tree. Elements in red
	 * nodes are preceded by an asterisk
	 */
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(" ");
		return toString(sj, root);
	}
	
	/**
	 * Checks if the tree contains the given object
	 * 
	 * @param object - object that is searched for; it must extend
	 * 					the Comparable<E> interface
	 * @param t - the node from which to begin the search
	 * @return true if object is found in the tree, false if object
	 * 			is null or is not found
	 */
	private boolean contains(Comparable<E> object, Node<E> t) {
		
		int compareResult;
		
		while (t != null) {
			compareResult = object.compareTo(t.element);
			
			if (compareResult < 0)
				t = t.leftChild;
			else if (compareResult > 0)
				t = t.rightChild;
			else						// if object == element
				return true;
		}
		
		return false;
	}
	
	/**
	 * Inserts an element into the tree if the element is not a duplicate
	 * 
	 * @param element - instance of type E to be inserted
	 * @param x - the node from which to begin the necessary comparisons
	 * @return the root of the resulting tree
	 */
	private Node<E> insert(E element, Node<E> x) {
		
		if (x == null) {
			root = new Node<E>(element, null);
			root.color = BLACK;
			numNodes++;
			return root;
		}
		
		Node<E> xPar = x.parent;
		int compareResult = 0;
		
		while (x != null) {
			
			xPar = x;
			compareResult = element.compareTo(x.element);
			
			if (compareResult < 0)
				x = x.leftChild;
			else if (compareResult > 0)
				x = x.rightChild;
			else						// if duplicate, no insertion
				return root;
		}
		
		x = new Node<>(element, xPar);
		
		if (compareResult < 0)
			xPar.leftChild = x;
		else
			xPar.rightChild = x;
			
		numNodes++;
		return balance(x);
	}
	
	/**
	 * Checks for adjacent red nodes and performs necessary recolorings and/or
	 * 			rotations
	 * @param x - the node from which to start checking for adjacent red nodes
	 * @return the root of the tree without adjacent red nodes
	 */
	private Node<E> balance(Node<E> x) {
		Node<E> xPar = x.parent;
		
		while (xPar != null) {
			if (xPar.color == RED) {	// if parent is red then it is not the root
				Node<E> sib = getSibling(xPar);
				if (sib != null && sib.color == RED) {
					x = recolor(xPar);
					xPar = x.parent;
				} else if (xPar == xPar.parent.leftChild) {
					if (x == xPar.leftChild)		// if x is left-left child
						rotateLeftLeft(xPar.parent);
					else							// if x is left-right child
						rotateLeftRight(xPar);
					
					return root;
				} else {
					if (x == xPar.rightChild)		// if x is right-right child
						rotateRightRight(xPar.parent);
					else							// if x is right-left child
						rotateRightLeft(xPar);
					
					return root;
				}
			} else
				return root;
		}
		
		return root;
	}
	
	/**
	 * Finds the "sibling" of the given node
	 * 
	 * @param x - cannot be null
	 * @return the sibling node of x
	 */
	private Node<E> getSibling(Node<E> x) {
		if (x.parent == null)
			return null;
		else if (x == x.parent.leftChild)
			return x.parent.rightChild;
		else
			return x.parent.leftChild;
	}
	
	/**
	 * Performs a single recoloring operation on the tree
	 * 
	 * @param x - the red parent of another red node
	 * @return the parent of x
	 */
	private Node<E> recolor(Node<E> x) {
			x.color = BLACK;
			getSibling(x).color = BLACK;
			if (x.parent != root)
				x.parent.color = RED;
			return x.parent;
	}
	
	/**
	 * Performs a single rotation to remedy a double red situation
	 * 		where both red nodes are left-children
	 * 
	 * @param k2 - the black node whose left-child is a red node with a red
	 * 			left-child
	 * @return the root of the resulting tree
	 */
	private Node<E> rotateLeftLeft(Node<E> k2) {
		Node<E> k1 = k2.leftChild;
		
		if (k2.parent != null && k2 == k2.parent.leftChild)
			k2.parent.leftChild = k1;
		else if (k2.parent != null)
			k2.parent.rightChild = k1;
		else				// if k2.parent == null, update the root
			root = k1;
		
		k1.parent = k2.parent;
		k2.leftChild = k1.rightChild;
		
		if (k2.leftChild != null)
			k2.leftChild.parent = k2;
		
		k1.rightChild = k2;
		k2.parent = k1;
		k2.color = RED;
		k1.color = BLACK;
		return k1;
	}
	
	/**
	 * Performs a single rotation to remedy a double red situation
	 * 		where both red nodes are right-children
	 * 
	 * @param k1 - the black node whose right-child is a red node with
	 * 				a red right-child
	 * @return the root of the resulting tree
	 */
	private Node<E> rotateRightRight(Node<E> k1) {
		Node<E> k2 = k1.rightChild;
		
		if (k1.parent != null && k1 == k1.parent.leftChild)
			k1.parent.leftChild = k2;
		else if (k1.parent != null)
			k1.parent.rightChild = k2;
		else				// if k1.parent == null, update the root
			root = k2;
		
		k2.parent = k1.parent;
		k1.rightChild = k2.leftChild;
		
		if (k1.rightChild != null)
			k1.rightChild.parent = k1;
		
		k2.leftChild = k1;
		k1.parent = k2;
		k1.color = RED;
		k2.color = BLACK;
		return k2;
	}
	
	/**
	 * Performs a double rotation to remedy a double red situation
	 * 		where the first red node is a left-child and the next red
	 * 		node is that node's right-child
	 * 
	 * @param k1 - the red node whose right-child is also red
	 * @return the root of the resulting tree
	 */
	private Node<E> rotateLeftRight(Node<E> k1) {
		Node<E> k2 = k1.rightChild;
		Node<E> k3 = k1.parent;
		
		if (k3.parent != null && k3 == k3.parent.leftChild)
			k3.parent.leftChild = k2;
		else if (k3.parent != null)
			k3.parent.rightChild = k2;
		else
			root = k2;
		
		k2.parent = k3.parent;
		k3.leftChild = k2.rightChild;
		
		if (k3.leftChild != null)
			k3.leftChild.parent = k3;
		
		k2.rightChild = k3;
		k3.parent = k2;
		k1.rightChild = k2.leftChild;
		
		if (k1.rightChild != null)
			k1.rightChild.parent = k1;
		
		k2.leftChild = k1;
		k1.parent = k2;
		k2.color = BLACK;
		k3.color = RED;
		return k2;
	}
	
	/**
	 * Performs a double rotation to remedy a double red situation
	 * 		where the first red node is a right-child and the next red
	 * 		node is that node's left-child
	 * 
	 * @param k3 - the red node whose left-child is also red
	 * @return the root of the resulting tree
	 */
	private Node<E> rotateRightLeft(Node<E> k3) {
		Node<E> k2 = k3.leftChild;
		Node<E> k1 = k3.parent;
		
		if (k1.parent != null && k1 == k1.parent.leftChild)
			k1.parent.leftChild = k2;
		else if (k1.parent != null)
			k1.parent.rightChild = k2;
		else
			root = k2;
		
		k2.parent = k1.parent;
		k1.rightChild = k2.leftChild;
		
		if (k1.rightChild != null)
			k1.rightChild.parent = k1;
		
		k2.leftChild = k1;
		k1.parent = k2;
		k3.leftChild = k2.rightChild;
		
		if (k3.leftChild != null)
			k3.leftChild.parent = k3;
		
		k2.rightChild = k3;
		k3.parent = k2;
		k2.color = BLACK;
		k1.color = RED;
		return k2;
	}
	
	/**
	 * Creates a string representation of the tree
	 * 
	 * @param sj - a StringJoiner object used to concatenate the string representations
	 * 			of each node in the tree
	 * @param x - the node from which to begin creating the string
	 * @return a string listing every element in the tree. Elements in red
	 * 			nodes are preceded by an asterisk
	 */
	private String toString(StringJoiner sj, Node<E> x) {
		if (x != null) {
			if (x.color == RED)
				sj.add("*" + x.toString());
			else
				sj.add(x.toString());
			
			toString(sj, x.leftChild);
			toString(sj, x.rightChild);
		}
		return sj.toString();
	}
	
	/**
	 * Node class
	 * 
	 * A private nested class defining the properties and methods of the individual nodes in the tree.
	 * 
	 * @author John Dixon
	 *
	 */
	private static class Node<E extends Comparable<E>> {
		
		E element;
		boolean color;
		Node<E> leftChild = null;
		Node<E> rightChild = null;
		Node<E> parent;
		
		/**
		 * Constructor for nodes
		 * 
		 * @param e - the element of the node
		 * @param p - the parent of the node
		 */
		public Node (E e, Node<E> p) {
			element = e;
			color = RED;
			parent = p;
		}

		/**
		 * Overrides Object.toSting() - returns a String representation
		 * 			of the element of the node
		 */
		@Override
		public String toString() {
			return element.toString();
		}
	}
}