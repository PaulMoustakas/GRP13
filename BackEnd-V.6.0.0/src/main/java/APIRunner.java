
import Entities.Country;
import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import kong.unirest.HttpResponse;
import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13
 * @version 2.0.0
 */


public class APIRunner {

    /**
     * Necessary to bypass CORS-FILTER.
     * Found on github, dont fully understand it.
     */

    public static void main(String[] args) {
        port(3000);
        APIRunner runner = new APIRunner();
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

        runner.getCountry();
    }

    /**
     * Retrieves a country from the client, converts it into a
     * Country object bean using marshalling, and sends it
     * to the spotifyConnection method to be used as a search
     * term in the Spotify-API.
     */

    public void getCountry () {
        post("/country", (req,res)->{
            res.type("application/json");
            System.out.println(req.body() + " req body");
            System.out.println(req.body() + " response body");
            Country country = new Gson().fromJson(req.body(), Country.class);
            spotifyConnection(country);
            return "Country transferred to server";
        });
    }


    /**
     * WORK IN PROGRESS
     * @param queryCountry
     * @return
     */


    public String spotifyConnection (Country queryCountry){
        System.out.println("Spotify connection " + queryCountry.toString());
        String url = "http://api.spotify.com/v1/search";

        HttpResponse<JsonNode> spotifyResponse;

        try {
            spotifyResponse = (HttpResponse<JsonNode>) Unirest.get(url)
                    .header("accept", "application/json")
                    .queryString("query", queryCountry.toString())
                    .queryString("type", "track");

            System.out.println(spotifyResponse.getBody().getObject().getJSONObject("tracks")
                    .getJSONArray("items").getJSONObject(0).getString("preview_url"));

            return spotifyResponse.getBody().getObject().getJSONObject("tracks")
                    .getJSONArray("items").getJSONObject(0).getString("preview_url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

