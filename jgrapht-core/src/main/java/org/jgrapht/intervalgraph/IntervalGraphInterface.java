package org.jgrapht.intervalgraph;

import java.util.Collection;

import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.intervalgraph.interval.Interval;

public interface IntervalGraphInterface<T extends Comparable<T>, I extends Interval<T>> extends Graph<I, DefaultEdge>{
    public Collection<I> overlapWithPoint(T queryPoint);

    public Collection<I> overlapWithInterval(I queryInterval);
}
