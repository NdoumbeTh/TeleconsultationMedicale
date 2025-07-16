package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Parent;

public class DashboardPatientView {    
	private VBox root;

public DashboardPatientView() {
    root = new VBox();
    root.setSpacing(20);
    root.getChildren().add(new Label("Bienvenue dans le Dashboard Admin"));
}

public Parent getView() {
    return root;

}
}
