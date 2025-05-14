package miempresa;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TurnoSemanaEmpleado {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty empleado;
    private final SimpleStringProperty lunes;
    private final SimpleStringProperty martes;
    private final SimpleStringProperty miercoles;
    private final SimpleStringProperty jueves;
    private final SimpleStringProperty viernes;
    private final SimpleStringProperty sabado;
    private final SimpleStringProperty domingo;

    public TurnoSemanaEmpleado(int id, String empleado) {
        this.id = new SimpleIntegerProperty(id);
        this.empleado = new SimpleStringProperty(empleado);
        this.lunes = new SimpleStringProperty("Libre");
        this.martes = new SimpleStringProperty("Libre");
        this.miercoles = new SimpleStringProperty("Libre");
        this.jueves = new SimpleStringProperty("Libre");
        this.viernes = new SimpleStringProperty("Libre");
        this.sabado = new SimpleStringProperty("Libre");
        this.domingo = new SimpleStringProperty("Libre");
    }

    public int getId() {
        return id.get();
    }

    public String getEmpleado() {
        return empleado.get();
    }

    public String getLunes() {
        return lunes.get();
    }

    public void setLunes(String value) {
        lunes.set(value);
    }

    public String getMartes() {
        return martes.get();
    }

    public void setMartes(String value) {
        martes.set(value);
    }

    public String getMiercoles() {
        return miercoles.get();
    }

    public void setMiercoles(String value) {
        miercoles.set(value);
    }

    public String getJueves() {
        return jueves.get();
    }

    public void setJueves(String value) {
        jueves.set(value);
    }

    public String getViernes() {
        return viernes.get();
    }

    public void setViernes(String value) {
        viernes.set(value);
    }

    public String getSabado() {
        return sabado.get();
    }

    public void setSabado(String value) {
        sabado.set(value);
    }

    public String getDomingo() {
        return domingo.get();
    }

    public void setDomingo(String value) {
        domingo.set(value);
    }
}
