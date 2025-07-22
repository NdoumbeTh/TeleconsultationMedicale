package epf.csi.examen.teleconsultation.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardMedecinView {

    private final int medecinId;
    private final VBox root;  // garde la racine

    public DashboardMedecinView(Stage stage, int medecinId) {
        this.medecinId = medecinId;
        this.root = buildView(stage);

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Dashboard Médecin");
        stage.show();
    }

    private VBox buildView(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Tableau de bord - Médecin");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        HBox menu = new HBox(10);
        menu.setAlignment(Pos.CENTER);

        Button btnConsultations = new Button("Mes consultations");
        Button btnNouvelleConsultation = new Button("Nouvelle consultation");
        Button btnDeconnexion = new Button("Déconnexion");

        menu.getChildren().addAll(
            btnConsultations,
            btnNouvelleConsultation,
            btnDeconnexion
        );

        root.getChildren().addAll(title, menu);

        btnConsultations.setOnAction(e -> {
            VBox consultationsView = new ConsultationListView(medecinId).getView();
            stage.getScene().setRoot(consultationsView);
        });

        btnNouvelleConsultation.setOnAction(e -> {
            ConsultationCreateView consultationCreateView = new ConsultationCreateView(medecinId, stage, this);
            stage.getScene().setRoot(consultationCreateView.getView());
        });

        btnDeconnexion.setOnAction(e -> stage.close());

        return root;
    }

    public VBox getView() {
        return root;
    }
}
