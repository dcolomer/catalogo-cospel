package com.catalogo.servlets.helpers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class loader para cargar recursos necesarios por Jasper Report (subreports, imagenes...)
 *
 */
public class ReportResourcesClassLoader extends URLClassLoader {

    private URLStreamHandler urlStreamHandler;

    public ReportResourcesClassLoader(Map<String, byte[]> reportResources) {
        super(new URL[] {});
        this.urlStreamHandler = new ReportResourcesHandler(reportResources);
    }

    @Override
    public URL findResource(String name) {
        try {
            return new URL("report", "", 0, name, urlStreamHandler);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

/**
 * URL stream handler for Jasper report resources.
 *
 */
class ReportResourcesHandler extends URLStreamHandler {

    private static final Logger log = LogManager.getLogger(ReportResourcesHandler.class);

    private Map<String, byte[]> reportResources;

    public ReportResourcesHandler(Map<String, byte[]> reportResources) {
        this.reportResources = reportResources;
    }

    protected URLConnection openConnection(URL url) throws IOException {

        byte[] resourceBytes = reportResources.get(url.getPath());

        if (resourceBytes == null) {
            URL resourceURL = getClass().getClassLoader().getResource(
                    url.getPath());
            if (resourceURL != null) {
                log.warn("Recurso no encontrado para la URL: " + url);
                return resourceURL.openConnection();
            }
            return null;
        }
        return new ReportResourceURLConnection(new ByteArrayInputStream(
                resourceBytes));
    }
}

/**
 * URL connection for report resource.
 *
 */
class ReportResourceURLConnection extends URLConnection {

    private InputStream resourceStream;

    protected ReportResourceURLConnection(InputStream resourceStream) {
        super(null);
        this.resourceStream = resourceStream;
    }

    public void connect() throws IOException {
        /* empty implementation */
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resourceStream;
    }
}
