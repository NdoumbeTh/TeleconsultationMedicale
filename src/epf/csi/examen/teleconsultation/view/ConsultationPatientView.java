package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.model.Consultation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class ConsultationPatientView {

    private final int patientId;
    private final ConsultationController controller = new ConsultationController();
    private TableView<Consultation> table;

    public ConsultationPatientView(int patientId) {
        this.patientId = patientId;
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Label title = new Label("Mes consultations");
        title.setStyle("-fx-font-size: 20px; -fx-padding: 10;");

        table = new TableView<>();
        setupColumns();
        loadConsultations();

        root.setTop(title);
        root.setCenter(table);

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);
        stage.setTitle("Consultations du patient");
        stage.show();
    }

    private void setupColumns() {
        TableColumn<Consultation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());

        TableColumn<Consultation, String> medecinCol = new TableColumn<>("Médecin");
        medecinCol.setCellValueFactory(c -> c.getValue().nomMedecinProperty());

        TableColumn<Consultation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDateHeure().toString()
        ));

        TableColumn<Consultation, String> motifCol = new TableColumn<>("Motif");
        motifCol.setCellValueFactory(c -> c.getValue().motifProperty());

        TableColumn<Consultation, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(c -> c.getValue().statutProperty());

        table.getColumns().addAll(idCol, medecinCol, dateCol, motifCol, statutCol);
    }

    private void loadConsultations() {
        List<Consultation> consultations = controller.listerConsultationsPatient(patientId);
        ObservableList<Consultation> data = FXCollections.observableArrayList(consultations);
        table.setItems(data);
    }
}
