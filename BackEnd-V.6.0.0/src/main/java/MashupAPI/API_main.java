package MashupAPI;
import Entities.Country;
import com.google.gson.Gson;
import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13
 * @version 2.0.0
 */


public class API_main {

    private Gson gson;
    private API_Caller caller;

    /**
     * The /country route recieves a country
     * from the client, converts it from JSON into
     * a JAVA bean using marshalling  and then forwards it
     * to be used as a search term in the Spotify-API.
     *
     */

    public API_main() {

        port(3000);
        gson = new Gson();
        caller = new API_Caller();

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


        get("playlist/:country", (req, res) -> {
            res.type("application/json");
            Country queryCountry = new Country();
            queryCountry.setCountryName(req.params("country"));
            res.body(gson.toJson(caller.spotifyConnection(queryCountry)));
            return res.body();
        });

        get("information/:country", (req, res) -> {
            res.type("application/json");
            Country queryCountry = new Country();
            queryCountry.setCountryName(req.params("country"));
            res.body(gson.toJson(caller.wikipediaConnection(queryCountry)));
            return res.body();
        });
    }


}