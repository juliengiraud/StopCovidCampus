package fr.univlyon1.m1if.m1if03.classes;

import java.util.Objects;

public class Salle {

    public static final int CAPACITE_MAX = 2;
    private final String nom;

    public Salle(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salle salle = (Salle) o;
        return Objects.equals(nom, salle.nom);
    }

    @Override
    public int hashCode() { // Sans ça la HashMap ne risquait pas de marcher
        return nom.hashCode();
    }
}