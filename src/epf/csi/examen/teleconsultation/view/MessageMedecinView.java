package epf.csi.examen.teleconsultation.view;

import java.sql.Connection;
import java.sql.SQLException;

import epf.csi.examen.teleconsultation.controller.MessageMedecinController;
import epf.csi.examen.teleconsultation.controller.PatientController;
import epf.csi.examen.teleconsultation.model.Message;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
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

    public MessageMedecinView(Utilisateur medecin) throws SQLException {
        this.medecin = medecin;

        Connection conn = DBConnection.getConnection(); // Connexion unique
        this.messageController = new MessageMedecinController();
        this.patientController = new PatientController(conn);
    }

    public MessageMedecinView(int id, Stage stage, DashboardMedecinView dashboardMedecinView) {
		this.medecin = new Utilisateur();
		this.messageController = new MessageMedecinController();
		this.patientController = null;
		// TODO Auto-generated constructor stub
	}

	public Scene getScene(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titre = new Label("Messagerie - Médecin");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Liste des messages reçus
        ListView<Message> listView = new ListView<>();
        listView.setItems(messageController.getMessagesRecus(medecin.getId()));

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                } else {
                    String status = msg.isLu() ? "" : " (Nouveau)";
                    setText("De Patient #" + msg.getExpediteurId() + ": " + msg.getContenu() + status);
                }
            }
        });

        listView.setOnMouseClicked(event -> {
            Message msg = listView.getSelectionModel().getSelectedItem();
            if (msg != null && !msg.isLu()) {
                messageController.marquerCommeLu(msg.getId());
                listView.setItems(messageController.getMessagesRecus(medecin.getId())); // Rafraîchir
            }
        });

        // Formulaire d'envoi
        TextArea contenuArea = new TextArea();
        contenuArea.setPromptText("Écrire un message...");
        contenuArea.setWrapText(true);

        ComboBox<Utilisateur> patientBox = new ComboBox<>();
        patientBox.setItems(patientController.getAllPatients());
        patientBox.setPromptText("Choisir un patient");

        Button envoyerBtn = new Button("Envoyer");
        envoyerBtn.setOnAction(e -> {
            Utilisateur patient = patientBox.getValue();
            String contenu = contenuArea.getText();
            if (patient != null && !contenu.isBlank()) {
                messageController.envoyerMessage(contenu, medecin.getId(), patient.getId());
                contenuArea.clear();
                listView.setItems(messageController.getMessagesRecus(medecin.getId())); // rafraîchir
            }
        });

        VBox formBox = new VBox(10, patientBox, contenuArea, envoyerBtn);
        formBox.setAlignment(Pos.CENTER_LEFT);

        layout.getChildren().addAll(titre, listView, new Separator(), formBox);
        return new Scene(layout, 600, 500);
    }
}
