package miempresa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        stage.setTitle("Gestión de Turnos");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

