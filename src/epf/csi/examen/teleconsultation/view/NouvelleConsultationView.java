package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NouvelleConsultationView {

    public NouvelleConsultationView(Stage stage) {
        Label lblTitre = new Label("Nouvelle Consultation");

        TextField txtDate = new TextField();
        txtDate.setPromptText("YYYY-MM-DD");

        TextField txtMotif = new TextField();
        txtMotif.setPromptText("Motif de la consultation");

        Button btnEnregistrer = new Button("Enregistrer");
        btnEnregistrer.setOnAction(e -> {
            // À remplacer par appel à ConsultationController pour enregistrer en BDD
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Consultation enregistrée !");
            alert.showAndWait();
            stage.close();
        });

        VBox root = new VBox(10, lblTitre, txtDate, txtMotif, btnEnregistrer);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 300, 200);
        Stage formStage = new Stage();
        formStage.setTitle("Nouvelle Consultation");
        formStage.setScene(scene);
        formStage.show();
    }
}
