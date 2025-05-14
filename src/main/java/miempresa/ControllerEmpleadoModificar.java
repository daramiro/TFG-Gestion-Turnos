package miempresa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControllerEmpleadoModificar {

    @FXML
    private Label campoMensaje;
    @FXML
    private PasswordField campoPassActual;
    @FXML
    private PasswordField campoPassNueva;
    @FXML
    private javafx.scene.control.Button botonCerrar;

    private int idEmpleado;

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    // Acción del botón "Cambiar": valida los campos y realiza el cambio de contraseña
    public void botonCambiarEnAccion(ActionEvent e) {
        if (campoPassActual.getText().isBlank() || campoPassNueva.getText().isBlank()) {
            campoMensaje.setText("Faltan datos por rellenar");
        } else {
            validarModificar();
        }
    }

    // Acción del botón "Cerrar": cierra la ventana de modificación
    public void botonCerrarEnAccion(ActionEvent e) {
        Stage stage = (Stage) botonCerrar.getScene().getWindow();
        stage.close();
    }

    // Valida la contraseña actual y actualiza la contraseña en la base de datos si es correcta
    private void validarModificar() {
        try {
            ConexionBaseDatos conectar = new ConexionBaseDatos();
            Connection conectarBD = conectar.getConnection();

            String passActual = campoPassActual.getText();
            String passNueva = campoPassNueva.getText();

            // Consulta la contraseña actual en la base de datos
            String sqlPass = "SELECT password FROM empleados WHERE id = ?";
            PreparedStatement ps = conectarBD.prepareStatement(sqlPass);
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String passActualBD = rs.getString("password");
                // Verifica si la contraseña actual introducida es correcta
                if (passActual.equals(passActualBD)) {
                    // Actualiza la contraseña en la base de datos
                    String update = "UPDATE empleados SET password = ? WHERE id = ?";
                    PreparedStatement psUpdate = conectarBD.prepareStatement(update);
                    psUpdate.setString(1, passNueva);
                    psUpdate.setInt(2, idEmpleado);
                    int filas = psUpdate.executeUpdate();
                    if (filas > 0) {
                        campoMensaje.setText("Contraseña actualizada correctamente");
                        campoPassActual.setText("");
                        campoPassNueva.setText("");
                    } else {
                        campoMensaje.setText("No se ha realizado ningún cambio");
                    }
                } else {
                    campoMensaje.setText("Contraseña actual incorrecta");
                }
            } else {
                campoMensaje.setText("Usuario no encontrado");
            }
        } catch (Exception e) {
            campoMensaje.setText("Error al modificar datos");
        }
    }
}