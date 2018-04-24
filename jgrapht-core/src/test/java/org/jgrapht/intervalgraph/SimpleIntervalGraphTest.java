package org.jgrapht.intervalgraph;

import org.jgrapht.intervalgraph.interval.IntegerInterval;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SimpleIntervalGraphTest {
    private SimpleIntervalGraph<Integer, IntegerInterval> testGraph;

    private ArrayList<IntegerInterval> intervals;
    private int n = 1000;

    @Before
    public void setUp() {
        testGraph = new SimpleIntervalGraph<>();
        intervals = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            IntegerInterval interval = new IntegerInterval(i, i+1);
            intervals.add(interval);
            testGraph.addVertex(interval);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() {
        assertEquals(n, testGraph.overlapWithInterval(new IntegerInterval(0, n)).size());
        assertEquals(1, testGraph.overlapWithPoint(n).size());
        for (int i = 1; i < n-1; i++) {
            assertEquals("error at interval " + i, 2, testGraph.outDegreeOf(intervals.get(i)));
            assertEquals("error at interval " + i, 2, testGraph.inDegreeOf(intervals.get(i)));
        }
        assertEquals(1, testGraph.inDegreeOf(intervals.get(0)));
        assertEquals(1, testGraph.inDegreeOf(intervals.get(n-1)));

    }
}