import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

public class DataCollection {
    public static void main(String[] args) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("<your_client_id>")
                .setClientSecret("<your_client_secret>")
                .setRedirectUri("<your_redirect_uri>")
                .build();
    }
}
