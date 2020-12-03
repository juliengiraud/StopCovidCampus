package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.User;
import org.json.JSONObject;
import org.json.XML;

import java.io.Serializable;

public class UserDTO implements Serializable, GenericDTO {

    private User user;

    public UserDTO() {
    }

    public String getJSON() {
        return new JSONObject(this.user).toString();
    }

    public String getXML() {
        String xml = String.format("<user>\n" +
                    "    <login>%s</login>\n" +
                    "    <nom>%s</nom>\n" +
                    "    <admin>%b</admin>\n" +
                    "</user>",
            user.getLogin(), user.getNom(), user.getAdmin());
        return xml;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
