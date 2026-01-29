import org.junit.jupiter.api.*;
import org.sunburn.introuccionbasesdedatos.Database;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseTest {

    private Database db;
    private Connection conn;

    @BeforeAll
    void setUp() throws SQLException {
        db = new Database();
        conn = db.connect("root", "TU_PASSWORD"); // usa agenda_test
        limpiarTablas();
    }

    @AfterEach
    void limpiar() throws SQLException {
        limpiarTablas();
    }

    @AfterAll
    void cerrar() {
        Database.close();
    }

    private void limpiarTablas() throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM Telefonos");
        st.executeUpdate("DELETE FROM Personas");
        st.close();
    }

    // 🔹 PRUEBA 1: realizarAlta
    @Test
    void testRealizarAlta() throws SQLException {

        db.realizarAlta("Juan", "Calle 1", "12345");

        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Personas WHERE nombre = ?"
        );
        ps.setString(1, "Juan");
        ResultSet rs = ps.executeQuery();
        rs.next();

        assertEquals(1, rs.getInt(1));
    }

    // 🔹 PRUEBA 2: insertarTelefono
    @Test
    void testInsertarTelefono() throws SQLException {

        db.realizarAlta("Ana", "Calle 2", "11111");
        db.insertarTelefono("Ana", "22222");

        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Telefonos"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();

        assertEquals(2, rs.getInt(1));
    }

    // 🔹 PRUEBA 3: eliminarTelefono
    @Test
    void testEliminarTelefono() throws SQLException {

        db.realizarAlta("Luis", "Calle 3", "33333");
        db.eliminarTelefono("Luis", "33333");

        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Telefonos"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();

        assertEquals(0, rs.getInt(1));
    }

    // 🔹 PRUEBA 4: realizarBaja
    @Test
    void testRealizarBaja() throws SQLException {

        db.realizarAlta("Pedro", "Calle 4", "44444");
        db.realizarBaja("Pedro");

        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Personas"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();

        assertEquals(0, rs.getInt(1));
    }
}

