// Fichier modifié : RendezVousListView.java (affiche le nom du patient dans la table)
package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.RendezVousController;
import epf.csi.examen.teleconsultation.dao.PatientDAO;
import epf.csi.examen.teleconsultation.model.RendezVous;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class RendezVousListView {

    private final int medecinId;
    private final Stage stage;
    private final DashboardMedecinView dashboard;

    private TableView<RendezVous> table;
    private ObservableList<RendezVous> data;

    public RendezVousListView(int medecinId, Stage stage, DashboardMedecinView dashboard) {
        this.medecinId = medecinId;
        this.stage = stage;
        this.dashboard = dashboard;
    }

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Liste des rendez-vous");
        title.setFont(Font.font("Arial", 22));

        table = new TableView<>();
        table.setPrefWidth(700);
        table.setPrefHeight(400);

        // Colonnes
        TableColumn<RendezVous, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<RendezVous, LocalDateTime> colDate = new TableColumn<>("Date & Heure");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        colDate.setPrefWidth(200);

        TableColumn<RendezVous, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setPrefWidth(150);

        TableColumn<RendezVous, String> colPatientNom = new TableColumn<>("Nom Patient");
        colPatientNom.setCellValueFactory(new PropertyValueFactory<>("patientNom"));
        colPatientNom.setPrefWidth(200);

        table.getColumns().addAll(colId, colDate, colStatut, colPatientNom);

        loadRendezVous();

        // Boutons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button btnCreer = new Button("Créer");
        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");
        Button btnRetour = new Button("Retour au dashboard");

        buttons.getChildren().addAll(btnCreer, btnModifier, btnSupprimer, btnRetour);

        // Actions
        btnCreer.setOnAction(e -> ouvrirFormulaireRendezVous(null));
        btnModifier.setOnAction(e -> {
            RendezVous selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ouvrirFormulaireRendezVous(selected);
            } else {
                showAlert("Aucun rendez-vous sélectionné", "Veuillez sélectionner un rendez-vous à modifier.", Alert.AlertType.WARNING);
            }
        });
        btnSupprimer.setOnAction(e -> {
            RendezVous selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean conf = showConfirmation("Confirmer la suppression", "Voulez-vous vraiment supprimer ce rendez-vous ?");
                if (conf) {
                    supprimerRendezVous(selected);
                }
            } else {
                showAlert("Aucun rendez-vous sélectionné", "Veuillez sélectionner un rendez-vous à supprimer.", Alert.AlertType.WARNING);
            }
        });
        btnRetour.setOnAction(e -> stage.getScene().setRoot(dashboard.getView()));

        root.getChildren().addAll(title, table, buttons);
        return root;
    }

    private void loadRendezVous() {
        RendezVousController controller = new RendezVousController();
        try {
            List<RendezVous> rdvs = controller.listerRendezVousMedecin(medecinId);
            data = FXCollections.observableArrayList(rdvs);
            table.setItems(data);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les rendez-vous.", Alert.AlertType.ERROR);
        }
    }

 // Fichier modifié : RendezVousListView.java (méthode ouvrirFormulaireRendezVous)

