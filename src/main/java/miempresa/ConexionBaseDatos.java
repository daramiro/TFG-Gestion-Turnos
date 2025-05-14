package miempresa;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBaseDatos {

    public Connection enlaceBaseDatos;

    public Connection getConnection() {
        String nombreBaseDatos = "gestion_turnos";
        String usuarioBaseDatos = "root";
        String passBaseDatos = "1234";
        String url = "jdbc:mysql://localhost:3306/" + nombreBaseDatos;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Carga el driver de MySQL
            enlaceBaseDatos = DriverManager.getConnection(url, usuarioBaseDatos, passBaseDatos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return enlaceBaseDatos;

    }

}
