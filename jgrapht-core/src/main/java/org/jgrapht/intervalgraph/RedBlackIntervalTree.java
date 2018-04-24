package org.jgrapht.intervalgraph;

import org.jgrapht.intervalgraph.interval.Interval;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

// Essentially, this class updates the hi value after any operation on this tree.
// The hi value equals to the highest endpoint in the subtree
public class RedBlackIntervalTree<
        T extends Comparable<T>,
        I extends Interval<T>>
        extends RedBlackTree<T, I>
        implements IntervalTreeInterface<T, I> {
    @Override
    public void add(I interval) {
        insert(interval.getStart(), interval);
    }

    @Override
    public void remove(I interval) {
        delete(interval.getStart());
    }

    @Override
    public List<I> overlapsWith(I interval) {
        List<I> result = new LinkedList<>();

        overlapsWith(this.getRoot(), interval, result);

        return result;
    }

    @Override
    public List<I> overlapsWithPoint(T point) {
        List<I> result = new LinkedList<>();

        overlapsWithPoint(this.getRoot(), point, result);
        return result;
    }

    @Override
    protected Node<T, I> rotateRight(Node<T, I> node) {
        // Perform rotation as usual
        Node<T, I> result = super.rotateRight(node);

        // update hi vals
        result.setHi(node.getHi());
        updateHi(node);

        return result;
    }

    @Override
    protected Node<T, I> rotateLeft(Node<T, I> node) {
        // Perform rotation as usual
        Node<T, I> result = super.rotateLeft(node);

        // update hi vals
        result.setHi(node.getHi());
        updateHi(node);

        return result;
    }

    @Override
    protected Node<T, I> delete(Node<T, I> current, T key) {
        Node<T, I> result = super.delete(current, key);
        updateHi(result);
        return result;
    }

    @Override
    protected Node<T, I> insert(Node<T, I> current, T key, I val) {
        Node<T, I> result = super.insert(current, key, val);
        updateHi(result);
        return result;
    }

    @Override
    protected Node<T, I> balance(Node<T, I> node) {
        Node<T, I> result = super.balance(node);
        updateHi(result);
        return result;
    }

    // sets the hi attribute of the given node to the max of the subtree or itself
    private void updateHi(Node<T, I> node) {
        if (node == null) {
            return;
        }

        T result = node.getVal().getEnd();
        if (node.getRightChild() != null) {
            result = max(result, node.getRightChild().getHi());
        }

        if (node.getLeftChild() != null) {
            result = max(result, node.getLeftChild().getHi());
        }

        node.setHi(result);
    }

    // returns the max of two values
    public T max(T t1, T t2) {
        if (t1.compareTo(t2) > 0) {
            return t1;
        } else {
            return t2;
        }
    }

    private void overlapsWith(Node<T, I> node, Interval<T> interval, List<I> result) {
        if (node == null) {
            return;
        }

        // query starts strictly after any interval in the subtree
        if (interval.getStart().compareTo(node.getHi()) > 0) {
            return;
        }

        // node and query overlap
        if (node.getVal().isIntersecting(interval)) {
            result.add(node.getVal());
        }

        // if the node starts before the query ends, check right children
        if (node.getVal().getStart().compareTo(interval.getEnd()) <= 0) {
            overlapsWith(node.getRightChild(), interval, result);
        }

        overlapsWith(node.getLeftChild(), interval, result);
    }

    private void overlapsWithPoint(Node<T, I> node, T point, List<I> result) {
        if (node == null) {
            return;
        }

        // point is bigger than the endpoint of any interval in the subtree
        if (point.compareTo(node.getHi()) > 0) {
            return;
        }

        // check left subtrees
        overlapsWithPoint(node.getLeftChild(), point, result);

        // add node interval if it contains the query point
        if (node.getVal().contains(point)) {
            result.add(node.getVal());
        }

        // check right subtree if their start values are smaller (equal) than query point
        if (point.compareTo(node.getVal().getStart()) >= 0) {
            overlapsWithPoint(node.getRightChild(), point, result);
        }
    }

    public void removeAll(Collection<? extends I> vertices) {
        for (I vertex : vertices) {
            delete(vertex.getStart());
        }
    }
}
