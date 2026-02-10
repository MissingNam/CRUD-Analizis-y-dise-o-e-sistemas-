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

        // 1. Insertar persona
        String sqlPersona = "INSERT INTO Personas (nombre) VALUES (?)";
        PreparedStatement psPersona = conn.prepareStatement(
                sqlPersona,
                Statement.RETURN_GENERATED_KEYS
        );
        psPersona.setString(1, nombre);
        psPersona.executeUpdate();

        ResultSet rsPersona = psPersona.getGeneratedKeys();
        rsPersona.next();
        int idPersona = rsPersona.getInt(1);

        rsPersona.close();
        psPersona.close();

        // 2. Obtener o crear dirección
        int idDireccion = obtenerIdDireccion(direccion);

        // 3. Relacionar persona-dirección
        String sqlRelacion = "INSERT INTO PersonaDireccion (personaId, direccionId) VALUES (?, ?)";
        PreparedStatement psRelacion = conn.prepareStatement(sqlRelacion);
        psRelacion.setInt(1, idPersona);
        psRelacion.setInt(2, idDireccion);
        psRelacion.executeUpdate();
        psRelacion.close();

        // 4. Insertar teléfono
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

    public void modificarUsuario(String nombreOG, String nombreNW) throws SQLException {


        String sql = "UPDATE Personas SET nombre = ? WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombreNW);
        ps.setString(2, nombreOG);

        ps.executeUpdate();
        ps.close();
    }

    public void insertarDireccion(String nombre, String direccion)throws SQLException
    {
        int direccionID = obtenerIdDireccion(direccion);
        int nombreID = 0;

        String sql = "SELECT id FROM Personas WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            nombreID = rs.getInt(1);
        }

        rs.close();
        ps.close();

        String sql2 = "INSERT INTO PersonaDireccion (personaId, direccion) VALUES (?, ?)\n";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setInt(1, nombreID);
        ps2.setInt(2, direccionID);
        ps2.executeUpdate();

        ps2.close();
    }

    public void eliminarDireccion(String nombre, String direccion) throws SQLException
    {
        // buscar la direccion sin insertarla porque obtenerID la inserta si no esta
        int direccionID = 0;


        String sql = "SELECT id FROM direcciones WHERE direccion = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,direccion);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            direccionID = rs.getInt(1);
        }
        rs.close();
        ps.close();

        // luego buscamos al usuario
        int usuarioID = 0;
        String sql2 = "SELECT id FROM personas WHERE nombre = ?";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        ps2.setString(1,nombre);
        ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            usuarioID = rs2.getInt(1);
        }

        rs2.close();
        ps2.close();

        String sql3 = "DELETE FROM PersonaDireccion WHERE personaId = ? AND direccion = ?";
        PreparedStatement ps3 = conn.prepareStatement(sql3);
        ps3.setInt(1, usuarioID);
        ps3.setInt(2, direccionID);
        ps3.executeUpdate();
        ps3.close();

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

        // Buscar si la dirección ya existe
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

        // Si no existe, insertarla
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



