package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.AuthController;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView extends VBox {
    public RegisterView(Stage primaryStage) {
        AuthController authController = new AuthController();

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("patient", "medecin", "admin");
        roleBox.setValue("patient");

        Button registerBtn = new Button("S'inscrire");
        registerBtn.setOnAction(e -> {
            boolean success = authController.inscription(
                nomField.getText(),
                emailField.getText(),
                passwordField.getText(),
                roleBox.getValue()
            );
            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Inscription réussie !").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de l'inscription.").show();
            }
        });

        getChildren().addAll(nomField, emailField, passwordField, roleBox, registerBtn);
        setSpacing(10);
    }
}
