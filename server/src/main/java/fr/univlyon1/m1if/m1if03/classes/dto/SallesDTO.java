package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;

import java.io.Serializable;
import java.util.List;

public class SallesDTO implements Serializable {

    private List<Salle> salles;

    public SallesDTO() {
    }

    public List<Salle> getSalles() {
        return salles;
    }

    public void setSalles(List<Salle> salles) {
        this.salles = salles;
    }

}
