package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

public final class Route {

    private String path;
    private String method;

    private Route(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static List<Route> getWhiteList(User user) {
        List<Route> list = new ArrayList<>();

        if (user == null) {
            list.add(new Route("/users/login", "POST"));
        } else if (!user.isAdmin()) {
            String userId = user.getLogin();
            System.out.println(userId);
            list.add(new Route("/passages", "POST"));
            list.add(new Route("/passages/byUser/" + userId, "GET"));
            list.add(new Route("/passages/byUser/" + userId + "/enCours", "GET"));
            list.add(new Route("/passages/byUserAndDates/" + userId + "/", "GET"));
            list.add(new Route("/passages/byUserAndSalle/" + userId + "/", "GET"));
            list.add(new Route("/users/" + userId, "GET"));
            list.add(new Route("/users/" + userId + "/nom", "PUT"));
            list.add(new Route("/users/" + userId + "/passages", "GET"));
            list.add(new Route("/users/login", "POST"));
            list.add(new Route("/users/logout", "POST"));
        } else {
            list.add(new Route("/salles", "GET"));
            list.add(new Route("/salles", "POST"));
            list.add(new Route("/salles", "PUT"));
            list.add(new Route("/salles", "DELETE"));
            list.add(new Route("/passages", "POST"));
            list.add(new Route("/passages", "GET"));
            list.add(new Route("/users", "GET"));
            list.add(new Route("/users", "PUT"));
            list.add(new Route("/users", "POST"));
        }

        return list;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

}
