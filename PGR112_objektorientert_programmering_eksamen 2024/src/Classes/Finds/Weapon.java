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

public class Weapon extends Find {
    private String type;
    private String material;
    private int weight;

    public Weapon(int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum, String type, String material, int weight) {
        super(id, findingLocation, discoverer, discoveryDate, assumedDateOfOrigin, museum);
        this.type = type;
        this.material = material;
        this.weight = weight;
    }

    public static Weapon readWeaponSpecificFile(Scanner fileInput, int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum) {
        String type = fileInput.nextLine();
        String material = fileInput.nextLine();
        String weight = fileInput.nextLine();

        Weapon weapon = new Weapon(id, findingLocation, discoverer, discoveryDate, assumedDateOfOrigin, museum, type, material, Integer.parseInt(weight));
        return weapon;
    }

    public boolean checkIfExists() {
        try {
            String query = """
                    SELECT Id FROM Vaapen WHERE Id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.getId());
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                result.close();
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to check if the weapon already exists: \{this.getId()}", e);
        }
        return false;
    }

    public void insertIntoDatabase() {
        try {
            String query = """
                    INSERT INTO Vaapen (Id, Funnsted, Finner_id, Funntidspunkt, Antatt_årstall, Museum_id, Type, Materiale, Vekt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
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
            preparedStatement.setString(8, this.getMaterial());
            preparedStatement.setInt(9, this.getWeight());

            preparedStatement.executeUpdate();

            preparedStatement.close();

        }catch (SQLException e) {
            throw new RuntimeException(STR."Failed to insert the weapon to the database: \{this.getId()}", e);
        }
    }

    public static ArrayList<Weapon> getAll() {
        try {
            ArrayList<Weapon> weapons = new ArrayList<>();
            String query = """
                    SELECT * FROM Vaapen;
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
                String material = result.getString("Materiale");
                int weight = result.getInt("Vekt");

                Weapon weapon = new Weapon(id, findingLocation, Person.getPersonById(discoverer), discoveryDate, assumedDateOfOrigin, Museum.getMuseumById(museum), type, material, weight);
                weapons.add(weapon);
            }
            result.close();
            return weapons;
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get all weapons: " ,e);
        }
    }

    public static ArrayList<Weapon> getSpecificYears(int fromYear) {
        try {
            ArrayList<Weapon> weapons = new ArrayList<>();
            String query = """
                SELECT * FROM Vaapen WHERE Antatt_årstall >= ?;
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
                String material = result.getString("Materiale");
                int weight = result.getInt("Vekt");

                Weapon weapon = new Weapon(id, findingLocation, Person.getPersonById(discoverer), discoveryDate, assumedDateOfOrigin, Museum.getMuseumById(museum), type, material, weight);
                weapons.add(weapon);
            }
            result.close();
            return weapons;
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get the specific year of weapons from the database: ", e);
        }
    }

    public String getType() {
        return type;
    }

    public String getMaterial() {
        return material;
    }

    public int getWeight() {
        return weight;
    }
}
