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
import com.catalogo.beans.Producto;
import com.catalogo.servlets.helpers.SecurityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/buscarProductos")
public class BuscarProductosPorDescripcion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(BuscarProductosPorDescripcion.class);

    private static final String RUTA_JSP = "/WEB-INF/jsp/";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SecurityManager securityManager = new SecurityManager();

        if (!securityManager.userInSession(request)) {
            response.sendRedirect("login.html");
        } else {

            ServicioCatalogo servicioCatalogo = ServicioCatalogoService.getInstancia();

            RequestDispatcher dispatcher = null;
            HttpSession sesion = request.getSession();

            String texto = request.getParameter("texto");

            sesion.setAttribute("catPadre", "Resultados de la busqueda (" + texto + ")");

            List<Producto> productos = servicioCatalogo.getProductosPorDescripcion(texto);

            if (productos == null) {
                log.info("La búsqueda de productos por descripción no ha producido resultados");
            }
            sesion.setAttribute("productos", productos);
            dispatcher = sesion.getServletContext()
                    .getRequestDispatcher(RUTA_JSP + "productos.jsp");

            dispatcher.forward(request, response);
        }
    }
}
