package miempresa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VistaEmpleadoController implements Initializable {


    @FXML
    private TableView<TurnoSemanaEmpleado> tablaTurnos;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, Integer> colID;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colEmpleado;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colLunes;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colMartes;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colMiercoles;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colJueves;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colViernes;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colSabado;
    @FXML
    private TableColumn<TurnoSemanaEmpleado, String> colDomingo;
    @FXML
    private Label nombreEmpleado;

    // Variables para almacenar el id y nombre del empleado actual
    private int idEmpleado;
    private String nombre;

    // Método que se ejecuta al inicializar la vista
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (nombre != null && nombreEmpleado != null) {
            nombreEmpleado.setText(nombre);
        }
        cargarTurnosEmpleado();
    }

    public void setIdEmpleado(int idEmpleado, String nombre) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        if (nombreEmpleado != null) {
            nombreEmpleado.setText(nombre);
        }
    }

    // Carga los turnos del empleado desde la base de datos y los muestra en la tabla
    public void cargarTurnosEmpleado() {
        ObservableList<TurnoSemanaEmpleado> lista = FXCollections.observableArrayList();
        try {
            // Conexión a la base de datos
            ConexionBaseDatos conectar = new ConexionBaseDatos();
            Connection conectarBD = conectar.getConnection();
            // Consulta para obtener los turnos del empleado actual
            String sql = "SELECT dia, hora_inicio, hora_fin FROM turnos WHERE id_empleado = ?";
            PreparedStatement ps = conectarBD.prepareStatement(sql);
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();

            TurnoSemanaEmpleado tse = new TurnoSemanaEmpleado(idEmpleado, nombre);
            DateTimeFormatter formatoBD = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter formatoUI = DateTimeFormatter.ofPattern("HH:mm");
            // Recorrer los resultados y asignar los turnos al objeto
            while (rs.next()) {
                String dia = rs.getString("dia");
                String inicio = rs.getString("hora_inicio");
                String fin = rs.getString("hora_fin");
                String horario = LocalTime.parse(inicio, formatoBD).format(formatoUI) + "-" + LocalTime.parse(fin, formatoBD).format(formatoUI);
                switch (dia.toLowerCase()) {
                    case "lunes":
                        tse.setLunes(horario);
                        break;
                    case "martes":
                        tse.setMartes(horario);
                        break;
                    case "miércoles":
                        tse.setMiercoles(horario);
                        break;
                    case "jueves":
                        tse.setJueves(horario);
                        break;
                    case "viernes":
                        tse.setViernes(horario);
                        break;
                    case "sábado":
                        tse.setSabado(horario);
                        break;
                    case "domingo":
                        tse.setDomingo(horario);
                        break;
                }
            }
            // Añadir el objeto a la lista para mostrarlo en la tabla
            lista.add(tse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("empleado"));
        colLunes.setCellValueFactory(new PropertyValueFactory<>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<>("viernes"));
        colSabado.setCellValueFactory(new PropertyValueFactory<>("sabado"));
        colDomingo.setCellValueFactory(new PropertyValueFactory<>("domingo"));
        // Mostrar la lista en la tabla
        tablaTurnos.setItems(lista);
    }

    // Abre la ventana para editar los datos del empleado
    public void botonEditarEmpleado() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vista_empleado_modificar.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modificar empleado");
            stage.setScene(new Scene(loader.load()));

            ControllerEmpleadoModificar controller = loader.getController();
            controller.setIdEmpleado(idEmpleado);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cierra la sesión y vuelve a la pantalla de login
    public void botonCerrarSesionEnAccion(ActionEvent e) {
        try {
            FXMLLoader cargar = new FXMLLoader((getClass().getResource("login.fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(cargar.load()));
            stage.show();

            Stage actual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            actual.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
