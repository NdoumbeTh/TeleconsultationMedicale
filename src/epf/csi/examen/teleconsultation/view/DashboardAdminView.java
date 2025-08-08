package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.UtilisateurController;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardAdminView {

    private final UtilisateurController userController = new UtilisateurController();
    private final ObservableList<Utilisateur> userList = FXCollections.observableArrayList();
    private final ObservableList<Utilisateur> filteredUserList = FXCollections.observableArrayList();
    private TableView<Utilisateur> tableView;
    private ComboBox<String> filterRoleBox;
    private TextField searchField;
    private Label statsLabel;
    private BarChart<String, Number> barChart;

    public DashboardAdminView(Stage stage) {
        Label title = new Label("Interface Administrateur - Gestion de Tous les Utilisateurs");
        title.getStyleClass().add("dashboard-title");

        statsLabel = new Label();
        statsLabel.getStyleClass().add("stats-label");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Rôle");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'utilisateurs");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Utilisateurs par rôle");
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(300);

        HBox filterSection = createFilterSection();
        tableView = createTableView();
        VBox formSection = createFormSection();

        VBox root = new VBox(15, title, statsLabel, barChart, filterSection, tableView, formSection);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("dashboard-root");

        Scene scene = new Scene(root, 1100, 900);
        java.net.URL cssURL = getClass().getResource("/epf/csi/examen/teleconsultation/ressources/carelinker.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("⚠️ Le fichier CSS est introuvable à l'emplacement /epf/csi/examen/teleconsultation/ressources/carelinker.css");
        }

        stage.setTitle("CareLinker - Dashboard Administrateur Complet");
        stage.setScene(scene);
        stage.show();

        chargerTousLesUtilisateurs();
    }

    private HBox createFilterSection() {
        Label filterLabel = new Label("Filtrer par rôle:");
        filterLabel.getStyleClass().add("filter-label");

        filterRoleBox = new ComboBox<>();
        filterRoleBox.getItems().addAll("Tous", "admin", "medecin", "patient");
        filterRoleBox.setValue("Tous");
        filterRoleBox.setOnAction(e -> filtrerUtilisateurs());
        filterRoleBox.getStyleClass().add("role-combobox");

        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom ou email");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filtrerUtilisateurs());
        searchField.getStyleClass().add("search-field");

        Button refreshButton = new Button("🔄 Actualiser");
        refreshButton.setOnAction(e -> chargerTousLesUtilisateurs());
        refreshButton.getStyleClass().add("refresh-button");

        Button exportButton = new Button("📊 Exporter");
        exportButton.setOnAction(e -> exporterUtilisateurs());
        exportButton.getStyleClass().add("export-button");

        HBox filterBox = new HBox(15, filterLabel, filterRoleBox, searchField, refreshButton, exportButton);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10));
        filterBox.getStyleClass().add("filter-box");

        return filterBox;
    }

    private TableView<Utilisateur> createTableView() {
        TableView<Utilisateur> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("user-table");

        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomCol.setPrefWidth(180);

        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(250);

        TableColumn<Utilisateur, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(120);

        TableColumn<Utilisateur, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(150);
        actionCol.setCellFactory(column -> new TableCell<Utilisateur, Void>() {
            private final Button deleteBtn = new Button("🗑️");
            private final Button editBtn = new Button("✏️");

            {
                deleteBtn.getStyleClass().add("delete-button");
                editBtn.getStyleClass().add("edit-button");

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
        formTitle.getStyleClass().add("form-title");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom complet");
        nomField.getStyleClass().add("form-field");

        TextField emailField = new TextField();
        emailField.setPromptText("Adresse email");
        emailField.getStyleClass().add("form-field");

        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("Mot de passe");
        mdpField.getStyleClass().add("form-field");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "medecin", "patient");
        roleBox.setPromptText("Sélectionner un rôle");
        roleBox.getStyleClass().add("form-field");

        Button addButton = new Button("➕ Créer Utilisateur");
        addButton.setDefaultButton(true);
        addButton.getStyleClass().add("add-button");

        Label messageLabel = new Label();
        // Optionnel: ajouter une classe css
        messageLabel.getStyleClass().add("message-label");

        addButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String mdp = mdpField.getText();
            String role = roleBox.getValue();

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
                showMessage(messageLabel, "⚠️ Tous les champs sont requis.", "red");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                showMessage(messageLabel, "⚠️ Format d'email invalide.", "red");
                return;
            }

            boolean success = UtilisateurController.creerUtilisateur(nom, email, mdp, role);
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
        formSection.getStyleClass().add("form-section");

        return formSection;
    }

    private void chargerTousLesUtilisateurs() {
        try {
            List<Utilisateur> liste = UtilisateurController.obtenirTousLesUtilisateurs();
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

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Admins", admins));
        series.getData().add(new XYChart.Data<>("Médecins", medecins));
        series.getData().add(new XYChart.Data<>("Patients", patients));

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    private void supprimerUtilisateur(Utilisateur user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur: " + user.getNom());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur? Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = UtilisateurController.supprimerUtilisateur(user.getId());
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
            boolean success = UtilisateurController.mettreAJourUtilisateur(result);
            if (success) {
                chargerTousLesUtilisateurs();
                showAlert("Succès", "Utilisateur modifié avec succès.");
            } else {
                showAlert("Erreur", "Impossible de modifier l'utilisateur.");
            }
        });
    }

    private void exporterUtilisateurs() {
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
