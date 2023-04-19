import se.michaelthelin.spotify.*;
import java.net.URI;
import java.net.URISyntaxException;

public class DataCollection {

    String startArtist;
    Graph graph;
    int size;

    // graph initialized by start artist and how many nodes we want to limit our graph by
    public DataCollection(String startArtist, int size) {
        this.startArtist = startArtist;
        this.graph = new Graph(size);
        this.size = size;
    }

    public Graph buildGraph() {
        // will search until size number of artists are discovered - some counter variable
        // current artist indexed by current counter value
        // edges added accordingly

        // loop: current artist, get albums, get album tracks, get artists
        return graph;
    }

    // TO-DO delete main method
    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("https://www.cis.upenn.edu/");
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("<your_client_id>")
                .setClientSecret("<your_client_secret>")
                .setRedirectUri(uri)
                .build();


    }
}
