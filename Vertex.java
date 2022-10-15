import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
//Vertex class is implemented such that it can also be used for directed graphs.
public class Vertex<T> implements VertexInterface<T> {
    private T label;
    private List<Edge> edgeList;

    private boolean visited;
    private VertexInterface<T> previousVertex;
    private double cost;

    public Vertex(T vertexLabel)
    {
        label = vertexLabel;
        edgeList = new LinkedList<Edge>();
        visited = false;
        previousVertex = null;
        cost = 0;
    }

    public boolean hasNeighbor()
    {
        return !edgeList.isEmpty();
    }

    public VertexInterface<T> getUnvisitedNeighbor()
    {
        VertexInterface<T> result = null;
        Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
        while ( (neighbors.hasNext() && (result == null) ))
        {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (!nextNeighbor.isVisited())
                result = nextNeighbor;
        } // end while
        return result;
    } // end getUnvisitedNeighbor

    public void setPredecessor(VertexInterface<T> predecessor) {
        previousVertex = predecessor;
    }

    public VertexInterface<T> getPredecessor() {
        return previousVertex;
    }

    public boolean hasPredecessor() {
        return previousVertex != null;
    }

    public void setCost(double newCost) {
        cost = newCost;
    }

    public double getCost() {
        return cost;
    }

    public boolean equals(Object other)
    {
        boolean result;
        if ((other == null) || (getClass() != other.getClass()))
            result = false;
        else
        {
            Vertex<T> otherVertex = (Vertex<T>)other;
            result = label.equals(otherVertex.label);
        } // end if
        return result;
    } // end equals

    public T getLabel() {
        return label;
    }

    public void visit() {
        visited = true;
    }

    public void unvisit() {
        visited = false;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean connect(VertexInterface<T> endVertex, double edgeWeight)
    {
        boolean result = false;
        if (!this.equals(endVertex))
        { // vertices are distinct
            Iterator<VertexInterface<T>> neighbors = this.getNeighborIterator();
            boolean duplicateEdge = false;
            while (!duplicateEdge && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    duplicateEdge = true;
            } // end while
            if (!duplicateEdge)
            {
                edgeList.add(new Edge(endVertex, edgeWeight));
                result = true;
            } // end if
        } // end if
        return result;
    } // end connect
    public boolean connect(VertexInterface<T> endVertex)
    {
        return connect(endVertex, 0);
    } // end connect

    public Iterator<VertexInterface<T>> getNeighborIterator()
    {
        return new neighborIterator();
    } // end getNeighborIterator

    public Iterator<Double> getWeightIterator() {
        return null;
    }

    private class neighborIterator implements Iterator<VertexInterface<T>>
    {
        private Iterator<Edge> edges;
        private neighborIterator()
        {
            edges = edgeList.iterator();
        } // end default constructor
        public boolean hasNext()
        {
            return edges.hasNext();
        } // end hasNext
        public VertexInterface<T> next()
        {
            VertexInterface<T> nextNeighbor = null;
            if (edges.hasNext())
            {
                Edge edgeToNextNeighbor = edges.next();
                nextNeighbor = edgeToNextNeighbor.getEndVertex();
            }
            else
                throw new NoSuchElementException();
            return nextNeighbor;
        } // end next
        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end neighborIterator
    protected class Edge
    {
        private VertexInterface<T> vertex; // end vertex
        private double weight;
        protected Edge(VertexInterface<T> endVertex, double edgeWeight)
        {
            vertex = endVertex;
            weight = edgeWeight;
        } // end constructor
        protected VertexInterface<T> getEndVertex()
        {
            return vertex;
        } // end getEndVertex
        protected double getWeight()
        {
            return weight;
        } // end getWeight
    } // end Edge
}
