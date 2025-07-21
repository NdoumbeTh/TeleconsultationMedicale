package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private AuthController authController;

    public LoginView(Stage stage) {
        this.authController = new AuthController();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400, 300);

        Label titre = new Label("Connexion");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        Button loginButton = new Button("Se connecter");

        Label message = new Label();

        Hyperlink registerLink = new Hyperlink("Pas encore de compte ? S'inscrire");
        registerLink.setOnAction(e -> {
            RegisterView registerView = new RegisterView(stage);
            Scene registerScene = new Scene(registerView.getView(), 400, 350);
            stage.setScene(registerScene);
        });

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String mdp = passwordField.getText();
            authController.connexion(email, mdp, stage);
        });

        root.getChildren().addAll(titre, emailField, passwordField, loginButton, message, registerLink);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Connexion");
        stage.show();
    }

    public VBox getView() {
        // Pour accéder à la vue depuis un autre point (optionnel)
        return null; // inutilisé ici
    }
}
