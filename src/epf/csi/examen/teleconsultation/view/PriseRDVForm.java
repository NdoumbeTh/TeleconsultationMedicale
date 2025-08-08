package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.RendezVousController;
import epf.csi.examen.teleconsultation.controller.UtilisateurController;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PriseRDVForm {

    private final int patientId;
    private final DashboardPatientView parentView;
    private final RendezVousController rdvController = new RendezVousController();
    private final UtilisateurController utilisateurController;

    public PriseRDVForm(int patientId, DashboardPatientView parentView) throws SQLException {
        this.patientId = patientId;
        this.parentView = parentView;
        this.utilisateurController = new UtilisateurController();
    }

    public void start(Stage stage) {
        stage.setTitle("Prendre un rendez-vous");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Médecin sélection (ComboBox avec noms)
        Label medecinLabel = new Label("Médecin:");
        ComboBox<Utilisateur> medecinComboBox = new ComboBox<>();
        ObservableList<Utilisateur> medecins = utilisateurController.getAllMedecins();
        medecinComboBox.setItems(medecins);
        medecinComboBox.setPromptText("Choisir un médecin");

        // Affichage uniquement du nom du médecin
        medecinComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
        medecinComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        // Date
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        // Heure
        Label heureLabel = new Label("Heure (HH:mm):");
        TextField heureField = new TextField("09:00");

        Button btnValider = new Button("Valider");
        Label messageLabel = new Label();

        btnValider.setOnAction(e -> {
            try {
                Utilisateur medecin = medecinComboBox.getValue();
                if (medecin == null) {
                    messageLabel.setText("Veuillez choisir un médecin.");
                    return;
                }

                LocalDate date = datePicker.getValue();
                String[] heureParts = heureField.getText().split(":");
                LocalTime heure = LocalTime.of(Integer.parseInt(heureParts[0]), Integer.parseInt(heureParts[1]));
                LocalDateTime dateHeure = LocalDateTime.of(date, heure);

                boolean success = rdvController.creerRendezVous(medecin.getId(), patientId, dateHeure, "en attente");
                if (success) {
                    messageLabel.setText("Rendez-vous créé avec succès !");
                    parentView.refreshAfterCreation();
                    stage.close();
                } else {
                    messageLabel.setText("Erreur lors de la création.");
                }
            } catch (Exception ex) {
                messageLabel.setText("Veuillez vérifier les données saisies.");
            }
        });

        grid.add(medecinLabel, 0, 0);
        grid.add(medecinComboBox, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(heureLabel, 0, 2);
        grid.add(heureField, 1, 2);
        grid.add(btnValider, 1, 3);
        grid.add(messageLabel, 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
