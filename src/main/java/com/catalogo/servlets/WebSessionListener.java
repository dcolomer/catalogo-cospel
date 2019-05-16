package com.catalogo.servlets;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class WebSessionListener implements HttpSessionListener {

    private static Logger log = LogManager.getLogger(WebSessionListener.class);

    public void sessionCreated(HttpSessionEvent sessionEvent) {

        log.info("Session creada. ID: " + sessionEvent.getSession().getId());

        ServletContext contexto = sessionEvent.getSession().getServletContext();

        synchronized (contexto) {

            Integer usuariosConectados = (Integer) contexto.getAttribute("usuariosConectados");

            if (usuariosConectados == null) {
                usuariosConectados = new Integer(0);
            }
            usuariosConectados += 1;
            contexto.setAttribute("usuariosConectados", usuariosConectados);
        } // synchronized
    }

    public void sessionDestroyed(HttpSessionEvent sessionEvent) {

        log.info("Session destruida. ID: " + sessionEvent.getSession().getId());

        ServletContext contexto = sessionEvent.getSession().getServletContext();

        synchronized (contexto) {

            Integer usuariosConectados = (Integer) contexto.getAttribute("usuariosConectados");

            if (usuariosConectados == null) {
                usuariosConectados = new Integer(0);
            }
            usuariosConectados -= 1;
            contexto.setAttribute("usuariosConectados", usuariosConectados);
        } // synchronized
    }
}
