package MashupAPI;
import Entities.Country;
import com.google.gson.Gson;
import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13, Malmö Universitet - Webbtjänster DA159A & DA358A
 * @version 6.0.0
 */

public class API_main {

    private Gson gson;
    private API_Caller caller;


    API_main() {
        port(3000); // PORT
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
        System.out.println("API IS RUNNING");

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