package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardMedecinView {
    public DashboardMedecinView(Stage stage) {
        VBox root = new VBox(10, new Label("Bienvenue, Medecin"));
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("CareLinker - Admin");
        stage.setScene(scene);
        stage.show();
    }
}
