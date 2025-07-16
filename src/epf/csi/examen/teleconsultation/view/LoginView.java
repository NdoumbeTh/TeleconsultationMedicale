package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView extends VBox {
    public LoginView(Stage primaryStage) {
        AuthController authController = new AuthController();

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        Button loginBtn = new Button("Se connecter");
        loginBtn.setOnAction(e -> {
            authController.connexion(
                emailField.getText(),
                passwordField.getText(),
                primaryStage
            );
        });

        getChildren().addAll(emailField, passwordField, loginBtn);
        setSpacing(10);
    }
}
