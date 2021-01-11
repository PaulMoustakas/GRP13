package Entities;

/**
 * Class represents a JSON-object
 * containing Top 50 song and information
 * relating to a requested country
 * @author GRP 13, Malmö Universitet - Webbtjänster DA159A & DA358A
 * @version 2.0.0
 */
public class Country {

    private String countryName = "";
    private String top50Playlist = "";
    private String wikiText = "";

    public Country () {
    }

    public String getWikiText() {
        return wikiText;
    }

    public void setWikiText(String wikiText) {
        this.wikiText = wikiText;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getTop50Playlist() {
        return top50Playlist;
    }

    public void setTop50Playlist(String top50Playlist) {
        this.top50Playlist = top50Playlist;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
