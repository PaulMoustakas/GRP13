package MashupAPI;

import Entities.Country;
import Entities.DB_Connection;
import com.google.gson.Gson;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import jdk.dynalink.beans.StaticClass;
import kong.unirest.HttpResponse;

import static spark.Spark.*;

/**
 * Proj X - API mashup
 * Spotify <--> Google maps
 * @author GRP 13
 * @version 2.0.0
 */


public class API_main {

    private DB_Connection connection;

    public API_main () throws ClassNotFoundException {
        port(3000);
        Gson gson = new Gson();

        connection = new DB_Connection();
        connection.setupDB();

        post("/country", (req,res)->{
                    res.type("application/json");
                    System.out.println(req.body() + " req body");
                    System.out.println(req.body() + " response body");
                    Country country = gson.fromJson(req.body(), Country.class);
                    connection.addCountry(country);
                    spotifyConnection();

            return "Country transferred to server";
        });

    }


    public void spotifyConnection() {
        System.out.println("Spotify connection " + connection.getCountry().countryName);
        

    }
}
