package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView {
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button loginButton = new Button("Se connecter");
    private Hyperlink signupLink = new Hyperlink("Créer un compte");

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(40));
        Label title = new Label("Connexion - CareLinker");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        emailField.setPromptText("Email");
        passwordField.setPromptText("Mot de passe");

        root.getChildren().addAll(title, emailField, passwordField, loginButton, signupLink);
        return new Scene(root, 400, 300);
    }

    public TextField getEmailField() { return emailField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getLoginButton() { return loginButton; }
    public Hyperlink getSignupLink() { return signupLink; }
}
