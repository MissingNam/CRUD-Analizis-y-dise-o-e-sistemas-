package org.sunburn.introuccionbasesdedatos;

import java.sql.*;

public class Database {
    private static Connection conn;

    public Connection connect(String user, String pass) throws SQLException {
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

    public void realizarAlta(String nombre, String direccion, String telefono) throws SQLException {

        int idDireccion = obtenerIdDireccion(direccion);

        String sqlPersona = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";
        PreparedStatement psPersona = conn.prepareStatement(
                sqlPersona,
                Statement.RETURN_GENERATED_KEYS
        );

        psPersona.setString(1, nombre);
        psPersona.setInt(2, idDireccion);
        psPersona.executeUpdate();

        ResultSet rsKeys = psPersona.getGeneratedKeys();
        rsKeys.next();
        int idPersona = rsKeys.getInt(1);

        rsKeys.close();
        psPersona.close();

        String sqlTelefono = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
        PreparedStatement psTelefono = conn.prepareStatement(sqlTelefono);
        psTelefono.setInt(1, idPersona);
        psTelefono.setString(2, telefono);
        psTelefono.executeUpdate();
        psTelefono.close();
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

    public void modificarUsuario(String nombreOG, String nombreNW, String direccionNW) throws SQLException {

        int idDireccion = obtenerIdDireccion(direccionNW);

        String sql = "UPDATE Personas SET nombre = ?, direccion = ? WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombreNW);
        ps.setInt(2, idDireccion);
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


    private int obtenerIdDireccion(String direccion) throws SQLException {

        // 1. Buscar si la dirección ya existe
        String sqlSelect = "SELECT id FROM Direcciones WHERE direccion = ?";
        PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
        psSelect.setString(1, direccion);
        ResultSet rs = psSelect.executeQuery();

        if (rs.next()) {
            int idDireccion = rs.getInt(1);
            rs.close();
            psSelect.close();
            return idDireccion;
        }

        rs.close();
        psSelect.close();

        // 2. Si no existe, insertarla
        String sqlInsert = "INSERT INTO Direcciones (direccion) VALUES (?)";
        PreparedStatement psInsert = conn.prepareStatement(
                sqlInsert,
                Statement.RETURN_GENERATED_KEYS
        );
        psInsert.setString(1, direccion);
        psInsert.executeUpdate();

        ResultSet rsKeys = psInsert.getGeneratedKeys();
        rsKeys.next();
        int idDireccion = rsKeys.getInt(1);

        rsKeys.close();
        psInsert.close();

        return idDireccion;
    }


}



