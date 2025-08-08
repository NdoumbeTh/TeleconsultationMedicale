package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {
    private AuthController authController;
    private VBox root;
    private Stage stage;

    public RegisterView(Stage stage) {
        this.stage = stage;
        this.authController = new AuthController();

        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("auth-root");

        HBox appBar = createAppBar();

        Label titre = new Label("Créer un compte");
        titre.getStyleClass().add("auth-title");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom complet");
        nomField.getStyleClass().add("auth-field");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("auth-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("auth-field");

        Button registerButton = new Button("S'inscrire");
        registerButton.getStyleClass().add("auth-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("auth-message");

        Hyperlink loginLink = new Hyperlink("Déjà un compte ? Se connecter");
        loginLink.setOnAction(e -> {
            // On crée une nouvelle instance de LoginView
            LoginView loginView = new LoginView(stage);
            // On demande au stage de changer de scène avec la nouvelle vue
            stage.setScene(new javafx.scene.Scene(loginView.getView(), 400, 400));
        });

        registerButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = passwordField.getText();
            String role = "patient";

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                messageLabel.setText("⚠️ Tous les champs sont requis.");
            } else {
                boolean success = authController.inscription(nom, email, mdp, role);
                if (success) {
                    messageLabel.setText("✅ Inscription réussie !");
                    LoginView loginView = new LoginView(stage);
                    stage.setScene(new javafx.scene.Scene(loginView.getView(), 400, 400));
                } else {
                    messageLabel.setText("❌ Erreur lors de l'inscription.");
                }
            }
        });

        VBox formBox = new VBox(10, titre, nomField, emailField, passwordField, registerButton, messageLabel, loginLink);
        formBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(appBar, formBox);
    }

    private HBox createAppBar() {
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/epf/csi/examen/teleconsultation/ressources/images/logo.png")));
        logoView.setFitWidth(40);
        logoView.setFitHeight(40);

        Label appName = new Label("CareLinker");
        appName.getStyleClass().add("app-title");

        HBox appBar = new HBox(10, logoView, appName);
        appBar.setAlignment(Pos.CENTER_LEFT);
        appBar.setPadding(new Insets(10));
        appBar.getStyleClass().add("app-bar");

        return appBar;
    }

    public VBox getView() {
        return root;
    }
}
