package org.jgrapht.intervalgraph;

import org.jgrapht.intervalgraph.interval.Interval;

import java.io.Serializable;
import java.util.*;

/**
 * Implementation of a Red-Black-Tree
 *
 * @param <K> the key
 * @param <V> the value
 *
 * @author Daniel Mock (danielmock)
 * @author Christoph Grüne (christophgruene)
 * @since Apr 18, 2018
 */
public class RedBlackTree<K extends Comparable<K>, V> implements BinarySearchTree<K, V>, Serializable {

    private static final long serialVersionUID = 1199228564356373435L;

    protected Node<K, V> root;

    public Node<K, V> getRoot() {
        return root;
    }

    /**
     * Returns the value associated with the given key
     *
     * @param key the key
     * @return the value associated with the given key. If the key is not in the tree, null is returned.
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }

        return searchNode(key).getVal();
    }

    private boolean isRed(Node<K, V> node){
        if (node == null) {
            return false;
        }
        return node.isRed();
    }

    /**
     * Returns whether a key is contained in the tree
     *
     * @param key the key
     * @return true if tree contains key, false otherwise
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }

        return searchNode(key) != null;
    }

    /**
     * Insertes the given (key, value) pair into the tree. If the tree contains already a symbol with the given key
     * it overwrites the old value with the new.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public void insert(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }

        root = insert(root, key, val);
        root.setBlack();
    }

    protected Node<K, V> insert(Node<K, V> current, K key, V val) {
        if (current == null){
            return new Node<>(key, val, true, 1);
        }

        if (key.equals(current.getKey())) {
            current.setVal(val);
        } else if (key.compareTo(current.getKey()) < 0) {
            current.setLeftChild(insert(current.getLeftChild(), key, val));
        } else {
            current.setRightChild(insert(current.getRightChild(), key, val));
        }

        // Fixup

        if (isRed(current.getRightChild()) && !isRed(current.getLeftChild())) {
            current = rotateLeft(current);
        }
        if (isRed(current.getLeftChild()) && isRed(current.getLeftChild().getLeftChild())) {
            current = rotateRight(current);
        }
        if (isRed(current.getLeftChild()) && isRed(current.getRightChild())) {
            changeColor(current);
        }
        current.setSize(size(current.getLeftChild()) + size(current.getRightChild()) + 1);

        return current;
    }

    /**
     * Removes the specified key and its associated value from this tree
     *
     * @param key the key
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        if (!contains(key)) {
            return;
        }

        if (!isRed(root.getLeftChild()) && !isRed(root.getRightChild())) {
            root.setRed();
        }

        root = delete(root, key);
        if (!isEmpty()) {
            root.setBlack();
        }
    }

    private boolean isEmpty() {
        return root == null;
    }

    protected Node<K, V> delete(Node<K, V> current, K key) {
            if (key.compareTo(current.getKey()) < 0) {
                if (!isRed(current.getLeftChild()) && !isRed(current.getLeftChild().getLeftChild())) {
                    current = moveRedLeft(current);
                }
                current.setLeftChild(delete(current.getLeftChild(), key));
            } else {
                if (isRed(current.getLeftChild())) {
                    current = rotateRight(current);
                }
                if (key.compareTo(current.getKey()) == 0 && current.getRightChild() == null) {
                    return null;
                }
                if (!isRed(current.getRightChild()) && !isRed(current.getRightChild().getLeftChild())) {
                    current = moveRedRight(current);
                }
                if (key.compareTo(current.getKey()) == 0) {
                    Node<K, V> node = min(current.getRightChild());
                    current.setKey(node.getKey());
                    current.setVal(node.getVal());
                    current.setRightChild(deleteMin(current.getRightChild()));
                }
                else current.setRightChild(delete(current.getRightChild(), key));
            }

            return balance(current);
    }

    protected Node<K, V> balance(Node<K, V> node) {
        if (isRed(node.getRightChild())) {
            node = rotateLeft(node);
        }
        if (isRed(node.getLeftChild()) && isRed(node.getLeftChild().getLeftChild())) {
            node = rotateRight(node);
        }
        if (isRed(node.getLeftChild()) && isRed(node.getRightChild())) {
            changeColor(node);
        }

        node.setSize(size(node.getLeftChild()) + size(node.getRightChild() ) + 1);
        return node;
    }

    /**
     * Removes the smallest key and associated value from the tree.
     *
     * @throws NoSuchElementException if the tree is empty
     */
    @Override
    public void deleteMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty tree");
        }

        if (!isRed(root.getLeftChild()) && !isRed(root.getRightChild())) {
            root.setRed();
        }

        root = deleteMin(root);
        if (!isEmpty()) {
            root.setBlack();
        }
    }

    private Node<K, V> deleteMin(Node<K, V> node) {
        if (node.getLeftChild() == null) {
            return null;
        }

        if (!isRed(node.getLeftChild()) && !isRed(node.getLeftChild().getLeftChild())) {
            root = moveRedLeft(node);
        }

        node.setLeftChild(deleteMin(node.getLeftChild()));
        return balance(node);
    }

    /**
     * Removes the largest key and associated value from the tree.
     *
     * @throws NoSuchElementException if the tree is empty
     */
    @Override
    public void deleteMax() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (!isRed(root.getRightChild()) && !isRed(root.getRightChild())) {
            root.setRed();
        }

        root = deleteMax(root);
        if (!isEmpty()) {
            root.setBlack();
        }
    }

    private Node<K, V> deleteMax(Node<K, V> node) {
        if (isRed(node.getLeftChild())) {
            node = rotateRight(node);
        }
        if (node.getRightChild() == null) {
            return null;
        }
        if (!isRed(node.getRightChild()) && !isRed(node.getRightChild().getLeftChild())) {
            node = moveRedRight(node);
        }

        node.setRightChild(deleteMax(node.getRightChild()));

        return balance(node);
    }

    /**
     * Returns the height of the BST.
     *
     * @return the height of the BST (a tree with 1 node has height 0)
     */
    @Override
    public int height() {
        return height(root);
    }

    private int height(Node<K, V> node) {
        if (node == null) {
            return -1;
        }

        return 1 + Math.max(height(node.getLeftChild()), height(node.getRightChild()));
    }

    /**
     * Returns the smallest key in the tree.
     *
     * @return the smallest key in the tree
     * @throws NoSuchElementException if the tree is empty
     */
    @Override
    public K min() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty tree");
        }

        return min(root).getKey();
    }

    private Node<K, V> min(Node<K, V> node) {
        if (node.getLeftChild() == null) {
            return node;
        }
        return max(node.getLeftChild());
    }

    /**
     * Returns the largest key in the tree.
     *
     * @return the largest key in the tree
     * @throws NoSuchElementException if the tree is empty
     */
    @Override
    public K max() {
        if (isEmpty()) {
            throw new NoSuchElementException("empty tree");
        }

        return max(root.getRightChild()).getKey();
    }

    private Node<K,V> max(Node<K, V> node) {
        if (node.getRightChild() == null) {
            return node;
        }

        return max(node.getRightChild());
    }

    /**
     * Returns the largest key in the tree less than or equal to <code>key</code>.
     *
     * @param key the key
     * @return the largest key in the tree less than or equal to <code>key</code>
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public K floor(K key) {
        return null;
    }

    /**
     * Returns the smallest key in the tree greater than or equal to <code>key</code>.
     *
     * @param key the key
     * @return the smallest key in the tree greater than or equal to <code>key</code>
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public K ceiling(K key) {
        return null;
    }

    /**
     * Return the key in the tree whose rank is <code>k</code>.
     * This is the (k+1)st smallest key in the tree.
     *
     * @param k the position
     * @return the key in the tree of rank <code>k</code>
     * @throws IllegalArgumentException if <code>k</code> not in {0, ..., n-1}
     */
    @Override
    public K select(int k) {
        return null;
    }

    /**
     * Return the number of keys in the tree strictly less than <code>key</code>.
     *
     * @param key the key
     * @return the number of keys in the tree strictly less than <code>key</code>
     * @throws IllegalArgumentException if <code>key</code> is <code>null</code>
     */
    @Override
    public int rank(K key) {
        return 0;
    }

    /**
     * Returns all keys in the symbol table as an <code>Iterable</code>.
     * To iterate over all of the keys in the symbol table named <code>st</code>,
     * use the foreach notation: <code>for (Key key : st.keys())</code>.
     *
     * @return all keys in the symbol table as an <code>Iterable</code>
     */
    @Override
    public Iterable<K> keys() {
        return null;
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an <code>Iterable</code>.
     *
     * @param min minimum endpoint
     * @param max maximum endpoint
     * @return all keys in the sybol table between <code>min</code>
     * (inclusive) and <code>max</code> (inclusive) as an <code>Iterable</code>
     * @throws IllegalArgumentException if either <code>min</code> or <code>max</code>
     *                                  is <code>null</code>
     */
    @Override
    public Iterable<K> keys(K min, K max) {
        return null;
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param min minimum endpoint
     * @param max maximum endpoint
     * @return the number of keys in the sybol table between <code>min</code>
     * (inclusive) and <code>max</code> (inclusive)
     * @throws IllegalArgumentException if either <code>min</code> or <code>max</code>
     *                                  is <code>null</code>
     */
    @Override
    public int size(K min, K max) {
        return 0;
    }

    public int size(Node<K,V> node) {
        if (node == null) {
            return 0;
        }

        return getSize(node);
    }

    /*******************************************************************************************************************
     * HELPER METHODS                                                                                                  *
     ******************************************************************************************************************/

    protected Node<K, V> rotateLeft(Node<K, V> node) {
        Node<K, V> rightChild = node.getRightChild();
        node.setRightChild(rightChild.getLeftChild());
        rightChild.setLeftChild(node);
        rightChild.setRed(isRed(rightChild.getLeftChild()));
        rightChild.getLeftChild().setRed(true);
        rightChild.setSize(getSize(node));
        node.setSize(getSize(node.getLeftChild()) + getSize(node.getRightChild()) + 1);
        return rightChild;
    }

    private int getSize(Node<K, V> node) {
        return node != null ? node.getSize() : 0;
    }

    protected Node<K, V> rotateRight(Node<K, V> node) {
        Node<K, V> leftChild = node.getLeftChild();
        node.setLeftChild(leftChild.getRightChild());
        leftChild.setRightChild(node);
        leftChild.setRed(isRed(leftChild.getRightChild()));
        leftChild.getRightChild().setRed(true);
        leftChild.setSize(getSize(node));
        node.setSize(getSize(node.getLeftChild()) + getSize(node.getRightChild()) + 1);
        return leftChild;
    }

    private void changeColor(Node<K, V> node) {
        node.setRed(!isRed(node));
        node.getRightChild().setRed(!isRed(node.getRightChild()));
        node.getLeftChild().setRed(!isRed(node.getLeftChild()));
    }

    private Node<K, V> searchNode(K key) {
        Node<K, V> current = root;
        while (current != null) {
            if (current.getKey().equals(key)) {
                return current;
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
            } else {
                current = current.getLeftChild();
            }
        }

        return null;
    }

    private Node<K, V> moveRedRight(Node<K, V> node) {
        changeColor(node);
        if (isRed(node.getLeftChild().getLeftChild())) {
            node = rotateRight(node);
            changeColor(node);
        }

        return node;
    }

    private Node<K,V> moveRedLeft(Node<K,V> node) {
        changeColor(node);
        if (isRed(node.getRightChild().getLeftChild())) {
            node.setRightChild(rotateRight(node.getRightChild()));
            node = rotateLeft(node);
            changeColor(node);
        }

        return node;
    }

    // returns the nodes inorder
    private List<Node<K, V>> inorder() {
        if (root == null) {
            return new ArrayList<>();
        }

        List<Node<K, V>> result = new ArrayList<>(getSize(root));
        inorder(root, result);
        return result;
    }

    private void inorder(Node<K, V> current, List<Node<K, V>> result) {
        if (current == null) {
            return;
        }

        inorder(current.getLeftChild(), result);
        result.add(current);
        inorder(current.getRightChild(), result);
    }

    public List<V> inorderValues(){
        List<Node<K, V>> inorder = inorder();
        List<V> result = new ArrayList<>(inorder.size());
        for (Node<K, V> node: inorder) {
            result.add(node.getVal());
        }
        return result;
    }

    private List<Node<K, V>> preorder() {
        if (root == null) {
            return new ArrayList<>();
        }

        List<Node<K, V>> result = new ArrayList<>(getSize(root));
        preorder(root, result);
        return result;
    }

    private void preorder(Node<K, V> current, List<Node<K, V>> result) {
        if (current == null) {
            return;
        }

        result.add(current);
        inorder(current.getLeftChild(), result);
        inorder(current.getRightChild(), result);
    }

    // returns the minimum node in the subtree of input node
    private Node<K, V> getMin(Node<K, V> node) {
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node;
    }
}
