import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains API for undirected graph.
 * NOTE: add methods necessary for BFS
 */
public class Graph {

    List<String>[] vertices;
    // maps artist name to their index in the adjacency list.
    HashMap<String, Integer> namesToIndex;

    /**
     * Constructor for graph object.
     * @param n number of nodes in our graph
     */
    public Graph(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of vertices must be positive");
        }
        this.vertices = new List[n];
        int i = 0;
        while (i < n) {
            vertices[i] = new LinkedList<>();
            i++;
        }
        this.namesToIndex = new HashMap<>();
    }

    public void addArtist(String name, int index) {
        namesToIndex.put(name, index);
    }

    // helper that checks if an edge exists between two artists so that not double added
    public boolean hasEdge(String artist1, String artist2) {
        // must ensure that both artists already assigned an index
        Integer u = namesToIndex.get(artist1);
        Integer v = namesToIndex.get(artist2);
        if (u == null || v == null) {
            throw new IllegalArgumentException("One of inputted artists does not exist in graph.");
        }
        List<String> relatedArtists = vertices[u];
        return relatedArtists.contains(v);
    }

    // adds undireced edge to graph between two artists
    public boolean addEdge(String artist1, String artist2) {
        if (hasEdge(artist1, artist2)) {
            return false;
        }
        Integer u = namesToIndex.get(artist1);
        Integer v = namesToIndex.get(artist2);
        if (u == null || v == null) {
            throw new IllegalArgumentException("One of inputted artists does not exist in graph.");
        } else if (u.equals(v)) {
            return false;
        }
        List<String> relatedArtists1 = vertices[u];
        List<String> relatedArtists2 = vertices[v];
        relatedArtists1.add(artist2);
        relatedArtists2.add(artist1);
        return true;
    }
}
