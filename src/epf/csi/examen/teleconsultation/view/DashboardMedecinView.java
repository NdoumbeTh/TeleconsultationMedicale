package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardMedecinView {

    private Button btnConsultations = new Button("Voir les consultations");
    private Button btnPatients = new Button("Liste des patients");
    private Button btnPrescriptions = new Button("Prescrire un traitement");

    public Node getView() {
        BorderPane root = new BorderPane();

        Label title = new Label("Bienvenue Docteur - Dashboard Médecin");
        title.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

        VBox centerBox = new VBox(15, btnConsultations, btnPatients, btnPrescriptions);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(title);
        root.setCenter(centerBox);

        return root;
    }

    public Button getBtnConsultations() { return btnConsultations; }
    public Button getBtnPatients() { return btnPatients; }
    public Button getBtnPrescriptions() { return btnPrescriptions; }
}
