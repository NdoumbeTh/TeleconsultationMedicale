package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private AuthController authController;
    private VBox root;

    public LoginView(Stage stage) {
        this.authController = new AuthController();
        root = new VBox(10); // ⚠️ CORRIGÉ ICI
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
    }

    public Parent getView() {
        return root;
    }
}
