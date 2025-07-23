package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.PrescriptionController;
import epf.csi.examen.teleconsultation.model.Prescription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class PrescriptionPatientView {

    public void start(Stage stage, int patientId) {
        TableView<Prescription> table = new TableView<>();

        TableColumn<Prescription, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Prescription, String> medicamentCol = new TableColumn<>("Médicaments");
        medicamentCol.setCellValueFactory(new PropertyValueFactory<>("medicaments"));

        TableColumn<Prescription, String> remarquesCol = new TableColumn<>("Remarques");
        remarquesCol.setCellValueFactory(new PropertyValueFactory<>("remarques"));

        table.getColumns().addAll(dateCol, medicamentCol, remarquesCol);

        try {
            PrescriptionController controller = new PrescriptionController();
            List<Prescription> prescriptions = controller.listerPrescriptionsPatient(patientId);
            ObservableList<Prescription> data = FXCollections.observableArrayList(prescriptions);
            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Mes Ordonnances");
        stage.setScene(scene);
        stage.show();
    }
}
