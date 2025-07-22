package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultationCreateView {

    private final int medecinId;
    private final Stage stage;
    private final DashboardMedecinView dashboard;

    private ObservableList<Utilisateur> patientsList;

    // Contrôles du formulaire
    private ComboBox<Utilisateur> cbPatients;
    private DatePicker dpDate;
    private TextArea taDescription;
    private Label lblMessage;

    public ConsultationCreateView(int medecinId, Stage stage, DashboardMedecinView dashboard) {
        this.medecinId = medecinId;
        this.stage = stage;
        this.dashboard = dashboard;
        loadPatients(); // Charger la liste des patients
    }

    // Simulation ou appel réel pour récupérer la liste des patients depuis la base
    private void loadPatients() {
        // TODO: Remplacer par appel réel via ConsultationController ou DAO
        patientsList = FXCollections.observableArrayList();

        // Exemple statique
        patientsList.add(new Utilisateur(1, "Diop Samba", "diop@example.com", "patient", null));
        patientsList.add(new Utilisateur(2, "Fall Awa", "fall@example.com", "patient", null));
        patientsList.add(new Utilisateur(3, "Ngom Mame", "ngom@example.com", "patient", null));
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Créer une nouvelle consultation");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // Patient
        Label lblPatient = new Label("Patient :");
        cbPatients = new ComboBox<>(patientsList);
        cbPatients.setPrefWidth(300);
        cbPatients.setPromptText("Sélectionnez un patient");

        // Date de consultation
        Label lblDate = new Label("Date de la consultation :");
        dpDate = new DatePicker();
        dpDate.setPrefWidth(300);

        // Description
        Label lblDesc = new Label("Description :");
        taDescription = new TextArea();
        taDescription.setPrefWidth(300);
        taDescription.setPrefHeight(100);
        taDescription.setWrapText(true);

        // Message d'erreur ou succès
        lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: red;");

        // Boutons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button btnCreer = new Button("Créer");
        Button btnRetour = new Button("Retour au dashboard");

        buttons.getChildren().addAll(btnCreer, btnRetour);

        // Actions boutons
        btnCreer.setOnAction(e -> creerConsultation());
        btnRetour.setOnAction(e -> stage.getScene().setRoot(dashboard.getView()));

        // Ajout des éléments à root
        root.getChildren().addAll(
            title,
            lblPatient, cbPatients,
            lblDate, dpDate,
            lblDesc, taDescription,
            lblMessage,
            buttons
        );

        return root;
    }

    private void creerConsultation() {
        Utilisateur patient = cbPatients.getValue();
        if (patient == null) {
            lblMessage.setText("Veuillez sélectionner un patient.");
            return;
        }

        if (dpDate.getValue() == null) {
            lblMessage.setText("Veuillez sélectionner une date.");
            return;
        }

        String description = taDescription.getText().trim();
        if (description.isEmpty()) {
            lblMessage.setText("Veuillez saisir une description.");
            return;
        }

        // Conversion LocalDatePicker -> LocalDateTime (à midi par exemple)
        LocalDateTime dateConsult = dpDate.getValue().atTime(12, 0);

        // Appeler le controller pour enregistrer (à implémenter)
        ConsultationController controller = new ConsultationController();
        boolean success = controller.creerConsultation(medecinId, patient.getId(), dateConsult, description);

        if (success) {
            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setText("Consultation créée avec succès !");
            // Optionnel : vider formulaire
            cbPatients.getSelectionModel().clearSelection();
            dpDate.setValue(null);
            taDescription.clear();
        } else {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Erreur lors de la création de la consultation.");
        }
    }
}
