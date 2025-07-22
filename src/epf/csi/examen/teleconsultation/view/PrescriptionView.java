package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.PrescriptionController;
import epf.csi.examen.teleconsultation.model.Prescription;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrescriptionView {

    private TableView<Prescription> table;
    private ObservableList<Prescription> prescriptionData;

    public void start(Stage stage, Utilisateur medecin) {
        Label title = new Label("Gestion des Ordonnances");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Formulaire de création
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField medicamentsField = new TextField();
        medicamentsField.setPromptText("Médicaments");
        TextArea remarquesArea = new TextArea();
        remarquesArea.setPromptText("Remarques");
        TextField patientIdField = new TextField();
        patientIdField.setPromptText("ID du patient");

        Button ajouterBtn = new Button("Ajouter l'ordonnance");

        ajouterBtn.setOnAction(e -> {
            try {
                Prescription p = new Prescription();
                p.setDate(datePicker.getValue());
                p.setMedicaments(medicamentsField.getText());
                p.setRemarques(remarquesArea.getText());
                p.setPatientId(Integer.parseInt(patientIdField.getText()));
                p.setMedecinId(medecin.getId());

                PrescriptionController controller = new PrescriptionController();
                controller.creerPrescription(p);
                prescriptionData.add(p);

                // Réinitialisation
                medicamentsField.clear();
                remarquesArea.clear();
                patientIdField.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur", "Impossible d'ajouter l'ordonnance. Vérifiez les champs.");
            }
        });

        GridPane formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.add(new Label("Date :"), 0, 0);
        formPane.add(datePicker, 1, 0);
        formPane.add(new Label("Médicaments :"), 0, 1);
        formPane.add(medicamentsField, 1, 1);
        formPane.add(new Label("Remarques :"), 0, 2);
        formPane.add(remarquesArea, 1, 2);
        formPane.add(new Label("ID Patient :"), 0, 3);
        formPane.add(patientIdField, 1, 3);
        formPane.add(ajouterBtn, 1, 4);
        formPane.setPadding(new Insets(10));

        // Tableau
        table = new TableView<>();
        TableColumn<Prescription, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> cell.getValue().dateProperty());

        TableColumn<Prescription, String> medsCol = new TableColumn<>("Médicaments");
        medsCol.setCellValueFactory(cell -> cell.getValue().medicamentsProperty());

        TableColumn<Prescription, String> remarksCol = new TableColumn<>("Remarques");
        remarksCol.setCellValueFactory(cell -> cell.getValue().remarquesProperty());

        TableColumn<Prescription, Number> patientCol = new TableColumn<>("ID Patient");
        patientCol.setCellValueFactory(cell -> cell.getValue().patientIdProperty());

        table.getColumns().addAll(dateCol, medsCol, remarksCol, patientCol);

        // Charger données
        try {
            PrescriptionController controller = new PrescriptionController();
            List<Prescription> liste = controller.listerPrescriptionsMedecin(medecin.getId());
            prescriptionData = FXCollections.observableArrayList(liste);
            table.setItems(prescriptionData);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les prescriptions.");
        }

        VBox root = new VBox(15, title, formPane, new Label("Ordonnances enregistrées :"), table);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Ordonnances - Médecin");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
