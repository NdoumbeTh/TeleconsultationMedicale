package epf.csi.examen.teleconsultation.main;

import epf.csi.examen.teleconsultation.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane root;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        mainScene = new Scene(root, 900, 600);

        // Écran d'accueil qui laisse choisir le rôle
        AccueilView accueilView = new AccueilView();
        root.setCenter(accueilView.getView());

        primaryStage.setTitle("CareLinker - Plateforme de téléconsultation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // 👉 Insérer ici la méthode buildNavigationBar(String role)
    private HBox buildNavigationBar(String role) {
        HBox navBar = new HBox(10);
        navBar.setStyle("-fx-padding: 10; -fx-background-color: #E0E0E0;");
        
        // Bouton commun pour revenir à l'accueil
        Button btnAccueil = new Button("Accueil");
        btnAccueil.setOnAction(e -> {
            // Remettre l'écran d'accueil qui offre le choix du rôle
            AccueilView accueilView = new AccueilView();
            root.setTop(null); // On enlève la navBar pour l'accueil si besoin
            root.setCenter(accueilView.getView());
        });
        navBar.getChildren().add(btnAccueil);
        
        if ("patient".equalsIgnoreCase(role)) {
            Button btnDashboard = new Button("Dashboard");
            Button btnRdv = new Button("Prendre RDV");
            Button btnCarnet = new Button("Carnet de Santé");
            Button btnPrescription = new Button("Prescriptions");

            // Actions spécifiques au patient
            btnDashboard.setOnAction(e -> root.setCenter(new DashboardPatientView().getView()));
            btnRdv.setOnAction(e -> root.setCenter(new RendezVousView().getView()));
            btnCarnet.setOnAction(e -> root.setCenter(new CarnetSanteView().getView()));
            btnCarnet.setOnAction(e -> root.setCenter(new AccueilView().getView()));
            btnPrescription.setOnAction(e -> root.setCenter(new PrescriptionView().getView()));
            
            navBar.getChildren().addAll(btnDashboard, btnRdv, btnCarnet, btnPrescription);
            
        } else if ("medecin".equalsIgnoreCase(role)) {
            Button btnDashboard = new Button("Dashboard");
            Button btnPatients = new Button("Liste des patients");
            Button btnConsultations = new Button("Consultations");
            Button btnPrescrire = new Button("Prescrire");

            // Actions pour le médecin
            btnDashboard.setOnAction(e -> root.setCenter(new DashboardMedecinView().getView()));
            // Vous pourrez compléter ces actions avec de véritables vues
            btnPatients.setOnAction(e -> {
                // Exemple : afficher la vue des patients
                // root.setCenter(new PatientsView().getView());
                System.out.println("Affichage de la liste des patients");
            });
            btnConsultations.setOnAction(e -> {
                // Exemple : afficher la vue des consultations
                System.out.println("Affichage des consultations");
            });
            btnPrescrire.setOnAction(e -> {
                // Exemple : afficher la vue de prescription
                System.out.println("Vue pour prescrire un traitement");
            });
            
            navBar.getChildren().addAll(btnDashboard, btnPatients, btnConsultations, btnPrescrire);
            
        } else if ("admin".equalsIgnoreCase(role)) {
            Button btnDashboard = new Button("Dashboard");
            Button btnUtilisateurs = new Button("Gérer Utilisateurs");
            Button btnStatistiques = new Button("Statistiques");
            Button btnConfiguration = new Button("Configuration");

            // Actions pour l'administrateur
            btnDashboard.setOnAction(e -> root.setCenter(new DashboardAdminView().getView()));
            btnUtilisateurs.setOnAction(e -> {
                // Exemple : afficher la vue de gestion des utilisateurs
                System.out.println("Gestion des utilisateurs");
            });
            btnStatistiques.setOnAction(e -> {
                // Exemple : afficher la vue des statistiques
                System.out.println("Affichage des statistiques");
            });
            btnConfiguration.setOnAction(e -> {
                // Exemple : afficher la configuration du système
                System.out.println("Configuration du système");
            });

            navBar.getChildren().addAll(btnDashboard, btnUtilisateurs, btnStatistiques, btnConfiguration);
        }
        
        return navBar;
    }

    // Méthodes utilitaires pour charger chaque interface
    public void afficherDashboardPatient() {
        root.setTop(buildNavigationBar("patient"));
        root.setCenter(new DashboardPatientView().getView());
    }

    public void afficherDashboardMedecin() {
        root.setTop(buildNavigationBar("medecin"));
        root.setCenter(new DashboardMedecinView().getView());
    }

    public void afficherDashboardAdmin() {
        root.setTop(buildNavigationBar("admin"));
        root.setCenter(new DashboardAdminView().getView());
    }

    public void afficherAccueil() {
        root.setTop(null);
        root.setCenter(new AccueilView().getView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
