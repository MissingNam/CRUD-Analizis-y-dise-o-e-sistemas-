package org.sunburn.introuccionbasesdedatos.DataBaseRelated;

import java.sql.SQLException;

public interface IPersonaRepository {
    void realizarAlta(String nombre, String direccion, String telefono) throws SQLException;
    void realizarBaja(String nombre) throws SQLException;
    void modificarUsuario(String nombreOG, String nombreNW) throws SQLException;
    void insertarDireccion(String nombre, String direccion) throws SQLException;
    void eliminarDireccion(String nombre, String direccion) throws SQLException;
    void insertarTelefono(String nombre, String telefono) throws SQLException;
    void eliminarTelefono(String nombre, String telefono) throws SQLException;
}
