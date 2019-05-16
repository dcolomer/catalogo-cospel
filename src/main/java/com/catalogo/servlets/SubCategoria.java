package com.catalogo.servlets;

import com.catalogo.helpers.SecurityManager;

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
import com.catalogo.beans.Categoria;
import com.catalogo.beans.Producto;

@WebServlet("/subcategoria")
public class SubCategoria extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String RUTA_JSP = "/WEB-INF/jsp/";

    private static final int OUTLET = 707;
    private static final int PACKS = 740;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SecurityManager securityManager = new SecurityManager();

        if (!securityManager.userInSession(request)) {
            response.sendRedirect("login.html");
        } else {

            ServicioCatalogo servicioCatalogo = ServicioCatalogoService.getInstancia();

            int subcategoria = 0;

            if (request.getAttribute("cmbCategorias") != null) {
                subcategoria = (Integer) request.getAttribute("cmbCategorias");
            } else {
                subcategoria = Integer.parseInt(request.getParameter("cmbCategorias"));
            }

            List<Categoria> subcategorias =
                    servicioCatalogo.getCategorias(subcategoria);
            RequestDispatcher dispatcher = null;

            HttpSession sesion = request.getSession();

            String catActual = servicioCatalogo.getPathCategoria(subcategoria);
            sesion.setAttribute("catPadre", catActual);

            int catPrevia = servicioCatalogo.getIdCategoriaPadre(subcategoria);
            sesion.setAttribute("catPrevia", catPrevia);

            boolean existenCategoriasAnidadas = !subcategorias.isEmpty();

            if (existenCategoriasAnidadas) {
                sesion.setAttribute("categorias", subcategorias);
                dispatcher = sesion.getServletContext()
                        .getRequestDispatcher(RUTA_JSP + "categorias.jsp");
            } else {

                List<Producto> productos = null;

                if (subcategoria == OUTLET) {
                    productos = servicioCatalogo.getProductosOutlet(subcategoria);
                } else if (subcategoria == PACKS) {
                    productos = servicioCatalogo.getProductosPacks(subcategoria);
                } else {
                    productos = servicioCatalogo.getProductos(subcategoria);
                }

                sesion.setAttribute("productos", productos);
                dispatcher = sesion.getServletContext()
                        .getRequestDispatcher(RUTA_JSP + "productos.jsp");
            }

            dispatcher.forward(request, response);
        }
    }

}
