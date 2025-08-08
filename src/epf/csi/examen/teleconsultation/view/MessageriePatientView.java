package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.MessageController;
import epf.csi.examen.teleconsultation.controller.UtilisateurController;
import epf.csi.examen.teleconsultation.model.Message;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MessageriePatientView {

    private final int patientId;
    private final MessageController messageController = new MessageController();
    private final UtilisateurController utilisateurController = new UtilisateurController();

    private ListView<String> messageList;
    private TextArea messageInput;
    private ComboBox<Utilisateur> medecinComboBox;

    public MessageriePatientView(int patientId) {
        this.patientId = patientId;
    }

public void start(Stage stage) {
    BorderPane root = new BorderPane();
    VBox topBox = new VBox(10);
    topBox.setPadding(new Insets(10));

    medecinComboBox = new ComboBox<>();
    medecinComboBox.setItems(utilisateurController.getAllMedecins());
    medecinComboBox.setPromptText("Sélectionner un médecin");

    medecinComboBox.setOnAction(e -> refreshMessages());

    messageList = new ListView<>();
    messageInput = new TextArea();
    messageInput.setPrefRowCount(3);

    Button btnEnvoyer = new Button("Envoyer");
    btnEnvoyer.setOnAction(e -> {
        Utilisateur selectedMedecin = medecinComboBox.getValue();
        String contenu = messageInput.getText().trim();
        if (selectedMedecin != null && !contenu.isEmpty()) {
            messageController.envoyerMessage(patientId, selectedMedecin.getId(), contenu);
            messageInput.clear();
            refreshMessages();
        }
    });

    // ✅ Bouton retour
    Button btnRetour = new Button("Retour Dashboard");
    btnRetour.setOnAction(e -> {
        DashboardPatientView dashboard = new DashboardPatientView(patientId);
        dashboard.start(stage);  // Réutilisation du même Stage
    });

    HBox inputBox = new HBox(10, messageInput, btnEnvoyer);
    inputBox.setPadding(new Insets(10));

    VBox bottomBox = new VBox(10, inputBox, btnRetour);
    bottomBox.setPadding(new Insets(10));

    topBox.getChildren().addAll(new Label("Messagerie avec le médecin"), medecinComboBox);
    root.setTop(topBox);
    root.setCenter(messageList);
    root.setBottom(bottomBox);  // ✅ Bouton retour ajouté ici

    Scene scene = new Scene(root, 600, 450);
    stage.setScene(scene);
    stage.setTitle("Messagerie Patient");
    stage.show();
}

    private void refreshMessages() {
        Utilisateur selectedMedecin = medecinComboBox.getValue();
        if (selectedMedecin == null) return;

        List<Message> messages = messageController.getMessagesBetweenUsers(patientId, selectedMedecin.getId());
        messageController.markMessagesAsRead(selectedMedecin.getId(), patientId);

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Message m : messages) {
            String prefix = m.getExpediteurId() == patientId ? "Moi: " : selectedMedecin.getNom() + ": ";
            items.add(prefix + m.getContenu());
        }
        messageList.setItems(items);
    }
}
