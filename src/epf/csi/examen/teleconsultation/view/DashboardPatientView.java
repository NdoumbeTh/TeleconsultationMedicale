package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.RendezVousController;
import epf.csi.examen.teleconsultation.model.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    // Titre à gauche
    Label title = new Label("Mes Rendez-vous");
    title.setStyle("-fx-font-size: 20px; -fx-padding: 10;");

    // Boutons à droite
    Button btnNouveauRDV = new Button("Prendre un nouveau rendez-vous");
    btnNouveauRDV.setOnAction(e -> openPriseRDVForm());

    Button btnOrdonnances = new Button("Voir mes ordonnances");
    btnOrdonnances.setOnAction(e -> new PrescriptionPatientView().start(new Stage(), patientId));
    
    Button btnConsultations = new Button("Voir mes consultations");
    btnConsultations.setOnAction(e -> {
        new ConsultationPatientView(patientId).start(new Stage());
    });
    Button btnMessages = new Button("Messagerie");
    btnMessages.setOnAction(e -> {
        // Vous pouvez améliorer ça si vous avez un système pour retrouver l’ID du médecin principal du patient
        int medecinId = 1; // <-- Remplacez par le bon ID si dynamique
        MessageriePatientView vue = new MessageriePatientView(patientId, medecinId);
        vue.start(new Stage());
    });


    


    // Conteneur horizontal haut : Titre à gauche, boutons à droite
    HBox leftBox = new HBox(title);
    HBox rightBox = new HBox(10, btnNouveauRDV, btnOrdonnances, btnConsultations,btnMessages);
    leftBox.setAlignment(Pos.CENTER_LEFT);
    rightBox.setAlignment(Pos.CENTER_RIGHT);

    BorderPane topPane = new BorderPane();
    topPane.setLeft(leftBox);
    topPane.setRight(rightBox);
    topPane.setPadding(new Insets(10));

    // Tableau des rendez-vous
    tableView = new TableView<>();
    setupTableColumns();
    refreshTable();

    root.setTop(topPane);
    root.setCenter(tableView);

    Scene scene = new Scene(root, 800, 450);
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
