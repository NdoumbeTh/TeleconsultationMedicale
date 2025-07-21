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
    private VBox root; // Ajoutez en haut


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



        Button registerButton = new Button("S'inscrire");

        Label messageLabel = new Label();

        Hyperlink loginLink = new Hyperlink("Déjà un compte ? Se connecter");
        loginLink.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            Scene loginScene = new Scene(loginView.getView(), 400, 300);
            stage.setScene(loginScene);
        });


        registerButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = passwordField.getText();
            String role = "patient"; // Rôle imposé


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

        root.getChildren().addAll(titre, nomField, emailField, passwordField, registerButton, messageLabel, loginLink);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inscription");
        stage.show();
    }

    public VBox getView() {
        return root;
    }
}
