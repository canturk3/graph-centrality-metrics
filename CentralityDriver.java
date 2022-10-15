import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CentralityDriver {
    private static UndirectedGraph<Integer> readFileIntoGraph(String pathname){
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        File textFile = new File(pathname);
        Scanner sc;
        try{
            sc = new Scanner(textFile);
            while(sc.hasNext()){
                String text = sc.nextLine();
                String[] splitted = text.split(" ");
                if (splitted.length == 2) {
                    int beginVertex = Integer.parseInt(splitted[0]);
                    int endVertex = Integer.parseInt(splitted[1]);

                    graph.addVertex(beginVertex);
                    graph.addVertex(endVertex);
                    graph.addEdge(beginVertex,endVertex);
                }
                else if(splitted.length != 0){//length is 0 if it is an empty line
                    System.out.println("ERROR: Please make sure there are 2 pairs of integers divided by space in each line.");
                    return null;
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e){
            System.out.println("ERROR: File \"" + pathname + "\" was not found.");
            return null;
        }
        catch(NumberFormatException e){
            System.out.println("ERROR: Please enter only integer numbers for vertices. Correct line format is:\"2 4\"");
            return null;
        }
        return graph;
    }
    private static void printBetweennessAndCloseness(UndirectedGraph<Integer> graph,HashMap<LinkedHashSet<Integer>,Integer> allShortestPaths,String graphName){
        double[] highestBetweennessValues = graph.getHighestBetweenness(allShortestPaths);
        double[] highestClosenessValues = graph.getHighestCloseness(allShortestPaths);
        System.out.println(graphName + " - " + "The Highest Node for Betweenness " + (int)highestBetweennessValues[0] + " and the value " + String.format("%.3f",highestBetweennessValues[1]) + "\n" +
                           graphName + " - " + "The Highest Node for Closeness " + (int)highestClosenessValues[0] + " and the value " + String.format("%.3f",highestClosenessValues[1]));
    }
    public static void main(String[] args){
        String FIRST_PATH_NAME = "src/karate_club_network.txt";
        String SECOND_PATH_NAME = "src/facebook_social_network.txt";

        String FIRST_GRAPH_NAME = "Zachary Karate Club Network";
        String SECOND_GRAPH_NAME = "Facebook Social Network";

        String HEADER = "2020510060 Can Türk Küçük:";

        UndirectedGraph<Integer> firstGraph = readFileIntoGraph(FIRST_PATH_NAME);
        if(firstGraph != null){

            UndirectedGraph<Integer> secondGraph = readFileIntoGraph(SECOND_PATH_NAME);
            if(secondGraph != null){
                System.out.println(HEADER);

                //Calculated all shortest paths beforehand to save time by not calculating it twice
                //in both getHighestBetweenness and getHighestCloseness methods
                HashMap<LinkedHashSet<Integer>,Integer> allShortestPaths = firstGraph.getAllShortestPaths();
                printBetweennessAndCloseness(firstGraph,allShortestPaths,FIRST_GRAPH_NAME);

                System.out.println();

                allShortestPaths = secondGraph.getAllShortestPaths();
                printBetweennessAndCloseness(secondGraph,allShortestPaths,SECOND_GRAPH_NAME);
            }
        }
    }
}
