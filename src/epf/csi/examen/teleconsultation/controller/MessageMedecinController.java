package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.MessageDAO;
import epf.csi.examen.teleconsultation.model.Message;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class MessageMedecinController {
    private final MessageDAO messageDAO = new MessageDAO();

    public ObservableList<Message> getMessagesAvecPatient(int medecinId, int patientId) {
        List<Message> messages = messageDAO.getMessagesEntreUtilisateurs(medecinId, patientId);
        return FXCollections.observableArrayList(messages);
    }

    // ✅ Méthode ajoutée pour tous les messages reçus par le médecin
    public ObservableList<Message> getMessagesRecus(int medecinId) {
        List<Message> messages = messageDAO.getMessagesRecus(medecinId);
        return FXCollections.observableArrayList(messages);
    }

    public void envoyerMessage(String contenu, int medecinId, int patientId) {
        Message message = new Message();
        message.setExpediteurId(medecinId);
        message.setDestinataireId(patientId);
        message.setContenu(contenu);
        message.setDateEnvoi(LocalDateTime.now());
        message.setLu(false);
        messageDAO.envoyerMessage(message);
    }

    // ✅ Nouvelle version, qui marque un message comme lu via son ID
    public void marquerCommeLu(int messageId) {
        messageDAO.marquerMessageCommeLu(messageId);
    }

    public int getNombreMessagesNonLus(int patientId, int medecinId) {
        return messageDAO.countMessagesNonLus(patientId, medecinId);
    }

    public List<Utilisateur> getPatientsAyantCommunique(int medecinId) {
        return messageDAO.getPatientsAyantCommuniqueAvec(medecinId);
    }
}
