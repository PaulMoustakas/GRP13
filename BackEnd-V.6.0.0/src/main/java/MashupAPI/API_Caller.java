package MashupAPI;

import Entities.Country;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.util.Scanner;
import java.util.Stack;

public class API_caller {

    public Country wikipediaConnection (Country queryCountry) {

        System.out.println("Country to use in Wikipedia API query: " + queryCountry.getCountryName());
        String URL = "http://en.wikipedia.org/w/api.php";

        HttpResponse<JsonNode> wikiRequest = Unirest.get(URL)
                .queryString("format", "json")
                .queryString("action", "query")
                .queryString("prop", "extracts")
                .queryString("exlimit", "max")
                .queryString("explaintext", "")
                .queryString("exintro", "")
                .queryString("titles", queryCountry.getCountryName())
                .asJson();

        try {
            Stack<String> stack = new Stack<>();
            Scanner scanner = new Scanner(wikiRequest.getBody().toString());
            String wikiText;
            scanner.useDelimiter("extract");

            while (scanner.hasNextLine() && stack.size() < 2) {
                stack.push(scanner.next());
            }

            wikiText = stack.pop();
            queryCountry.setWikiText(wikiText.substring(3, wikiText.length()-17));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return queryCountry;
    }

    public Country spotifyConnection(Country queryCountry) {

        System.out.println("Country to use in Spotify API query: " + queryCountry.getCountryName());
        String URL = "https://accounts.spotify.com/api/token";

        HttpResponse<JsonNode> authRequest = Unirest.post(URL)
                .basicAuth("74259314b7904a7b827c730f5f7d3cd8", "0e00d4fa078b449e95578569289f01fd")
                .field("grant_type", "client_credentials")
                .asJson();

        System.out.println(authRequest.getBody());
        JSONObject jsonAuth = authRequest.getBody().getObject();
        String authString = jsonAuth.getString("access_token");

        String apiURL = "https://api.spotify.com/v1/search";

        HttpResponse<JsonNode> playlistRequest = Unirest.get(apiURL)
                .header("Authorization", "Bearer " + authString)
                .queryString("q", "Top 50 " + queryCountry.getCountryName() + " charts")
                .queryString("type", "playlist")
                .queryString("limit", "1")
                .asJson();


        if (playlistRequest.getBody().toString().length() >= 15) {
            try {
                Stack<String> stack = new Stack<>();
                Scanner scanner = new Scanner(playlistRequest.getBody().toString());
                String playlistID;
                scanner.useDelimiter(",");

                while (scanner.hasNextLine() && stack.size() < 6) {
                    stack.push(scanner.next());
                }

                playlistID = stack.pop().substring(6, 28);

                System.out.println(playlistID);
                queryCountry.setTop50Playlist(playlistID);

            } catch (Exception e) {
                queryCountry.setTop50Playlist("undefined");
                System.err.println("Not a valid country Exeption | API_main | Row 122 ");
            }
        }
        return queryCountry;

    }
}
