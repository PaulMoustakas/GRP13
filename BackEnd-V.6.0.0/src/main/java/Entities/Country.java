package Entities;

public class Country {

    public String countryName = "";
    public String top50Playlist = "";

    public Country () {
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
