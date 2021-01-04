package MashupAPI;
import Entities.Country;
import Entities.DB_Connection;
import com.google.gson.Gson;
import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import spark.Spark;

import java.util.concurrent.CompletableFuture;
import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13
 * @version 2.0.0
 */


public class API_main {

    /**
     * The /country route recieves a country
     * from the client, converts it from JSON into
     * a JAVA bean using marshalling  and then forwards it
     * to be used as a search term in the Spotify-API.
     * @throws ClassNotFoundException
     */

    public API_main () throws ClassNotFoundException {

        port(3000);
        Gson gson = new Gson();

        DB_Connection connection = new DB_Connection();
        connection.setupDB();


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

        System.out.println("Spotify connection " + country.countryName);
        String URL = "https://accounts.spotify.com/api/token";
        String accessToken;

        HttpResponse<JsonNode> response = Unirest.post(URL)
                    .basicAuth("74259314b7904a7b827c730f5f7d3cd8","0e00d4fa078b449e95578569289f01fd")
                .field("grant_type","client_credentials")
                .asJson();

        accessToken = response.getBody().toString().substring(17,100);
        System.out.println(accessToken);
    }
}

