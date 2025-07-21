package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.UserController;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardAdminView {

    private final UserController userController = new UserController();
    private final ObservableList<Utilisateur> userList = FXCollections.observableArrayList();
    private final ObservableList<Utilisateur> filteredUserList = FXCollections.observableArrayList();
    private TableView<Utilisateur> tableView;
    private ComboBox<String> filterRoleBox;
    private Label statsLabel;

    public DashboardAdminView(Stage stage) {
        // Titre principal
        Label title = new Label("Interface Administrateur - Gestion de Tous les Utilisateurs");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Section de statistiques
        statsLabel = new Label();
        statsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e; -fx-padding: 10px;");

        // Section de filtrage
        HBox filterSection = createFilterSection();

        // TableView améliorée
        tableView = createTableView();

        // Section de formulaire d'ajout
        VBox formSection = createFormSection();

        // Layout principal
        VBox root = new VBox(15);
        root.getChildren().addAll(title, statsLabel, filterSection, tableView, formSection);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("CareLinker - Dashboard Administrateur Complet");
        stage.setScene(scene);
        stage.show();

        chargerTousLesUtilisateurs();
    }

    private HBox createFilterSection() {
        Label filterLabel = new Label("Filtrer par rôle:");
        filterLabel.setStyle("-fx-font-weight: bold;");

        filterRoleBox = new ComboBox<>();
        filterRoleBox.getItems().addAll("Tous", "admin", "medecin", "patient");
        filterRoleBox.setValue("Tous");
        filterRoleBox.setOnAction(e -> filtrerUtilisateurs());

        Button refreshButton = new Button("🔄 Actualiser");
        refreshButton.setOnAction(e -> chargerTousLesUtilisateurs());
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Button exportButton = new Button("📊 Exporter");
        exportButton.setOnAction(e -> exporterUtilisateurs());
        exportButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        HBox filterBox = new HBox(15, filterLabel, filterRoleBox, refreshButton, exportButton);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10));
        filterBox.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        return filterBox;
    }

    private TableView<Utilisateur> createTableView() {
        TableView<Utilisateur> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: white;");

        // Colonne ID
        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        // Colonne Nom
        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(180);

        // Colonne Email
        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(250);

        // Colonne Rôle avec style coloré
        TableColumn<Utilisateur, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(120);
        roleCol.setCellFactory(column -> new TableCell<Utilisateur, String>() {
            @Override
            protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(role.toUpperCase());
                    switch (role.toLowerCase()) {
                        case "admin":
                            setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                            break;
                        case "medecin":
                            setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                            break;
                        case "patient":
                            setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold;");
                            break;
                    }
                }
            }
        });

        // Colonne Actions
        TableColumn<Utilisateur, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(150);
        actionCol.setCellFactory(column -> new TableCell<Utilisateur, Void>() {
            private final Button deleteBtn = new Button("🗑️");
            private final Button editBtn = new Button("✏️");

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");

                deleteBtn.setOnAction(e -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    supprimerUtilisateur(user);
                });

                editBtn.setOnAction(e -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    modifierUtilisateur(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    buttons.setAlignment(Pos.CENTER);
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(idCol, nomCol, emailCol, roleCol, actionCol);
        table.setItems(filteredUserList);
        table.setPrefHeight(300);

        return table;
    }

    private VBox createFormSection() {
        Label formTitle = new Label("Ajouter un Nouvel Utilisateur");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Champs de formulaire
        TextField nomField = new TextField();
        nomField.setPromptText("Nom complet");
        nomField.setPrefWidth(200);

        TextField emailField = new TextField();
        emailField.setPromptText("Adresse email");
        emailField.setPrefWidth(250);

        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("Mot de passe");
        mdpField.setPrefWidth(200);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "medecin", "patient");
        roleBox.setPromptText("Sélectionner un rôle");
        roleBox.setPrefWidth(150);

        Button addButton = new Button("➕ Créer Utilisateur");
        addButton.setDefaultButton(true);
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        Label messageLabel = new Label();

        addButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = mdpField.getText();
            String role = roleBox.getValue();

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
                showMessage(messageLabel, "⚠️ Tous les champs sont requis.", "red");
                return;
            }

            // Validation email basique
            if (!email.contains("@") || !email.contains(".")) {
                showMessage(messageLabel, "⚠️ Format d'email invalide.", "red");
                return;
            }

            boolean success = userController.creerUtilisateur(nom, email, mdp, role);
            if (success) {
                showMessage(messageLabel, "✅ Utilisateur créé avec succès !", "green");
                nomField.clear();
                emailField.clear();
                mdpField.clear();
                roleBox.setValue(null);
                chargerTousLesUtilisateurs();
            } else {
                showMessage(messageLabel, "❌ Erreur: Email déjà existant ou problème de base de données.", "red");
            }
        });

        HBox form = new HBox(10, nomField, emailField, mdpField, roleBox, addButton);
        form.setAlignment(Pos.CENTER);

        VBox formSection = new VBox(10, formTitle, form, messageLabel);
        formSection.setAlignment(Pos.CENTER);
        formSection.setPadding(new Insets(15));
        formSection.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        return formSection;
    }

    private void chargerTousLesUtilisateurs() {
        try {
            List<Utilisateur> liste = userController.obtenirTousLesUtilisateurs();
            userList.setAll(liste);
            filtrerUtilisateurs();
            mettreAJourStatistiques();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les utilisateurs: " + e.getMessage());
        }
    }

    private void filtrerUtilisateurs() {
        String selectedRole = filterRoleBox.getValue();
        if ("Tous".equals(selectedRole)) {
            filteredUserList.setAll(userList);
        } else {
            List<Utilisateur> filtered = userList.stream()
                    .filter(user -> user.getRole().equalsIgnoreCase(selectedRole))
                    .collect(Collectors.toList());
            filteredUserList.setAll(filtered);
        }
    }

    private void mettreAJourStatistiques() {
        long totalUsers = userList.size();
        long admins = userList.stream().filter(u -> "admin".equalsIgnoreCase(u.getRole())).count();
        long medecins = userList.stream().filter(u -> "medecin".equalsIgnoreCase(u.getRole())).count();
        long patients = userList.stream().filter(u -> "patient".equalsIgnoreCase(u.getRole())).count();

        String stats = String.format(
                "📊 Statistiques: Total: %d utilisateurs | 👨‍💼 Admins: %d | 👨‍⚕️ Médecins: %d | 🏥 Patients: %d",
                totalUsers, admins, medecins, patients
        );
        statsLabel.setText(stats);
    }

    private void supprimerUtilisateur(Utilisateur user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur: " + user.getNom());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur?\nCette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = userController.supprimerUtilisateur(user.getId());
                if (success) {
                    chargerTousLesUtilisateurs();
                    showAlert("Succès", "Utilisateur supprimé avec succès.");
                } else {
                    showAlert("Erreur", "Impossible de supprimer l'utilisateur.");
                }
            }
        });
    }

    private void modifierUtilisateur(Utilisateur user) {
        // Créer une boîte de dialogue pour la modification
        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'utilisateur");
        dialog.setHeaderText("Modification de: " + user.getNom());

        ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField(user.getNom());
        TextField emailField = new TextField(user.getEmail());
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "medecin", "patient");
        roleBox.setValue(user.getRole());

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Rôle:"), 0, 2);
        grid.add(roleBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                user.setNom(nomField.getText());
                user.setEmail(emailField.getText());
                user.setRole(roleBox.getValue());
                return user;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            boolean success = userController.mettreAJourUtilisateur(result);
            if (success) {
                chargerTousLesUtilisateurs();
                showAlert("Succès", "Utilisateur modifié avec succès.");
            } else {
                showAlert("Erreur", "Impossible de modifier l'utilisateur.");
            }
        });
    }

    private void exporterUtilisateurs() {
        // Fonction d'export simple (console pour l'exemple)
        System.out.println("=== EXPORT DES UTILISATEURS ===");
        userList.forEach(user -> 
            System.out.printf("%d;%s;%s;%s%n", 
                user.getId(), user.getNom(), user.getEmail(), user.getRole())
        );
        showAlert("Export", "Liste des utilisateurs exportée dans la console.");
    }

    private void showMessage(Label label, String message, String color) {
        label.setText(message);
        label.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}