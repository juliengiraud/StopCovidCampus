package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;

import java.io.Serializable;

public class SalleDTO implements Serializable {

    private Salle salle;

    public SalleDTO() {
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

}
