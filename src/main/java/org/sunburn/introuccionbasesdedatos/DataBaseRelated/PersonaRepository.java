package org.sunburn.introuccionbasesdedatos.DataBaseRelated;

import java.sql.*;

public class PersonaRepository implements IPersonaRepository {

    private final Connection conn;

    // La conexión se inyecta desde afuera — bajo acoplamiento
    public PersonaRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void realizarAlta(String nombre, String direccion, String telefono) throws SQLException {
        String sqlPersona = "INSERT INTO Personas (nombre) VALUES (?)";
        PreparedStatement psPersona = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
        psPersona.setString(1, nombre);
        psPersona.executeUpdate();

        ResultSet rsPersona = psPersona.getGeneratedKeys();
        rsPersona.next();
        int idPersona = rsPersona.getInt(1);
        rsPersona.close();
        psPersona.close();

        int idDireccion = obtenerIdDireccion(direccion);

        String sqlRelacion = "INSERT INTO PersonaDireccion (personaId, direccionId) VALUES (?, ?)";
        PreparedStatement psRelacion = conn.prepareStatement(sqlRelacion);
        psRelacion.setInt(1, idPersona);
        psRelacion.setInt(2, idDireccion);
        psRelacion.executeUpdate();
        psRelacion.close();

        String sqlTelefono = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
        PreparedStatement psTelefono = conn.prepareStatement(sqlTelefono);
        psTelefono.setInt(1, idPersona);
        psTelefono.setString(2, telefono);
        psTelefono.executeUpdate();
        psTelefono.close();
    }

    @Override
    public void realizarBaja(String nombre) throws SQLException {
        String sql = "DELETE FROM Personas WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void modificarUsuario(String nombreOG, String nombreNW) throws SQLException {
        String sql = "UPDATE Personas SET nombre = ? WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombreNW);
        ps.setString(2, nombreOG);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void insertarDireccion(String nombre, String direccion) throws SQLException {
        int direccionID = obtenerIdDireccion(direccion);
        int nombreID = obtenerIdPersona(nombre);

        String sql = "INSERT INTO PersonaDireccion (personaId, direccionId) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, nombreID);
        ps.setInt(2, direccionID);
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void eliminarDireccion(String nombre, String direccion) throws SQLException {
        int direccionID = 0;
        String sql = "SELECT id FROM Direcciones WHERE direccion = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, direccion);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) direccionID = rs.getInt(1);
        rs.close();
        ps.close();

        int usuarioID = obtenerIdPersona(nombre);

        String sql3 = "DELETE FROM PersonaDireccion WHERE personaId = ? AND direccionId = ?";
        PreparedStatement ps3 = conn.prepareStatement(sql3);
        ps3.setInt(1, usuarioID);
        ps3.setInt(2, direccionID);
        ps3.executeUpdate();
        ps3.close();
    }

    @Override
    public void insertarTelefono(String nombre, String telefono) throws SQLException {
        int id = obtenerIdPersona(nombre);

        String sqlInsert = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
        PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
        psInsert.setInt(1, id);
        psInsert.setString(2, telefono);
        psInsert.executeUpdate();
        psInsert.close();
    }

    @Override
    public void eliminarTelefono(String nombre, String telefono) throws SQLException {
        int id = obtenerIdPersona(nombre);

        String sqlDelete = "DELETE FROM Telefonos WHERE personaId = ? AND telefono = ?";
        PreparedStatement ps = conn.prepareStatement(sqlDelete);
        ps.setInt(1, id);
        ps.setString(2, telefono);
        ps.executeUpdate();
        ps.close();
    }

    // ── Métodos privados de apoyo (alta cohesión: solo los usa esta clase) ──

    private int obtenerIdPersona(String nombre) throws SQLException {
        String sql = "SELECT id FROM Personas WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new SQLException("No existe usuario con nombre: " + nombre);
        }
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }

    private int obtenerIdDireccion(String direccion) throws SQLException {
        String sqlSelect = "SELECT id FROM Direcciones WHERE direccion = ?";
        PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
        psSelect.setString(1, direccion);
        ResultSet rs = psSelect.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            rs.close();
            psSelect.close();
            return id;
        }
        rs.close();
        psSelect.close();

        String sqlInsert = "INSERT INTO Direcciones (direccion) VALUES (?)";
        PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
        psInsert.setString(1, direccion);
        psInsert.executeUpdate();
        ResultSet rsKeys = psInsert.getGeneratedKeys();
        rsKeys.next();
        int id = rsKeys.getInt(1);
        rsKeys.close();
        psInsert.close();
        return id;
    }
}
