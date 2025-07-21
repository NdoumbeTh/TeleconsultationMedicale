package epf.csi.examen.teleconsultation.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/teleconsultationmedicale";
    private static final String USER = "root"; // Remplacez par votre nom d'utilisateur
    private static final String PASSWORD = ""; // Remplacez par votre mot de passe

    // Constructeur privé pour empêcher l'instanciation
    private DBConnection() {}

    // Méthode pour obtenir une nouvelle connexion à chaque appel
    public static Connection getConnection() throws SQLException {
        try {
            // Charger explicitement le driver MySQL (optionnel avec les versions récentes)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Créer une nouvelle connexion
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            System.out.println("Connexion à la base de données réussie !");
            return connection;
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL non trouvé : " + e.getMessage());
            throw new SQLException("Driver MySQL non disponible", e);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            throw e;
        }
    }

    // Méthode utilitaire pour tester la connexion
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Test de connexion échoué : " + e.getMessage());
            return false;
        }
    }
}