package Entities;
import javax.swing.*;
import java.sql.*;

public class DB_Connection {

    private Connection connection = null;


    public DB_Connection () throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MashupAPIDatabase?user=root&password=" + "bubba1bobo2");
            } catch (Exception e) {
                e.printStackTrace();


            }

    }

    @Override
    protected void finalize() throws Throwable {
        connection.close();
    }

    public void setupDB () {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS Country");
            statement.executeUpdate("CREATE TABLE Country (countryID varchar(30), playlistID varchar (29)" + ",PRIMARY KEY (countryID,playlistID))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCountry (Country mapCountry) {

        try

        {
            Statement statement = connection.createStatement();
            String sqlStatement = "INSERT INTO Country (countryID, playlistID)"
                    + "VALUES ('" + mapCountry.countryName + "', "
                    +  "'" + mapCountry.top50Playlist + "')";
            statement.executeUpdate(sqlStatement);
            statement.close();

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    public Country getCountry () {

        Country country = new Country();
        String countryName;

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT* FROM Country");
            if (rs.next()) {

                countryName = rs.getString("countryID");
                country.setCountryName(countryName);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return country;
    }
}
