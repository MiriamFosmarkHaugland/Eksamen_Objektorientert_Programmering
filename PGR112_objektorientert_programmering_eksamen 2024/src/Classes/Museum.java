package Classes;

import Database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Museum {
    private int id;
    private String name;
    private String location;

    public Museum(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static void readMuseumFile() {
        try {
            File file = new File("funn.txt");
            Scanner fileInput = new Scanner(file);

            // Indicates to skip the next line, usually done when finding a section title, and wanting to skip the amount in that section.
            boolean skipNextLine = false;

            // Indicates if we are past the "Personer" section, in that case, we know we're at the "Museer" section.
            boolean pastPeople = false;

            while (fileInput.hasNextLine()) {
                String id = fileInput.nextLine();

                if(skipNextLine) {
                    skipNextLine = false;
                    continue;
                }

                if (id.contains("Museer:")) {
                    pastPeople = true;
                    skipNextLine = true;
                    continue;
                } else if (id.contains("Funn:")) {
                    break;
                }

                if (!pastPeople) {
                    continue;
                }

                String name = fileInput.nextLine();
                String location = fileInput.nextLine();

                Museum museum = new Museum(Integer.parseInt(id), name, location);
                if (!museum.checkIfMuseumIdExists()) {
                    museum.insertMuseumToDatabase();
                }
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find the file: ", e);
        }
    }

    public boolean checkIfMuseumIdExists() {
        try {
            String query = """
                    SELECT Id FROM Museum WHERE Id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                result.close();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to check if museum already exists \{this.getId()}, \{this.getName()}, \{this.getLocation()}");
        }
        return false;
    }

    public void insertMuseumToDatabase() {
        try {
            String query = """
                    INSERT INTO Museum (Id, Navn, Sted) VALUES (?, ?, ?);
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.getId());
            preparedStatement.setString(2, this.getName());
            preparedStatement.setString(3, this.getLocation());

            preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to insert museum to the database: \{this.getId()}, \{this.getName()}, \{this.getLocation()}", e);
        }
    }

    public static Museum getMuseumById(int id) {
        try {
            String query = """
                    SELECT * FROM Museum WHERE id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                int museumId = result.getInt("Id");
                String museumName = result.getString("Navn");
                String museumLocation = result.getString("Sted");

                Museum museum = new Museum(museumId, museumName, museumLocation);
                result.close();
                return museum;
            }
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get museum information: \{id}", e);
        }
        return null;
    }

        public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
