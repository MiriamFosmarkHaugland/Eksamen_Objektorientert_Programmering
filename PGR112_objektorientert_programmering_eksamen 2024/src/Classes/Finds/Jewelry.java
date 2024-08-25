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

public class Jewelry extends Find {
    private String type;
    private int expectedValue;
    private String fileName;

    public Jewelry(int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum, String type, int expectedValue, String fileName) {
        super(id, findingLocation, discoverer, discoveryDate, assumedDateOfOrigin, museum);
        this.type = type;
        this.expectedValue = expectedValue;
        this.fileName = fileName;
    }

    public static Jewelry readJewelrySpecificFile(Scanner fileInput, int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum) {
        String type = fileInput.nextLine();
        String expectedValue = fileInput.nextLine();
        String filename = fileInput.nextLine();

        Jewelry jewelry = new Jewelry(id, findingLocation, discoverer, discoveryDate, assumedDateOfOrigin, museum, type, Integer.parseInt(expectedValue), filename);
        return jewelry;
    }

    public boolean checkIfExists() {
        try {
            String query = """
                    SELECT Id FROM Smykke WHERE Id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.getId());
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                result.close();
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to check if the jewelry already exists: \{this.getId()}", e);
        }
        return false;
    }

    public void insertIntoDatabase() {
        try {
            String query = """
                    INSERT INTO Smykke (Id, Funnsted, Finner_id, Funntidspunkt, Antatt_årstall, Museum_id, Type, Verdiestimat, filnavn) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
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
            preparedStatement.setString(7, this.getType());
            preparedStatement.setInt(8, this.getExpectedValue());
            preparedStatement.setString(9, this.getFileName());

            preparedStatement.executeUpdate();

            preparedStatement.close();

        }catch (SQLException e) {
            throw new RuntimeException(STR."Failed to insert the jewelry to the database: \{this.getId()}", e);
        }
    }

    public static ArrayList<Jewelry> getAll() {
        try {
            ArrayList<Jewelry> jewelries = new ArrayList<>();
            String query = """
                    SELECT * FROM Smykke;
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
                String type = result.getString("Type");
                int expectedValue = result.getInt("Verdiestimat");
                String fileName = result.getString("filnavn");

                Jewelry jewelry = new Jewelry(id, findingLocation, Person.getPersonById(discoverer), discoveryDate, assumedDateOfOrigin, Museum.getMuseumById(museum), type, expectedValue, fileName);
                jewelries.add(jewelry);
            }
            result.close();
            return jewelries;
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get all jewelries: " ,e);
        }
    }

    public static ArrayList<Jewelry> getSpecificYears(int fromYear) {
        try {
            ArrayList<Jewelry> jewelries = new ArrayList<>();
            String query = """
                SELECT * FROM Smykke WHERE Antatt_årstall >= ?;
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
                String type = result.getString("Type");
                int expectedValue = result.getInt("Verdiestimat");
                String fileName = result.getString("filnavn");

                Jewelry jewelry = new Jewelry(id, findingLocation, Person.getPersonById(discoverer), discoveryDate, assumedDateOfOrigin, Museum.getMuseumById(museum), type, expectedValue, fileName);
                jewelries.add(jewelry);
            }
            result.close();
            return jewelries;
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get the specific year of jewelries from the database: ", e);
        }
    }

    public String getType() {
        return type;
    }

    public int getExpectedValue() {
        return expectedValue;
    }

    public String getFileName() {
        return fileName;
    }
}
