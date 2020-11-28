package fr.univlyon1.m1if.m1if03.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class Utilities {

    public static String getBody(HttpServletRequest request) throws IOException {
        String body = "";
        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        while (line != null) {
            body += line;
            line = reader.readLine();
        }
        return body == "" ? "{}" : body;
    }

}
