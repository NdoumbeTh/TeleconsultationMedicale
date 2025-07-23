package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.MessageController;
import epf.csi.examen.teleconsultation.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class MessageriePatientView {

    private final int patientId;
    private final int medecinId;
    private final MessageController messageController = new MessageController();
    private ListView<String> messageList;
    private TextArea messageInput;

    public MessageriePatientView(int patientId, int medecinId) {
        this.patientId = patientId;
        this.medecinId = medecinId;
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        messageList = new ListView<>();
        messageInput = new TextArea();
        messageInput.setPrefRowCount(3);

        Button btnEnvoyer = new Button("Envoyer");
        btnEnvoyer.setOnAction(e -> {
            String contenu = messageInput.getText().trim();
            if (!contenu.isEmpty()) {
                messageController.envoyerMessage(patientId, medecinId, contenu);
                messageInput.clear();
                refreshMessages();
            }
        });

        HBox inputBox = new HBox(10, messageInput, btnEnvoyer);
        inputBox.setPadding(new Insets(10));

        root.setCenter(messageList);
        root.setBottom(inputBox);

        refreshMessages();

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Messagerie avec le Médecin");
        stage.show();
    }

    private void refreshMessages() {
        List<Message> messages = messageController.getMessagesBetweenUsers(patientId, medecinId);
        messageController.markMessagesAsRead(medecinId, patientId);

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Message m : messages) {
            String prefix = m.getExpediteurId() == patientId ? "Moi: " : "Médecin: ";
            items.add(prefix + m.getContenu());
        }
        messageList.setItems(items);
    }
}
