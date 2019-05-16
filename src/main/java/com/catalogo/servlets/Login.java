package com.catalogo.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.beans.Usuario;

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(Login.class);

    private String siguientePag = "";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        siguientePag = "/home";

        // Intentamos obtener el nombre del usuario si es que esta ya validado
        String sessionUser = (String) request
                .getSession().getAttribute("sesNombreUsuarioValidado");

        // Si el usuario no esta validado hay que validarlo
        if (sessionUser == null) {

            String usuarioIntro = request.getParameter("txtUsuario");
            String passwIntro = request.getParameter("txtContrasenya");

            String usuarioValidado = autenticarUsuario(usuarioIntro, passwIntro);

            // Ha fallado la validacion
            if (usuarioValidado == null) {
                siguientePag = "/error_login.html";
                log.error("Usuario introducido: " + usuarioIntro +
                        " Password introducido: " + passwIntro);

                // Ha triunfado la validacion
            } else {
                Usuario usuario = new Usuario(usuarioValidado);
                request.getSession().setAttribute("usuario", usuario);
                request.getSession().setAttribute("sesNombreUsuarioValidado", usuario.getNombre());
                log.info("El usuario " + usuarioIntro + " ha iniciado sesion.");
            }
        }

        getServletContext().getRequestDispatcher(siguientePag)
                .forward(request, response);
    }

    // Nos devuelve el nombre del usuario si la pareja
    // nombre-passw es correcta
    // Tendriamos que comparar con valores de la BD!
    private String autenticarUsuario(String usuarioIntro, String passwIntro) {
        String retorno = null;

        if (usuarioIntro.equals("catalogo") && passwIntro.equals("catalogo")) {
            retorno = usuarioIntro;
        }

        return retorno;
    }

}