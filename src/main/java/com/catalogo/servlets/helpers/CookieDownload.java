package com.catalogo.servlets.helpers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieDownload {

    public static HttpServletResponse insertarCookie(HttpServletRequest request,
                                            HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        boolean foundCookie = false;

        for(int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals("fileDownload")) {
                foundCookie = true;
            }
        }

        if (!foundCookie) {
            Cookie cookie = new Cookie("fileDownload", "true");
            cookie.setPath("/");
            cookie.setMaxAge(24*60*60);
            response.addCookie(cookie);
        }

        return response;
    }
}
