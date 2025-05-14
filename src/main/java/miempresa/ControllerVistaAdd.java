package miempresa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;
import java.util.ResourceBundle;

public class ControllerVistaAdd implements Initializable {

    @FXML
    private Button botonCerrar;
    @FXML
    private Label campoMensaje;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoApellido;
    @FXML
    private TextField campoDni;
    @FXML
    private TextField campoEmail;
    @FXML
    private TextField campoHoras;
    @FXML
    private ComboBox comboBoxPuesto;
    @FXML
    private ComboBox comboBoxTurno;


    @Override
    public void initialize(URL url, ResourceBundle resource) {
        comboBoxPuesto.getItems().addAll("manager", "empleado");
        comboBoxTurno.getItems().addAll("mañana", "tarde", "indiferente");
    }

    public void botonAddEnAccion(ActionEvent e) throws IOException {
        if (campoNombre.getText().isBlank() || campoApellido.getText().isBlank() || campoDni.getText().isBlank() || campoEmail.getText().isBlank() || campoHoras.getText().isBlank() || comboBoxPuesto.getValue() == null || comboBoxTurno.getValue() == null) {
            campoMensaje.setText("Faltan información por rellenar");
        } else {
            validarAdd();
        }
    }

    public void validarAdd() {
        String puesto = comboBoxPuesto.getValue().toString();
        String turno = comboBoxTurno.getValue().toString();
        int nuevoId = 1;
        ConexionBaseDatos conectar = new ConexionBaseDatos();
        Connection conectarBD = conectar.getConnection();
        String password = campoDni.getText();
        String verificarId = "SELECT MAX(id) FROM empleados";
        try {
            Statement statement = conectarBD.createStatement();
            ResultSet ultimo = statement.executeQuery(verificarId);
            if (ultimo.next()) {
                nuevoId = ultimo.getInt(1) + 1;
            }
            String insertarEmpleado = "INSERT INTO empleados (id, nombre, apellido, dni, email, puesto, turno, horas_contrato, password) VALUES (" +
                    nuevoId + ", '" +
                    campoNombre.getText() + "', '" +
                    campoApellido.getText() + "', '" +
                    campoDni.getText() + "', '" +
                    campoEmail.getText() + "', '" +
                    puesto + "', '" +
                    turno + "', '" +
                    campoHoras.getText() + "', '" +
                    password + "')";
            statement.executeUpdate(insertarEmpleado);
            campoMensaje.setText("Empleado añadido correctamente");
            campoNombre.clear();
            campoApellido.clear();
            campoDni.clear();
            campoEmail.clear();
            campoHoras.clear();
            comboBoxPuesto.setValue(null);
            comboBoxTurno.setValue(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Acción del botón "Cerrar": cierra la ventana actual de alta de empleado
    public void botonCerrarEnAccion(ActionEvent e) {
        Stage stage = (Stage) botonCerrar.getScene().getWindow();
        stage.close();
    }

}
