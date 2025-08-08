package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.MessageDAO;
import epf.csi.examen.teleconsultation.model.Message;
import epf.csi.examen.teleconsultation.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

public class MessageController {

    private final MessageDAO messageDAO;

    public MessageController() {
        this.messageDAO = new MessageDAO();
    }

    // Envoi d'un objet message complet
    public void envoyerMessage(Message msg) {
        messageDAO.envoyerMessage(msg);
    }

    // Envoi rapide via paramètres
    public void envoyerMessage(int expediteurId, int destinataireId, String contenu) {
        Message msg = new Message();
        msg.setExpediteurId(expediteurId);
        msg.setDestinataireId(destinataireId);
        msg.setContenu(contenu);
        msg.setDateEnvoi(LocalDateTime.now());
        msg.setLu(false);
        messageDAO.envoyerMessage(msg);
    }

    // Liste des messages reçus par un utilisateur (ex: médecin)
    public List<Message> getMessagesRecus(int destinataireId) {
        return messageDAO.getMessagesRecus(destinataireId);
    }

    // Liste des messages entre deux utilisateurs
    public List<Message> getMessagesBetweenUsers(int user1Id, int user2Id) {
        return messageDAO.getMessagesEntreUtilisateurs(user1Id, user2Id);
    }

    // Marquer un message comme lu par son ID
    public void marquerCommeLu(int messageId) {
        messageDAO.marquerMessageCommeLu(messageId);
    }

    // Marquer tous les messages d’un expéditeur vers un destinataire comme lus
    public void markMessagesAsRead(int expediteurId, int destinataireId) {
        messageDAO.marquerCommeLu(expediteurId, destinataireId);
    }

    // Compter les messages non lus
    public int countUnreadMessages(int expediteurId, int destinataireId) {
        return messageDAO.countMessagesNonLus(expediteurId, destinataireId);
    }

    // Liste des patients ayant discuté avec un médecin
    public List<Utilisateur> getPatientsCommunicantAvecMedecin(int medecinId) {
        return messageDAO.getPatientsAyantCommuniqueAvec(medecinId);
    }
}
