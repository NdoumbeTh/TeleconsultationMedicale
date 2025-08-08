package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.dao.CarnetSanteDAO;
import epf.csi.examen.teleconsultation.model.CarnetSante;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class CarnetSantePatientView {

    private final Utilisateur patient;
    private final CarnetSanteDAO carnetSanteDAO;

    private Label groupeSanguinLabel;
    private TextArea allergiesArea;
    private TextArea antecedentsArea;
    private Label statusLabel;

    private final int patientId; // <-- change ici

    public CarnetSantePatientView(int patientId) throws SQLException {
        this.patient = new Utilisateur();
		this.patientId = patientId;
        Connection conn = DBConnection.getConnection();
        this.carnetSanteDAO = new CarnetSanteDAO(conn);
    }


    public Scene getScene(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Carnet de Santé - Patient");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        groupeSanguinLabel = new Label();
        allergiesArea = new TextArea();
        allergiesArea.setEditable(false);
        allergiesArea.setWrapText(true);
        allergiesArea.setPrefRowCount(3);

        antecedentsArea = new TextArea();
        antecedentsArea.setEditable(false);
        antecedentsArea.setWrapText(true);
        antecedentsArea.setPrefRowCount(3);

        Button downloadBtn = new Button("Télécharger en PDF");
        downloadBtn.setOnAction(e -> {
            // TODO: Implémenter export PDF
            statusLabel.setText("Fonction PDF non encore implémentée.");
        });

        statusLabel = new Label();

        root.getChildren().addAll(title,
                new Label("Groupe sanguin:"), groupeSanguinLabel,
                new Label("Allergies:"), allergiesArea,
                new Label("Antécédents:"), antecedentsArea,
                downloadBtn,
                statusLabel);

        chargerCarnet();

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.setTitle("Carnet de Santé - Patient");
        stage.show();

        return scene;
    }

    private void chargerCarnet() {
        CarnetSante carnet = carnetSanteDAO.getCarnetByPatientId(patientId);  // <-- utilise l’int
        if (carnet != null) {
            groupeSanguinLabel.setText(carnet.getGroupeSanguin());
            allergiesArea.setText(carnet.getAllergies());
            antecedentsArea.setText(carnet.getAntecedents());
            statusLabel.setText("");
        } else {
            groupeSanguinLabel.setText("-");
            allergiesArea.setText("-");
            antecedentsArea.setText("-");
            statusLabel.setText("Aucun carnet de santé trouvé.");
        }
    }

}
