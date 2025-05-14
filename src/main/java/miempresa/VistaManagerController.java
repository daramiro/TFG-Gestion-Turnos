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
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class VistaManagerController implements Initializable {

    @FXML
    private AnchorPane contenidoVentana;
    @FXML
    private TableView<BusquedaEmpleados> vistaTablaEmpleados;
    @FXML
    private TableColumn<BusquedaEmpleados, Integer> columnaID;
    @FXML
    private TableColumn<BusquedaEmpleados, String> columnaNombre;
    @FXML
    private TableColumn<BusquedaEmpleados, String> columnaApellido;
    @FXML
    private TableColumn<BusquedaEmpleados, String> columnaDNI;
    @FXML
    private TableColumn<BusquedaEmpleados, String> columnaEmail;
    @FXML
    private TableColumn<BusquedaEmpleados, String> columnaPuesto;
    @FXML
    private TableColumn<BusquedaEmpleados, String> columnaTurno;
    @FXML
    private TableColumn<BusquedaEmpleados, Integer> columnaHorasContrato;
    @FXML
    private TextField campoBusqueda;
    @FXML
    private TextField horaInicioTurno;
    @FXML
    private TextField horaFinalTurno;
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


    private static final DateTimeFormatter FORMATO_BD = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATO_UI = DateTimeFormatter.ofPattern("HH:mm");
    private int idEmpleado;

    ObservableList<BusquedaEmpleados> busquedaEmpleadosObservableList = FXCollections.observableArrayList();

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    // Cambia la vista para mostrar empleados
    @FXML
    private void botonMostrarEmpleados() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("manager_empleados.fxml"));
            contenidoVentana.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cambia la vista para mostrar turnos
    @FXML
    private void botonMostrarTurnos() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("manager_turnos.fxml"));
            contenidoVentana.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cierra la sesión y vuelve al login
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

    // Inicializa la tabla de empleados y el filtrado
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        ConexionBaseDatos conectar = new ConexionBaseDatos();
        Connection conectarBD = conectar.getConnection();
        String busquedaEmpleados = "SELECT id, nombre, apellido, dni, email, puesto, turno, horas_contrato FROM empleados;";
        try {
            Statement statement = conectarBD.createStatement();
            ResultSet resultadosQuery = statement.executeQuery(busquedaEmpleados);
            while (resultadosQuery.next()) {
                Integer queryID = resultadosQuery.getInt("id");
                String queryNombre = resultadosQuery.getString("nombre");
                String queryApellido = resultadosQuery.getString("apellido");
                String queryDNI = resultadosQuery.getString("dni");
                String queryEmail = resultadosQuery.getString("email");
                String queryPuesto = resultadosQuery.getString("puesto");
                String queryTurno = resultadosQuery.getString("turno");
                Integer queryHorasContrato = resultadosQuery.getInt("horas_contrato");

                busquedaEmpleadosObservableList.add(new BusquedaEmpleados(queryID, queryNombre, queryApellido, queryDNI, queryEmail, queryPuesto, queryTurno, queryHorasContrato));

            }

            columnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            columnaDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
            columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            columnaPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto"));
            columnaTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
            columnaHorasContrato.setCellValueFactory(new PropertyValueFactory<>("horasContrato"));

            vistaTablaEmpleados.setItems(busquedaEmpleadosObservableList);

            //Iniciamos el filtrado de la lista de empleados
            FilteredList<BusquedaEmpleados> filtrado = new FilteredList<>(busquedaEmpleadosObservableList, b -> true);
            campoBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrado.setPredicate(busquedaEmpleados1 -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String busquedaTexto = newValue.toLowerCase();

                    if (busquedaEmpleados1.getNombre().toLowerCase().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en Nombre
                    } else if (busquedaEmpleados1.getApellido().toLowerCase().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en Apellido
                    } else if (busquedaEmpleados1.getDni().toLowerCase().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en DNI
                    } else if (busquedaEmpleados1.getEmail().toLowerCase().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en Email
                    } else if (busquedaEmpleados1.getPuesto().toLowerCase().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en Puesto
                    } else if (busquedaEmpleados1.getTurno().toLowerCase().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en Turno
                    } else if (busquedaEmpleados1.getId().toString().indexOf(busquedaTexto) > -1) {
                        return true; // Significa que encontramos conexion en ID
                    } else
                        return false;
                });
            });
            SortedList<BusquedaEmpleados> organizado = new SortedList<>(filtrado);
            organizado.comparatorProperty().bind(vistaTablaEmpleados.comparatorProperty());
            //Aplicamos el filtrado y la organizacion de la tabla
            vistaTablaEmpleados.setItems(organizado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Abre ventana para añadir empleados
    @FXML
    private void botonAñadirEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vista_add.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Crear un nuevo empleado");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Abre ventana para editar empleados
    @FXML
    private void botonEditarEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vista_modificar.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modificar empleado");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Abre ventana para eliminar empleados
    @FXML
    private void botonEliminarEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("vista_eliminar.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Eliminar empleado");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Obtiene la lista de empleados desde la base de datos
    public List<Empleado> obtenerEmpleadosDesdeBD() {
        List<Empleado> empleados = new ArrayList<>();
        try {
            ConexionBaseDatos conectar = new ConexionBaseDatos();
            Connection conectarBD = conectar.getConnection();
            String consulta = "SELECT id, nombre, apellido, puesto, turno, horas_contrato FROM empleados";
            Statement statement = conectarBD.createStatement();
            ResultSet resultado = statement.executeQuery(consulta);
            while (resultado.next()) {
                Empleado emp = new Empleado(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("puesto"),
                        resultado.getString("turno"),
                        resultado.getInt("horas_contrato")
                );
                empleados.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empleados;
    }

    // Asigna días libres a los empleados, considerando restricciones y equilibrio
    public Map<Integer, List<String>> asignarDiasLibres(List<Empleado> empleados) {
        Map<Integer, List<String>> diasLibres = new HashMap<>();
        String[] diasSemana = {"lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"};
        int diasLibresPorEmpleado = 2;
        // Separar managers y empleados normales
        List<Empleado> managers = new ArrayList<>();
        List<Empleado> empleadosNormales = new ArrayList<>();
        for (Empleado emp : empleados) {
            if ("manager".equalsIgnoreCase(emp.getPuesto())) {
                managers.add(emp);
            } else {
                empleadosNormales.add(emp);
            }
        }
        int totalManagers = managers.size();
        int totalLibresNormales = empleadosNormales.size() * diasLibresPorEmpleado;
        int maxNormalesPorDia = (int) Math.ceil((double) totalLibresNormales / 7);

        // Contadores por día
        Map<String, Integer> contadorNormalesPorDia = new HashMap<>();
        Map<String, Integer> contadorManagersPorDia = new HashMap<>();
        for (String dia : diasSemana) {
            contadorNormalesPorDia.put(dia, 0);
            contadorManagersPorDia.put(dia, 0);
        }
        // 1. Asignar días libres a empleados con restricciones primero
        List<Empleado> empleadosSinRestricciones = new ArrayList<>();
        for (Empleado emp : empleados) {
            List<String> libres = new ArrayList<>();
            List<String> diasRestringidos = new ArrayList<>();
            // Consultar restricciones de este empleado
            try {
                ConexionBaseDatos conectar = new ConexionBaseDatos();
                Connection conectarBD = conectar.getConnection();
                String consulta = "SELECT dia_no_trabajo FROM restricciones WHERE id_empleado = " + emp.getId();
                Statement statement = conectarBD.createStatement();
                ResultSet resultado = statement.executeQuery(consulta);
                if (resultado.next()) {
                    String dias = resultado.getString("dia_no_trabajo");
                    if (dias != null && !dias.isEmpty()) {
                        diasRestringidos.addAll(Arrays.asList(dias.split(",")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Si tiene restricciones, asignar primero esos días
            for (String dia : diasRestringidos) {
                dia = dia.trim();
                if (libres.size() < diasLibresPorEmpleado && !libres.contains(dia)) {
                    if ("manager".equalsIgnoreCase(emp.getPuesto())) {
                        if (contadorManagersPorDia.get(dia) < totalManagers - 2) {
                            libres.add(dia);
                            contadorManagersPorDia.put(dia, contadorManagersPorDia.get(dia) + 1);
                        }
                    } else {
                        if (contadorNormalesPorDia.get(dia) < maxNormalesPorDia) {
                            libres.add(dia);
                            contadorNormalesPorDia.put(dia, contadorNormalesPorDia.get(dia) + 1);
                        }
                    }
                }
            }
            if (!diasRestringidos.isEmpty()) {
                // Completar si faltan días libres
                for (String dia : diasSemana) {
                    if (libres.size() < diasLibresPorEmpleado && !libres.contains(dia)) {
                        if ("manager".equalsIgnoreCase(emp.getPuesto())) {
                            if (contadorManagersPorDia.get(dia) < totalManagers - 2) {
                                libres.add(dia);
                                contadorManagersPorDia.put(dia, contadorManagersPorDia.get(dia) + 1);
                            }
                        } else {
                            if (contadorNormalesPorDia.get(dia) < maxNormalesPorDia) {
                                libres.add(dia);
                                contadorNormalesPorDia.put(dia, contadorNormalesPorDia.get(dia) + 1);
                            }
                        }
                    }
                }
                diasLibres.put(emp.getId(), libres);
            } else {
                empleadosSinRestricciones.add(emp);
            }
        }
        // 2. Asignar días libres al resto de empleados
        for (Empleado emp : empleadosSinRestricciones) {
            List<String> libres = new ArrayList<>();
            for (String dia : diasSemana) {
                if (libres.size() < diasLibresPorEmpleado && !libres.contains(dia)) {
                    if ("manager".equalsIgnoreCase(emp.getPuesto())) {
                        if (contadorManagersPorDia.get(dia) < totalManagers - 2) {
                            libres.add(dia);
                            contadorManagersPorDia.put(dia, contadorManagersPorDia.get(dia) + 1);
                        }
                    } else {
                        if (contadorNormalesPorDia.get(dia) < maxNormalesPorDia) {
                            libres.add(dia);
                            contadorNormalesPorDia.put(dia, contadorNormalesPorDia.get(dia) + 1);
                        }
                    }
                }
            }
            diasLibres.put(emp.getId(), libres);
        }
        for (Map.Entry<Integer, List<String>> entry : diasLibres.entrySet()) {
            System.out.println("Empleado ID: " + entry.getKey() + " - Días libres: " + entry.getValue());
        }
        return diasLibres;
    }

    // Asigna turnos a empleados normales según preferencias y restricciones
    public List<TurnoGenerado> asignarTurnosEmpleados(List<Empleado> empleados, Map<Integer, List<String>> diasLibres, String horaApertura, String horaCierre) {
        List<TurnoGenerado> turnos = new ArrayList<>();
        String[] diasSemana = {"lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"};
        LocalTime apertura = LocalTime.parse(horaApertura, FORMATO_UI);
        LocalTime cierre = LocalTime.parse(horaCierre, FORMATO_UI);
        LocalTime finMañana = LocalTime.of(15, 0);

        // Contadores para equilibrar turnos por franja horaria
        Map<String, Integer> contMañana = new HashMap<>(), contMedio = new HashMap<>(), contTarde = new HashMap<>();
        for (String dia : diasSemana) {
            contMañana.put(dia, 0);
            contMedio.put(dia, 0);
            contTarde.put(dia, 0);
        }

        List<Empleado> sinRestricciones = new ArrayList<>();
        for (Empleado emp : empleados) {
            boolean tieneRestriccion = false;
            String restriccionInicio = null, restriccionFin = null;
            // Consultar si el empleado tiene restricciones horarias
            try {
                ConexionBaseDatos conectar = new ConexionBaseDatos();
                Connection conectarBD = conectar.getConnection();
                String consulta = "SELECT hora_inicio, hora_fin FROM restricciones WHERE id_empleado = " + emp.getId();
                Statement statement = conectarBD.createStatement();
                ResultSet resultado = statement.executeQuery(consulta);
                if (resultado.next()) {
                    restriccionInicio = resultado.getString("hora_inicio");
                    restriccionFin = resultado.getString("hora_fin");
                    if ((restriccionInicio != null && !restriccionInicio.isEmpty()) || (restriccionFin != null && !restriccionFin.isEmpty())) {
                        tieneRestriccion = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tieneRestriccion) {
                // Si tiene restricciones, intentar asignar turnos antes o después de la restricción
                for (String dia : diasSemana) {
                    if (diasLibres.get(emp.getId()).contains(dia)) continue;
                    int horasDia = emp.getHorasContrato() / 5;
                    LocalTime aperturaDia = apertura;
                    LocalTime cierreDia = cierre;

                    if (restriccionInicio != null && !restriccionInicio.isEmpty() &&
                            restriccionFin != null && !restriccionFin.isEmpty()) {
                        LocalTime restriccionIni = LocalTime.parse(restriccionInicio, FORMATO_BD);
                        LocalTime restriccionFi = LocalTime.parse(restriccionFin, FORMATO_BD);

                        // Intentar asignar turno antes de la restricción
                        if (restriccionIni.minusHours(horasDia).isAfter(aperturaDia) || restriccionIni.minusHours(horasDia).equals(aperturaDia)) {
                            LocalTime inicio = aperturaDia;
                            LocalTime fin = inicio.plusHours(horasDia);
                            if (fin.isAfter(restriccionIni)) fin = restriccionIni;
                            if (fin.isAfter(inicio)) {
                                turnos.add(new TurnoGenerado(emp.getId(), emp.getNombre(), dia, inicio.format(FORMATO_UI), fin.format(FORMATO_UI), emp.getPuesto()));
                                // Actualizar contadores según la franja horaria asignada
                                if (inicio.isBefore(finMañana)) {
                                    contMañana.put(dia, contMañana.get(dia) + 1);
                                } else if (inicio.isAfter(finMañana) || inicio.equals(finMañana)) {
                                    contTarde.put(dia, contTarde.get(dia) + 1);
                                } else {
                                    contMedio.put(dia, contMedio.get(dia) + 1);
                                }
                                continue;
                            }
                        }
                        // Si no cabe antes, intentar después de la restricción
                        if (restriccionFi.plusHours(horasDia).isBefore(cierreDia) || restriccionFi.plusHours(horasDia).equals(cierreDia)) {
                            LocalTime inicio = restriccionFi;
                            LocalTime fin = inicio.plusHours(horasDia);
                            if (fin.isAfter(cierreDia)) fin = cierreDia;
                            if (fin.isAfter(inicio)) {
                                turnos.add(new TurnoGenerado(emp.getId(), emp.getNombre(), dia, inicio.format(FORMATO_UI), fin.format(FORMATO_UI), emp.getPuesto()));
                                if (inicio.isBefore(finMañana)) {
                                    contMañana.put(dia, contMañana.get(dia) + 1);
                                } else if (inicio.isAfter(finMañana) || inicio.equals(finMañana)) {
                                    contTarde.put(dia, contTarde.get(dia) + 1);
                                } else {
                                    contMedio.put(dia, contMedio.get(dia) + 1);
                                }
                            }
                        }
                    } else {
                        // Si solo hay restricción de inicio o fin, ajustar el turno en consecuencia
                        LocalTime inicio = aperturaDia;
                        LocalTime fin = cierreDia;
                        if (restriccionInicio != null && !restriccionInicio.isEmpty()) {
                            fin = LocalTime.parse(restriccionInicio, FORMATO_BD);
                        }
                        if (restriccionFin != null && !restriccionFin.isEmpty()) {
                            inicio = LocalTime.parse(restriccionFin, FORMATO_BD);
                        }
                        if (fin.isAfter(inicio.plusHours(horasDia))) {
                            fin = inicio.plusHours(horasDia);
                        }
                        if (fin.isAfter(inicio)) {
                            turnos.add(new TurnoGenerado(emp.getId(), emp.getNombre(), dia, inicio.format(FORMATO_UI), fin.format(FORMATO_UI), emp.getPuesto()));
                            if (inicio.isBefore(finMañana)) {
                                contMañana.put(dia, contMañana.get(dia) + 1);
                            } else if (inicio.isAfter(finMañana) || inicio.equals(finMañana)) {
                                contTarde.put(dia, contTarde.get(dia) + 1);
                            } else {
                                contMedio.put(dia, contMedio.get(dia) + 1);
                            }
                        }
                    }
                }
            } else {
                // Si no tiene restricciones, se asigna después según preferencia
                sinRestricciones.add(emp);
            }
        }

        // Asignar turnos a empleados sin restricciones, equilibrando franjas horarias
        for (String dia : diasSemana) {
            for (Empleado emp : sinRestricciones) {
                if (diasLibres.get(emp.getId()).contains(dia)) continue;
                int horasDia = emp.getHorasContrato() / 5;
                String preferencia = emp.getTurno().toLowerCase();
                LocalTime inicio = null, fin = null;

                // Preferencia de turno de mañana
                if (preferencia.equals("mañana") && contMañana.get(dia) <= contTarde.get(dia) + 1 && contMañana.get(dia) <= contMedio.get(dia) + 1) {
                    inicio = apertura;
                    fin = inicio.plusHours(horasDia);
                    contMañana.put(dia, contMañana.get(dia) + 1);
                    // Preferencia de turno de tarde
                } else if (preferencia.equals("tarde") && contTarde.get(dia) <= contMañana.get(dia) + 1 && contTarde.get(dia) <= contMedio.get(dia) + 1) {
                    inicio = finMañana;
                    fin = inicio.plusHours(horasDia);
                    // Ajustar si se pasa del cierre
                    if (fin.isAfter(cierre)) {
                        fin = cierre;
                        inicio = fin.minusHours(horasDia);
                        if (inicio.isBefore(finMañana)) inicio = finMañana;
                    }
                    contTarde.put(dia, contTarde.get(dia) + 1);
                } else {
                    // Si no hay preferencia clara, asignar donde haya menos empleados
                    int min = Math.min(contMañana.get(dia), Math.min(contMedio.get(dia), contTarde.get(dia)));
                    if (contMañana.get(dia) == min) {
                        inicio = apertura;
                        fin = inicio.plusHours(horasDia);
                        contMañana.put(dia, contMañana.get(dia) + 1);
                    } else if (contMedio.get(dia) == min) {
                        LocalTime horaMedia = apertura.plusHours((cierre.toSecondOfDay() - apertura.toSecondOfDay()) / 3600 / 2);
                        inicio = horaMedia.minusHours(horasDia / 2);
                        fin = inicio.plusHours(horasDia);
                        contMedio.put(dia, contMedio.get(dia) + 1);
                    } else {
                        fin = cierre;
                        inicio = fin.minusHours(horasDia);
                        contTarde.put(dia, contTarde.get(dia) + 1);
                    }
                }
                // Solo añadir el turno si el rango horario es válido
                if (fin.isAfter(inicio)) {
                    turnos.add(new TurnoGenerado(emp.getId(), emp.getNombre(), dia, inicio.format(FORMATO_UI), fin.format(FORMATO_UI), emp.getPuesto()));
                }
            }
        }
        // Ordenar los turnos por id de empleado para facilitar la visualización
        turnos.sort(Comparator.comparingInt(TurnoGenerado::getIdEmpleado));
        return turnos;
    }

    // Asigna turnos a managers alternando apertura y cierre
    public List<TurnoGenerado> asignarTurnosManager(List<Empleado> managers, Map<Integer, List<String>> diasLibres, String horaApertura, String horaCierre) {
        List<TurnoGenerado> turnos = new ArrayList<>();
        String[] diasSemana = {"lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"};
        LocalTime apertura = LocalTime.parse(horaApertura, FORMATO_UI);
        LocalTime cierre = LocalTime.parse(horaCierre, FORMATO_UI);

        for (String dia : diasSemana) {
            // Managers disponibles ese día
            List<Empleado> disponibles = new ArrayList<>();
            for (Empleado m : managers) {
                List<String> libres = diasLibres.get(m.getId());
                if (libres == null || !libres.contains(dia)) {
                    disponibles.add(m);
                }
            }
            if (disponibles.size() < 2) continue;

            // Separar por preferencia
            List<Empleado> aperturaPrefs = new ArrayList<>();
            List<Empleado> cierrePrefs = new ArrayList<>();
            List<Empleado> sinPref = new ArrayList<>();
            for (Empleado m : disponibles) {
                if ("mañana".equalsIgnoreCase(m.getTurno())) aperturaPrefs.add(m);
                else if ("tarde".equalsIgnoreCase(m.getTurno())) cierrePrefs.add(m);
                else sinPref.add(m);
            }

            // Asignar apertura y cierre alternando si hay más managers
            Set<Integer> asignados = new HashSet<>();
            int total = disponibles.size();
            int idxApertura = 0, idxCierre = 0, idxSinPref = 0;
            for (int i = 0; i < total; i++) {
                Empleado m;
                boolean esApertura = (i % 2 == 0);
                if (esApertura) {
                    if (idxApertura < aperturaPrefs.size()) {
                        m = aperturaPrefs.get(idxApertura++);
                    } else if (idxSinPref < sinPref.size()) {
                        m = sinPref.get(idxSinPref++);
                    } else if (idxCierre < cierrePrefs.size()) {
                        m = cierrePrefs.get(idxCierre++);
                    } else {
                        continue;
                    }
                    int horas = m.getHorasContrato() / 5;
                    LocalTime fin = apertura.plusHours(horas);
                    if (fin.isAfter(cierre)) fin = cierre;
                    turnos.add(new TurnoGenerado(
                            m.getId(), m.getNombre(), dia,
                            apertura.format(FORMATO_UI), fin.format(FORMATO_UI), m.getPuesto()
                    ));
                    asignados.add(m.getId());
                } else {
                    if (idxCierre < cierrePrefs.size()) {
                        m = cierrePrefs.get(idxCierre++);
                    } else if (idxSinPref < sinPref.size()) {
                        m = sinPref.get(idxSinPref++);
                    } else if (idxApertura < aperturaPrefs.size()) {
                        m = aperturaPrefs.get(idxApertura++);
                    } else {
                        continue;
                    }
                    if (asignados.contains(m.getId())) continue;
                    int horas = m.getHorasContrato() / 5;
                    LocalTime inicio = cierre.minusHours(horas);
                    if (inicio.isBefore(apertura)) inicio = apertura;
                    turnos.add(new TurnoGenerado(
                            m.getId(), m.getNombre(), dia,
                            inicio.format(FORMATO_UI), cierre.format(FORMATO_UI), m.getPuesto()
                    ));
                    asignados.add(m.getId());
                }
            }
        }
        turnos.sort(Comparator.comparingInt(TurnoGenerado::getIdEmpleado));
        return turnos;
    }

    // Reparte los turnos entre empleados y managers
    List<TurnoGenerado> repartirTurnos(List<Empleado> empleados, Map<Integer, List<String>> diasLibres, String horaApertura, String horaCierre) {
        List<TurnoGenerado> turnos = new ArrayList<>();
        List<TurnoGenerado> turnosEmpleados = new ArrayList<>();
        List<Empleado> empleadosNormales = new ArrayList<>();
        List<Empleado> managers = new ArrayList<>();
        for (Empleado emp : empleados) {
            if ("manager".equalsIgnoreCase(emp.getPuesto())) {
                managers.add(emp);
            } else {
                empleadosNormales.add(emp);
            }
        }
        turnosEmpleados = asignarTurnosEmpleados(empleadosNormales, diasLibres, horaApertura, horaCierre);
        turnos.addAll(turnosEmpleados);
        turnos.addAll(asignarTurnosManager(managers, diasLibres, horaApertura, horaCierre));
        turnos.sort(Comparator.comparingInt(TurnoGenerado::getIdEmpleado));
        return turnos;
    }


    // Guarda los turnos generados en la base de datos
    public void guardarTurnosEnBD(List<TurnoGenerado> turnos) {
        String sql = "INSERT INTO turnos (id_empleado, dia, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        DateTimeFormatter formatoBD = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatoUI = DateTimeFormatter.ofPattern("HH:mm");
        try {
            ConexionBaseDatos conectar = new ConexionBaseDatos();
            Connection conectarBD = conectar.getConnection();
            for (TurnoGenerado turno : turnos) {
                try (java.sql.PreparedStatement statement = conectarBD.prepareStatement(sql)) {
                    statement.setInt(1, turno.getIdEmpleado());
                    statement.setString(2, turno.getDia());
                    // Convertir HH:mm a HH:mm:ss
                    LocalTime inicio = LocalTime.parse(turno.getHoraInicio(), formatoUI);
                    LocalTime fin = LocalTime.parse(turno.getHoraFin(), formatoUI);
                    statement.setString(3, inicio.format(formatoBD));
                    statement.setString(4, fin.format(formatoBD));
                    statement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Muestra los turnos generados en la tabla de la interfaz
    public void mostrarTurnosEnTabla(List<TurnoGenerado> turnos) {
        Map<Integer, TurnoSemanaEmpleado> mapa = new HashMap<>();
        for (TurnoGenerado t : turnos) {
            TurnoSemanaEmpleado tse = mapa.computeIfAbsent(
                    t.getIdEmpleado(),
                    id -> new TurnoSemanaEmpleado(id, t.getNombreEmpleado())
            );
            String horario = t.getHoraInicio() + "-" + t.getHoraFin();
            switch (t.getDia().toLowerCase()) {
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
        ObservableList<TurnoSemanaEmpleado> lista = FXCollections.observableArrayList(mapa.values());
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("empleado"));
        colLunes.setCellValueFactory(new PropertyValueFactory<>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<>("viernes"));
        colSabado.setCellValueFactory(new PropertyValueFactory<>("sabado"));
        colDomingo.setCellValueFactory(new PropertyValueFactory<>("domingo"));
        tablaTurnos.setItems(lista);
    }

    // Acción del botón para generar turnos
    @FXML
    private void botonGenerarTurnosEnAccion() {
        try {
            // 1. Obtener empleados y sus datos
            List<Empleado> empleados = obtenerEmpleadosDesdeBD();
            // 2. Asignar días libres consecutivos aleatorios
            Map<Integer, List<String>> diasLibres = asignarDiasLibres(empleados);
            // 3. Repartir turnos equilibrados por franja horaria
            List<TurnoGenerado> turnos = repartirTurnos(empleados, diasLibres, horaInicioTurno.getText(), horaFinalTurno.getText());
            // 4. Guardar turnos en la base de datos
            guardarTurnosEnBD(turnos);
            // 5. Actualizar la tabla de la vista
            mostrarTurnosEnTabla(turnos);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Mostrar error en la interfaz
        }
    }
}
