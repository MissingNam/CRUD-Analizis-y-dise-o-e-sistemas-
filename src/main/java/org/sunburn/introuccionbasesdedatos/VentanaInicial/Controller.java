package org.sunburn.introuccionbasesdedatos.VentanaInicial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.sunburn.introuccionbasesdedatos.Database;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Controller{

    Database database = new Database();

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    public void startAll(ActionEvent event) throws IOException {

        try {
            database.connect(
                    usernameField.getText(),
                    passwordField.getText()
            );

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/sunburn/introuccionbasesdedatos/VentanaInicial/MainWindow.fxml")
            );

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ventana Principal");
            stage.setOnCloseRequest(events -> {
                Database.close();
            });

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        }
    }

    public void consulta() throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;


        System.out.println("\n=== LISTADO DE PERSONAS ===");
        stmt = database.getConnection().createStatement();
        rs = stmt.executeQuery("SELECT * FROM Personas");

        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String direccion = rs.getString("direccion");

            System.out.println("ID: " + id + ", Nombre: " + nombre + ", Dirección: " + direccion);

            // 4. Consultar los teléfonos de cada persona
            System.out.println("  Teléfonos:");
            Statement stmtTelefonos = database.getConnection().createStatement();
            ResultSet rsTelefonos = stmtTelefonos.executeQuery(
                    "SELECT telefono FROM Telefonos WHERE personaId = " + id);

            while (rsTelefonos.next()) {
                System.out.println("    - " + rsTelefonos.getString("telefono"));
            }
            rsTelefonos.close();
            stmtTelefonos.close();
        }
    }

    public void solicitarAlta()
    {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ingresar datos");
        dialog.setHeaderText("Introduce la información");

        // Botones
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL
        );

        // Campos de texto
        TextField txtNombre = new TextField();
        TextField txtDireccion = new TextField();
        TextField txtTelefono = new TextField();

        txtNombre.setPromptText("Nombre");
        txtDireccion.setPromptText("Dirección");
        txtTelefono.setPromptText("Teléfono");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Dirección:"), 0, 1);
        grid.add(txtDireccion, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);
        grid.add(txtTelefono, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String nombre = txtNombre.getText();
                String direccion = txtDireccion.getText();
                String telefono = txtTelefono.getText();

                // Aquí ya tienes los strings
                System.out.println("Nombre: " + nombre);
                System.out.println("Dirección: " + direccion);
                System.out.println("Teléfono: " + telefono);

                try{database.realizarAlta(nombre,direccion,telefono);} catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void solicitarBaja()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrada de datos");
        dialog.setHeaderText("Ingrese el nombre a eliminar");
        dialog.setContentText("Nombre:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            String texto = resultado.get();
            try{database.realizarBaja(texto);} catch (SQLException e) {e.printStackTrace();}
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No existe el nombre");
            alert.showAndWait();
            solicitarBaja();
        }

    }

    public void solicitarCambio()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ingresar datos");
        dialog.setHeaderText("Introduce la información");

        // Botones
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL
        );

        // Campos de texto
        TextField txtNombreOG = new TextField();
        TextField txtNombreNW = new TextField();
        TextField txtDireccionNW = new TextField();

        txtNombreOG.setPromptText("Nombre Original");
        txtNombreNW.setPromptText("Nombre Nuevo");
        txtDireccionNW.setPromptText("Direccion Nueva");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nombre Previo:"), 0, 0);
        grid.add(txtNombreOG, 1, 0);
        grid.add(new Label("Nombre Nuevo:"), 0, 1);
        grid.add(txtNombreNW, 1, 1);
        grid.add(new Label("Direccion Nueva:"), 0, 2);
        grid.add(txtDireccionNW, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String nombreOGText = txtNombreOG.getText();
                String nombreNWText = txtNombreNW.getText();
                String direccionNWText = txtDireccionNW.getText();

                try{database.modificarUsuario(nombreOGText,nombreNWText,direccionNWText);} catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void solicitarAñadirTelefono()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ingresar datos");
        dialog.setHeaderText("Introduce la información");

        // Botones
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL
        );

        // Campos de texto
        TextField txtNombre = new TextField();
        TextField txtTelefono = new TextField();

        txtNombre.setPromptText("Nombre");
        txtTelefono.setPromptText("Telefono");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nombre a añadir:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Telefono Nuevo:"), 0, 1);
        grid.add(txtTelefono, 1, 1);


        dialog.getDialogPane().setContent(grid);

        // Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String nombreOGText = txtNombre.getText();
                String nombreNWText = txtTelefono.getText();

                try{database.insertarTelefono(nombreOGText,nombreNWText);} catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void solicitarEliminarTelefono()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ingresar datos");
        dialog.setHeaderText("Introduce la información");

        // Botones
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL
        );

        // Campos de texto
        TextField txtNombre = new TextField();
        TextField txtTelefono = new TextField();

        txtNombre.setPromptText("Nombre");
        txtTelefono.setPromptText("Telefono");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("De quien?:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Cual Telefono:"), 0, 1);
        grid.add(txtTelefono, 1, 1);


        dialog.getDialogPane().setContent(grid);

        // Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String nombreOGText = txtNombre.getText();
                String nombreNWText = txtTelefono.getText();

                try{database.eliminarTelefono(nombreOGText,nombreNWText);} catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}