package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.UserController;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class DashboardAdminView {

    private final UserController userController = new UserController();
    private final ObservableList<Utilisateur> userList = FXCollections.observableArrayList();

    public DashboardAdminView(Stage stage) {
        // Titre
        Label title = new Label("Interface Administrateur - Gestion des Utilisateurs");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TableView
        TableView<Utilisateur> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(cell -> cell.getValue().nomProperty());

        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell -> cell.getValue().emailProperty());

        TableColumn<Utilisateur, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(cell -> cell.getValue().roleProperty());

        tableView.getColumns().addAll(idCol, nomCol, emailCol, roleCol);
        tableView.setItems(userList);

        // Formulaire d’ajout
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("Mot de passe");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "medecin");
        roleBox.setPromptText("Rôle");

        Button addButton = new Button("Créer utilisateur");
        addButton.setDefaultButton(true);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        addButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = mdpField.getText();
            String role = roleBox.getValue();

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
                messageLabel.setText("⚠️ Tous les champs sont requis.");
                return;
            }

            boolean success = userController.creerUtilisateur(nom, email, mdp, role);
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("✅ Utilisateur ajouté !");
                nomField.clear();
                emailField.clear();
                mdpField.clear();
                roleBox.setValue(null);
                chargerUtilisateurs();
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("❌ Erreur lors de la création.");
            }
        });

        HBox form = new HBox(10, nomField, emailField, mdpField, roleBox, addButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(10));

        VBox root = new VBox(15, title, tableView, form, messageLabel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 900, 550);
        stage.setTitle("CareLinker - Dashboard Administrateur");
        stage.setScene(scene);
        stage.show();

        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        List<Utilisateur> liste = userController.listerAdminsEtMedecins();
        userList.setAll(liste);
    }
}
