import java.util.HashMap;
import java.util.Iterator;
import java.util.*;

public class UndirectedGraph<T> implements BasicGraphInterface<T>{
    private HashMap<T, VertexInterface<T>> vertices;
    private int edgeCount;

    public UndirectedGraph()
    {
        vertices = new HashMap<>();
        edgeCount = 0;
    }

    public boolean addVertex(T vertexLabel)
    {
        //Does not add vertices that are already present in the graph to protect the connected edges.
        if(!vertices.containsKey(vertexLabel)){
            vertices.put(vertexLabel, new Vertex(vertexLabel));
            return true;
        }
        else return false;
    }

    public boolean hasVertex(T vertexLabel){
        return vertices.containsKey(vertexLabel);
    }

    public boolean addEdge(T begin, T end)
    {
        boolean result = false;
        VertexInterface<T> beginVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        if ( (beginVertex != null) && (endVertex != null) ){
            result = beginVertex.connect(endVertex);
            result = endVertex.connect(beginVertex);// TODO fix boolean return value
        }
        if (result)
            edgeCount++;
        return result;
    }

    //This method is added to accommodate basic graph interface.
    public boolean addEdge(T begin, T end, double edgeWeight) {
        return addEdge(begin,end);
    }

    public boolean hasEdge(T begin, T end)
    {
        boolean found = false;
        VertexInterface<T> beginVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        if ( (beginVertex != null) && (endVertex != null) )
        {
            Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
            while (!found && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    found = true;
            } // end while
        } // end if
        return found;
    } // end hasEdge
    public boolean isEmpty()
    {
        return vertices.isEmpty();
    } // end isEmpty
    public void clear()
    {
        vertices.clear();
        edgeCount = 0;
    } // end clear
    public int getNumberOfVertices()
    {
        return vertices.size();
    } // end getNumberOfVertices
    public int getNumberOfEdges()
    {
        return edgeCount;
    } // end getNumberOfEdges

    /** Checks inside all of the shortest paths and increases the frequency of each found vertex,
     * then finds the highest occurent vertex and divides by the total path count to return as the highest betweenness.
     * @return 2 sized double array: 1st index is node label, 2nd index is betweenness value*/
    //takes all shortest paths as parameter instead of calculating it inside to allow user to store the paths once and
    //use it in other methods also, thus saving time.
    public double[] getHighestBetweenness(HashMap<LinkedHashSet<T>,Integer> allShortestPaths){
        HashMap<T,Integer> vertexFrequency = new HashMap<>();
        T highestOccurVertexLabel = null;
        int highestOccurCount = -1;

        //increases the frequency of each vertex whenever occurred in any path.
        for (Map.Entry<LinkedHashSet<T>, Integer> shortestPath : allShortestPaths.entrySet()){
            LinkedHashSet<T> path = shortestPath.getKey();
            for (T vertexLabel : path) {
                if (vertexFrequency.containsKey(vertexLabel)) {
                    vertexFrequency.put(vertexLabel, vertexFrequency.get(vertexLabel) + 1);
                } else {
                    vertexFrequency.put(vertexLabel, 1);
                }
            }
        }
        //find the highest frequency of all vertices that was found inside the shortest paths
        for (Map.Entry<T, Integer> vertex : vertexFrequency.entrySet()){
            if((vertex.getValue() > highestOccurCount) || (highestOccurCount == -1)){
                highestOccurCount = vertex.getValue();
                highestOccurVertexLabel = vertex.getKey();
            }
        }
        if(highestOccurVertexLabel != null){
            double node = Double.parseDouble((highestOccurVertexLabel.toString()));
            double betweenness = (double)highestOccurCount / allShortestPaths.size();

            return (new double[]{node,betweenness});
        }
        else return null;
    }
    //This method is for the users that don't need to calculate shortest paths beforehand to save time.
    public double[] getHighestBetweenness(){
        return getHighestBetweenness(getAllShortestPaths());
    }
    /** For all of the shortest paths get beginning and end vertices, increase these vertices's
     * distance to other vertices by that shortest path length.This way all of the shortest path distances
     * any vertex has to any other vertex is added up in one loop.
     * @return 2 sized double array: 1st index is vertex label, 2nd index is closeness value(closeness value is in sum form)*/
    /*To calculate the closeness, even in disconnected graphs, I used the formula defined in this page:
    https://toreopsahl.com/2010/03/20/closeness-centrality-in-networks-with-disconnected-components/
    In short; closeness is defined as sum(1/d(i,n)) so that disconnected paths have lesser sum while
    connected paths have higher. */
    public double[] getHighestCloseness(HashMap<LinkedHashSet<T>,Integer> allShortestPaths){
        double sumOneOverDistance = -1;
        T closestVertexLabel = null;
        T beginVertexLabel = null,endVertexLabel;

        HashMap<T,Double> nodesWithCloseness = new HashMap<>();
        //for all of the shortest paths
        for (Map.Entry<LinkedHashSet<T>, Integer> shortestPath : allShortestPaths.entrySet()){
            LinkedHashSet<T> path = shortestPath.getKey();
            Iterator<T> pathIter = path.iterator();

            //get begin vertex and end vertex
            endVertexLabel = (T)(pathIter.next());
            assert pathIter.hasNext();//asserts that vertices doesn't have paths to themselves
            while(pathIter.hasNext())
                beginVertexLabel = (T)(pathIter.next());

            double closeness = ((double)1 / shortestPath.getValue());

            //increase the distance begin and end vertex has to other vertices.
            if (nodesWithCloseness.containsKey(endVertexLabel))
                nodesWithCloseness.put(endVertexLabel, nodesWithCloseness.get(endVertexLabel) + closeness);
            else
                nodesWithCloseness.put(endVertexLabel, closeness);

            if (beginVertexLabel != null && nodesWithCloseness.containsKey(beginVertexLabel))
                nodesWithCloseness.put(beginVertexLabel, nodesWithCloseness.get(beginVertexLabel) + closeness);
            else
                nodesWithCloseness.put(beginVertexLabel, closeness);
        }
        //find smallest distance by getting highest value since the distances were summed up as 1/distance each step
        for (Map.Entry<T, Double> node : nodesWithCloseness.entrySet()){
            if((node.getValue() > sumOneOverDistance) || (sumOneOverDistance == -1)){
                sumOneOverDistance = node.getValue();
                closestVertexLabel = node.getKey();
            }
        }
        if(closestVertexLabel != null){
            double node = Double.parseDouble((closestVertexLabel).toString());

            return (new double[]{node,sumOneOverDistance});//sumOneOverDistance represents closeness. higher the sum, higher the closeness.
        }
        else return null;
    }
    //This method is for the users that don't need to calculate shortest paths beforehand to save time.
    public double[] getHighestCloseness(){
        return getHighestCloseness(getAllShortestPaths());
    }

