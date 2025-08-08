package epf.csi.examen.teleconsultation.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.model.Consultation;

import java.util.List;

public class ConsultationListView {

    private final int medecinId;
    private final Stage stage;
    private final DashboardMedecinView dashboard;

    private TableView<Consultation> table;

    public ConsultationListView(int medecinId, Stage stage, DashboardMedecinView dashboard) {
        this.medecinId = medecinId;
        this.stage = stage;
        this.dashboard = dashboard;
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Liste des consultations");
        title.setFont(Font.font("Arial", 22));

        table = new TableView<>();
        // Ici tu peux configurer les colonnes de la table (ex : date, patient, motif, etc.)
        // Exemple colonne date :
        TableColumn<Consultation, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(cellData -> cellData.getValue().dateHeureProperty().asString());
        TableColumn<Consultation, String> colPatient = new TableColumn<>("Patient");
        colPatient.setCellValueFactory(cellData -> cellData.getValue().nomPatientProperty());

        table.getColumns().addAll(colDate, colPatient);

        // Chargement des données
        loadConsultations();

        Button btnRetour = new Button("Retour au dashboard");
        btnRetour.setOnAction(e -> {
            // Retourner au dashboard
            stage.getScene().setRoot(dashboard.getView());
        });

        root.getChildren().addAll(title, table, btnRetour);

        return root;
    }

    private void loadConsultations() {
        ConsultationController controller = new ConsultationController();
        List<Consultation> consultations = controller.listerConsultationsMedecin(medecinId);
        ObservableList<Consultation> data = FXCollections.observableArrayList(consultations);
        table.setItems(data);
    }
}
