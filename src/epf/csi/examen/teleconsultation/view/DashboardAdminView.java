package epf.csi.examen.teleconsultation.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardAdminView {
    private VBox root;

    public DashboardAdminView() {
        root = new VBox();
        root.setSpacing(20);
        root.getChildren().add(new Label("Bienvenue dans le Dashboard Admin"));
    }

    public Parent getView() {
        return root;
    }
}
