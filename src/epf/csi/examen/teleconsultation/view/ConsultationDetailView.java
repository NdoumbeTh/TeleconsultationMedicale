package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.model.Consultation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.format.DateTimeFormatter;

public class ConsultationDetailView {

    private final ConsultationController consultationController = new ConsultationController();
    private Consultation consultation;

    private Label idLabel;
    private Label patientLabel;
    private DatePicker datePicker;
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;
    private ComboBox<String> statutComboBox;
    private TextField typeField;
    private TextArea motifArea;

    public ConsultationDetailView(Consultation consultation) {
        this.consultation = consultation;
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Détails de la Consultation");
        title.setFont(Font.font(24));

        idLabel = new Label("ID: " + consultation.getId());
        patientLabel = new Label("Patient: " + consultation.getNomPatient());

        // Date et heure
        datePicker = new DatePicker(consultation.getDateHeure().toLocalDate());

        hourSpinner = new Spinner<>(0, 23, consultation.getDateHeure().getHour());
        hourSpinner.setEditable(true);
        minuteSpinner = new Spinner<>(0, 59, consultation.getDateHeure().getMinute());
        minuteSpinner.setEditable(true);

        HBox timeBox = new HBox(10, new Label("Heure:"), hourSpinner, new Label(":"), minuteSpinner);
        timeBox.setAlignment(Pos.CENTER_LEFT);

        // Statut
        statutComboBox = new ComboBox<>();
        statutComboBox.getItems().addAll("En attente", "En cours", "Terminée", "Annulée");
        statutComboBox.setValue(consultation.getStatut());

        // Type et motif
        typeField = new TextField(consultation.getType());
        motifArea = new TextArea(consultation.getMotif());
        motifArea.setPrefRowCount(4);

        Button saveBtn = new Button("Sauvegarder");
        saveBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setOnAction(e -> sauvegarderModifications());

        root.getChildren().addAll(
            title,
            idLabel,
            patientLabel,
            new Label("Date:"), datePicker,
            timeBox,
            new Label("Statut:"), statutComboBox,
            new Label("Type:"), typeField,
            new Label("Motif:"), motifArea,
            saveBtn
        );

        return root;
    }

    private void sauvegarderModifications() {
        try {
            consultation.setDateHeure(
                datePicker.getValue().atTime(hourSpinner.getValue(), minuteSpinner.getValue())
            );
            consultation.setStatut(statutComboBox.getValue());
            consultation.setType(typeField.getText());
            consultation.setMotif(motifArea.getText());

            consultationController.modifierConsultation(consultation);

            showAlert("Succès", "Consultation mise à jour avec succès.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
