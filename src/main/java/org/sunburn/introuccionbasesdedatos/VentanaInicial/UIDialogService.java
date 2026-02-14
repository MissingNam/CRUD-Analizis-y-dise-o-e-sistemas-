package org.sunburn.introuccionbasesdedatos.VentanaInicial;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class UIDialogService {


    public UIDialogService(){

    }


    public String[] pedirInformacion(String Att1, String Att2)
    {
        String[] ret = new String[2];
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

        txtNombre.setPromptText(Att1);
        txtTelefono.setPromptText(Att2);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Dato 1:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Dato 2:"), 0, 1);
        grid.add(txtTelefono, 1, 1);


        dialog.getDialogPane().setContent(grid);

        // Mostrar y esperar respuesta
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String nombreOGText = txtNombre.getText();
                String nombreNWText = txtTelefono.getText();

                ret[0] = nombreOGText;
                ret[1] = nombreNWText;
            }
        });
        return ret;
    }


    public String askForElimination()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrada de datos");
        dialog.setHeaderText("Ingrese el nombre a eliminar");
        dialog.setContentText("Nombre:");

        Optional<String> resultado = dialog.showAndWait();

        return resultado.orElse(" ");
    }

   public String[] askForAddition()
   {
       Dialog<ButtonType> dialog = new Dialog<>();
       dialog.setTitle("Ingresar datos");
       dialog.setHeaderText("Introduce la información");

       String[] ret = new String[3];

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
               ret[0] = txtNombre.getText();
               ret[1] = txtDireccion.getText();
               ret[2] = txtTelefono.getText();
           }
       });

       return ret;

   }

   public void generarVentanaConsulta(String texto)
   {
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle("Consulta");
       alert.setHeaderText("Listado de personas");
       alert.setContentText(texto);
       alert.showAndWait();
   }



}
