package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersDTO implements Serializable, GenericDTO {

    private List<User> users;

    public UsersDTO() {
    }

    public String getJSON() {
        List<String> urls = new ArrayList<>();
        for (User user : this.users) {
            urls.add("\"http://localhost:8080/users/" + user.getLogin() + "\"");
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        return "";
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
