package Entities;

public class Country {

    private String country = "";

    public Country (String country) {
        this.country = country;
    }

    public Country () {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
