package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardMedecinView {

    private final Utilisateur medecin;
    private final VBox root;

    public DashboardMedecinView(Stage stage, Utilisateur medecin) {
        this.medecin = medecin;
        this.root = buildView(stage);

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Dashboard Médecin");
        stage.show();
    }

    private VBox buildView(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Tableau de bord - Médecin");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        HBox menu = new HBox(10);
        menu.setAlignment(Pos.CENTER);

        // Boutons
        Button btnConsultations = new Button("Mes consultations");
        Button btnNouvelleConsultation = new Button("Nouvelle consultation");
        Button btnRendezVous = new Button("Mes rendez-vous");
        Button btnPrescriptions = new Button("Mes ordonnances");
        Button btnDeconnexion = new Button("Déconnexion");

        menu.getChildren().addAll(
            btnConsultations,
            btnNouvelleConsultation,
            btnRendezVous,
            btnPrescriptions,
            btnDeconnexion
        );

        root.getChildren().addAll(title, menu);

        // Actions des boutons
        btnRendezVous.setOnAction(e -> {
            RendezVousListView rdvListView = new RendezVousListView(medecin.getId(), stage, this);
            stage.getScene().setRoot(rdvListView.getView());
        });

        btnConsultations.setOnAction(e -> {
            VBox consultationsView = new ConsultationListView(medecin.getId(), stage, this).getView();
            stage.getScene().setRoot(consultationsView);
        });

        btnNouvelleConsultation.setOnAction(e -> {
            ConsultationCreateView consultationCreateView = new ConsultationCreateView(medecin.getId(), stage, this);
            stage.getScene().setRoot(consultationCreateView.getView());
        });

        btnPrescriptions.setOnAction(e -> {
            PrescriptionView prescriptionView = new PrescriptionView();
            prescriptionView.start(stage, medecin);  // <-- on passe l'objet Utilisateur complet
        });

        btnDeconnexion.setOnAction(e -> stage.close());

        return root;
    }

    public VBox getView() {
        return root;
    }
}
