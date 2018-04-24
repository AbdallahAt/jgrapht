package org.jgrapht.intervalgraph;

import org.jgrapht.intervalgraph.interval.Interval;

import java.util.List;

public interface IntervalTreeInterface<T extends Comparable<T>, I extends Interval<T>> extends BinarySearchTree<T, I> {

    public List<I> overlapsWith(I interval);

    public List<I> overlapsWithPoint(T point);

    public void add(I interval);

    public void remove(I interval);
}
