package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import org.json.JSONObject;

import java.io.Serializable;

public class SalleDTO implements Serializable, GenericDTO {

    private Salle salle;
    private String basePath;

    public SalleDTO(String basePath) {
        this.basePath = basePath;
    }

    public String getJSON() {
        return new JSONObject(this.salle).toString();
    }

    public String getXML() {
        String xml = String.format(
                "<salle>\n" +
                "    <nomSalle>%s</nomSalle>\n" +
                "    <capacite>%d</capacite>\n" +
                "    <presents>%d</presents>\n" +
                "    <saturee>%b</saturee>\n" +
                "</salle>",
                salle.getNomSalle(), salle.getCapacite(), salle.getPresents(), salle.getSaturee());
        return xml;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

}
