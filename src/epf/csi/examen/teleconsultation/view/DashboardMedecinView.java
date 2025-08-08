package epf.csi.examen.teleconsultation.view;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.SQLException;

import epf.csi.examen.teleconsultation.dao.ConsultationDAO;
import epf.csi.examen.teleconsultation.dao.PrescriptionDAO;
import epf.csi.examen.teleconsultation.dao.RendezVousDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DashboardMedecinView {

    private final Utilisateur medecin;
    private final BorderPane root;

    public DashboardMedecinView(Stage stage, Utilisateur medecin) throws SQLException {
        this.medecin = medecin;
        this.root = buildView(stage);

        Scene scene = new Scene(root, 1000, 600);
        // Intégration du CSS externe
        scene.getStylesheets().add(getClass().getResource("/epf/csi/examen/teleconsultation/ressources/carelinker.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Dashboard Médecin");
        stage.show();
    }

    private BorderPane buildView(Stage stage) throws SQLException {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        // Sidebar gauche
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.getStyleClass().add("sidebar");

        // Logo + Nom App
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/epf/csi/examen/teleconsultation/ressources/images/logo.png")));
        logoView.setFitWidth(50);
        logoView.setFitHeight(50);

        Label appTitle = new Label("CareLinker");
        appTitle.getStyleClass().add("sidebar-title");

        VBox logoBox = new VBox(logoView, appTitle);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setSpacing(10);

        // Boutons menu
        Button btnConsultations = new Button("Mes consultations");
        Button btnNouvelleConsultation = new Button("Nouvelle consultation");
        Button btnRendezVous = new Button("Mes rendez-vous");
        Button btnPrescriptions = new Button("Mes ordonnances");
        Button btnCarnetSante = new Button("Carnet de Santé");
        Button btnMessagerie = new Button("Messagerie");
        Button btnTeleconsultation = new Button("Téléconsultation Vidéo");
        Button btnDeconnexion = new Button("Déconnexion");

        // Appliquer styles CSS
        Button[] menuButtons = {
            btnConsultations, btnNouvelleConsultation, btnRendezVous,
            btnPrescriptions, btnCarnetSante, btnMessagerie, btnTeleconsultation
        };
        for (Button btn : menuButtons) {
            btn.getStyleClass().add("sidebar-button");
        }
        btnDeconnexion.getStyleClass().addAll("sidebar-button", "logout-button");

        sidebar.getChildren().addAll(logoBox);
        sidebar.getChildren().addAll(menuButtons);
        sidebar.getChildren().add(btnDeconnexion);

        // Contenu principal
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getStyleClass().add("dashboard-content");

        Label dashboardTitle = new Label("Bienvenue Dr. " + medecin.getNom());
        dashboardTitle.getStyleClass().add("dashboard-title");

        // Cartes statistiques
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
     // Récupérer les données réelles
        Connection conn = DBConnection.getConnection();
        ConsultationDAO consultationDAO = new ConsultationDAO(conn);
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO(conn);
        RendezVousDAO rendezVousDAO = new RendezVousDAO(conn);

        int nbConsultations = consultationDAO.countConsultationsByMedecin(medecin.getId());
        int nbPrescriptions = prescriptionDAO.countPrescriptionsByMedecin(medecin.getId());
        int nbRendezVous = rendezVousDAO.countRendezVousByMedecin(medecin.getId());


        VBox card1 = createStatCard("Consultations", String.valueOf(nbConsultations));
        VBox card2 = createStatCard("Ordonnances", String.valueOf(nbPrescriptions));
        VBox card3 = createStatCard("Rendez-vous", String.valueOf(nbRendezVous));


        statsBox.getChildren().addAll(card1, card2, card3);

        mainContent.getChildren().addAll(dashboardTitle, statsBox);

        // Ajout dans le BorderPane
        root.setLeft(sidebar);
        root.setCenter(mainContent);

        // Actions boutons
        btnConsultations.setOnAction(e -> {
            VBox consultationsView = new ConsultationListView(medecin.getId(), stage, this).getView();
            stage.getScene().setRoot(consultationsView);
        });

        btnNouvelleConsultation.setOnAction(e -> {
            ConsultationCreateView consultationCreateView = new ConsultationCreateView(medecin.getId(), stage, this);
            consultationCreateView.setMedecinConnecte(medecin); // ← Cette ligne manquait
            stage.getScene().setRoot(consultationCreateView.getView());
        });


        btnRendezVous.setOnAction(e -> {
            RendezVousListView rdvListView = new RendezVousListView(medecin.getId(), stage, this);
            stage.getScene().setRoot(rdvListView.getView());
        });

        btnPrescriptions.setOnAction(e -> {
            PrescriptionView prescriptionView = new PrescriptionView();
            try {
                prescriptionView.start(stage, medecin);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        btnCarnetSante.setOnAction(e -> {
            try {
                new CarnetSanteMedecinView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir le carnet de santé.");
            }
        });

        btnMessagerie.setOnAction(e -> {
            try {
                if (medecin == null || medecin.getId() == 0) {
                    showAlert("Erreur", "Médecin non valide ou ID manquant.");
                    return;
                }

                MessageMedecinView messageView = new MessageMedecinView(medecin);
                stage.setScene(messageView.getScene(stage));
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Erreur base de données", "Impossible d'ouvrir la messagerie : " + ex.getMessage());
            }
        });

        btnTeleconsultation.setOnAction(e -> {
            TeleconsultationVideoView.open(medecin); // ou autre objet Utilisateur
        });



        btnDeconnexion.setOnAction(e -> stage.close());

        return root;
    }




    private VBox createStatCard(String label, String value) {
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");

        Label labelLabel = new Label(label);
        labelLabel.getStyleClass().add("card-label");

        VBox card = new VBox(5, valueLabel, labelLabel);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("stat-card");

        return card;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return root;
    }
}