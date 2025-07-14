package epf.csi.examen.teleconsultation.main;

import epf.csi.examen.teleconsultation.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane root;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        mainScene = new Scene(root, 900, 600);

        HBox navigationBar = buildNavigationBar();

        // Affichage par défaut
        root.setTop(navigationBar);
        root.setCenter(new DashboardPatientView());

        primaryStage.setTitle("CareLinker - Plateforme de téléconsultation");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private HBox buildNavigationBar() {
        HBox navBar = new HBox(10);
        navBar.setStyle("-fx-padding: 10; -fx-background-color: #E0E0E0;");

        Button btnDashboard = new Button("Dashboard");
        Button btnRdv = new Button("Prendre RDV");
        Button btnCarnet = new Button("Carnet de Santé");
        Button btnPrescription = new Button("Prescriptions");

        btnDashboard.setOnAction(e -> root.setCenter(new DashboardPatientView()));
        btnRdv.setOnAction(e -> root.setCenter(new RendezVousView()));
        btnCarnet.setOnAction(e -> root.setCenter(new CarnetSanteView()));
        btnPrescription.setOnAction(e -> root.setCenter(new PrescriptionView()));

        navBar.getChildren().addAll(btnDashboard, btnRdv, btnCarnet, btnPrescription);
        return navBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
