module org.sunburn.introuccionbasesdedatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens org.sunburn.introuccionbasesdedatos to javafx.fxml;
    exports org.sunburn.introuccionbasesdedatos;
    exports org.sunburn.introuccionbasesdedatos.VentanaInicial;
    opens org.sunburn.introuccionbasesdedatos.VentanaInicial to javafx.fxml;
    exports org.sunburn.introuccionbasesdedatos.DataBaseRelated;
    opens org.sunburn.introuccionbasesdedatos.DataBaseRelated to javafx.fxml;
}