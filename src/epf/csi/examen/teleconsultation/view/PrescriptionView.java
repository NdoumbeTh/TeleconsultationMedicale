package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PrescriptionView {
    private ListView<String> prescriptionList = new ListView<>();
    private Button telechargerButton = new Button("Télécharger l'ordonnance");

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Prescriptions médicales");
        title.setStyle("-fx-font-size: 18px;");

        prescriptionList.setPrefHeight(250);

        root.getChildren().addAll(title, prescriptionList, telechargerButton);
        return new Scene(root, 600, 400);
    }

    public ListView<String> getPrescriptionList() { return prescriptionList; }
    public Button getTelechargerButton() { return telechargerButton; }
}
