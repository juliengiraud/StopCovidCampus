package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SallesDTO implements Serializable, GenericDTO {

    private List<Salle> salles;

    public SallesDTO() {
    }

    public String getJSON() {
        List<String> urls = new ArrayList<>();
        for (Salle salle : this.salles) {
            urls.add(String.format("http://localhost:8080/salles/%s", salle.getNom()));
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        String xml = "<salles>";
        for (Salle salle : salles) {
            xml += String.format("<salle>http://localhost:8080/salles/%s</salle>", salle.getNom());
        }
        xml += "</salles>";
        return xml;
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

}
