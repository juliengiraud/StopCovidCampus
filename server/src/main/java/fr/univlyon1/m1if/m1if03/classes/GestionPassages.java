package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionPassages {
    private final List<Passage> passages = new ArrayList<>();

    public void add(Passage passage) {
        this.passages.add(passage);
    }

    /**
     * Renvoie tous les passages en mémoire
     * @return Une liste recopiée ; la liste originale ne doit pas être modifiée
     */
    public List<Passage> getAllPassages() {
        return new ArrayList<>(passages);
    }

    public List<Passage> getPassagesBySalle(Salle salle) {
        return filterBySalle(passages, salle);
    }

    public List<Passage> getPassagesByUser(User user) {
        return filterByUser(passages, user);
    }

    public List<Passage> getPassagesByUserLogin(String login) {
        return filterByUserLogin(passages, login);
    }

    public List<Passage> getPassagesByUserAndSalle(User user, Salle salle) {
        return filterBySalle(filterByUser(passages, user), salle);
    }

    /**
     * Renvoie les users dans une salle dans un intervalle de temps donné
     * @param salle La salle
     * @param debut Début de l'intervalle
     * @Param fin Fin de l'intervalle
     * @return Une liste de passages en cours ou terminés
     */
    public List<Passage> getPassagesBySalleAndDates(Salle salle, Date debut, Date fin) {
        return filterBySalle(filterByDates(passages, debut, fin), salle);
    }

    /**
     * Permet de retrouver un user pendant un intervalle de temps donné
     * @param user
     * @param debut Début de l'intervalle
     * @Param fin Fin de l'intervalle
     * @return Une liste de passages en cours ou terminés
     */
    public List<Passage> getPassagesByUserAndDates(User user, Date debut, Date fin) {
        return filterByUser(filterByDates(passages, debut, fin), user);
    }

    // Méthodes privées réutilisées par les getters

    private List<Passage> filterByUser(List<Passage> source, User user) {
        List<Passage> res = new ArrayList<>();
        for (Passage passage : source) {
            if(passage.getUser().equals(user))
                res.add(passage);
        }
        return res;
    }

    private List<Passage> filterByUserLogin(List<Passage> source, String login) {
        List<Passage> res = new ArrayList<>();
        for (Passage passage : source) {
            if(passage.getUser().getLogin() == login)
                res.add(passage);
        }
        return res;
    }

    private List<Passage> filterBySalle(List<Passage> source, Salle salle) {
        List<Passage> res = new ArrayList<>();
        for (Passage passage : source) {
            if(passage.getSalle().equals(salle))
                res.add(passage);
        }
        return res;
    }

    private List<Passage> filterByDates(List<Passage> source, Date debut, Date fin) {
        List<Passage> res = new ArrayList<>();
        for (Passage passage : source) {
            if(!passage.getEntree().after(debut) && (passage.getSortie() == null || !passage.getSortie().before(fin)))
                res.add(passage);
        }
        return res;
    }
}
