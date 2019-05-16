package com.catalogo.servlets;

import com.catalogo.helpers.ReportResourcesClassLoader;
import com.catalogo.helpers.SecurityManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.servicios.ServicioCatalogo;
import com.catalogo.servicios.ServicioCatalogoService;
import com.catalogo.beans.Categoria;
import com.catalogo.beans.Producto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@WebServlet("/informe")
public class Informe extends HttpServlet {

    private static Logger log = LogManager.getLogger(Informe.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SecurityManager securityManager = new SecurityManager();

        if (!securityManager.userInSession(request)) {
            response.sendRedirect("login.html");
        } else {

            ServicioCatalogo servicioCatalogo = ServicioCatalogoService.getInstancia();

            insertarCookieFileDownload(request, response);

            boolean reportPorCategorias =
                    request.getParameter("categorias") != null;

            if (reportPorCategorias) {
                HttpSession sesion = request.getSession();

                @SuppressWarnings("unchecked")
                List<Categoria> categorias = (ArrayList<Categoria>) sesion.getAttribute("categorias");
                List<Producto> productos = new ArrayList<Producto>();

                for (Categoria categoria : categorias) {
                    productos.addAll(servicioCatalogo.getProductos(categoria.getId()));
                }

                sesion.setAttribute("productos", productos);
                generarInforme(request, response, "catalogoAgrupado.jrxml");
            } else {
                generarInforme(request, response, "catalogo.jrxml");
            }
        }
    }

    private void insertarCookieFileDownload(HttpServletRequest request,
                                            HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        boolean foundCookie = false;

        for(int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals("fileDownload")) {
                foundCookie = true;
            }
        }

        if (!foundCookie) {
            Cookie cookie = new Cookie("fileDownload", "true");
            cookie.setPath("/");
            cookie.setMaxAge(24*60*60);
            response.addCookie(cookie);
        }

    }

    private void generarInforme(HttpServletRequest request,
                                HttpServletResponse response, String nomPlantilla) {

        response.setContentType("application/pdf");

        String nomPdf = normalizarNombre(request);

        response.setHeader( "Content-Disposition", "attachment;filename=" + nomPdf);

        HttpSession sesion = request.getSession();

        File informe = new File(sesion.getServletContext().getRealPath(
                "/informes/" + nomPlantilla));

        @SuppressWarnings("unchecked")
        Collection<Producto> productos =
                (Collection<Producto>) sesion.getAttribute("productos");

        // Pasar las imagenes al report en un mapa
        Map<String, byte[]> reportResources = new HashMap<String, byte[]>();

        for (Producto prod : productos) {
            reportResources.put(String.valueOf(prod.getId()), prod.getImagen());
        }

        ReportResourcesClassLoader rrcl =
                new ReportResourcesClassLoader(reportResources);

        Map<String, Object> pars = crearParametrosInforme(request);
        pars.put("rrcl", rrcl);

        // 1-Llenar el datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        try {

            // 2-Compilamos el archivo XML y lo cargamos en memoria
            JasperReport jasperReport = JasperCompileManager.compileReport(informe.toString());

            // 3-Llenamos el report con la informacion de la coleccion y
            // parametros necesarios para la consulta
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, pars, dataSource);

            // 4-Exportamos el report a pdf y lo guardamos en disco
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } catch (JRException | IOException e) {
            log.error(e);
        }
    }

    private Map<String, Object> crearParametrosInforme(HttpServletRequest request) {

        Map<String, Object> pars = new HashMap<String, Object>();

        pars.put("LOGO_URL", request.getSession().getServletContext().getRealPath(
                "/informes/catalogo-logo.png"));
        pars.put("P_TITULO", "Catalogo");
        pars.put("P_SUBTITULO", request.getSession().getAttribute("catPadre"));

        return pars;
    }

    private String normalizarNombre(HttpServletRequest request) {
        String sufijoNomPdf = (String) request.getSession().getAttribute("catPadre");
        sufijoNomPdf = sufijoNomPdf.replaceAll("/| ", Matcher.quoteReplacement("_"));
        return "Catalogo" + sufijoNomPdf + ".pdf";
    }
}
