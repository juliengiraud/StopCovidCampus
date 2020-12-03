package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable, GenericDTO {

    private User user;

    public UserDTO() {
    }

    public String getJSON() {
        return new JSONObject(this.user).toString();
    }

    public String getXML() {
        return XML.toString(this.getJSON());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
