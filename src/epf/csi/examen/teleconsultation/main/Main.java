package epf.csi.examen.teleconsultation.main;

import epf.csi.examen.teleconsultation.utils.DatabaseSeeder;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import epf.csi.examen.teleconsultation.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tester la connexion à la base de données d'abord
            System.out.println("Test de connexion à la base de données...");
            if (!DBConnection.testConnection()) {
                System.err.println("ERREUR: Impossible de se connecter à la base de données !");
                System.err.println("Vérifiez que MySQL est démarré et que la base 'teleconsultationmedicale' existe.");
                return;
            }

            // Création de l'admin par défaut s'il n'existe pas déjà
            System.out.println("Initialisation de la base de données...");
            DatabaseSeeder.seedAdminIfNotExists();

            // Affichage de la page de connexion
            System.out.println("Lancement de l'interface utilisateur...");
            LoginView loginView = new LoginView(primaryStage);
            Scene scene = new Scene(loginView.getView(), 400, 300);
            scene.getStylesheets().add(getClass().getResource("/epf/csi/examen/teleconsultation/ressources/carelinker.css").toExternalForm());


            primaryStage.setTitle("CareLinker - Connexion");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("Application démarrée avec succès !");
            System.out.println("Utilisez admin@carelinker.com / admin123 pour vous connecter comme admin.");

        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}