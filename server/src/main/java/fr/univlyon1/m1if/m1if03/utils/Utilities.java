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
        String origin = request.getRequestURL().substring(
                0, request.getRequestURL().indexOf(request.getContextPath())
        ) + request.getContextPath();
        if (origin.contains("127.0.0.1")) {
            origin = "https://192.168.75.76/api/v3";
        }
        return origin;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return PresenceUcblJwtHelper.verifyAdmin(PresenceUcblJwtHelper.getTokenFromRequest(request));
    }

}
