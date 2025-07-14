package epf.csi.examen.teleconsultation.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SignupView {
    private TextField nomField = new TextField();
    private TextField prenomField = new TextField();
    private TextField emailField = new TextField();
    private TextField telephoneField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button signupButton = new Button("S'inscrire");
    private Hyperlink loginLink = new Hyperlink("Déjà un compte ? Se connecter");

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(40));
        Label title = new Label("Créer un compte");
        title.setStyle("-fx-font-size: 20px;");

        nomField.setPromptText("Nom");
        prenomField.setPromptText("Prénom");
        emailField.setPromptText("Email");
        telephoneField.setPromptText("Téléphone");
        passwordField.setPromptText("Mot de passe");

        root.getChildren().addAll(title, nomField, prenomField, emailField, telephoneField, passwordField, signupButton, loginLink);
        return new Scene(root, 400, 400);
    }

    public TextField getNomField() { return nomField; }
    public TextField getPrenomField() { return prenomField; }
    public TextField getEmailField() { return emailField; }
    public TextField getTelephoneField() { return telephoneField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getSignupButton() { return signupButton; }
    public Hyperlink getLoginLink() { return loginLink; }
}
