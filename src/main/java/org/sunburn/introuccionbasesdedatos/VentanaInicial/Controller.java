package org.sunburn.introuccionbasesdedatos.VentanaInicial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.DataBaseConnection;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.IPersonaRepository;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.IReportService;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.PersonaRepository;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.ReportService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Controller {

    public IPersonaRepository personaRepo;
    public IReportService reportService;
    private DataBaseConnection dbConnection = new DataBaseConnection();

    private UIDialogService UIService = new UIDialogService();

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    public void startAll(ActionEvent event) {
        try {
            dbConnection.connect(usernameField.getText(), passwordField.getText());
            Connection conn = dbConnection.getConnection();

            IPersonaRepository personaRepo  = new PersonaRepository(conn);
            IReportService     reportService = new ReportService(conn);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/sunburn/introuccionbasesdedatos/VentanaInicial/MainWindow.fxml")
            );

            // 1. Carga el FXML
            Scene scene = new Scene(loader.load());

            // 2. Obtén el Controller que JavaFX creó para esa ventana
            MainWindowController mainController = loader.getController();

            // 3. Pásale las dependencias
            mainController.init(personaRepo, reportService);

            // 4. Muestra la ventana
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ventana Principal");
            stage.setOnCloseRequest(e -> DataBaseConnection.close());

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}