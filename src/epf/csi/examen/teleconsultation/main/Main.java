package epf.csi.examen.teleconsultation.main;

import epf.csi.examen.teleconsultation.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Affiche la vue de connexion dès le lancement
        new LoginView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
