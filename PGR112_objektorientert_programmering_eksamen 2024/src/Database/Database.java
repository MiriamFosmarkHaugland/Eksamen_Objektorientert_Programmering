package Database;

import com.mysql.cj.jdbc.Driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.StringTemplate.STR;

public class Database {
    private static final Properties properties = new Properties();

    // Stores the database connection which gets reused throughout the program
    private static Connection connection = null;

    // Checks that the 5 keys specified, are present in the database.properties file, if not, it would throw an exception.
    // code/solutions/database/terminal/src/Terminal.java - Marcus Alexander Dahl
    static {
        try {
            DriverManager.registerDriver(new Driver());
            Database.properties.load(new FileInputStream("src/Database/database.properties"));

            String[] keys = {"host", "port", "database", "username", "password"};
            for (String key : keys) {
                if (!Database.properties.containsKey(key)) {
                    throw new RuntimeException(STR."The following key is missing data: \{key}");
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("Could not find the location of the file.");
            throw new RuntimeException(e);
        }
    }

    // code/lectures/_20/Database.java - Marcus Alexander Dahl
    public static Connection getDatabaseConnection() throws SQLException {

        // If there is no connection to the database, create a new one.
        if(Database.connection == null) {
            Database.connection = DriverManager.getConnection(
                    STR."\{"jdbc:mysql://%s:%s/%s".formatted(
                            Database.properties.getProperty("host"),
                            Database.properties.getProperty("port"),
                            Database.properties.getProperty("database")
                    )}",
                    Database.properties.getProperty("username"),
                    Database.properties.getProperty("password")
            );
        }
        return connection;
    }
}
