package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import org.json.JSONObject;

import java.io.Serializable;

public class SalleDTO implements Serializable, GenericDTO {

    private Salle salle;

    public SalleDTO() {
    }

    public String getJSON() {
        return new JSONObject(this.salle).toString();
    }

    public String getXML() {
        return "";
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

}
