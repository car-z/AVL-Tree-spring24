package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Map implemented as a Treap.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class TreapMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'rand'. ***/
  private static Random rand;
  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;

  private int size;

  /**
   * Make a TreapMap.
   */
  public TreapMap() {
    rand = new Random();
    size = 0;
    root = null;
  }

  /**
   * Make a TreapMap that uses a seed to generate random priorities for each key-value pair.
   * @param seed to generate random integers
   */
  public TreapMap(int seed) {
    rand = new Random(seed);
    size = 0;
    root = null;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    root = insert(root, k, v);
    size++;
  }

  /**
   * recursively insert given key and value into subtree rooted at given node.
   * @param n the node which subtree is rooted at
   * @param k given key to add
   * @param v given value to add
   * @return changed subtree with new node added
   */
  private TreapMap.Node<K, V> insert(TreapMap.Node<K, V> n, K k, V v) {
    //base case, leaf is reached
    if (n == null) {
      return new TreapMap.Node<>(k, v);
    }
    boolean right = false;
    if (k.compareTo(n.key) > 0) {
      //key has value greater than current subtreeRoot, recur down right side of subtree
      n.right = insert(n.right, k, v);
      //node to be added is right node
      right = true;
    } else if (k.compareTo(n.key) < 0) {
      //key has value less than current subtree, recur down left side of subtree
      n.left = insert(n.left, k, v);
    } else {
      //key has same value as current subtree, thus key already exists in map --> not allowed in map!
      throw new IllegalArgumentException("duplicate key " + k);
    }
    //execution returns to here after recursion
    //call function to "fix" subtree to maintain min-heap ordering
    n = maintainPriority(n,right);
    return n;
  }

  /**
   * Checks and corrects imbalances in the tree so that the treap follows min-heap priority ordering.
   * @param n the parent node to be checked to see if it is in the correct position
   * @param right boolean variable to indicate whether the updated node was the left or right child
   * @return changed subtree that is now in correct ordering by priority
   */
  private TreapMap.Node<K,V> maintainPriority(TreapMap.Node<K,V> n, boolean right) {
    TreapMap.Node<K,V> child;
    //if right child was the updated node
    if (right) {
      child = n.right;
      //if left child was the updated node
    } else {
      child = n.left;
    }
    //if priorities are in wrong order, perform appropriate rotation
    if (n.priority > child.priority) {
      if (right) {
        n = leftRotation(n);
      } else {
        n = rightRotation(n);
      }
    }
    return n;
  }

  /**
   * function to perform single right rotation of node n.
   * @param n node to be rotated
   * @return reference to node after rotation
   */
  private TreapMap.Node<K,V> rightRotation(TreapMap.Node<K,V> n) {
    TreapMap.Node<K,V> child = n.left;
    n.left = child.right;
    child.right = n;
    n = child;
    return n;
  }

  /**
   * function to perform single left rotation of node n.
   * @param n node to be rotated
   * @return reference to node after rotation
   */
  private TreapMap.Node<K,V> leftRotation(TreapMap.Node<K,V> n) {
    TreapMap.Node<K,V> child = n.right;
    n.right = child.left;
    child.left = n;
    n = child;
    return n;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    //find node to be removed, if it exists
    Node<K,V> target = findForSure(k);
    size--;
    V value = target.value;
    //change priority of target node to be maximum value (so it sinks to bottom of min heap)
    target.priority = Integer.MAX_VALUE;
    root = findRemove(root,target);
    return value;
  }

  /**
   * private helper function to recursively travel down treap to find node that needs to be removed.
   * @param subtreeRoot root of subtree to be searched
   * @param target the node to be removed
   *                Pre-condition: target is in this treap
   * @return subtree root after removal of node
   */
  private Node<K,V> findRemove(Node<K,V> subtreeRoot, Node<K,V> target) {
    //find position of node to be removed (target) through recursion
    if (subtreeRoot.key.compareTo(target.key) > 0) {
      subtreeRoot.left = findRemove(subtreeRoot.left, target);
    } else if (subtreeRoot.key.compareTo(target.key) < 0) {
      subtreeRoot.right = findRemove(subtreeRoot.right, target);
    } else {
      //node is found, call function to actually perform the removal
      return sinkRemove(subtreeRoot);
    }
    return subtreeRoot;
  }

  /**
   * private helper function to recursively sink the node to be removed to bottom of tree to be removed.
   * @param toRemove the node that is to be removed
   * @return reference to subtree after removal of node
   */
  private Node<K,V> sinkRemove(Node<K,V> toRemove) {
    //base case, node to be removed is leaf of tree
    if (toRemove.left == null && toRemove.right == null) {
      return null;
    }
    //if the right child has the minimum priority between the children of the node to be removed
    if (minIsRightChild(toRemove)) {
      toRemove = leftRotation(toRemove);
      //follow node to be removed down tree
      toRemove.left = sinkRemove(toRemove.left);
    } else {
      //if left child has minimum priority between children of toRemove
      toRemove = rightRotation(toRemove);
      //follow toRemove down tree
      toRemove.right = sinkRemove(toRemove.right);
    }
    return toRemove;
  }

  /**
   * private helper function to check if right child node has minimum priority of the children nodes of a given node.
   * @param n node to check the priorities of its children
   * @return true if the right node has the minimum priority of the children nodes.
   */
  private boolean minIsRightChild(Node<K,V> n) {
    if (n.left == null) {
      return true;
    } else if (n.right == null) {
      return false;
    } else {
      return n.left.priority >= n.right.priority;
    }
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    TreapMap.Node<K, V> n = findForSure(k);
    n.value = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    TreapMap.Node<K, V> n = findForSure(k);
    return n.value;
  }

  @Override
  public boolean has(K k) {
    if (root == null) {
      return false;
    }
    return find(k) != null;
  }

  /**
   * private helper function to check recursively if an element with key k exists.
   * @param k key to search for
   * @return reference to element if it exists, null if it does not exist
   */
  private TreapMap.Node<K,V> find(K k) {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    TreapMap.Node<K, V> n = root;
    while (n != null) {
      if (k.compareTo(n.key) < 0) {
        n = n.left;
      } else if (k.compareTo(n.key) > 0) {
        n = n.right;
      } else {
        return n;
      }
    }
    return null;
  }

  /**
   * function to find reference to element with key k. If this element does not exist, an error is thrown.
   * @param k key to search for
   * @return the reference to the element with key k
   */
  private TreapMap.Node<K, V> findForSure(K k) {
    TreapMap.Node<K, V> n = find(k);
    if (n == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    return n;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    return new TreapMapInOrderIterator();
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }


  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   * Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers. Since this is
   * a node class for a Treap we also include a priority field.
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int priority;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      priority = generateRandomInteger();
    }

    // Use this function to generate random values
    // to use as node priorities as you insert new
    // nodes into your TreapMap.
    private int generateRandomInteger() {
      // Note: do not change this function!
      return rand.nextInt();
    }

    @Override
    public String toString() {
      return key + ":" + value + ":" + priority;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
      return left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
      return right;
    }
  }

  /**
   * private class that declares and defines an in-order iterator for this treap.
   */
  private class TreapMapInOrderIterator implements Iterator<K> {
    private final Stack<TreapMap.Node<K, V>> stack;

    /**
     * create a new iterator.
     */
    TreapMapInOrderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    /**
     * function which pushes the nodes - in reverse order - onto the stack to be popped off in proper order.
     * @param curr the root of the subtree to be pushed onto the stack
     */
    private void pushLeft(TreapMap.Node<K, V> curr) {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public K next() {
      TreapMap.Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }
}
