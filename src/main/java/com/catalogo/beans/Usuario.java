package com.catalogo.beans;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Usuario implements HttpSessionBindingListener {

    private final Logger log = LogManager.getLogger(Usuario.class);

    private String nombre = "";

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void valueBound(HttpSessionBindingEvent sessionBindingEvent) {

        log.info("User agregado a la session. ID: "+
                sessionBindingEvent.getSession().getId());

        gestionarSesionUsuario(sessionBindingEvent, true);
    }

    public void valueUnbound(HttpSessionBindingEvent sessionBindingEvent) {

        log.info("User eliminado de session. ID: "+
                sessionBindingEvent.getSession().getId());

        gestionarSesionUsuario(sessionBindingEvent, false);
    }

    private void gestionarSesionUsuario(HttpSessionBindingEvent sessionBindingEvent,
                                        boolean agregarUsuario) {

        ServletContext contexto =
                sessionBindingEvent.getSession().getServletContext();

        synchronized (contexto) {
            Integer usuariosValidados = (Integer) contexto
                    .getAttribute("usuariosValidados");
            if (usuariosValidados == null) {
                usuariosValidados = new Integer(0);
            }

            if (agregarUsuario) {
                usuariosValidados += 1;
            } else {
                usuariosValidados -= 1;
            }
            contexto.setAttribute("usuariosValidados", usuariosValidados);
        }

    }
}
