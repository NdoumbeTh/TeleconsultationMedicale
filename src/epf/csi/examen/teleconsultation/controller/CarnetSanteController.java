package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.model.CarnetSante;

import java.util.ArrayList;
import java.util.List;

public class CarnetSanteController {

    public CarnetSante getCarnetSanteParPatient(int patientId) {
        // Simulé — retourne un carnet fictif
        return new CarnetSante(patientId, "Groupe sanguin : A+\nAllergies : aucune\nAntécédents : asthme");
    }

    public void mettreAJourCarnet(CarnetSante carnet) {
        // Simuler l’update
        System.out.println("Carnet mis à jour pour patient : " + carnet.getPatientId());
    }
}
