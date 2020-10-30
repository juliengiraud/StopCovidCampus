package fr.univlyon1.m1if.m1if03.classes;

import java.util.Date;
import java.util.Objects;

public class Passage {
    private final User user;
    private final Salle salle;
    private final Date entree;
    private Date sortie;

    public Passage(User user, Salle salle, Date entree) {
        this.user = user;
        this.salle = salle;
        this.entree = entree;
    }

    public void setSortie(Date sortie) throws IllegalArgumentException {
        if(sortie.after(this.entree))
            this.sortie = sortie;
        else
            throw new IllegalArgumentException("La date de sortie ne peut pas être antérieure à celle d'entrée.");
    }

    public User getUser() {
        return user;
    }

    public Salle getSalle() {
        return salle;
    }

    public Date getEntree() {
        return entree;
    }

    public Date getSortie() {
        return sortie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passage passage = (Passage) o;
        return user.equals(passage.user) &&
                salle.equals(passage.salle) &&
                entree.equals(passage.entree) &&
                Objects.equals(sortie, passage.sortie);
    }

    /**
     * Retourne vraie si deux passages ont lieu au même endroit au même moment
     * @param p
     * @return
     */
    public boolean isAuMemeEndroitEnMemeTemps(Passage p) {
        if (!salle.equals(p.salle)) { // Pas au même endroit
            return false;
        }
        Date a = getEntree();
        Date b = getSortie();
        Date c = p.getEntree();
        Date d = p.getSortie();
        // L'interval "a,b" touche l'interval "c,d" si "a" inclus dans "c,d" ou si b inclus dans "c,d"
        // ou si "c" inclus dans "a,b" ou si "d" inclus dans "a,b"
        return (a.after(c) && a.before(d) || b.after(c) && b.before(d)
                || c.after(a) && c.before(b) || d.after(a) && d.before(b));
    }
}