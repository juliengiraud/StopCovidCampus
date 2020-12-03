package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import org.json.JSONObject;

import java.io.Serializable;

public class SalleDTO implements Serializable {

    private Salle salle;

    public SalleDTO() {
    }

    public String getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.
        String json = "{";
        json += "\"nomSalle\": \"" + this.salle.getNom() + "\"";
        json += "\"capacite\": \"" + this.salle.getCapacite() + "\"";
        json += "\"present\": \"" + this.salle.getPresents() + "\"";
        json += "\"saturee\": \"" + this.salle.getSaturee() + "\"";
        json += "}";
        return json;
    }

    public String getXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<salle>";
        xml += "<nomSalle>" + this.salle.getNom() + "</nomSalle>";
        xml += "<capacitie>" + this.salle.getCapacite() + "</capacite>";
        xml += "<presents>" + this.salle.getPresents() + "</presents>";
        xml += "<saturee>" + this.salle.getSaturee() + "</saturee>";
        xml += "</salle>";
        return xml;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

}
