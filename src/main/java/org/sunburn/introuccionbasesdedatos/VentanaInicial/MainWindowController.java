package org.sunburn.introuccionbasesdedatos.VentanaInicial;

import org.sunburn.introuccionbasesdedatos.DataBaseRelated.IPersonaRepository;
import org.sunburn.introuccionbasesdedatos.DataBaseRelated.IReportService;

import java.sql.SQLException;

public class MainWindowController {


    private IPersonaRepository personaRepo;
    private IReportService reportService;
    private UIDialogService UIService = new UIDialogService();


    public void init(IPersonaRepository personaRepo,
                     IReportService reportService) {
        this.personaRepo   = personaRepo;
        this.reportService = reportService;
    }

    public void consulta() throws SQLException {

        UIService.generarVentanaConsulta(reportService.generateReport());
    }



    public void solicitarAlta()
    {
        String[] info = UIService.askForAddition();

        try{personaRepo.realizarAlta(info[0],info[1],info[2]);} catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void solicitarBaja()
    {
        String texto = UIService.askForElimination();

        try{personaRepo.realizarBaja(texto);} catch (SQLException e) {e.printStackTrace();}

    }

    public void solicitarCambio()
    {
        String[] info = UIService.pedirInformacion("Nombre Original","Nombre Nuevo");

        try{personaRepo.modificarUsuario(info[0],info[1]);} catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void solicitarAñadirDireccion()
    {

        String[] info = UIService.pedirInformacion("Nombre","Direccion");

        try{personaRepo.insertarDireccion(info[0],info[1]);} catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void solicitarAñadirTelefono()
    {

        String[] info = UIService.pedirInformacion("Nombre","Telefono");

        try{personaRepo.insertarTelefono(info[0],info[1]);} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void solicitarEliminarDireccion()
    {

        String[] info = UIService.pedirInformacion("Nombre","Direccion");

        try{personaRepo.eliminarDireccion(info[0],info[1]);} catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void solicitarEliminarTelefono()
    {
        String[] info = UIService.pedirInformacion("Nombre","Telefono");
        try{personaRepo.eliminarTelefono(info[0],info[1]);} catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



}
