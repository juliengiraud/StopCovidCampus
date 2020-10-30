package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Passage> getPassagesBySalleName(String salleName) {
        return getPassagesBySalle(new Salle(salleName));
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
            if(passage.getUser().getLogin().equals(login))
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

    public List<User> getContacts(User user) {
        List<User> contacts = new ArrayList<>();
        for (Passage p : passages) {
            if (p.getUser().equals(user)) { // pour chaque passage de l'utilisateur "user"
                for (Passage p2 : passages) {
                    if (!p2.getUser().equals(user) // et pour chaque passage d'un autre utilisateur
                            && p2.isAuMemeEndroitEnMemeTemps(p)) { // s'ils se sont croisés
                        contacts.add(p2.getUser()); // alors ils sont contacts
                    }
                }
            }
        }
        return contacts;
    }

    public List<Salle> getDistinctSalles() {
        List<Salle> salles = new ArrayList<>();
        for (Passage p : passages) {
            if (!salles.contains(p.getSalle())) {
                salles.add(p.getSalle());
            }
        }
        return salles;
    }

    public List<Salle> getFullSalles() {
        List<Salle> salles = new ArrayList<>();
        Map<Salle, Integer> sallesEtCompteur = new HashMap<>();

        for (Passage p : passages) { // D'abord on compte le nombre de personne dans chaque salle
            if (!sallesEtCompteur.containsKey(p.getSalle())) {
                sallesEtCompteur.put(p.getSalle(), 0);
            }
            if (p.getSortie() == null) {
                sallesEtCompteur.put(p.getSalle(), sallesEtCompteur.get(p.getSalle()) + 1);
            } else {
                sallesEtCompteur.put(p.getSalle(), sallesEtCompteur.get(p.getSalle()) - 1);
            }
        }

        for (Map.Entry<Salle, Integer> s : sallesEtCompteur.entrySet()) { // Puis on ajoute les salles pleines (voire trop pleines)
            if (s.getValue() >= Salle.CAPACITE_MAX) {
                salles.add(s.getKey());
            }
        }
        return salles;
    }
}
