package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.User;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersDTO implements Serializable, GenericDTO {

    private List<User> users;
    private String basePath;

    public UsersDTO(String basePath) {
        this.basePath = basePath;
    }

    public String getJSON() {
        List<String> urls = new ArrayList<>();
        for (User user : this.users) {
            urls.add(basePath + "/users/" + user.getLogin());
        }
        return new JSONArray(urls).toString();
    }

    public String getXML() {
        String xml = "<users>";
        for (User user : users) {
            xml += String.format(
                    "<user>%s/users/%s</user>",
                    basePath, user.getLogin());
        }
        xml += "</users>";
        return xml;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
