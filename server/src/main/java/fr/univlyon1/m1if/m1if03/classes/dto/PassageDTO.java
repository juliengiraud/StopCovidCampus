package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Passage;

import java.io.Serializable;

public class PassageDTO implements Serializable {

    private Passage passage;

    public PassageDTO() {
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

}
