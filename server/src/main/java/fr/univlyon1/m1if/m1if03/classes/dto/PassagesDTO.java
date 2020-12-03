package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Passage;

import java.io.Serializable;
import java.util.List;

public class PassagesDTO implements Serializable {

    private List<Passage> passages;

    public PassagesDTO() {
    }

    public List<Passage> getPassages() {
        return passages;
    }

    public void setPassages(List<Passage> passages) {
        this.passages = passages;
    }

}