    protected void resetVertices()
    {
        for (VertexInterface<T> vertex : vertices.values()) {
            vertex.unvisit();
            vertex.setCost(0);
            vertex.setPredecessor(null);
        }
    }

    /**@return all of the shortest paths in a HashMap that contains LinkedHashSet's as keys/paths and their total distances as values*/
    //LinkedHashSet is used to allow expected O(1) search time whenever it is needed to check inside any path, whether given vertex exists or not.
    //Shortest paths are not stored inside the graph since it would need constant updating whenever vertices are added or deleted
    public HashMap<LinkedHashSet<T>,Integer> getAllShortestPaths()
    {
        HashMap<LinkedHashSet<T>,Integer> paths = new HashMap<LinkedHashSet<T>, Integer>();
        Queue<VertexInterface<T>> vertexQueue = new LinkedList<VertexInterface<T>>();
        HashSet<T> beginVertices = new HashSet<>();

        for (Map.Entry<T, VertexInterface<T>> beginElement : vertices.entrySet()) {//Loop for each vertex
            resetVertices();
            VertexInterface<T> originVertex = beginElement.getValue();
            originVertex.visit();
            // Assertion: resetVertices() has executed setCost(0)
            // and setPredecessor(null) for originVertex
            vertexQueue.add(originVertex);
            beginVertices.add(originVertex.getLabel());

            while (!vertexQueue.isEmpty())//Loop for all the other vertices in the graph to draw shortest paths.
            {
                VertexInterface<T> frontVertex = vertexQueue.poll();
                Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
                while (neighbors.hasNext())//Visit each neighbor
                {
                    LinkedHashSet<T> path = new LinkedHashSet<>();
                    VertexInterface<T> nextNeighbor = neighbors.next();
                    if (!nextNeighbor.isVisited())
                    {
                        nextNeighbor.visit();
                        nextNeighbor.setCost(1 + frontVertex.getCost());
                        nextNeighbor.setPredecessor(frontVertex);
                        vertexQueue.add(nextNeighbor);

                        /*Do not calculate and add paths that was already calculated by opposite
                         beginning and end vertices */
                        if(!beginVertices.contains(nextNeighbor.getLabel())){
                            int pathLength = (int)nextNeighbor.getCost();
                            if(pathLength != 0){
                                path.add(nextNeighbor.getLabel());
                                while (nextNeighbor.hasPredecessor())
                                {
                                    nextNeighbor = nextNeighbor.getPredecessor();
                                    path.add(nextNeighbor.getLabel());
                                } // end while
                                paths.put(path,pathLength);
                            }
                        }
                    } // end if
                } // end while
            } // end while
        }
        return paths;
    } // end getShortestPath

    //General shortest path algorithm that works very similar to breadth first traversal
    public int getShortestPath(T begin, T end, LinkedHashSet<T> path)
    {
        resetVertices();
        boolean done = false;
        Queue<VertexInterface<T>> vertexQueue = new LinkedList<VertexInterface<T>>();
        VertexInterface<T> originVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        originVertex.visit();
        // Assertion: resetVertices() has executed setCost(0)
        // and setPredecessor(null) for originVertex
        vertexQueue.add(originVertex);
        while (!done && !vertexQueue.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexQueue.poll();
            Iterator<VertexInterface<T>> neighbors =
                    frontVertex.getNeighborIterator();
            while (!done && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.add(nextNeighbor);
                } // end if
                if (nextNeighbor.equals(endVertex))
                    done = true;
            } // end while
        } // end while
        // traversal ends; construct shortest path
        int pathLength = (int)endVertex.getCost();
        path.add(endVertex.getLabel());
        VertexInterface<T> vertex = endVertex;
        while (vertex.hasPredecessor())
        {
            vertex = vertex.getPredecessor();
            path.add(vertex.getLabel());
        } // end while
        return pathLength;
    } // end getShortestPath

    //Breadth first traversal has been added for the future use of this class.
    public Queue<T> getBreadthFirstTraversal(T origin)
    {
        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>();
        Queue<VertexInterface<T>> vertexQueue = new LinkedList<VertexInterface<T>>();
        VertexInterface<T> originVertex = vertices.get(origin);
        originVertex.visit();
        traversalOrder.add(origin);
        vertexQueue.add(originVertex);
        while (!vertexQueue.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexQueue.poll();
            Iterator<VertexInterface<T>> neighbors =
                    frontVertex.getNeighborIterator();
            while (neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    traversalOrder.add(nextNeighbor.getLabel());
                    vertexQueue.add(nextNeighbor);
                } // end if
            } // end while
        } // end while
        return traversalOrder;
    } // end getBreadthFirstTraversal
}
