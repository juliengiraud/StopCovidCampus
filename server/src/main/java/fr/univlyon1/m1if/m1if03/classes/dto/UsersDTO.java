package fr.univlyon1.m1if.m1if03.classes.dto;

import fr.univlyon1.m1if.m1if03.classes.User;

import java.io.Serializable;
import java.util.List;

public class UsersDTO implements Serializable {

    private List<User> users;

    public UsersDTO() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
