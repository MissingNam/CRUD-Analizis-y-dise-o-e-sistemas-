package org.sunburn.introuccionbasesdedatos.VentanaInicial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class DataBaseApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DataBaseApplication.class.getResource("/org/sunburn/introuccionbasesdedatos/VentanaInicial/ResourceImage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CRUD Beta");
        Image icon = new Image(getClass().getResourceAsStream("/org/sunburn/introuccionbasesdedatos/SunBurnLogo.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
}
