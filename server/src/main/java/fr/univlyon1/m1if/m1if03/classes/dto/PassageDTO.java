package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Passage;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PassageDTO implements Serializable, GenericDTO {

    private Passage passage;
    private String basePath;

    public PassageDTO(String basePath) {
        this.basePath = basePath;
    }

    public String getJSON() {
        JSONObject json = new JSONObject();
        json.put("user", basePath + "/users/" + passage.getUser().getLogin());
        json.put("salle", basePath + "/salles/" + passage.getSalle().getNom());
        json.put("dateEntree", passage.getEntree());
        json.put("dateSortie", passage.getSortie());
        return new JSONObject(this.passage).toString();
    }

    public String getXML() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
        String xml = String.format(
                "<passage>\n" +
                "    <user>%s/users/%s</user>\n" +
                "    <salle>%s/salles/%s</salle>\n" +
                "    <dateEntree>%s</dateEntree>\n" +
                "    <dateSortie>%s</DateSortie>\n" +
                "</passage>",
                basePath, passage.getUser().getLogin(),
                basePath, passage.getSalle().getNom(),
                sdf.format(passage.getEntree()),
                sdf.format(passage.getSortie()));
        return xml;
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

}
