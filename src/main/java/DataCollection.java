import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataCollection {
    Graph graph; // graph of artists, connects represent a track feature
    int size; // max size of the graph
    String currentArtist; // id of current artist
    HashMap<String, String> idToName; // maps each discovered artist id to artist name

    //String access = ;
    SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("516287a438c547dd9f8fc6695cbb029a")
            .setClientSecret("223e9ba4a1624f219835b9b8018dab7c")
            //.setAccessToken(access)
            .build();

    /**
     * Constructor. Initializes the graph that will represent the network of artists.
     * Sets the current artist to be the ID of the start artist.
     * Requests access token.
     * @param startArtist The name of the first artist the user inputted.
     * @param size The number of nodes/artists we want as an upperbound in our graph.
     */
    public DataCollection(String startArtist, int size) {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .grant_type("client_credentials").build();
        try {
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            String token = clientCredentials.getAccessToken();
            spotifyApi.setAccessToken(token);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        this.graph = new Graph(size);
        this.size = size;
        this.idToName = new HashMap<>();
        this.currentArtist = getID(startArtist);
    }

    /**
     * Finds ID of artist by searching spotify API. First artist found with a matching name
     * is selected. If no such artist is found, an IllegalArgumentException is thrown.
     * @param name The name of the start artist
     * @return The id of the start artist
     */
    String getID(String name) {
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(name).build();
        String id = "";
        try {
            Paging<Artist> artistSimplifiedPaging = searchArtistsRequest.execute();
            for (Artist artist: artistSimplifiedPaging.getItems()) {
                if (name.equals(artist.getName())) {
                    id = artist.getId();
                    idToName.put(id, name);
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

    /**
     * Finds ID of artist by searching spotify API. First artist found with a matching name
     * is selected. If no such artist is found, an IllegalArgumentException is thrown.
     * @param id The id of the artist
     * @return The id of the artist
    String getName(String id) {
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(id).build();
        String name = "";
        try {
            Paging<Artist> artistSimplifiedPaging = searchArtistsRequest.execute();
            for (Artist artist: artistSimplifiedPaging.getItems()) {
                if (id.equals(artist.getId())) {
                    name = artist.getName();
                    idToName.put(id, name);
                    break;
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("Artist id does not match any found artist.");
        }
        return name;
    }
     */

    /**
     * Method uses Spotify API to build graph of artists. Loops through the
     * following until the number of artists discovered reaches the limiting size
     * of the graph: For the current artist, finds all of their albums, all of these
     * albums' tracks, and all the artists featured on these tracks. These artists
     * are added to the graph if not previously discovered. And edge is added between
     * the current artist and all the discovered artists.
     *
     * @return Graph with nodes being the ids of discovered artist and an
     * edge representing a track feature (direction of feature disregarded)
     */
    public Graph buildGraph() {
        //try {
            //BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
            Queue<String> queue = new LinkedList<>();
            queue.add(currentArtist);
            graph.addArtist(currentArtist, 0, idToName.get(currentArtist));

            int currentIndex = 1;
            while (currentIndex < size - 1  || !queue.isEmpty()) {
                currentArtist = queue.poll();
                Collection<String> albumIDs = getAlbumIDs();
                for (String albumID : albumIDs) {
                    Collection<String> artistIDs = getArtistIDs(albumID);
                    for (String artist : artistIDs) {
                        if (! graph.containsArtist(artist) && currentIndex < size) {
                            graph.addArtist(artist, currentIndex, idToName.get(artist));
                            currentIndex++;
                            queue.add(artist);
                            graph.addEdge(currentArtist, artist);
                            //writer.write(currentArtist + " " + artist);
                            //writer.newLine();
                            //System.out.println(currentArtist + " " + artist);
                        }
                    }
                }
            }
            //writer.close();
            //writer.flush();

        //} catch (Exception e) {
            //System.out.println("No data file found.");
        //}
        printGraph();
        writeGraph();
        return graph;
    }

    /**
     * @return Collection of all the album ids of the current artist
     */
    Collection<String> getAlbumIDs() {
        GetArtistsAlbumsRequest getArtistsAlbumsRequest = spotifyApi.getArtistsAlbums(currentArtist).build();
        Collection<String> albumIDs = new ArrayList<>();
        try {
            Paging<AlbumSimplified> albumSimplifiedPaging = getArtistsAlbumsRequest.execute(); // bottleneck
            for (AlbumSimplified albumSimplified : albumSimplifiedPaging.getItems()) {
                albumIDs.add(albumSimplified.getId());
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return albumIDs;
    }

    /**
     * Returns all artists associated with a given album by finding all tracks of an album
     * and then finding all artists associated with these tracks. Also adds Entry to idToName
     * for every artist discovered.
     * @param albumID the ID string of an album
     * @return A collection of the artist IDs
     */
    Collection<String> getArtistIDs(String albumID) {
        GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumID).build();
        Collection<String> artistIDs = new ArrayList<>();
        try {
            Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();
            for (TrackSimplified trackSimplified : trackSimplifiedPaging.getItems()) {
                for (ArtistSimplified artistSimplified : trackSimplified.getArtists()) {
                    String id = artistSimplified.getId();
                    artistIDs.add(id);
                    idToName.put(id, artistSimplified.getName());
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return artistIDs;
    }

    /**
     * Method prints out graph built for given start artist for testing purposes.
     */
    void printGraph() {
        for (int i  = 0; i < graph.vertices.length; i++) {
            System.out.print("Artist " + i + ": "
                    + idToName.get(graph.ids[i]) + "\n Related Artists:");
            for (String artist : graph.vertices[i]) {
                System.out.println(idToName.get(artist));
            }
        }
    }

    /**
     * Method called in buildGraph(). Writes graph onto data.txt file.
     */
    void writeGraph() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("files/data.txt"));
            for (int i = 0; i < graph.vertices.length; i++) {
                writer.write("Artist:");
                writer.newLine();
                // write the id of the Artist
                writer.write(graph.ids[i]);
                writer.write("@");
                // write the index of the Artist with the id
                writer.write("" + graph.IDToIndex.get(graph.ids[i]));
                writer.write("@");
                // write the artist name
                writer.write(graph.IDToName.get(graph.ids[i]));
                writer.newLine();
                writer.write("Related Artists:");
                writer.newLine();
                for (String artist : graph.vertices[i]) {
                    writer.write(artist);
                    writer.write("@");
                    writer.write("" + graph.IDToIndex.get(artist));
                    writer.write("@");
                    writer.write(graph.IDToName.get(artist));
                    writer.newLine();
                }
            }
            writer.close();
            writer.flush();
        } catch (Exception e) {
            System.out.println("No data file found.");
        }
    }
}
