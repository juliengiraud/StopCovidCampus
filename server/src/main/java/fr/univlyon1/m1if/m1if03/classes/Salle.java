package fr.univlyon1.m1if.m1if03.classes;

import java.util.Objects;

public class Salle {
    private final String nomSalle;
    private int capacite = -1;
    private int presents = 0;
    private Boolean sature = false;

    public Salle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
        sature = getSaturee();
    }

    public int getPresents() {
        return presents;
    }

    public void incPresent() {
        this.presents++;
        sature = getSaturee();
    }

    public void decPresent() {
        this.presents--;
        sature = getSaturee();
    }

    public boolean getSaturee() {
        return capacite > -1 && this.getPresents() > this.getCapacite();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salle salle = (Salle) o;
        return Objects.equals(nomSalle, salle.nomSalle);
    }
}