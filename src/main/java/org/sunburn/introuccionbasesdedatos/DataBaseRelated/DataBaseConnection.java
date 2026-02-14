package org.sunburn.introuccionbasesdedatos.DataBaseRelated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {


    private static Connection conn;

    public static Connection connect(String user, String pass) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/agenda2", user, pass);
        return conn;
    }

    public Connection getConnection() {
        return conn;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
