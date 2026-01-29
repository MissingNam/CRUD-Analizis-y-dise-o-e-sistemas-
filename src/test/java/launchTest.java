import org.sunburn.introuccionbasesdedatos.Database;

import java.sql.SQLException;

public class launchTest {

    public static void main(String[] args) throws SQLException {
        DatabaseTest db = new DatabaseTest();

        db.setUp();
        db.testRealizarAlta();
        db.testInsertarTelefono();
        db.testEliminarTelefono();
        db.testRealizarBaja();
        db.limpiar();
        db.cerrar();
    }
}
