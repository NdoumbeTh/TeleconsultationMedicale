package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.PrescriptionController;
import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Prescription;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionView {

    private TableView<Prescription> table;
    private ObservableList<Prescription> prescriptionData;
    private Map<String, Integer> nomPatientToIdMap = new HashMap<>();

    public void start(Stage stage, Utilisateur medecin) throws SQLException {
        Label title = new Label("Gestion des Ordonnances");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Formulaire de création
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField medicamentsField = new TextField();
        medicamentsField.setPromptText("Médicaments");
        TextArea remarquesArea = new TextArea();
        remarquesArea.setPromptText("Remarques");

        ComboBox<String> comboPatients = new ComboBox<>();
        chargerPatients(comboPatients);

        Button ajouterBtn = new Button("Ajouter l'ordonnance");
        Button retourBtn = new Button("Retour Dashboard");

        ajouterBtn.setOnAction(e -> {
            try {
                String nomPatient = comboPatients.getValue();
                if (nomPatient == null || !nomPatientToIdMap.containsKey(nomPatient)) {
                    showAlert("Erreur", "Veuillez sélectionner un patient.");
                    return;
                }
                int patientId = nomPatientToIdMap.get(nomPatient);

                Prescription p = new Prescription();
                p.setDate(datePicker.getValue());
                p.setMedicaments(medicamentsField.getText());
                p.setRemarques(remarquesArea.getText());
                p.setPatientId(patientId);
                p.setMedecinId(medecin.getId());

                PrescriptionController controller = new PrescriptionController();
                controller.creerPrescription(p);
                prescriptionData.add(p);

                // Réinitialisation
                medicamentsField.clear();
                remarquesArea.clear();
                comboPatients.getSelectionModel().clearSelection();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur", "Impossible d'ajouter l'ordonnance.");
            }
        });

        retourBtn.setOnAction(e -> {
            try {
                DashboardMedecinView dashboard = new DashboardMedecinView(stage, medecin);
                stage.getScene().setRoot(dashboard.getView());
            } catch (SQLException e1) {
                e1.printStackTrace();
                showAlert("Erreur", "Impossible de retourner au dashboard.");
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
        formPane.add(new Label("Patient :"), 0, 3);
        formPane.add(comboPatients, 1, 3);
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

        TableColumn<Prescription, Number> patientIdCol = new TableColumn<>("Patient ID");
        patientIdCol.setCellValueFactory(cell -> cell.getValue().patientIdProperty());

        table.getColumns().addAll(dateCol, medsCol, remarksCol, patientIdCol);

        PrescriptionController controller = new PrescriptionController();
		List<Prescription> liste = controller.listerPrescriptionsMedecin(medecin.getId());
		prescriptionData = FXCollections.observableArrayList(liste);
		table.setItems(prescriptionData);

        VBox root = new VBox(15, title, formPane, new Label("Ordonnances enregistrées :"), table, retourBtn);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Ordonnances - Médecin");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerPatients(ComboBox<String> comboBox) {
        try {
            Connection conn = DBConnection.getConnection();
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
            List<Utilisateur> patients = utilisateurDAO.listerPatients();

            for (Utilisateur p : patients) {
                String nomComplet =  p.getNom();
                nomPatientToIdMap.put(nomComplet, p.getId());
                comboBox.getItems().add(nomComplet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la liste des patients.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
