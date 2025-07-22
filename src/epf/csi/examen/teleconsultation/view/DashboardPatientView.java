package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.RendezVousController;
import epf.csi.examen.teleconsultation.model.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardPatientView {

    private final int patientId; // id du patient connecté
    private final RendezVousController rdvController = new RendezVousController();

    private TableView<RendezVous> tableView;

    public DashboardPatientView(int patientId) {
        this.patientId = patientId;
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Label title = new Label("Mes Rendez-vous");
        title.setStyle("-fx-font-size: 20px; -fx-padding: 10;");

        tableView = new TableView<>();
        setupTableColumns();

        Button btnNouveauRDV = new Button("Prendre un nouveau rendez-vous");
        btnNouveauRDV.setOnAction(e -> openPriseRDVForm());

        HBox topBox = new HBox(10, title, btnNouveauRDV);
        topBox.setPadding(new Insets(10));

        root.setTop(topBox);
        root.setCenter(tableView);

        refreshTable();

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Dashboard Patient");
        stage.show();
    }

    private void setupTableColumns() {
        TableColumn<RendezVous, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<RendezVous, Integer> medecinCol = new TableColumn<>("Médecin ID");
        medecinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getMedecinId()));

        TableColumn<RendezVous, LocalDateTime> dateCol = new TableColumn<>("Date et Heure");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDateHeure()));

        TableColumn<RendezVous, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatut()));

        tableView.getColumns().addAll(idCol, medecinCol, dateCol, statutCol);
    }

    private void refreshTable() {
        List<RendezVous> rdvs = rdvController.listerRendezVousPatient(patientId);
        ObservableList<RendezVous> observableList = FXCollections.observableArrayList(rdvs);
        tableView.setItems(observableList);
    }

    private void openPriseRDVForm() {
        Stage formStage = new Stage();
        PriseRDVForm priseRDVForm = new PriseRDVForm(patientId, this);
        priseRDVForm.start(formStage);
    }

    // Méthode appelée après prise RDV pour rafraîchir la liste
    public void refreshAfterCreation() {
        refreshTable();
    }
}
