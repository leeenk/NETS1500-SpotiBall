import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class DataCollection {

    private static final String accessToken = "BQBNPieapJQ9PQtEQ2vH1AQSCoxhbslWeWOZVmtvRwco77l61Yrp7vMyJUMWOIMKatfdG7T5yZpG3ZTIivCe0uAnDIuNzpMSwhnDAXVFyO_UQjEa9B8Q";
    private static final String artistOneId = "2FXC3k01G6Gw61bmprjgqS?si=Op7CMfI2QrqXmy3G4YJRdg";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();
    private static final GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistOneId)
            .build();


    public static void getArtist_Sync() {
        try {
            final Artist artist = getArtistRequest.execute();

            System.out.println("Name: " + artist.getName());
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public static void getArtist_Async() {
        try {
            final CompletableFuture<Artist> albumFuture = getArtistRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Artist artist = albumFuture.join();

            System.out.println("Name: " + artist.getName());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }


    public static void main(String[] args) {
//        try {
//            SpotifyApi spotifyApi = new SpotifyApi.Builder()
//                    .setClientId("4178e4fadaa84a568ff86645b5e7422e")
//                    .setClientSecret("0524427f150b41b2b3dfc64da0e87e95")
//                    .setRedirectUri(new URI("http://localhost:3000"))
//                    .build();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

        getArtist_Sync();
        getArtist_Async();


    }
}
