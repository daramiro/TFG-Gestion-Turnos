package miempresa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;

public class Controller {
    @FXML
    private Button botonCancelar;
    @FXML
    private Label campoMensajeInicio;
    @FXML
    private TextField campoUser;
    @FXML
    private PasswordField campoPass;

    // Acción del botón "Iniciar sesión": valida que los campos no estén vacíos y llama a la validación
    public void botonInicioEnAccion(ActionEvent e) throws IOException {
        if (campoUser.getText().isBlank() || campoPass.getText().isBlank()) {
            campoMensajeInicio.setText("Por favor introduce tu usuario y contraseña");
        } else {
            validarInicio();
        }

    }

    // Acción del botón "Cancelar": cierra la ventana de inicio de sesión
    public void botonCancelarEnAccion(ActionEvent e) {
        Stage stage = (Stage) botonCancelar.getScene().getWindow();
        stage.close();
    }

    // Carga la vista correspondiente según el tipo de usuario (manager o empleado)
    private void cargarVista(String fxml, String titulo, int idEmpleado, String nombre) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(loader.load()));

        Object controller = loader.getController();
        // Si es empleado, pasa el id y nombre y carga sus turnos
        if (controller instanceof VistaEmpleadoController) {
            ((VistaEmpleadoController) controller).setIdEmpleado(idEmpleado, nombre);
            ((VistaEmpleadoController) controller).cargarTurnosEmpleado();
            // Si es manager, solo pasa el id
        } else if (controller instanceof VistaManagerController) {
            ((VistaManagerController) controller).setIdEmpleado(idEmpleado);
        }

        stage.show();
        // Cierra la ventana de login actual
        Stage actual = (Stage) campoUser.getScene().getWindow();
        actual.close();
    }

    // Valida el usuario y contraseña contra la base de datos y abre la ventana correspondiente
    public void validarInicio() {
        ConexionBaseDatos conectar = new ConexionBaseDatos();
        Connection conectarBD = conectar.getConnection();
        // Consulta SQL para verificar usuario y contraseña
        String verificarInicio = "SELECT id, puesto, nombre FROM empleados WHERE nombre = '" + campoUser.getText() + "' AND password = '" + campoPass.getText() + "'";
        try {
            Statement statement = conectarBD.createStatement();
            ResultSet rs = statement.executeQuery(verificarInicio);
            if (rs.next()) {
                int idEmpleado = rs.getInt("id");
                String puesto = rs.getString("puesto");
                String nombre = rs.getString("nombre");
                // Si es manager, abre la vista de manager; si no, la de empleado
                if ("manager".equals(puesto)) {
                    cargarVista("vista_manager.fxml", "Ventana Manager", idEmpleado, nombre);
                } else {
                    cargarVista("vista_empleado.fxml", "Ventana Empleado", idEmpleado, nombre);
                }
            } else {
                campoMensajeInicio.setText("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
