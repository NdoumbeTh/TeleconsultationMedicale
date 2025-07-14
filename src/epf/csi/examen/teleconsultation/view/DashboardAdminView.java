package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardAdminView {

    private Button btnUtilisateurs = new Button("Gérer les utilisateurs");
    private Button btnStatistiques = new Button("Voir les statistiques");
    private Button btnConfiguration = new Button("Configuration système");

    public Node getView() {
        BorderPane root = new BorderPane();

        Label title = new Label("Panneau de Contrôle - Administrateur");
        title.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

        VBox centerBox = new VBox(15, btnUtilisateurs, btnStatistiques, btnConfiguration);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(title);
        root.setCenter(centerBox);

        return root;
    }

    public Button getBtnUtilisateurs() { return btnUtilisateurs; }
    public Button getBtnStatistiques() { return btnStatistiques; }
    public Button getBtnConfiguration() { return btnConfiguration; }
}
