package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

public class RendezVousView {

    public Node getView() {
        BorderPane root = new BorderPane();
        Label title = new Label("Prendre un rendez-vous");
        title.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(
            new Label("Formulaire de rendez-vous à venir..."),
            new Button("Simuler RDV")
        );

        root.setTop(title);
        root.setCenter(content);

        return root;
    }
}
