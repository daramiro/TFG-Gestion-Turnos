package miempresa;

public class BusquedaEmpleados {
    Integer id, horasContrato;
    String nombre, apellido, dni, email, puesto, turno;

    public BusquedaEmpleados(Integer id, String nombre, String apellido, String dni, String email, String puesto, String turno, Integer horasContrato) {
        this.id = id;
        this.horasContrato = horasContrato;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.puesto = puesto;
        this.turno = turno;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHorasContrato() {
        return horasContrato;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getPuesto() {
        return puesto;
    }

    public String getTurno() {
        return turno;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHorasContrato(Integer horasContrato) {
        this.horasContrato = horasContrato;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }


}
