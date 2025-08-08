// Fichier modifié : CarnetSanteMedecinView.java
package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.PatientController;
import epf.csi.examen.teleconsultation.dao.CarnetSanteDAO;
import epf.csi.examen.teleconsultation.model.CarnetSante;
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
import java.util.List;
import java.util.stream.Collectors;

public class CarnetSanteMedecinView {
    private final Connection conn;
    private final CarnetSanteDAO carnetDAO;
    private final PatientController patientController;

    public CarnetSanteMedecinView() throws SQLException {
        conn = DBConnection.getConnection();
        carnetDAO = new CarnetSanteDAO(conn);
        patientController = new PatientController(conn);
    }

    public void start(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un patient");

        ListView<Utilisateur> patientListView = new ListView<>();
        ObservableList<Utilisateur> allPatients = patientController.getAllPatients();
        patientListView.setItems(allPatients);

        // Personnaliser l'affichage pour ne montrer que le nom
        patientListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        Label carnetLabel = new Label("Aucun carnet chargé.");
        Button btnCharger = new Button("Charger Carnet");
        Button btnSauvegarder = new Button("Sauvegarder");

        TextField groupeField = new TextField();
        groupeField.setPromptText("Groupe sanguin");

        TextArea allergiesArea = new TextArea();
        allergiesArea.setPromptText("Allergies");

        TextArea antecedentsArea = new TextArea();
        antecedentsArea.setPromptText("Antécédents");

        // Recherche dynamique
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            List<Utilisateur> filtered = allPatients.stream()
                .filter(p -> p.getNom().toLowerCase().contains(newText.toLowerCase()))
                .collect(Collectors.toList());
            patientListView.setItems(FXCollections.observableArrayList(filtered));
        });

        btnCharger.setOnAction(e -> {
            Utilisateur patient = patientListView.getSelectionModel().getSelectedItem();
            if (patient != null) {
                try {
                    CarnetSante carnet = carnetDAO.getCarnetParPatientId(patient.getId());
                    if (carnet != null) {
                        carnetLabel.setText("Carnet existant");
                        groupeField.setText(carnet.getGroupeSanguin());
                        allergiesArea.setText(carnet.getAllergies());
                        antecedentsArea.setText(carnet.getAntecedents());
                    } else {
                        carnetLabel.setText("Nouveau carnet à créer");
                        groupeField.clear();
                        allergiesArea.clear();
                        antecedentsArea.clear();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    carnetLabel.setText("Erreur de chargement.");
                }
            }
        });

        btnSauvegarder.setOnAction(e -> {
            Utilisateur patient = patientListView.getSelectionModel().getSelectedItem();
            if (patient == null) return;

            try {
                CarnetSante carnet = carnetDAO.getCarnetParPatientId(patient.getId());
                if (carnet == null) {
                    carnet = new CarnetSante();
                    carnet.setPatientId(patient.getId());
                }
                carnet.setGroupeSanguin(groupeField.getText());
                carnet.setAllergies(allergiesArea.getText());
                carnet.setAntecedents(antecedentsArea.getText());

                if (carnet.getId() > 0) {
                    carnetDAO.updateCarnet(carnet);
                } else {
                    carnetDAO.creerCarnet(carnet);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Carnet sauvegardé !");
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la sauvegarde.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(searchField, patientListView, carnetLabel, groupeField, allergiesArea, antecedentsArea, btnCharger, btnSauvegarder);
        stage.setScene(new Scene(layout, 400, 600));
        stage.setTitle("Carnet de Santé (Médecin)");
        stage.show();
    }
}
