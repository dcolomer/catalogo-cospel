package com.catalogo.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.beans.Producto;

@WebServlet("/imagenes")
public class Imagenes extends HttpServlet {

    private static final Logger log = LogManager.getLogger(Imagenes.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String codart = request.getParameter("codart");
        byte[] b_imagen;

        try {
            @SuppressWarnings("unchecked")
            List<Producto> productos =
                    (ArrayList<Producto>) request.getSession()
                            .getAttribute("productos");

            Producto busquedaProd = new Producto();
            busquedaProd.setId(Integer.parseInt(codart));

            int index = Collections.binarySearch(productos, busquedaProd, Producto.comparadorProducto);

            Producto encontradoProd = productos.get(index);

            b_imagen = encontradoProd.getImagen();

            response.setContentType("image/jpeg");
            response.setContentLength(b_imagen.length);
            response.getOutputStream().write(b_imagen);
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            log.error(e);
        }
    }

}
