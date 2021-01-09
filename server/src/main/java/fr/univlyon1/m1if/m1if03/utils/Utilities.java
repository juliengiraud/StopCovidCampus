package fr.univlyon1.m1if.m1if03.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Utilities {

    private static String getBody(HttpServletRequest request) throws IOException {
        String body = "";
        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        while (line != null) {
            body += line;
            line = reader.readLine();
        }
        return body;
    }

    private static JSONObject getUrlData(HttpServletRequest request) {
        String body = (String) request.getParameterMap().keySet().toArray()[0];
        return new JSONObject(body);
    }

    private static JSONObject getJsonPayload(HttpServletRequest request) throws IOException {
        String body = getBody(request);
        return new JSONObject(body);
    }

    private static JSONObject getXmlPayload(HttpServletRequest request) throws IOException {
        String body = getBody(request);
        JSONObject xmlJSONObj = XML.toJSONObject(body);
        String key = (String) xmlJSONObj.keySet().toArray()[0];
        return xmlJSONObj.getJSONObject(key);
    }

    public static JSONObject getParams(HttpServletRequest request, List<String> params) throws IOException {
        JSONObject map;
        String contentType = request.getHeader("content-type");

        if (contentType.equals("application/x-www-form-urlencoded")) {
            map = getUrlData(request);
        } else if (contentType.equals("application/json")) {
            map = getJsonPayload(request);
        } else if (contentType.equals("application/xml")) {
            map = getXmlPayload(request);
        } else {
            throw new IOException("not implemented");
        }

        for (String param : params) {
            try {
                map.get(param);
            } catch (JSONException e) {
                throw new IOException("the request is missing a parameter");
            }
        }
        return map;
    }

    public static String getAcceptType(HttpServletRequest request) {
        String header = request.getHeader("Accept");
        if (header == null) {
            header = request.getHeader("accept");
        }
        if (header == null) {
            header = "";
        }
        if (header.contains("html")) {
            return "html";
        }
        if (header.contains("xml")) {
            return "xml";
        }
        return "json";
    }

    public static String getPathBase(HttpServletRequest request) {
        System.out.println("requestURL " + request.getRequestURL());
        System.out.println("requestURI" + request.getRequestURI());
        System.out.println("contextPath " + request.getContextPath());
        System.out.println("host " + request.getHeader("host"));
        System.out.println("origin " + request.getHeader("origin"));
        String origin = request.getHeader("origin");
        if (origin == null || origin.equals("null")) {
            origin = request.getHeader("host");
        }
        if (origin.startsWith("https")) {
            origin += "/api";
        } else {
            origin += ":8080";
        }
        return origin + request.getContextPath();
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return PresenceUcblJwtHelper.verifyAdmin(PresenceUcblJwtHelper.getTokenFromRequest(request));
    }

}
