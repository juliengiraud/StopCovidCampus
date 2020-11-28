package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

public final class Route {
    private String path;
    private String method;
    private boolean checkUserConnectedIfNotAdmin;

    private Route(String path, String method, boolean checkUser) {
        this.path = path;
        this.method = method;
        this.checkUserConnectedIfNotAdmin = checkUser;
    }

    public static List<Route> getWhiteListForNonAdmin() {
        List<Route> whiteList = new ArrayList<>();

        whiteList.add(new Route("passages", "POST", false));
        whiteList.add(new Route("users/login", "POST", false));
        whiteList.add(new Route("users/logout", "POST", false));
        whiteList.add(new Route("users/{userId}", "GET", true));

        return whiteList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isCheckUserConnectedIfNotAdmin() {
        return checkUserConnectedIfNotAdmin;
    }

    public void setCheckUserConnectedIfNotAdmin(boolean checkUserConnectedIfNotAdmin) {
        this.checkUserConnectedIfNotAdmin = checkUserConnectedIfNotAdmin;
    }
}
