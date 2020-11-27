package fr.univlyon1.m1if.m1if03.classes;

import java.util.HashMap;
import java.util.Map;

public final class Routes {


    private Routes() {}

    public static Map<String, String> getWhiteList() {
        Map<String, String> whiteList = new HashMap<>();

        whiteList.put("passages", "POST");
        whiteList.put("users/login", "POST");
        whiteList.put("users/logout", "POST");

        return whiteList;
    }
}
