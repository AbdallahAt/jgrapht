package org.jgrapht.intervalgraph;

import org.jgrapht.intervalgraph.interval.Interval;

import java.io.Serializable;
import java.util.Objects;

/**
 * Implementation of IntervalTreeNodeValue
 * This is a container class to store the necessary data for the (augmented) interval tree in the nodes of the tree.
 *
 * @param <T> the type of the interval
 *
 * @author Christoph Grüne (christophgruene)
 * @since Apr 26, 2018
 */
public class IntervalTreeNodeValue<I extends Interval<T>, T extends Comparable<T>> implements Serializable {

    private static final long serialVersionUID = 1111005364785643338L;

    /**
     * The interval
     */
    private Interval<T> interval;

    /**
     * the greatest end point of all intervals in the subtree rooted at that node.
     */
    private T highValue;

    /**
     * Create a new pair
     *
     * @param interval the first element
     * @param highValue the second element
     */
    public IntervalTreeNodeValue(I interval, T highValue) {
        this.interval = interval;
        this.highValue = highValue;
    }

    /**
     * Get the first element of the pair
     *
     * @return the first element of the pair
     */
    public Interval<T> getInterval()
    {
        return interval;
    }

    /**
     * Get the second element of the pair
     *
     * @return the second element of the pair
     */
    public T getHighValue()
    {
        return highValue;
    }

    public void setHighValue(T highValue) {
        this.highValue = highValue;
    }

    @Override
    public String toString()
    {
        return "(" + interval + "," + highValue + ")";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        else if (!(o instanceof IntervalTreeNodeValue))
            return false;

        @SuppressWarnings("unchecked") IntervalTreeNodeValue<I, T> other = (IntervalTreeNodeValue<I, T>) o;
        return Objects.equals(interval, other.interval) && Objects.equals(highValue, other.highValue);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(interval, highValue);
    }
}
