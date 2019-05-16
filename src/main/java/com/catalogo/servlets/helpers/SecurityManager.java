package com.catalogo.servlets.helpers;

import javax.servlet.http.HttpServletRequest;

public class SecurityManager {

    public boolean userInSession(HttpServletRequest request) {
        String sessionUser = (String) request
                .getSession().getAttribute("sesNombreUsuarioValidado");

        return sessionUser == null ? false : true;
    }
}
