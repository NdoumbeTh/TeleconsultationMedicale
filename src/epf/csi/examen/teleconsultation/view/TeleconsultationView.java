package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TeleconsultationView {
    private Label statutLabel = new Label("En attente de connexion...");
    private Button demarrerButton = new Button("Démarrer");
    private Button quitterButton = new Button("Quitter");

    public Scene getScene() {
        BorderPane root = new BorderPane();
        statutLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        HBox centerBox = new HBox(20, statutLabel);
        centerBox.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(20, demarrerButton, quitterButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setStyle("-fx-padding: 20px;");

        root.setCenter(centerBox);
        root.setBottom(bottomBox);

        return new Scene(root, 600, 400);
    }

    public Label getStatutLabel() { return statutLabel; }
    public Button getDemarrerButton() { return demarrerButton; }
    public Button getQuitterButton() { return quitterButton; }
}
