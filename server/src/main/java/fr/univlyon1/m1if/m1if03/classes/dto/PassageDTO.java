package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Passage;
import org.json.JSONObject;
import org.json.XML;

import javax.xml.crypto.dsig.XMLObject;
import java.io.Serializable;

public class PassageDTO implements Serializable, GenericDTO {

    private Passage passage;

    public PassageDTO() {
    }

    public String getJSON() {
        return new JSONObject(this.passage).toString();
    }

    public String getXML() {
        return XML.toString(this.getJSON());
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

}
