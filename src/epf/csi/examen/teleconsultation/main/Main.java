package epf.csi.examen.teleconsultation.main;

import epf.csi.examen.teleconsultation.utils.DatabaseSeeder;
import epf.csi.examen.teleconsultation.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création de l'admin par défaut s'il n'existe pas déjà
        DatabaseSeeder.seedAdminIfNotExists();

        // Affichage de la page de connexion
        LoginView loginView = new LoginView(primaryStage);
        Scene scene = new Scene(loginView.getView(), 400, 300);  // getView() ne doit pas être null


        primaryStage.setTitle("CareLinker - Connexion");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
