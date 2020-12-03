package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import org.json.JSONArray;
import org.json.XML;

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
            urls.add("\"http://localhost:8080/salles/" + salle.getNom() + "\"");
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        return XML.toString(this.getJSON());
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

}
