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
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;
import java.util.ResourceBundle;

public class ControllerVistaModificar implements Initializable {

    @FXML
    private Button botonCerrar;
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
    @FXML
    private TextField campoID;
    @FXML
    private Label campoMensaje;

    // Inicializa los valores de los ComboBox al cargar la ventana
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        comboBoxPuesto.getItems().addAll("manager", "empleado");
        comboBoxTurno.getItems().addAll("mañana", "tarde", "indiferente");
    }

    // Acción del botón "Buscar": busca un empleado por ID y carga sus datos en los campos
    public void botonBuscarEnAccion(ActionEvent e) {
        if (campoID.getText().isBlank()) {
            campoMensaje.setText("Introduce el ID del empleado");
        }
        try {
            int idEmpleado = Integer.parseInt(campoID.getText());
            cargarDatosEmpleado(idEmpleado);
        } catch (NumberFormatException ex) {
            campoMensaje.setText("El ID debe ser un número entero");
        } catch (Exception ex) {
            campoMensaje.setText("Error al buscar empleado");
        }
    }

    // Carga los datos del empleado desde la base de datos y los muestra en los campos
    private void cargarDatosEmpleado(int id) {
        try {
            ConexionBaseDatos conectar = new ConexionBaseDatos();
            Connection conectarBD = conectar.getConnection();
            String verificarID = "SELECT * FROM empleados WHERE id = ?";
            PreparedStatement statement = conectarBD.prepareStatement(verificarID);
            statement.setInt(1, id);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                campoNombre.setText(resultado.getString("nombre"));
                campoApellido.setText(resultado.getString("apellido"));
                campoDni.setText(resultado.getString("dni"));
                campoEmail.setText(resultado.getString("email"));
                campoHoras.setText(resultado.getString("horas_contrato"));
                comboBoxPuesto.setValue(resultado.getString("puesto"));
                comboBoxTurno.setValue(resultado.getString("turno"));
            } else {
                campoMensaje.setText("ID no encontrado");
                limpiarCampos();
            }
        } catch (Exception e) {
            campoMensaje.setText("Error al buscar empleado");
            limpiarCampos();
        }
    }

    // Acción del botón "Cambiar": valida los campos y actualiza los datos del empleado
    @FXML
    public void botonCambiarEnAccion(ActionEvent e) {
        if (campoNombre.getText().isBlank() || campoApellido.getText().isBlank() ||
                campoDni.getText().isBlank() || campoEmail.getText().isBlank() || campoHoras.getText().isBlank() ||
                comboBoxPuesto.getValue() == null || comboBoxTurno.getValue() == null) {
            campoMensaje.setText("Faltan datos por rellenar");
        } else {
            validarModificar();
        }
    }

    // Realiza la actualización de los datos del empleado en la base de dato
    public void validarModificar() {
        try {
            ConexionBaseDatos conectar = new ConexionBaseDatos();
            Connection conectarBD = conectar.getConnection();
            String actualizar = "UPDATE empleados SET nombre=?, apellido=?, dni=?, email=?, puesto=?, turno=?, horas_contrato=? WHERE id=?";
            PreparedStatement statement = conectarBD.prepareStatement(actualizar);
            statement.setString(1, campoNombre.getText());
            statement.setString(2, campoApellido.getText());
            statement.setString(3, campoDni.getText());
            statement.setString(4, campoEmail.getText());
            statement.setString(5, comboBoxPuesto.getValue().toString());
            statement.setString(6, comboBoxTurno.getValue().toString());
            statement.setString(7, campoHoras.getText());
            statement.setInt(8, Integer.parseInt(campoID.getText()));
            int filas = statement.executeUpdate();
            if (filas > 0) {
                campoMensaje.setText("Empleado modificado correctamente");
                limpiarCampos();
            } else {
                campoMensaje.setText("No se pudo modificar");
            }
        } catch (Exception ex) {
            campoMensaje.setText("Error al modificar empleado");
        }
    }

    // Acción del botón "Cerrar": cierra la ventana de modificación
    public void botonCerrarEnAccion(ActionEvent e) {
        Stage stage = (Stage) botonCerrar.getScene().getWindow();
        stage.close();
    }

    // Limpia todos los campos del formulario
    private void limpiarCampos() {
        campoNombre.clear();
        campoApellido.clear();
        campoDni.clear();
        campoEmail.clear();
        campoHoras.clear();
        comboBoxPuesto.setValue(null);
        comboBoxTurno.setValue(null);
    }
}
