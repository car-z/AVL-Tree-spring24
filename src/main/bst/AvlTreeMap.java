package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * Map implemented as an AVL Tree.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class AvlTreeMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int size;

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    root = insert(root, k, v);
    size++;
  }

  /**
   * private helper function to recursively insert given key and value into subtree rooted at given node.
   * @param n root of subtree to be searched
   * @param k key to insert
   * @param v value to insert
   * @return changed subtree with new node added
   */
  private Node<K, V> insert(Node<K, V> n, K k, V v) {
    //base case, bottom of tree reached
    if (n == null) {
      return new Node<>(k, v);
    }
    //if key is less than key of subtree root, recur down  left side of tree
    if (k.compareTo(n.key) < 0) {
      n.left = insert(n.left, k, v);
      //if key is greater than key of subtree root, recur down right side of tree
    } else if (k.compareTo(n.key) > 0) {
      n.right = insert(n.right, k, v);
    } else {
      //if key is equal to key of subtree root, throw exception as duplicate keys are not allowed in trees
      throw new IllegalArgumentException("duplicate key " + k);
    }
    //executes after recursion "unstacks" back to here
    //update the height of the subtree based on the updated heights of the nodes below it
    n.height = maxHeight(n) + 1;
    //call function to make sure that subtree is balanced
    n = maintainBalance(n);
    return n;
  }

  /**
   * function to find the maximum height of a node based on its children.
   * @param n the node to find the maximum height of
   * @return the maximum height of the node based on its children
   */
  private int maxHeight(Node<K,V> n) {
    if (n.left == null && n.right == null) {
      return 0;
    } else if (n.left == null) {
      return n.right.height;
    } else if (n.right == null) {
      return n.left.height;
    }
    return Math.max(n.left.height, n.right.height);
  }

  /**
   * function to calculate the balance factor of node n.
   * @param n the node to calculate the balance factor of
   * @return the balance factor of node n
   */
  private int calculateBalanceFactor(Node<K,V> n) {
    if (n.left == null && n.right == null) {
      return 0;
    } else if (n.left == null) {
      return -(n.right.height + 1);
    } else if (n.right == null) {
      return n.left.height + 1;
    }
    return (n.left.height + 1) - (n.right.height + 1);
  }

  /**
   * function to keep the AVL tree balanced by calculating balance factors and calling necessary structural rotations.
   * @param n node to check the balance of
   * @return reference to node n after balancing
   */
  private Node<K,V> maintainBalance(Node<K,V> n) {
    int balanceFactor = calculateBalanceFactor(n);
    int balanceFactorChild;
    //problem in right subtree
    if (balanceFactor == -2) {
      balanceFactorChild = calculateBalanceFactor(n.right);
      //problem in left child of right subtree
      if (balanceFactorChild == 1) {
        n = rightLeftRotation(n);
        //problem in right child of right subtree
      } else {
        n = leftRotation(n);
      }
      //problem in left subtree
    } else if (balanceFactor == 2) {
      balanceFactorChild = calculateBalanceFactor(n.left);
      //problem in right child of left subtree
      if (balanceFactorChild == -1) {
        n = leftRightRotation(n);
        //problem in left child of left subtree
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
  private Node<K,V> rightRotation(Node<K,V> n) {
    Node<K,V> child = n.left;
    n.left = child.right;
    child.right = n;
    n = child;
    //update heights of nodes accordingly
    if (maxHeight(n.right) == 0) {
      //if node is leaf, has height zero
      if (n.right.left == null && n.right.right == null) {
        n.right.height = 0;
        //if node has one child (child must be leaf), node has height one
      } else {
        n.right.height = 1;
      }
    } else {
      n.right.height = maxHeight(n.right);
    }
    n.height = maxHeight(n) + 1;
    return n;
  }

  /**
   * function to perform single left rotation of node n.
   * @param n node to be rotated
   * @return reference to node after rotation
   */
  private Node<K,V> leftRotation(Node<K,V> n) {
    Node<K,V> child = n.right;
    n.right = child.left;
    child.left = n;
    n = child;
    //update heights of rotated nodes accordingly
    if (maxHeight(n.left) == 0) {
      //if node is leaf, node has height zero
      if (n.left.left == null && n.left.right == null) {
        n.left.height = 0;
        //if node has one child (child must be leaf), then node has height one
      } else {
        n.left.height = 1;
      }
    } else {
      n.left.height = maxHeight(n.left);
    }
    n.height = maxHeight(n) + 1;
    return n;
  }

  /**
   * function to perform double left-right rotation on node n.
   * @param n node to be rotated
   * @return reference to node n after rotation
   */
  private Node<K,V> leftRightRotation(Node<K,V> n) {
    n.left = leftRotation(n.left);
    n = rightRotation(n);
    return n;
  }

  /**
   * function to perform double right-left rotation on node n.
   * @param n node to be rotated
   * @return reference to node n after rotation
   */
  private Node<K,V> rightLeftRotation(Node<K,V> n) {
    n.right = rightRotation(n.right);
    n = leftRotation(n);
    return n;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    Node<K, V> node = findForSure(k);
    V value = node.value;
    root = remove(root, node);
    size--;
    return value;
  }

  /**
   * private helper function to recursively find the node to be removed, and then call the remove function to remove it.
   * @param subtreeRoot the root of the subtree to search for the target node
   * @param toRemove the target node to be removed
   *                  Pre-condition: target exists in tree
   * @return the root of the subtree which was searched
   */
  private Node<K, V> remove(Node<K, V> subtreeRoot, Node<K, V> toRemove) {
    //if target has been found, call function to do the actual removing
    if (subtreeRoot.key.compareTo(toRemove.key) == 0) {
      return remove(subtreeRoot);
    } else if (subtreeRoot.key.compareTo(toRemove.key) > 0) {
      subtreeRoot.left = remove(subtreeRoot.left, toRemove);
    } else {
      subtreeRoot.right = remove(subtreeRoot.right, toRemove);
    }
    //execution begins here after recursion un-stacks back to here
    //update heights of subtree accordingly
    if (maxHeight(subtreeRoot) == 0) {
      //if subtree is a leaf, height of subtree is zero
      if (subtreeRoot.left == null && subtreeRoot.right == null) {
        subtreeRoot.height = 0;
        //if subtree has one child, height of subtree is one
      } else {
        subtreeRoot.height = 1;
      }
    } else {
      subtreeRoot.height = maxHeight(subtreeRoot) + 1;
    }
    //call function to maintain balance of subtree
    subtreeRoot = maintainBalance(subtreeRoot);
    return subtreeRoot;
  }

  /**
   * private helper function to remove the parameter node from the tree.
   * @param node which is being removed
   *             Pre-condition: this node exists
   * @return reference to subtree after node has been removed
   */
  private Node<K, V> remove(Node<K, V> node) {
    //if node to be removed has one child, simply replace it with its child
    if (node.right == null) {
      return node.left;
    } else if (node.left == null) {
      return node.right;
    }
    //find the maximum key in left subtree
    Node<K, V> toReplaceWith = max(node);
    //replace the node to be removed with the maximum key in left subtree
    node.key = toReplaceWith.key;
    node.value = toReplaceWith.value;
    //recursively iterate down to remove the maximum key in left subtree from the left subtree
    node.left = remove(node.left, toReplaceWith);
    //call function to maintain balance of subtree
    node = maintainBalance(node);
    return node;
  }

  /**
   * private helper function to find maximum key in left subtree of node.
   * @param node Node to search for maximum key in its left subtree
   * @return reference to node which holds maximum key in left subtree
   */
  private Node<K, V> max(Node<K, V> node) {
    Node<K, V> curr = node.left;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    Node<K, V> n = findForSure(k);
    n.value = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    Node<K, V> n = findForSure(k);
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
  private Node<K,V> find(K k) {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> n = root;
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
  private Node<K, V> findForSure(K k) {
    Node<K, V> n = find(k);
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
    return new AvlInOrderIterator();
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
   *
   * <p>Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers.</p>
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int height;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      height = 0;
    }

    @Override
    public String toString() {
      return key + ":" + value;
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
   * private class to declare and define in order iterator for AVL tree.
   */
  private class AvlInOrderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    /**
     * create new iterator for avl tree.
     */
    AvlInOrderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    /**
     * function to push elements on stack (in reverse order) to be popped off in order.
     * @param curr subtree to be pushed onto stack
     */
    private void pushLeft(Node<K, V> curr) {
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
      Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }
}
