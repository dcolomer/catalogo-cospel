package com.catalogo.servlets;

import com.catalogo.servlets.helpers.SecurityManager;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/desconexion")
public class Desconexion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SecurityManager securityManager = new SecurityManager();

        if (!securityManager.userInSession(request)) {
            response.sendRedirect("login.html");
        } else {
            request.getSession().invalidate();

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/login.html");

            dispatcher.forward(request, response);
        }
    }

}
