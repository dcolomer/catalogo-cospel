package com.catalogo.servlets;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JInitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void init(ServletConfig config) throws ServletException {

        /*System.out.println("Inicializando el servlet Log4JInitServlet para cargar " +
                "la configuracion de Log4J");

        String log4jLocation = config.getInitParameter("log4j-ubicacion-propiedades");

        ServletContext sc = config.getServletContext();

        if (log4jLocation == null) {
            System.err.println("*** No se ha encontrado el parametro de inicializacion " +
                    "log4j-ubicacion-propiedades. Inicializando configuracion por defecto " +
                    "de log4j (BasicConfigurator)");
            BasicConfigurator.configure();
        } else {
            String webAppPath = sc.getRealPath("/");
            String log4jProp = webAppPath + log4jLocation;
            File fileExists = new File(log4jProp);
            if (fileExists.exists()) {
                System.out.println("Inicializando log4j con la configuracion de " + log4jProp);
                PropertyConfigurator.configure(log4jProp);
            } else {
                System.err.println("*** No se ha encontrado el fichero " + log4jProp +
                        ". Inicializando configuracion por defecto de log4j " +
                        "(BasicConfigurator)");
                BasicConfigurator.configure();
            }
        }

        super.init(config);*/
    }
}
