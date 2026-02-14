package org.sunburn.introuccionbasesdedatos.DataBaseRelated;

import org.sunburn.introuccionbasesdedatos.DataBaseRelated.IReportService;

import java.sql.*;

public class ReportService implements IReportService {

    private final Connection conn;

    public ReportService(Connection conn) {
        this.conn = conn;
    }

    @Override
    public String generateReport() throws SQLException {
        StringBuilder texto = new StringBuilder();
        texto.append("=== LISTADO DE PERSONAS ===\n\n");

        String sql = """
            SELECT p.id, p.nombre, d.direccion, t.telefono
            FROM Personas p
            LEFT JOIN PersonaDireccion pd ON p.id = pd.personaId
            LEFT JOIN Direcciones d ON pd.direccionId = d.id
            LEFT JOIN Telefonos t ON p.id = t.personaId
            ORDER BY p.id
            """;

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int personaActual = -1;

        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String direccion = rs.getString("direccion");
            String telefono = rs.getString("telefono");

            if (id != personaActual) {
                personaActual = id;
                texto.append("ID: ").append(id).append(", Nombre: ").append(nombre).append("\n");
                texto.append("  Direcciones:\n");
                if (direccion != null) texto.append("    - ").append(direccion).append("\n");
                texto.append("  Teléfonos:\n");
                if (telefono != null) texto.append("    - ").append(telefono).append("\n");
                texto.append("\n");
            } else {
                if (direccion != null) texto.append("    - ").append(direccion).append("\n");
                if (telefono != null) texto.append("    - ").append(telefono).append("\n");
            }
        }
        rs.close();
        stmt.close();
        return texto.toString();
    }
}
