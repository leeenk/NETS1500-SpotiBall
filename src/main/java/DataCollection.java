import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class DataCollection {
    Graph graph;
    int size;
    String currentArtist;
    URI uri;
    // Does this work instead of using access token?
    SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("<your_client_id>")
            .setClientSecret("<your_client_secret>")
            .setRedirectUri(uri)
            .build();

    // graph initialized by start artist and how many nodes we want to limit our graph by
    public DataCollection(String startArtist, int size) {
        // set to ID of start artist
        this.currentArtist = getID(startArtist);
        this.graph = new Graph(size);
        this.size = size;
        try {
            this.uri = new URI("https://www.cis.upenn.edu/");
        } catch (Exception e) {
            System.out.print("Redirect URI invalid.");
        }
    }

    String getID(String name) {
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(name).build();
        String id = "";
        try {
            Paging<Artist> artistSimplifiedPaging = searchArtistsRequest.execute();
            for (Artist artist: artistSimplifiedPaging.getItems()) {
                if (name.equals(artist.getName())) {
                    id = artist.getId();
                    break;
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (id.equals("")) {
            throw new IllegalArgumentException("Artist name does not match any found artist.");
        }
        return id;
    }


    // will search until size number of artists are discovered - some counter variable
    // current artist indexed by current counter value
    // edges added accordingly

    // loop: current artist, get albums, get album tracks, get artists
    /**
     *
     * Method uses Web API to build graph connecting artists.
     * @return
     */
    public Graph buildGraph() {
        Queue<String> queue = new LinkedList<>();
        queue.add(currentArtist);
        graph.addArtist(currentArtist, 1);
        int currentIndex = 2;
        while (currentIndex < size || !queue.isEmpty()) {
            currentArtist = queue.poll();
            Collection<String> albumIDs = getAlbumIDs();
            for (String albumID : albumIDs) {
                Collection<String> artistIDs = getArtistIDs(albumID);
                for (String artist : artistIDs) {
                    if (! graph.containsArtist(artist)) {
                        graph.addArtist(artist, currentIndex);
                        currentIndex++;
                        queue.add(artist);
                    }
                    graph.addEdge(currentArtist, artist);
                }
            }
        }
        return graph;
    }

    /**
     * @return Collection of all the album ids of the current artist
     */
    Collection<String> getAlbumIDs() {
        GetArtistsAlbumsRequest getArtistsAlbumsRequest = spotifyApi.getArtistsAlbums(currentArtist).build();
        Collection<String> albumIDs = new ArrayList<>();
        try {
            Paging<AlbumSimplified> albumSimplifiedPaging = getArtistsAlbumsRequest.execute();
            for (AlbumSimplified albumSimplified : albumSimplifiedPaging.getItems()) {
                albumIDs.add(albumSimplified.getId());
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return albumIDs;
    }

    Collection<String> getArtistIDs(String albumID) {
        GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumID).build();
        Collection<String> trackIDs = new ArrayList<>();
        try {
            Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();
            for (TrackSimplified trackSimplified : trackSimplifiedPaging.getItems()) {
                for (ArtistSimplified artistSimplified : trackSimplified.getArtists()) {
                    // NOTE also have the option to add name
                    trackIDs.add(artistSimplified.getId());
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return trackIDs;
    }
}
