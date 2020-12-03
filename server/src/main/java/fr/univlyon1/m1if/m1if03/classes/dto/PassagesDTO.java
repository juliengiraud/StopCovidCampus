package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Passage;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PassagesDTO implements Serializable, GenericDTO {

    private List<Passage> passages;
    private String basePath;

    public PassagesDTO(String basePath) {
        this.basePath = basePath;
    }

    public String getJSON() {
        List<String> urls = new ArrayList<>();
        for (Passage passage : this.passages) {
            urls.add(basePath + "/passages/" + passage.getId());
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        String xml = "<passages>";
        for (Passage passage : passages) {
            xml += String.format(
                    "<passage>%s/passages/%d</passage>",
                    basePath, passage.getId());
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
