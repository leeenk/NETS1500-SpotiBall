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
    private ArrayList<String> queue;
    private ArrayList<String> discovered;
    private int[] parent;

    public BFS() {
        // create graph from data.txt
        BufferedReader br;
        int count = 0;
        try {
            br = new BufferedReader(new FileReader("files/data.txt"));
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
        this.graph = new Graph(count);
        queue = new ArrayList<>();
        discovered = new ArrayList<>();
        parent = new int[count];
    }


    /**
     * Method that populates a graph with the current artist and relevant artists data in data.txt
     *
     * @return Graph with nodes being the ids of discovered artist and an
     * edge representing a track feature (direction of feature disregarded)
     */
    public void populateGraph() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("files/data.txt"));
            String currArtist = br.readLine();
            while (currArtist != null) {
                currArtist = br.readLine();
//               // 0 = id, 1 = index, 2 = name
//               // split name-ID of currArtist by "@"
                String[] arrCurrArtist = currArtist.split("@");
                // currArtists input here should be ID
                if (!graph.containsArtist(arrCurrArtist[0])) {
                    graph.addArtist(arrCurrArtist[0], Integer.parseInt(arrCurrArtist[1]), arrCurrArtist[2]);
                }
                br.readLine(); // reads related artist line
                String currRelArtist = br.readLine();
                while (!currRelArtist.equals("Artist:") && currRelArtist != null) {
                    String[] arrCurrRelArtist = currRelArtist.split("@");
                    if (!graph.containsArtist(arrCurrRelArtist[0])) {
                        graph.addArtist(arrCurrRelArtist[0], Integer.parseInt(arrCurrRelArtist[1]), arrCurrRelArtist[2]);
                    }
                    if (!graph.hasEdge(arrCurrArtist[0], arrCurrRelArtist[0])) {
                        graph.addEdge(arrCurrArtist[0], arrCurrRelArtist[0]);
                    }
                    currRelArtist = br.readLine();
                }
            }
            br.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }


    // static method runs BFS
    // change output type if want
    // correctly output empty list if there is no connection between the artists (given the limiting size of graph)
    public void runBFS(String source, String target) {
        String firstArtist = source;
        discovered.add(source);
        queue.add(source);

        while (queue.size() != 0) {

            String temp = queue.get(0);
            firstArtist = temp;


            // if the queue has the friend, remove the friend from the queue /remove the first source friend from queue
            if (discovered.contains(queue.get(0))) {
                queue.remove(0);
            }

            /* get the friends of the current node and add the neighbors to queue & discovered, if the neighbor is
             not discovered.
             */
            for (int i = 0; i < graph.vertices.length; i++) {
                List<String> artists = graph.vertices[i];
                for (String artist : artists) {
                    // find only undiscovered friend neighbors
                    if (!discovered.contains(artist)) {
                        queue.add(artist);
                        discovered.add(artist);
                        // need index of related artist
                        parent[graph.IDToIndex.get(artist)] = graph.IDToIndex.get(temp);
                    }
                }
            }
        }
        // traversing through parent array



    }


}
