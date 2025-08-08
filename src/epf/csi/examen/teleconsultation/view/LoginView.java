package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private AuthController authController;
    private VBox root;
    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.authController = new AuthController();

        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("auth-root");

        HBox appBar = createAppBar();

        Label titre = new Label("Connexion");
        titre.getStyleClass().add("auth-title");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("auth-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("auth-field");

        Button loginButton = new Button("Se connecter");
        loginButton.getStyleClass().add("auth-button");

        Label message = new Label();
        message.getStyleClass().add("auth-message");

        Hyperlink registerLink = new Hyperlink("Pas encore de compte ? S'inscrire");
        registerLink.setOnAction(e -> {
            RegisterView registerView = new RegisterView(stage);
            stage.setScene(new javafx.scene.Scene(registerView.getView(), 400, 400));
        });

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String mdp = passwordField.getText();
            authController.connexion(email, mdp, stage);
        });

        VBox formBox = new VBox(10, titre, emailField, passwordField, loginButton, message, registerLink);
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
