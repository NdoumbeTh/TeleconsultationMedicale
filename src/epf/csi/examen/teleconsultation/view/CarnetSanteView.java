package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class CarnetSanteView {
    private TextArea historiqueText = new TextArea();
    private Button actualiserButton = new Button("Actualiser");

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Carnet de santé");
        title.setStyle("-fx-font-size: 18px;");

        historiqueText.setPromptText("Historique médical...");
        historiqueText.setPrefSize(500, 300);
        historiqueText.setEditable(false);

        root.getChildren().addAll(title, historiqueText, actualiserButton);
        return new Scene(root, 600, 400);
    }

    public TextArea getHistoriqueText() { return historiqueText; }
    public Button getActualiserButton() { return actualiserButton; }
}
