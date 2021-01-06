package MashupAPI;
import Entities.Country;
import Entities.DB_Connection;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import java.util.Scanner;
import java.util.Stack;
import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13
 * @version 2.0.0
 */


public class API_main {

    private DB_Connection connection;
    private Stack stack;
    private Gson gson;

    /**
     * The /country route recieves a country
     * from the client, converts it from JSON into
     * a JAVA bean using marshalling  and then forwards it
     * to be used as a search term in the Spotify-API.
     * @throws ClassNotFoundException
     */

    public API_main () throws ClassNotFoundException {

        port(3000);
        gson = new Gson();
        stack = new Stack();

        connection = new DB_Connection();
        connection.setupDB();

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));



        post("/country", (req,res)->{
                    res.type("application/json");
                    Country country = gson.fromJson(req.body(), Country.class);
                    spotifyConnection(country);

            return "Country transferred to server";
        });
    }

    /**
     * Receives the country from the post method, acquires
     * an access token to the SPOTIFY API through a POST-request
     * and then uses the token and country name to search the
     * SPOTIFY API for the correct top 50 playlist.
     * @param country
     */

    public void spotifyConnection(Country country) {

        System.out.println("Country to use in Spotify API query:  " + country.countryName);
        String URL = "https://accounts.spotify.com/api/token";

        HttpResponse<JsonNode> authRequest = Unirest.post(URL)
                    .basicAuth("74259314b7904a7b827c730f5f7d3cd8","0e00d4fa078b449e95578569289f01fd")
                .field("grant_type","client_credentials")
                .asJson();


        JSONObject jsonAuth = authRequest.getBody().getObject();
        String authString = jsonAuth.getString("access_token");

            String apiURL = "https://api.spotify.com/v1/search";

            HttpResponse <JsonNode> playlistRequest = Unirest.get(apiURL)
                    .header("Authorization", "Bearer " + authString)
                    .queryString("q", "Top 50 " + country.countryName + " charts")
                    .queryString("type", "playlist")
                    .queryString("limit", "1")
                    .asJson();


            if(playlistRequest.getBody().toString().length() < 50) {

                try {

                    Scanner scanner = new Scanner(playlistRequest.getBody().toString());
                    String playlistID;
                    scanner.useDelimiter(",");

                    while (scanner.hasNextLine() && stack.size() < 6) {
                        stack.push(scanner.next());
                    }

                    playlistID = stack.pop().toString().substring(6, 28);
                    stack.removeAllElements();
                    System.out.println(playlistID);

                    country.setCountryName(country.countryName);
                    country.setTop50Playlist(playlistID);

                    connection.addCountry(country);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else
                System.out.println("This country does not have Spotify.");
    }
}
