// Fichier modifié : MessageMedecinView.java
package epf.csi.examen.teleconsultation.view;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import epf.csi.examen.teleconsultation.controller.MessageMedecinController;
import epf.csi.examen.teleconsultation.controller.PatientController;
import epf.csi.examen.teleconsultation.model.Message;
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

public class MessageMedecinView {

    private final Utilisateur medecin;
    private final MessageMedecinController messageController;
    private final PatientController patientController;
    private ListView<String> listView;
    private ListView<Utilisateur> patientListView;
    private Map<Integer, Integer> unreadCounts = new HashMap<>();
    private ObservableList<Utilisateur> allPatients;

    public MessageMedecinView(Utilisateur medecin) throws SQLException {
        if (medecin == null || medecin.getId() == 0) {
            throw new IllegalArgumentException("Médecin invalide ou non initialisé");
        }

        this.medecin = medecin;
        Connection conn = DBConnection.getConnection();
        this.messageController = new MessageMedecinController();
        this.patientController = new PatientController(conn);
    }

    public Scene getScene(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titre = new Label("Messagerie - Médecin");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un patient");

        patientListView = new ListView<>();
        allPatients = patientController.getAllPatients();
        patientListView.setItems(allPatients);

        // Affichage personnalisé : uniquement nom du patient avec badge si nouveau
        patientListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Utilisateur patient, boolean empty) {
                super.updateItem(patient, empty);
                if (empty || patient == null) {
                    setText(null);
                } else {
                    int unread = unreadCounts.getOrDefault(patient.getId(), 0);
                    String label = patient.getNom();
                    if (unread > 0) {
                        label += " (" + unread + " nouveau" + (unread > 1 ? "x" : "") + ")";
                    }
                    setText(label);
                }
            }
        });

        listView = new ListView<>();

        TextArea contenuArea = new TextArea();
        contenuArea.setPromptText("Écrire un message...");
        contenuArea.setWrapText(true);

        Button envoyerBtn = new Button("Envoyer");
        envoyerBtn.setOnAction(e -> {
            Utilisateur patient = patientListView.getSelectionModel().getSelectedItem();
            String contenu = contenuArea.getText().trim();
            if (patient != null && !contenu.isEmpty()) {
                messageController.envoyerMessage(contenu, medecin.getId(), patient.getId());
                contenuArea.clear();
                refreshMessages();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez choisir un patient et écrire un message.");
                alert.showAndWait();
            }
        });

        Button retourBtn = new Button("Retour Dashboard");
        retourBtn.setOnAction(e -> {
            try {
                DashboardMedecinView dashboard = new DashboardMedecinView(stage, medecin);
                stage.getScene().setRoot(dashboard.getView());
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Erreur", "Impossible de retourner au dashboard.");
            }
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            List<Utilisateur> filtered = allPatients.stream()
                .filter(p -> p.getNom().toLowerCase().contains(newText.toLowerCase()))
                .collect(Collectors.toList());
            patientListView.setItems(FXCollections.observableArrayList(filtered));
        });

        patientListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> refreshMessages());

        updateUnreadCounts();

        VBox formBox = new VBox(10, searchField, patientListView, contenuArea, envoyerBtn);
        formBox.setAlignment(Pos.CENTER_LEFT);

        layout.getChildren().addAll(titre, listView, new Separator(), formBox, retourBtn);

        Scene scene = new Scene(layout, 600, 550);
        stage.setTitle("Messagerie Médecin");
        stage.setScene(scene);
        stage.show();

        return scene;
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR, contenu);
        alert.setTitle(titre);
        alert.showAndWait();
    }

    private void refreshMessages() {
        Utilisateur patient = patientListView.getSelectionModel().getSelectedItem();
        if (patient == null) return;

        List<Message> messages = messageController.getMessagesAvecPatient(medecin.getId(), patient.getId());
        messageController.marquerMessagesCommeLus(patient.getId(), medecin.getId());

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Message m : messages) {
            String prefix = m.getExpediteurId() == medecin.getId() ? "Moi: " : "Patient: ";
            items.add(prefix + m.getContenu());
        }

        listView.setItems(items);
        updateUnreadCounts();
    }

    private void updateUnreadCounts() {
        unreadCounts = patientController.getUnreadMessageCounts(medecin.getId());
        patientListView.refresh();
    }
}
