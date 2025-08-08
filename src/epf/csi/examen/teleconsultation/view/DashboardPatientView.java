package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.controller.PrescriptionController;
import epf.csi.examen.teleconsultation.controller.RendezVousController;
import epf.csi.examen.teleconsultation.model.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class DashboardPatientView {

    private final int patientId;
    private final RendezVousController rdvController = new RendezVousController();
    private final ConsultationController consultationController = new ConsultationController();
    private final PrescriptionController prescriptionController = new PrescriptionController();

    private TableView<RendezVous> tableView;

    public DashboardPatientView(int patientId) {
        this.patientId = patientId;
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // ========== HEADER ===========
        Label headerTitle = new Label("Dashboard Patient");
        headerTitle.getStyleClass().add("header-title");
        HBox header = new HBox(headerTitle);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2E86C1;");
        header.setAlignment(Pos.CENTER_LEFT);
        headerTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        root.setTop(header);

        // ========== SIDEBAR ===========
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #1B4F72;");
        sidebar.setPrefWidth(200);

        Button btnDashboard = new Button("Dashboard");
        Button btnNouveauRDV = new Button("Nouveau RDV");
        Button btnOrdonnances = new Button("Ordonnances");
        Button btnConsultations = new Button("Consultations");
        Button btnMessages = new Button("Messagerie");
        Button btnCarnetSante = new Button("Carnet de santé");
        Button btnDeconnexion = new Button("Déconnexion");

        // Style sidebar buttons
        for (Button btn : new Button[]{btnDashboard, btnNouveauRDV, btnOrdonnances, btnConsultations, btnMessages, btnCarnetSante, btnDeconnexion}) {
            btn.getStyleClass().add("sidebar-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        // Actions Sidebar boutons
        btnDashboard.setOnAction(e -> refreshTable());
        btnNouveauRDV.setOnAction(e -> {
			try {
				openPriseRDVForm();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnOrdonnances.setOnAction(e -> new PrescriptionPatientView().start(new Stage(), patientId));
        btnConsultations.setOnAction(e -> new ConsultationPatientView(patientId).start(new Stage()));
        btnMessages.setOnAction(e -> new MessageriePatientView(patientId).start(new Stage()));
        btnCarnetSante.setOnAction(e -> {
            try {
                CarnetSantePatientView vue = new CarnetSantePatientView(patientId);
                Stage newStage = new Stage();
                Scene scene = vue.getScene(newStage);
                newStage.setScene(scene);
                newStage.setTitle("Carnet de Santé - Patient");
                newStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Erreur ouverture carnet santé.").showAndWait();
            }
        });
        btnDeconnexion.setOnAction(e -> {
            // TODO: ajouter la logique de déconnexion, puis fermer la fenêtre
            stage.close();
        });

        sidebar.getChildren().addAll(
                btnDashboard,
                btnNouveauRDV,
                btnOrdonnances,
                btnConsultations,
                btnMessages,
                btnCarnetSante,
                new Separator(),
                btnDeconnexion
        );

        root.setLeft(sidebar);

        // ========== CONTENU CENTRAL ===========
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(20));

        // Cards statistiques
        HBox cardsBox = new HBox(15);
        cardsBox.getStyleClass().add("card-container");
        cardsBox.setAlignment(Pos.CENTER);

        Label cardRdv = createStatCard("Mes Rendez-vous", String.valueOf(countRendezVous()));
        Label cardConsult = createStatCard("Mes Consultations", String.valueOf(countConsultations()));
        Label cardOrdo = createStatCard("Mes Ordonnances", String.valueOf(countOrdonnances()));

        cardsBox.getChildren().addAll(cardRdv, cardConsult, cardOrdo);

        // Tableau des rendez-vous
        tableView = new TableView<>();
        setupTableColumns();
        refreshTable();

        centerBox.getChildren().addAll(cardsBox, tableView);

        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/epf/csi/examen/teleconsultation/ressources/carelinker.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Dashboard Patient");
        stage.show();
    }

    private Label createStatCard(String title, String value) {
        Label label = new Label(title + "\n" + value);
        label.getStyleClass().add("dashboard-card");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private void setupTableColumns() {
        TableColumn<RendezVous, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<RendezVous, Integer> medecinCol = new TableColumn<>("Médecin");
        medecinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getMedecinId()));

        TableColumn<RendezVous, String> dateCol = new TableColumn<>("Date et Heure");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateHeure().toString()));

        TableColumn<RendezVous, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatut()));

        tableView.getColumns().addAll(idCol, medecinCol, dateCol, statutCol);
    }

    private void refreshTable() {
        List<RendezVous> rdvs = rdvController.listerRendezVousPatient(patientId);
        ObservableList<RendezVous> observableList = FXCollections.observableArrayList(rdvs);
        tableView.setItems(observableList);
    }

    private void openPriseRDVForm() throws SQLException {
        Stage formStage = new Stage();
        PriseRDVForm priseRDVForm = new PriseRDVForm(patientId, this);
        priseRDVForm.start(formStage);
    }

    private int countRendezVous() {
        return rdvController.listerRendezVousPatient(patientId).size();
    }

    private int countConsultations() {
        return consultationController.countConsultationsByPatient(patientId);
    }

    private int countOrdonnances() {
        return prescriptionController.countPrescriptionsByPatient(patientId);
    }

    // Méthode publique pour rafraîchir après prise RDV ou autres
    public void refreshAfterCreation() {
        refreshTable();
    }
}
