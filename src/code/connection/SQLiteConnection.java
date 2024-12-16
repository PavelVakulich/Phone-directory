package code.connection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:db" + File.separator + "phonebook.db";

            return DriverManager.getConnection(url);

        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
