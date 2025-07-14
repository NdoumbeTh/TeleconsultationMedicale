package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AccueilView {

    private Button btnPatient = new Button("Se connecter en tant que Patient");
    private Button btnMedecin = new Button("Se connecter en tant que Médecin");
    private Button btnAdmin = new Button("Se connecter en tant qu'Administrateur");

    public Node getView() {
        Label title = new Label("Bienvenue sur CareLinker");
        title.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");

        VBox vbox = new VBox(20, title, btnPatient, btnMedecin, btnAdmin);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    public Button getBtnPatient() { return btnPatient; }
    public Button getBtnMedecin() { return btnMedecin; }
    public Button getBtnAdmin() { return btnAdmin; }
}
