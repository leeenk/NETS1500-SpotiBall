import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BFS {

    Graph graph;
    private ArrayList<String> queue;
    private ArrayList<String> discovered;
    private HashMap<Integer, String> indexToName;
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
        indexToName = new HashMap<>();
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
                while (!currRelArtist.equals("Artist:")) {
                    String[] arrCurrRelArtist = currRelArtist.split("@");
                    if (!graph.containsArtist(arrCurrRelArtist[0])) {
                        graph.addArtist(arrCurrRelArtist[0], Integer.parseInt(arrCurrRelArtist[1]), arrCurrRelArtist[2]);
                    }
                    if (!graph.hasEdge(arrCurrArtist[0], arrCurrRelArtist[0])) {
                        graph.addEdge(arrCurrArtist[0], arrCurrRelArtist[0]);
                    }
                    currRelArtist = br.readLine();
                    if (currRelArtist == null) {
                        currArtist = null;
                        break;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // source is given as ID
    int[] findParentArray(String source) {
        int n = graph.size;
        boolean[] discovered = new boolean[n];
        int i = 0;
        while (i < n) {
            discovered[i] = false;
            i++;
        }
        int[] parent = new int[n];
        Queue<Integer> queue = new LinkedList<>();
        int s = graph.IDToIndex.get(source);
        queue.add(s);
        discovered[s] = true;

        while (queue.size() != 0) {
            int curr = queue.remove();
            for (String relatedArtist : graph.vertices[curr]) {
                int rel = graph.IDToIndex.get(relatedArtist);
                if (!discovered[rel]) {
                    discovered[rel] = true;
                    queue.add(rel);
                    parent[rel] = curr;
                }
            }
        }
        return parent;
    }

    public List<String> run(String source, String target) {
        String sourceID = graph.nameToID.get(source);
        String targetID = graph.nameToID.get(target);
        int sourceIndex = graph.IDToIndex.get(sourceID);
        int targetIndex = graph.IDToIndex.get(targetID);
        int[] parentArray = findParentArray(sourceID);
        List<String> path = pathFinder(parentArray, targetIndex, sourceIndex);
        return path;
    }

    List<String> pathFinder(int[] parents, int t, int s) {
        ArrayList<String> path = new ArrayList<>();
        String id = graph.ids[t];
        String name = graph.IDToName.get(id);
        path.add(name);
        int curr = t;
        while (curr != s) {
            int prev = parents[curr];
            id = graph.ids[prev];
            name = graph.IDToName.get(id);
            path.add(0, name);
            curr = prev;
        }
        return path;
    }
}