private void ouvrirFormulaireRendezVous(RendezVous rdv) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle(rdv == null ? "Créer un rendez-vous" : "Modifier le rendez-vous");

    VBox dialogVBox = new VBox(10);
    dialogVBox.setPadding(new Insets(20));
    dialogVBox.setAlignment(Pos.CENTER);

    Label lblDate = new Label("Date :");
    DatePicker dpDate = new DatePicker();
    dpDate.setPrefWidth(200);

    Label lblHeure = new Label("Heure (HH:mm) :");
    TextField tfHeure = new TextField();
    tfHeure.setPromptText("ex: 14:30");
    tfHeure.setPrefWidth(200);

    Label lblStatut = new Label("Statut :");
    ComboBox<String> cbStatut = new ComboBox<>();
    cbStatut.getItems().addAll("Prévu", "Confirmé", "Annulé");
    cbStatut.setPrefWidth(200);

    Label lblPatient = new Label("Patient :");
    ComboBox<Utilisateur> cbPatients = new ComboBox<>();
    cbPatients.setPrefWidth(200);
    cbPatients.setPromptText("Sélectionnez un patient");

    // Charger la liste des patients
    try {
        PatientDAO patientDAO = new PatientDAO(DBConnection.getConnection());
        ObservableList<Utilisateur> patientsList = patientDAO.getAllPatients();
        cbPatients.setItems(patientsList);

        cbPatients.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Utilisateur user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getNom());
            }
        });

        cbPatients.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Utilisateur user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getNom());
            }
        });

        // Pré-remplir en cas de modification
        if (rdv != null) {
            for (Utilisateur u : patientsList) {
                if (u.getId() == rdv.getPatientId()) {
                    cbPatients.setValue(u);
                    break;
                }
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    // Si modification, pré-remplir les champs
    if (rdv != null) {
        LocalDateTime dt = rdv.getDateHeure();
        if (dt != null) {
            dpDate.setValue(dt.toLocalDate());
            tfHeure.setText(String.format("%02d:%02d", dt.getHour(), dt.getMinute()));
        }
        cbStatut.setValue(rdv.getStatut());
    }

    Button btnValider = new Button(rdv == null ? "Créer" : "Modifier");
    Button btnAnnuler = new Button("Annuler");

    HBox btnBox = new HBox(10, btnValider, btnAnnuler);
    btnBox.setAlignment(Pos.CENTER);

    Label lblMessage = new Label();

    dialogVBox.getChildren().addAll(
        lblDate, dpDate,
        lblHeure, tfHeure,
        lblStatut, cbStatut,
        lblPatient, cbPatients,
        lblMessage,
        btnBox
    );

    Scene dialogScene = new Scene(dialogVBox, 300, 450);
    dialog.setScene(dialogScene);

    btnValider.setOnAction(ev -> {
        LocalDate date = dpDate.getValue();
        String heureStr = tfHeure.getText();
        String statut = cbStatut.getValue();
        Utilisateur selectedPatient = cbPatients.getValue();

        if (date == null || heureStr == null || heureStr.isEmpty() || statut == null || selectedPatient == null) {
            lblMessage.setText("Tous les champs doivent être remplis.");
            return;
        }

        // Parse heure (HH:mm)
        String[] parts = heureStr.split(":");
        if (parts.length != 2) {
            lblMessage.setText("Format heure invalide (HH:mm).");
            return;
        }

        int h, m;
        try {
            h = Integer.parseInt(parts[0]);
            m = Integer.parseInt(parts[1]);
            if (h < 0 || h > 23 || m < 0 || m > 59) {
                lblMessage.setText("Heure invalide.");
                return;
            }
        } catch (NumberFormatException ex) {
            lblMessage.setText("Heure invalide.");
            return;
        }

        int patientId = selectedPatient.getId();
        LocalDateTime dateHeure = LocalDateTime.of(date, LocalTime.of(h, m));

        RendezVousController controller = new RendezVousController();
        boolean success;

        if (rdv == null) {
            // Création
            success = controller.creerRendezVous(medecinId, patientId, dateHeure, statut);
        } else {
            // Modification
            rdv.setDateHeure(dateHeure);
            rdv.setPatientId(patientId);
            rdv.setStatut(statut);
            success = controller.modifierRendezVous(rdv);
        }

        if (success) {
            loadRendezVous();
            dialog.close();
        } else {
            lblMessage.setText("Erreur lors de l'enregistrement.");
        }
    });

    btnAnnuler.setOnAction(ev -> dialog.close());

    dialog.showAndWait();
}

    private void supprimerRendezVous(RendezVous rdv) {
        RendezVousController controller = new RendezVousController();
        boolean success = controller.supprimerRendezVous(rdv.getId());
        if (success) {
            data.remove(rdv);
        } else {
            showAlert("Erreur", "Impossible de supprimer le rendez-vous.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
