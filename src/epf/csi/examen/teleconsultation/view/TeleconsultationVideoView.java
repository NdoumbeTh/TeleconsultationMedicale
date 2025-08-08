package epf.csi.examen.teleconsultation.view;

import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.scene.control.Alert;

import java.awt.Desktop;
import java.net.URI;

/**
 * Classe utilitaire pour ouvrir une téléconsultation vidéo via Jitsi.
 */
public class TeleconsultationVideoView {

    /**
     * Ouvre une téléconsultation vidéo via Jitsi dans le navigateur par défaut.
     *
     * @param medecin L'utilisateur médecin initiateur de la téléconsultation.
     */
    public static void open(Utilisateur medecin) {
        try {
            // On génère un nom de salle unique
            String roomName = "carelinker-teleconsultation-" + medecin.getId();
            String url = "https://meet.jit.si/" + roomName;

            // Ouvre le lien dans le navigateur
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                showError("Erreur : le navigateur par défaut ne peut pas être ouvert.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture de la téléconsultation : " + e.getMessage());
        }
    }

    /**
     * Affiche une boîte d'alerte en cas d'erreur.
     *
     * @param message Le message d'erreur à afficher.
     */
    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
