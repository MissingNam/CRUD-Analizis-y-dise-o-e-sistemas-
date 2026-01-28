package org.sunburn.introuccionbasesdedatos;

import java.sql.*;

public class Database {
    private static Connection conn;

    public Connection connect(String user, String pass) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/agenda", user, pass);
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

    public void realizarAlta(String nombre, String direccion, String telefono) throws SQLException {

        String sqlPersona = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";
        PreparedStatement psPersona = conn.prepareStatement(
                sqlPersona,
                Statement.RETURN_GENERATED_KEYS
        );

        psPersona.setString(1, nombre);
        psPersona.setString(2, direccion);
        psPersona.executeUpdate();

        ResultSet rsKeys = psPersona.getGeneratedKeys();
        rsKeys.next();
        int idPersona = rsKeys.getInt(1);

        String sqlTelefono = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
        PreparedStatement psTelefono = conn.prepareStatement(sqlTelefono);
        psTelefono.setInt(1, idPersona);
        psTelefono.setString(2, telefono);
        psTelefono.executeUpdate();
    }


    public void realizarBaja(String nombre) throws SQLException {

        String sql = "DELETE FROM Personas WHERE nombre = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);

        ps.executeUpdate();
        ps.close();
    }


    public void insertarTelefono(String nombre, String telefono) throws  SQLException
    {
        String sqlID = "SELECT id FROM Personas WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sqlID);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new SQLException("No existe usuario con nombre: " + nombre);
        }
        int id = rs.getInt(1);
        ps.close();
        rs.close();

        String sqlInsert = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
        PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
        psInsert.setInt(1, id);
        psInsert.setString(2, telefono);
        psInsert.executeUpdate();
        psInsert.close();
    }

    public void modificarUsuario(String nombreOG, String nombreNW, String direccionNW) throws SQLException
    {
        String sql = "UPDATE Personas SET nombre = ?, direccion = ? WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombreNW);
        ps.setString(2, direccionNW);
        ps.setString(3, nombreOG);

        ps.executeUpdate();
        ps.close();
    }

    public void eliminarTelefono(String nombre, String telefono) throws  SQLException
    {
        String sqlID = "SELECT id FROM Personas WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sqlID);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new SQLException("No existe usuario con nombre: " + nombre);
        }
        int id = rs.getInt(1);
        ps.close();
        rs.close();

        String sqlDelete = "DELETE FROM Telefonos WHERE personaId = ? AND telefono = ?";
        PreparedStatement psInsert = conn.prepareStatement(sqlDelete);
        psInsert.setInt(1, id);
        psInsert.setString(2, telefono);
        psInsert.executeUpdate();
        psInsert.close();
    }
}
