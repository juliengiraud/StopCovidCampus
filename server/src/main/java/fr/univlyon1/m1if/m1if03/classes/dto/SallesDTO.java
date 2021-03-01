package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SallesDTO implements Serializable, GenericDTO {

    private List<Salle> salles;
    private String basePath;

    public SallesDTO(String basePath) {
        this.basePath = basePath;
    }

    public String getJSON() {
        List<String> urls = new ArrayList<>();
        for (Salle salle : this.salles) {
            urls.add(basePath + "/salles/" + salle.getNomSalle());
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        String xml = "<salles>";
        for (Salle salle : salles) {
            xml += String.format(
                    "<salle>%s/salles/%s</salle>",
                    basePath, salle.getNomSalle());
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
