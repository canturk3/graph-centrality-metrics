import java.util.Iterator;
public interface VertexInterface<T> {
    /** Gets the vertex's label.
     @return the object that labels the vertex */
    public T getLabel();
    /** Marks the vertex as visited. */
    public void visit();
    /** Removes the vertex's visited mark. */
    public void unvisit();
    /** Sees whether the vertex is marked as visited.
     @return true if the vertex is visited */
    public boolean isVisited();
    /** Connects this vertex and a given vertex with a weighted edge.
     The two vertices cannot be the same, and must not already
     have this edge between them. In a directed graph, the edge
     points toward the given vertex.
     @param endVertex a vertex in the graph that ends the edge
     @param edgeWeight a real-valued edge weight, if any
     @return true if the edge is added, or false if not */
    public boolean connect(VertexInterface<T> endVertex, double edgeWeight);
    /** Connects this vertex and a given vertex with an unweighted
     edge. The two vertices cannot be the same, and must not
     already have this edge between them. In a directed graph,
     the edge points toward the given vertex.
     @param endVertex a vertex in the graph that ends the edge
     @return true if the edge is added, or false if not */
    public boolean connect(VertexInterface<T> endVertex);
    /** Creates an iterator of this vertex's neighbors by following
     all edges that begin at this vertex.
     @return an iterator of the neighboring vertices of this vertex */
    public Iterator<VertexInterface<T>> getNeighborIterator();
    /** Creates an iterator of the weights of the edges to this
     vertex's neighbors.
     @return an iterator of edge weights for edges to neighbors of this
     vertex */
    public Iterator<Double> getWeightIterator();
    /** Sees whether this vertex has at least one neighbor.
     @return true if the vertex has a neighbor */
    public boolean hasNeighbor();
    /** Gets an unvisited neighbor, if any, of this vertex.
     @return either a vertex that is an unvisited neighbor or null
     if no such neighbor exists */
    public VertexInterface<T> getUnvisitedNeighbor();
    /** Records the previous vertex on a path to this vertex.
     @param predecessor the vertex previous to this one along a path
     */
    public void setPredecessor(VertexInterface<T> predecessor);
    /** Gets the recorded predecessor of this vertex.
     @return either this vertex's predecessor or null if no predecessor
     was recorded */
    public VertexInterface<T> getPredecessor();
    /** Sees whether a predecessor was recorded.
     @return true if a predecessor was recorded for this vertex */
    public boolean hasPredecessor();
    /** Records the cost of a path to this vertex.
     @param newCost the cost of the path */
    public void setCost(double newCost);
    /** Gets the recorded cost of the path to this vertex.
     @return the cost of the path */
    public double getCost();
}
