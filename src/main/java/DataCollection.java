
//import org.apache.hc.core5.http.ParseException;
//import se.michaelthelin.spotify.SpotifyApi;
//import se.michaelthelin.spotify.SpotifyHttpManager;
//import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
//import se.michaelthelin.spotify.model_objects.specification.Artist;
//import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
//import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.concurrent.CancellationException;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.CompletionException;
//
//
//
//public class DataCollection {
//
//    private static final String accessToken = "BQBNPieapJQ9PQtEQ2vH1AQSCoxhbslWeWOZVmtvRwco77l61Yrp7vMyJUMWOIMKatfdG7T5yZpG3ZTIivCe0uAnDIuNzpMSwhnDAXVFyO_UQjEa9B8Q";
//    private static final String artistOneId = "2FXC3k01G6Gw61bmprjgqS?si=Op7CMfI2QrqXmy3G4YJRdg";
//
//    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
//            .setAccessToken(accessToken)
//            .build();
//    private static final GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistOneId)
//            .build();
//
//
//    public static void getArtist_Sync() {
//        try {
//            final Artist artist = getArtistRequest.execute();
//
//            System.out.println("Name: " + artist.getName());
//        } catch (IOException | ParseException | SpotifyWebApiException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//
//    public static void getArtist_Async() {
//        try {
//            final CompletableFuture<Artist> albumFuture = getArtistRequest.executeAsync();
//
//            // Thread free to do other tasks...
//
//            // Example Only. Never block in production code.
//            final Artist artist = albumFuture.join();
//
//            System.out.println("Name: " + artist.getName());
//        } catch (CompletionException e) {
//            System.out.println("Error: " + e.getCause().getMessage());
//        } catch (CancellationException e) {
//            System.out.println("Async operation cancelled.");
//        }
//    }
//
//
//    public static void main(String[] args) {
////        try {
////            SpotifyApi spotifyApi = new SpotifyApi.Builder()
////                    .setClientId("4178e4fadaa84a568ff86645b5e7422e")
////                    .setClientSecret("0524427f150b41b2b3dfc64da0e87e95")
////                    .setRedirectUri(new URI("http://localhost:3000"))
////                    .build();
////        } catch (URISyntaxException e) {
////            e.printStackTrace();
////        }
////    }
//
//        getArtist_Sync();
//        getArtist_Async();
// }
//


import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class DataCollection {

    String startArtist;
    String endArtist;
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
    public DataCollection(String startArtist, String endArtist, int size) {
        this.startArtist = startArtist;

        // set to ID of start artist
        this.currentArtist = "";
        this.endArtist = endArtist;
        this.graph = new Graph(size);
        this.size = size;
        try {
            this.uri = new URI("https://www.cis.upenn.edu/");
        } catch (Exception e) {
            System.out.print("Redirect URI invalid.");
        }
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
        } catch (IOException | SpotifyWebApiException |
                 ParseException e) {
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
        } catch (IOException | SpotifyWebApiException |
                 ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return trackIDs;
    }
}
