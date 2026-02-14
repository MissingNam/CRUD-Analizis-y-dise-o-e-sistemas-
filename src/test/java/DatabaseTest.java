import org.junit.jupiter.api.*;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.DataBaseConnection;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.Database;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.IPersonaRepository;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.PersonaRepository;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseTest {

    private DataBaseConnection db;
    private Connection conn;

    private IPersonaRepository pR;


    @BeforeAll
    void setUp() throws SQLException {
        db = new DataBaseConnection();
        conn = db.getConnection();
        pR = new PersonaRepository(conn);
        conn = db.connect("usuario3", "superpassword"); // usa agenda_test
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

        pR.realizarAlta("Juan", "Calle 1", "12345");

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

        pR.realizarAlta("Ana", "Calle 2", "11111");
        pR.insertarTelefono("Ana", "22222");

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

        pR.realizarAlta("Luis", "Calle 3", "33333");
        pR.eliminarTelefono("Luis", "33333");

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

        pR.realizarAlta("Pedro", "Calle 4", "44444");
        pR.realizarBaja("Pedro");

        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Personas"
        );
        ResultSet rs = ps.executeQuery();
        rs.next();

        assertEquals(0, rs.getInt(1));
    }
}

