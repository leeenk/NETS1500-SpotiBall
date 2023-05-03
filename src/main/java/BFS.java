import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
        BufferedReader br;
        String currArtist = "";
        String currRelArtist = "";
        String[] arrCurrArtist = new String[3];
        String[] arrCurrRelArtist = new String[3];

        try {
            br = new BufferedReader(new FileReader("files/data.txt"));
            String line = br.readLine();
            while (line != null) {
                if (line.equals("Artist:")) {
                    currArtist = br.readLine();
//                    // 0 = id, 1 = index, 2 = name
//                    // split name-ID of currArtist by "@"
                    arrCurrArtist = currArtist.split("@");
//
                    // currArtists input here should be ID
                    if (!graph.containsArtist(arrCurrArtist[0])) {
                        graph.addArtist(arrCurrArtist[0], Integer.parseInt(arrCurrArtist[1]), arrCurrArtist[2]);
                    }
                } else if (line.equals("Related Artists:")) {
                    currRelArtist = br.readLine();
                    arrCurrRelArtist = currRelArtist.split("@");

                    while (!currRelArtist.equals("Artist:") && currRelArtist != null) {
                        if (!graph.containsArtist(arrCurrRelArtist[0])) {
                            graph.addArtist(arrCurrRelArtist[0], Integer.parseInt(arrCurrRelArtist[1]), arrCurrRelArtist[2]);
                            if (!graph.hasEdge(arrCurrArtist[0], arrCurrRelArtist[0])) {
                                graph.addEdge(arrCurrArtist[0], arrCurrRelArtist[0]);
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
    public LinkedList<String> runBFS(String source, String target) {
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
        LinkedList<String> finalPath = new LinkedList<>();
        // traversing through parent array
        // have to get id of name
        int startingIndex = 0;
        int endingIndex = 0;
        for (Map.Entry<String, String> entry: graph.IDToName.entrySet()) {
            if (entry.getValue().equals(source)) {
                // getting ID of source
                String firstArtistID = entry.getKey();
                // getting ID of source
                endingIndex = graph.IDToIndex.get(firstArtistID);
            }
            if (entry.getValue().equals(target)) {
                String lastArtistID = entry.getKey();
                startingIndex = graph.IDToIndex.get(lastArtistID);
            }
        }
        int currIndex = startingIndex;
        finalPath.add(target);
        while (currIndex != endingIndex) {
            //finalPath.add(graph.)
            //updating parent
            currIndex = parent[currIndex];
        }

        Collections.reverse(finalPath);
        return finalPath;

    }


}
