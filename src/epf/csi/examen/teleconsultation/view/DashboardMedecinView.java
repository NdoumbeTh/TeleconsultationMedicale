package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardMedecinView {

    private VBox root;

    public DashboardMedecinView() {
        root = new VBox();
        root.setSpacing(20);
        root.getChildren().add(new Label("Bienvenue dans le Dashboard Medecin"));
    }

    public Parent getView() {
        return root;
    
}
}