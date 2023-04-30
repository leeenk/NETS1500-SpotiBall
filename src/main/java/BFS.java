import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BFS {

    Graph graph;
    HashMap<String, String> nameToID;


    public BFS(Graph graph) {
        this.graph = graph;
    }


    /**
     * Method that creates a graph by counting the size of the number of artists
     *
     * @return Graph with nodes being the ids of discovered artist and an
     * edge representing a track feature (direction of feature disregarded)
     */
    public Graph createGraph() {
        BufferedReader br;
        int count = 0;
        try {
            br = new BufferedReader(new FileReader("/Users/nakyung/Desktop/NETS1500-SpotiBall/src/main/java/data.txt"));
            String line = br.readLine();
            while (line != null) {
                if (line.equals("Artist:")) {
                    count++;
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph = new Graph(count);
    }



    public void populateGraph() {
        BufferedReader br;
        String currArtist = "";
        String currRelArtist = "";
        try {
            br = new BufferedReader(new FileReader("/Users/nakyung/Desktop/NETS1500-SpotiBall/src/main/java/data.txt"));
            String line = br.readLine();
            while (line != null) {
                if (line.equals("Artist:")) {
                    currArtist = br.readLine();
                    // 0 = name, 1 = id
                    // split name-ID of currArtist by "-"
                    String[] arr = currArtist.split("@");


                    // currArtists input here should be ID
                    if (!graph.containsArtist(arr[1])) {
                        graph.addArtist(arr[1], graph.IDToIndex.get(arr[1]));
                    }
                } else if (line.equals("Related Artists:")) {
                    currRelArtist = br.readLine();
                    String[] arr = currArtist.split("@");

                    while (!currRelArtist.equals("Artist:") && currRelArtist != null) {
                        if (!graph.containsArtist(arr[1])) {
                            graph.addArtist(arr[1], graph.IDToIndex.get(arr[1]));
                            if (!graph.hasEdge(currArtist, currRelArtist)) {
                                graph.addEdge(currArtist, currRelArtist);
                            }
                        }
                        currRelArtist = br.readLine();
                    }
                }

                if (currRelArtist.equals("Artist:")) {
                    line = currRelArtist;
                } else {
                    line = br.readLine();
                }

            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // static method runs BFS
    // change output type if want
    // correctly output empty list if there is no connection between the artists (given the limiting size of graph)
    static List<String> runBFS(Graph graph, String source, String target) {
        return new ArrayList<>();
    }

//
//    public static void main(String[] args) {
//        createGraph();
//    }
}
