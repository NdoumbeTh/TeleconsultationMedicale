package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.controller.ConsultationController;
import epf.csi.examen.teleconsultation.model.Consultation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.List;

public class ConsultationListView {

    private final ConsultationController consultationController = new ConsultationController();
    private final ObservableList<Consultation> consultations = FXCollections.observableArrayList();

    private TableView<Consultation> tableView;
    private ComboBox<String> statusFilter;
    private DatePicker dateFilter;
    private Pagination pagination;

    private int rowsPerPage = 15;

    private int medecinId;

    public ConsultationListView(int medecinId) {
        this.medecinId = medecinId;
    }

    public VBox getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Liste des Consultations");
        title.setFont(Font.font(22));

        HBox filters = createFilters();

        tableView = createTableView();

        pagination = new Pagination(1, 0);
        pagination.setPageFactory(this::createPage);

        root.getChildren().addAll(title, filters, tableView, pagination);

        loadConsultations();
        updatePagination();

        return root;
    }

    private HBox createFilters() {
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Toutes", "En attente", "En cours", "Terminée", "Annulée");
        statusFilter.setValue("Toutes");
        statusFilter.setOnAction(e -> filterAndRefresh());

        dateFilter = new DatePicker();
        dateFilter.setPromptText("Filtrer par date");
        dateFilter.setOnAction(e -> filterAndRefresh());

        Button resetBtn = new Button("Réinitialiser filtres");
        resetBtn.setOnAction(e -> {
            statusFilter.setValue("Toutes");
            dateFilter.setValue(null);
            filterAndRefresh();
        });

        HBox box = new HBox(10, new Label("Statut:"), statusFilter, new Label("Date:"), dateFilter, resetBtn);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private TableView<Consultation> createTableView() {
        TableView<Consultation> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Consultation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Consultation, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        patientCol.setPrefWidth(180);

        TableColumn<Consultation, String> dateCol = new TableColumn<>("Date/Heure");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        dateCol.setPrefWidth(140);

        TableColumn<Consultation, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(120);

        TableColumn<Consultation, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        statutCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, patientCol, dateCol, typeCol, statutCol);

        return table;
    }

    private void loadConsultations() {
        try {
            List<Consultation> list = consultationController.listerConsultationsMedecin(medecinId);
            consultations.setAll(list);
        } catch (Exception e) {
            System.err.println("Erreur chargement consultations: " + e.getMessage());
        }
    }

    private void filterAndRefresh() {
        updatePagination();
    }

    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, filteredConsultations().size());
        List<Consultation> pageData = filteredConsultations().subList(fromIndex, toIndex);
        tableView.setItems(FXCollections.observableArrayList(pageData));
        return new VBox(tableView);
    }

    private List<Consultation> filteredConsultations() {
        String statut = statusFilter.getValue();
        LocalDate date = dateFilter.getValue();

        return consultations.filtered(c -> {
            boolean statutOk = statut.equals("Toutes") || c.getStatut().equalsIgnoreCase(statut);
            boolean dateOk = date == null || c.getDateHeure().toLocalDate().equals(date);
            return statutOk && dateOk;
        });
    }

    private void updatePagination() {
        int totalPage = (int) Math.ceil((double) filteredConsultations().size() / rowsPerPage);
        if (totalPage == 0) totalPage = 1;
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        createPage(0);
    }
}
