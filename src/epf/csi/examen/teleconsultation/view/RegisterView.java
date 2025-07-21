package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {
    private AuthController authController;

    public RegisterView(Stage stage) {
        this.authController = new AuthController();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400, 400);

        Label titre = new Label("Créer un compte");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("patient", "medecin");
        roleComboBox.setPromptText("Choisir un rôle");

        Button registerButton = new Button("S'inscrire");

        Label messageLabel = new Label();

        Hyperlink loginLink = new Hyperlink("Déjà un compte ? Se connecter");
        loginLink.setOnAction(e -> {
            new LoginView(stage); // Affiche la page de connexion
        });

        registerButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = passwordField.getText();
            String role = roleComboBox.getValue();

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
                messageLabel.setText("Veuillez remplir tous les champs.");
            } else {
                boolean success = authController.inscription(nom, email, mdp, role);
                if (success) {
                    messageLabel.setText("Inscription réussie !");
                    new LoginView(stage); // Redirige vers la connexion
                } else {
                    messageLabel.setText("Erreur lors de l'inscription.");
                }
            }
        });

        root.getChildren().addAll(titre, nomField, emailField, passwordField, roleComboBox, registerButton, messageLabel, loginLink);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inscription");
        stage.show();
    }

    public VBox getView() {
        // Optionnel si vous voulez injecter la vue ailleurs
        return null;
    }
}
