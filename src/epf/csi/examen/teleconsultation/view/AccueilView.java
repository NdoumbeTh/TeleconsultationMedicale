package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.main.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class AccueilView {
    private VBox root;

    public AccueilView(Main mainApp) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 30;");

        Label title = new Label("Bienvenue sur CareLinker");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button patientBtn = new Button("Je suis un Patient");
        Button medecinBtn = new Button("Je suis un Médecin");
        Button adminBtn = new Button("Je suis un Administrateur");


        root.getChildren().addAll(title, patientBtn, medecinBtn, adminBtn);
    }

    public Node getView() {
        return root;
    }
}
