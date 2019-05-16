package com.catalogo.servlets;

import com.catalogo.servlets.helpers.SecurityManager;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/comeBack")
public class ComeBack extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SecurityManager securityManager = new SecurityManager();

        if (!securityManager.userInSession(request)) {
            response.sendRedirect("login.html");
        } else {
            String catPrevia = request.getParameter("cmbCategorias");

            // Si no viene de una categoría previa, entonces viene del buscador de productos
            if (catPrevia.equals("undefined"))
                response.sendRedirect("home");
            else {
                // Si efectivamente viene de una categoría previa
                int valorCatPrevia = Integer.parseInt(request.getParameter("cmbCategorias"));
                request.setAttribute("cmbCategorias", valorCatPrevia);

                RequestDispatcher dispatcher = request.getSession().getServletContext()
                        .getRequestDispatcher("/subcategoria");
                dispatcher.forward(request, response);
            }
        }
    }

}
