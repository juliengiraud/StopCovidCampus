package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Passage;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PassagesDTO implements Serializable, GenericDTO {

    private List<Passage> passages;

    public PassagesDTO() {
    }

    public String getJSON() {
        List<String> urls = new ArrayList<>();
        for (Passage passage : this.passages) {
            urls.add(String.format("http://localhost:8080/passages/%d", passage.getId()));
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        String xml = "<passages>";
        for (Passage passage : passages) {
            xml += String.format("<passage>http://localhost:8080/passages/%d</passage>",passage.getId());
        }
        xml += "</passages>";
        return xml;
    }

    public List<Passage> getPassages() {
        return passages;
    }

    public void setPassages(List<Passage> passages) {
        this.passages = passages;
    }

}
