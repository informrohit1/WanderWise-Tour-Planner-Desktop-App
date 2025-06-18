package tms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {

    Connection c;
    Statement s;

    public Conn() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "appuser", "app_password");
            s = c.createStatement();
        } catch (Exception e) {
            // Print full stack trace so the real cause (authentication / network) is visible
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (c == null) {
            throw new SQLException("Database connection is not available. Check earlier stacktrace for connection error.");
        }
        return c.prepareStatement(query);
    }

    // Close the underlying connection safely
    public void close() {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
