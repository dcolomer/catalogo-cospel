package com.catalogo.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.catalogo.servicios.ServicioCatalogo;
import com.catalogo.servicios.ServicioCatalogoService;
import com.catalogo.beans.*;
import com.catalogo.helpers.SecurityManager;

@WebServlet("/home")
public class Home extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String RUTA_JSP = "/WEB-INF/jsp/";

    @Override
    protected void service(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        SecurityManager securityManager = new SecurityManager();

        if (!securityManager.userInSession(request)) {
            response.sendRedirect("login.html");
        } else {

            ServicioCatalogo servicioCatalogo = ServicioCatalogoService.getInstancia();

            List<Categoria> categorias =
                    servicioCatalogo.getCategorias();

            // Creacion de la sesion. Si ya existe la utiliza
            HttpSession sesion = request.getSession(true);

            sesion.setAttribute("catPadre", "/");
            request.setAttribute("categorias", categorias);

            RequestDispatcher dispatcher = sesion.getServletContext()
                    .getRequestDispatcher(RUTA_JSP + "home.jsp");

            dispatcher.forward(request, response);
        }
    }

}
