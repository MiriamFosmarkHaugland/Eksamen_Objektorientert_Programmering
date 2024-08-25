package Classes;

import Database.Database;

import java.io.File;
import java.io.FileNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Person {
    private int id;
    private String name;
    private String phone;
    private String email;

    public Person(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public static void readPersonFile() {
        try {
            File file = new File("funn.txt");
            Scanner fileInput = new Scanner(file);
            boolean skipNextLine = false;

            while (fileInput.hasNextLine()) {
                String id = fileInput.nextLine();

                if(skipNextLine) {
                    skipNextLine = false;
                    continue;
                }

                if (id.contains("Personer:")) {
                    // We want to skip this line, and the next one.
                    skipNextLine = true;
                    continue;
                } else if (id.contains("Museer")) {
                    break;
                }

                String name = fileInput.nextLine();
                String phone = fileInput.nextLine();
                String email = fileInput.nextLine();

                Person person = new Person(Integer.parseInt(id), name, phone, email);
                if (!person.checkIfPersonIdExists()) {
                    person.insertPeopleToDatabase();
                }
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find the file: ", e);
        }
    }

    public boolean checkIfPersonIdExists() {
        try {
            String query = """
                    SELECT Id FROM Person WHERE Id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                result.close();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to check if person already exists: \{this.getId()}, \{this.getName()}, \{this.getPhone()}, \{this.getEmail()}",e);
        }
        return false;
    }

    public void insertPeopleToDatabase() {
        try {
            String query = """
                    INSERT INTO Person (Id, Navn, Tlf, E_post) VALUES (?, ?, ?, ?);
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.getId());
            preparedStatement.setString(2, this.getName());
            preparedStatement.setString(3, this.getPhone());
            preparedStatement.setString(4, this.getEmail());

            preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to insert person to the database \{this.getId()}, \{this.getName()}, \{this.getPhone()}, \{this.getEmail()}", e);
        }
    }

    public static Person getPersonById(int id) {
        try {
            String query = """
                    SELECT * FROM Person WHERE Id = ?;
                    """;
            PreparedStatement preparedStatement = Database.getDatabaseConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                int personId = result.getInt("Id");
                String personName = result.getString("Navn");
                String personPhone = result.getString("Tlf");
                String personEmail = result.getString("E_post");

                Person person = new Person(personId, personName, personPhone, personEmail);
                result.close();
                return person;
            }
        } catch (SQLException e) {
            throw new RuntimeException(STR."Failed to get person information: \{id}" ,e);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
