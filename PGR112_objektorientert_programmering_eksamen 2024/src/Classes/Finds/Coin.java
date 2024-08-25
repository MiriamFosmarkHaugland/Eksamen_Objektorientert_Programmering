package Classes.Finds;

import Classes.Museum;
import Classes.Person;
import Database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;

public class Coin extends Find {
    private int diameter;
    private String metal;

    public Coin(int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum, int diameter, String metal) {
        super(id, findingLocation, discoverer, discoveryDate, assumedDateOfOrigin, museum);
        this.diameter = diameter;
        this.metal = metal;
    }

    public static Coin readCoinSpecificFile(Scanner fileInput, int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum) {
        String diameter = fileInput.nextLine();
        String metal = fileInput.nextLine();

        Coin coin = new Coin(id, findingLocation, discoverer, discoveryDate, assumedDateOfOrigin, museum, Integer.parseInt(diameter), metal);
        return coin;
    }

    public boolean checkIfExists() {
        try {
            String query = """
                    SELECT Id FROM Mynt WHERE Id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.getId());
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                result.close();
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to check if the coin already exists: \{this.getId()}", e);
        }
        return false;
    }

    public void insertIntoDatabase() {
        try {
            String query = """
                    INSERT INTO Mynt (Id, Funnsted, Finner_id, Funntidspunkt, Antatt_årstall, Museum_id, Diameter, Metall) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.getId());
            preparedStatement.setString(2, this.getFindingLocation());
            preparedStatement.setInt(3, this.getDiscoverer().getId());
            preparedStatement.setString(4, this.getDiscoveryDate());
            preparedStatement.setInt(5, this.getAssumedDateOfOrigin());
            if (this.getMuseum() == null) {
                preparedStatement.setNull(6, Types.NULL);
            } else {
                preparedStatement.setInt(6, this.getMuseum().getId());
            }
            preparedStatement.setInt(7, this.getDiameter());
            preparedStatement.setString(8, getMetal());

            preparedStatement.executeUpdate();

            preparedStatement.close();

        }catch (SQLException e) {
            throw new RuntimeException(STR."Failed to insert the coin to the database: \{this.getId()}", e);
        }
    }

    public static ArrayList<Coin> getAll() {
        try {
            ArrayList<Coin> coins = new ArrayList<>();
            String query = """
                    SELECT * FROM Mynt;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                int id = result.getInt("Id");
                String findingLocation = result.getString("Funnsted");
                int discoverer = result.getInt("Finner_id");
                String discoveryDate = result.getString("Funntidspunkt");
                int assumedDateOfOrigin = result.getInt("Antatt_årstall");
                int museum = result.getInt("Museum_id");
                int diameter = result.getInt("Diameter");
                String metal = result.getString("Metall");

                Coin coin = new Coin(id, findingLocation, Person.getPersonById(discoverer), discoveryDate, assumedDateOfOrigin, Museum.getMuseumById(museum), diameter, metal);
                coins.add(coin);
            }
            result.close();
            return coins;
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get all coins: " ,e);
        }
    }

    public static ArrayList<Coin> getSpecificYears(int fromYear) {
        try {
            ArrayList<Coin> coins = new ArrayList<>();
            String query = """
                SELECT * FROM Mynt WHERE Antatt_årstall >= ?;
                """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, fromYear);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                int id = result.getInt("Id");
                String findingLocation = result.getString("Funnsted");
                int discoverer = result.getInt("Finner_id");
                String discoveryDate = result.getString("Funntidspunkt");
                int assumedDateOfOrigin = result.getInt("Antatt_årstall");
                int museum = result.getInt("Museum_id");
                int diameter = result.getInt("Diameter");
                String metal = result.getString("Metall");

                Coin coin = new Coin(id, findingLocation, Person.getPersonById(discoverer), discoveryDate, assumedDateOfOrigin, Museum.getMuseumById(museum), diameter, metal);
                coins.add(coin);
            }
            result.close();
            return coins;
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get the specific year of coins from the database: ", e);
        }
    }
    public int getDiameter() {
        return diameter;
    }

    public String getMetal() {
        return metal;
    }
}
