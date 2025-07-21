package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class DashboardPatientView {
    public DashboardPatientView(Stage stage) {
        VBox root = new VBox(10, new Label("Bienvenue, Patient"));
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("CareLinker - Admin");
        stage.setScene(scene);
        stage.show();
    }
}

