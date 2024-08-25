package Classes.Finds;

import Classes.Museum;
import Classes.Person;
import Database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

// Java abstraction - Abstract classes and methods
// https://www.w3schools.com/java/java_abstract.asp
public abstract class Find {
    private int id;
    private String findingLocation;
    private Person discoverer;
    private String discoveryDate;
    private int assumedDateOfOrigin;
    private Museum museum;

    public Find(int id, String findingLocation, Person discoverer, String discoveryDate, int assumedDateOfOrigin, Museum museum) {
        this.id = id;
        this.findingLocation = findingLocation;
        this.discoverer = discoverer;
        this.discoveryDate = discoveryDate;
        this.assumedDateOfOrigin = assumedDateOfOrigin;
        this.museum = museum;
    }

    public abstract boolean checkIfExists();

    public abstract void insertIntoDatabase();

    public static void readFindFile() {
        try {
            File file = new File("funn.txt");
            Scanner fileInput = new Scanner(file);
            
            boolean pastPeopleAndMuseum = false;
            
            while (fileInput.hasNextLine()) {
                String id = fileInput.nextLine();

                if (id.contains("Funn:")) {
                    pastPeopleAndMuseum = true;
                    continue;
                } else if (id.contains("-------")) {
                    continue;
                }
                if (!pastPeopleAndMuseum) {
                    continue;
                }

                String findingLocation = fileInput.nextLine();
                String discovererId = fileInput.nextLine();
                String discoveryDate = fileInput.nextLine();
                String assumedDateOfOrigin = fileInput.nextLine();
                String museumId = fileInput.nextLine();
                String findType = fileInput.nextLine();

                Person discoverer = Person.getPersonById(Integer.parseInt(discovererId));
                Museum museum = null;
                if (!museumId.isEmpty()) {
                    museum = Museum.getMuseumById(Integer.parseInt(museumId));
                }

                if (findType.equals("Mynt")) {
                    Coin coin = Coin.readCoinSpecificFile(fileInput, Integer.parseInt(id), findingLocation, discoverer, discoveryDate, Integer.parseInt(assumedDateOfOrigin), museum);
                    if (!coin.checkIfExists()) {
                        coin.insertIntoDatabase();
                    }
                } else if (findType.equals("Våpen")) {
                    Weapon weapon = Weapon.readWeaponSpecificFile(fileInput, Integer.parseInt(id), findingLocation, discoverer, discoveryDate, Integer.parseInt(assumedDateOfOrigin), museum);
                    if (!weapon.checkIfExists()) {
                        weapon.insertIntoDatabase();
                    }
                } else if (findType.equals("Smykke")) {
                    Jewelry jewelry = Jewelry.readJewelrySpecificFile(fileInput, Integer.parseInt(id), findingLocation, discoverer, discoveryDate, Integer.parseInt(assumedDateOfOrigin), museum);
                    if (!jewelry.checkIfExists()) {
                        jewelry.insertIntoDatabase();
                    }
                }
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find the file: ", e);
        }
    }

    public static Integer getTotalNumberOfItems() {
        try {
            String coinQuery = """
                    SELECT COUNT(*) FROM Mynt;
                    """;
            String weaponQuery = """
                    SELECT COUNT(*) FROM Vaapen;
                    """;
            String jewelryQuery = """
                    SELECT COUNT(*) FROM Smykke;
                    """;
            PreparedStatement preparedCoinStatement = Database.getDatabaseConnection().prepareStatement(coinQuery);
            PreparedStatement preparedWeaponStatement = Database.getDatabaseConnection().prepareStatement(weaponQuery);
            PreparedStatement preparedJewelryStatement = Database.getDatabaseConnection().prepareStatement(jewelryQuery);
            ResultSet coinResult = preparedCoinStatement.executeQuery();
            ResultSet weaponResult = preparedWeaponStatement.executeQuery();
            ResultSet jewelryResult = preparedJewelryStatement.executeQuery();

            int coinAmount = 0;
            int weaponAmount = 0;
            int jewelryAmount = 0;

            if (coinResult.next()) {
                coinAmount = coinResult.getInt(1);
            }
            if (weaponResult.next()) {
                weaponAmount = weaponResult.getInt(1);
            }
            if (jewelryResult.next()) {
                jewelryAmount = jewelryResult.getInt(1);
            }

            int totalItemsFound = coinAmount + weaponAmount + jewelryAmount;

            coinResult.close();
            weaponResult.close();
            jewelryResult.close();
            return totalItemsFound;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get the total number of items: ", e);
        }
    }

    public int getId() {
        return id;
    }

    public String getFindingLocation() {
        return findingLocation;
    }

    public Person getDiscoverer() {
        return discoverer;
    }

    public String getDiscoveryDate() {
        return discoveryDate;
    }

    public int getAssumedDateOfOrigin() {
        return assumedDateOfOrigin;
    }

    public Museum getMuseum() {
        return museum;
    }
}
