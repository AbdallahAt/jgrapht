package org.jgrapht.intervalgraph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.intervalgraph.interval.Interval;

import java.util.Collection;
import java.util.Set;

public class SimpleIntervalGraph<T extends Comparable<T>, I extends Interval<T>> implements IntervalGraphInterface<T, I> {
    private RedBlackIntervalTree<T, I> intervalTree;
    private Graph<I, DefaultEdge> graph;

    private RuntimeException CANNOT_CHANGE_EDGE_EXCEPTION =
            new RuntimeException("Operation not supported: Edges are implicit in an interval graph");

    public SimpleIntervalGraph() {
        intervalTree = new RedBlackIntervalTree<>();
        graph = new SimpleGraph<>(DefaultEdge.class); //which graph to choose?
    }


    @Override
    public Collection<I> overlapWithPoint(T queryPoint) {
        return intervalTree.overlapsWithPoint(queryPoint);
    }

    @Override
    public Collection<I> overlapWithInterval(I queryInterval) {
        return intervalTree.overlapsWith(queryInterval);
    }

    @Override
    public Set<DefaultEdge> getAllEdges(I sourceVertex, I targetVertex) {
        return graph.getAllEdges(sourceVertex, targetVertex);
    }

    @Override
    public DefaultEdge getEdge(I sourceVertex, I targetVertex) {
        return graph.getEdge(sourceVertex, targetVertex);
    }

    @Override
    public EdgeFactory<I, DefaultEdge> getEdgeFactory() {
        return graph.getEdgeFactory();
    }

    @Override
    public DefaultEdge addEdge(I sourceVertex, I targetVertex) {
        throw CANNOT_CHANGE_EDGE_EXCEPTION;
    }

    @Override
    public boolean addVertex(I i) {
        graph.addVertex(i);
        for (I neighbour : intervalTree.overlapsWith(i)) {
            graph.addEdge(i, neighbour);
        }

        intervalTree.add(i);

        return true;
    }

    @Override
    public boolean containsEdge(I sourceVertex, I targetVertex) {
        return graph.containsEdge(sourceVertex, targetVertex);
    }

    @Override
    public boolean containsVertex(I i) {
        return graph.containsVertex(i);
    }

    @Override
    public Set<DefaultEdge> edgeSet() {
        return graph.edgeSet();
    }

    @Override
    public int degreeOf(I vertex) {
        return graph.degreeOf(vertex);
    }

    @Override
    public Set<DefaultEdge> edgesOf(I vertex) {
        return graph.edgesOf(vertex);
    }

    @Override
    public int inDegreeOf(I vertex) {
        return graph.inDegreeOf(vertex);
    }

    @Override
    public Set<DefaultEdge> incomingEdgesOf(I vertex) {
        return graph.incomingEdgesOf(vertex);
    }

    @Override
    public int outDegreeOf(I vertex) {
        return graph.outDegreeOf(vertex);
    }

    @Override
    public Set<DefaultEdge> outgoingEdgesOf(I vertex) {
        return graph.outgoingEdgesOf(vertex);
    }

    @Override
    public boolean removeAllEdges(Collection<? extends DefaultEdge> edges) {
        throw CANNOT_CHANGE_EDGE_EXCEPTION;
    }

    @Override
    public Set<DefaultEdge> removeAllEdges(I sourceVertex, I targetVertex) {
        throw CANNOT_CHANGE_EDGE_EXCEPTION;
    }

    @Override
    public boolean removeAllVertices(Collection<? extends I> vertices) {
        intervalTree.removeAll(vertices);
        return graph.removeAllVertices(vertices);
    }

    @Override
    public DefaultEdge removeEdge(I sourceVertex, I targetVertex) {
        throw CANNOT_CHANGE_EDGE_EXCEPTION;
    }
    
    @Override
    public boolean removeVertex(I i) {
        intervalTree.remove(i);
        return graph.removeVertex(i);
    }

    @Override
    public Set<I> vertexSet() {
        return graph.vertexSet();
    }
    
    @Override
    public GraphType getType() {
        return graph.getType();
    }

    @Override
    public boolean addEdge(I sourceVertex, I targetVertex, DefaultEdge edge) {
        throw CANNOT_CHANGE_EDGE_EXCEPTION;
    }

    @Override
    public boolean containsEdge(DefaultEdge edge) {
        return graph.containsEdge(edge);
    }

    @Override
    public boolean removeEdge(DefaultEdge edge) {
        throw CANNOT_CHANGE_EDGE_EXCEPTION;
    }

    @Override
    public I getEdgeSource(DefaultEdge edge) {
        return graph.getEdgeSource(edge);
    }

    @Override
    public I getEdgeTarget(DefaultEdge edge) {
        return graph.getEdgeTarget(edge);
    }

    @Override
    public double getEdgeWeight(DefaultEdge edge) {
        return graph.getEdgeWeight(edge);
    }

    @Override
    public void setEdgeWeight(DefaultEdge edge, double weight) {
        graph.setEdgeWeight(edge, weight);
    }
}
