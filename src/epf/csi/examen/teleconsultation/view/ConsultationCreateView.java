package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.dao.PatientDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDateTime;

public class ConsultationCreateView {

	private Utilisateur medecin; // Ajoute ceci
    private final Stage stage;
    private final DashboardMedecinView dashboard;

    private ObservableList<Utilisateur> patientsList;

    // Contrôles du formulaire
    private TextField tfSearch;
    private ListView<Utilisateur> lvPatients;
    private Utilisateur selectedPatient;
    private DatePicker dpDate;
    private TextArea taDescription;
    private Label lblMessage;
    // En haut de la classe, stockez le médecin connecté (par exemple après connexion)
    private Utilisateur medecinConnecte;

    public void setMedecinConnecte(Utilisateur medecin) {
        this.medecinConnecte = medecin;
    }
    public ConsultationCreateView(int medecinId, Stage stage, DashboardMedecinView dashboard) {
        this.medecin = medecin;
        this.stage = stage;
        this.dashboard = dashboard;
        loadPatients();
    }

    private void loadPatients() {
        try {
            Connection conn = DBConnection.getConnection();
            PatientDAO patientDAO = new PatientDAO(conn);
            patientsList = patientDAO.getAllPatients();

            lvPatients = new ListView<>(patientsList);
            lvPatients.setPrefHeight(150);

            lvPatients.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Utilisateur user, boolean empty) {
                    super.updateItem(user, empty);
                    setText(empty || user == null ? null : user.getNom());
                }
            });

            lvPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                selectedPatient = newVal;
            });

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage = new Label("Erreur lors du chargement des patients.");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Créer une nouvelle consultation");
        title.setFont(Font.font("Arial", 22));

        Label lblPatient = new Label("Patient :");

        tfSearch = new TextField();
        tfSearch.setPromptText("Rechercher un patient...");
        tfSearch.setPrefWidth(300);

        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            ObservableList<Utilisateur> filtered = FXCollections.observableArrayList(
                patientsList.filtered(u -> u.getNom().toLowerCase().contains(newVal.toLowerCase()))
            );
            lvPatients.setItems(filtered);
        });

        Label lblDate = new Label("Date de la consultation :");
        dpDate = new DatePicker();
        dpDate.setPrefWidth(300);

        Label lblDesc = new Label("Description :");
        taDescription = new TextArea();
        taDescription.setPrefWidth(300);
        taDescription.setPrefHeight(100);
        taDescription.setWrapText(true);

        lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: red;");

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button btnCreer = new Button("Créer");
        Button btnRetour = new Button("Retour au dashboard");
        buttons.getChildren().addAll(btnCreer, btnRetour);

        btnCreer.setOnAction(e -> creerConsultation());
        btnRetour.setOnAction(e -> stage.getScene().setRoot(dashboard.getView()));

        root.getChildren().addAll(
            title,
            lblPatient, tfSearch, lvPatients,
            lblDate, dpDate,
            lblDesc, taDescription,
            lblMessage,
            buttons
        );

        return root;
    }

private void creerConsultation() {
    Utilisateur patient = selectedPatient;


    if (patient == null) {
        lblMessage.setStyle("-fx-text-fill: red;");
        lblMessage.setText("Veuillez sélectionner un patient.");
        return;
    }

    if (dpDate.getValue() == null) {
        lblMessage.setStyle("-fx-text-fill: red;");
        lblMessage.setText("Veuillez sélectionner une date.");
        return;
    }

    String description = taDescription.getText().trim();
    if (description.isEmpty()) {
        lblMessage.setStyle("-fx-text-fill: red;");
        lblMessage.setText("Veuillez saisir une description.");
        return;
    }

    // Création de la date/heure à 12h00 par défaut
    LocalDateTime dateConsult = dpDate.getValue().atTime(12, 0);

    // Création de la consultation via le contrôleur
    ConsultationController controller = new ConsultationController();
    boolean success = controller.creerConsultation(patient, medecinConnecte, dateConsult, description);

    if (success) {
        lblMessage.setStyle("-fx-text-fill: green;");
        lblMessage.setText("Consultation créée avec succès !");
        lvPatients.getSelectionModel().clearSelection();
        dpDate.setValue(null);
        taDescription.clear();
    } else {
        lblMessage.setStyle("-fx-text-fill: red;");
        lblMessage.setText("Erreur lors de la création de la consultation.");
    }
}
}
