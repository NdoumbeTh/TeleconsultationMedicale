package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardPatientView {
    private Button carnetButton = new Button("Carnet de santé");
    private Button rdvButton = new Button("Prendre un rendez-vous");
    private Button prescriptionButton = new Button("Mes prescriptions");
    private Button teleconsultationButton = new Button("Téléconsultation");

    public Scene getScene() {
        BorderPane root = new BorderPane();
        Label title = new Label("Bienvenue sur CareLinker - Patient");
        title.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

        VBox centerBox = new VBox(10, carnetButton, rdvButton, prescriptionButton, teleconsultationButton);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(title);
        root.setCenter(centerBox);

        return new Scene(root, 600, 400);
    }

    public Button getCarnetButton() { return carnetButton; }
    public Button getRdvButton() { return rdvButton; }
    public Button getPrescriptionButton() { return prescriptionButton; }
    public Button getTeleconsultationButton() { return teleconsultationButton; }
}
