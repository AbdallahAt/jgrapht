package org.jgrapht.intervalgraph;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.jgrapht.Graph;
import org.jgrapht.intervalgraph.interval.Interval;

public class CenteredIntervalTree<T extends Comparable<T>>  {
    private T centerPoint;
    private CenteredIntervalTree<T> leftTree; // contains all the intervals *completely* to the left of the center point
    private CenteredIntervalTree<T> rightTree; // contains all the intervals *completely* to the right of the center point
    private List<Interval<T>> intersectionsByStart = new LinkedList<>(); // intervals intersecting center point, sorted by start point
    private List<Interval<T>> intersectionsByEnd = new LinkedList<>(); // ..., sorted by end point
    private boolean isEmpty = true;

    public CenteredIntervalTree(){}


    public CenteredIntervalTree(List<Interval<T>> intervals) {
        initialize(intervals);
    }

    private void initialize(List<Interval<T>> intervals) {
        if (intervals == null){
            throw new IllegalArgumentException();
        }

        if (intervals.isEmpty()) {
            return;
        }
        isEmpty = false;

        Interval<T> randomInterval = intervals.get(ThreadLocalRandom.current().nextInt(intervals.size()));
        if (ThreadLocalRandom.current().nextBoolean()) {
            this.centerPoint = randomInterval.getStart();
        } else {
            this.centerPoint = randomInterval.getEnd();
        }

        List<Interval<T>> leftIntervals = new LinkedList<>(); // containing the intervals completely to the left of the center point
        List<Interval<T>> rightIntervals = new LinkedList<>(); // ... to the right
        List<Interval<T>> intersectingIntervals = new LinkedList<>(); // intervals intersecting the center point

        for (Interval<T> interval: intervals) {
            if (interval.contains(centerPoint)) {
                intersectingIntervals.add(interval);
            } else if (interval.compareToPoint(centerPoint) < 0) { // is completely left to center point
                leftIntervals.add(interval);
            } else if (interval.compareToPoint(centerPoint) > 0) { // completely right
                rightIntervals.add(interval);
            }
        }

        // sorting the intervals according to start/end point
        intersectionsByStart = new LinkedList<>(intersectingIntervals);
        intersectionsByStart.sort(Comparator.comparing(Interval::getStart));

        intersectionsByEnd = intersectingIntervals;
        intersectionsByEnd.sort(Collections.reverseOrder(Comparator.comparing(Interval::getStart)));

        // add children to the left and right
        leftTree = new CenteredIntervalTree<>();
        leftTree.initialize(leftIntervals);

        rightTree = new CenteredIntervalTree<>();
        rightTree.initialize(rightIntervals);
    }

    //@Override
    public Collection<Interval<T>> intersections(T point) {
        Set<Interval<T>> result = new HashSet<>(); // TODO Be aware, that I dont know if using sets guarantees linear run time
        intersections(point, result);
        return result;
    }

    //@Override
    public Collection<Interval<T>> intersections(Interval<T> queryInterval) {
        throw new RuntimeException("Method not implemented.");
    }

    private void intersections(T point, Set<Interval<T>> result) {
        if (isEmpty) {
            return;
        }

        if (point.equals(centerPoint)){
            result.addAll(intersectionsByEnd); // or intersectionsByStart
        } else if (point.compareTo(centerPoint) < 0) {
            // add all intervals with start point <= center point
            for (Interval<T> interval: intersectionsByStart) {
                if (interval.getStart().compareTo(point) > 0) {
                    break;
                }
                result.add(interval);
            }

            // recursion to child in tree
            leftTree.intersections(point, result);
        } else { // point > centerPoint
            // add all intervals with end point >= center point
            for (Interval<T> interval: intersectionsByEnd) {
                if (interval.getEnd().compareTo(point) < 0) {
                    break;
                }
                result.add(interval);
            }

            // recursion to child in tree
            rightTree.intersections(point, result);
        }
    }

    //@Override
    public Graph asGraph() {
        throw new RuntimeException("Method not implemented.");
    }
}
