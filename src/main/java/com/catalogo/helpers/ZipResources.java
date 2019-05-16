package com.catalogo.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZipResources {

    private final Logger log = LogManager.getLogger(ZipResources.class);
    private final String USER_HOME = System.getProperty("user.home");
    private final String SEPARADOR = System.getProperty("file.separator");

    public byte[] getResource(String zipName, String fileName) {

        ByteArrayOutputStream out = null;
        ZipFile zf = null;
        InputStream in = null;
        try {
            out = new ByteArrayOutputStream();
            zf = new ZipFile(USER_HOME + SEPARADOR + zipName);
            ZipEntry entry = zf.getEntry(fileName);
            in = zf.getInputStream(entry);

            byte[] buffer = new byte[4096];
            for (int n; (n = in.read(buffer)) != -1;) {
                out.write(buffer, 0, n);
            }
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (zf != null) {
                    zf.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return null;
    }
}
