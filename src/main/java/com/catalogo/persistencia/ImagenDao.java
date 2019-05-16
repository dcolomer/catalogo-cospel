package com.catalogo.persistencia;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.beans.Producto;

class ImagenDao extends AbstractDao {

    private Logger log = LogManager.getLogger(ImagenDao.class);

    public byte[] obtenerImagenArticulo(Producto producto) {
        byte[] imageInByte = null;
        int image_id = 0;

        String folderImg = getProperty("rutaImg");
        String folderImgResolved = "no_disponible";
        Connection con = null;
        ResultSet res = null;

        try {
            String consulta = "select image_id, detailed_id from images_links where object_type='product' AND object_id = "
                    + producto.getId();
            con = ds.getConnection();
            res = con.createStatement().executeQuery(consulta);

            if (res.next()) {
                image_id = res.getInt("image_id");
                if (image_id == 0) {
                    image_id = res.getInt("detailed_id");
                    folderImg = getProperty("rutaImgDetail");
                }

                folderImgResolved = getFolderImageResolved(con, folderImg, image_id);
                producto.setPathImagen(folderImgResolved);

            } else {
                log.debug("La consulta no produjo resultados: " + consulta);
            }

            imageInByte = getImageInBytes(folderImgResolved);

        } catch (SQLException e) {
            log.error(e);
        } finally {
            closeQuiet(res, con);
        }
        return imageInByte;
    }

    public String getFolderImageResolved(Connection con, String folderImg, int image_id) {

        String image_name = null;
        ResultSet res = null;

        try {
            String consulta = "select image_path from images where image_id = "
                    + image_id;
            res = con.createStatement().executeQuery(consulta);
            if (res.next()) {
                image_name = res.getString("image_path");
                int floor = image_id / 1000;
                folderImg += String.valueOf(floor) + "/" + image_name;
            }
        } catch (SQLException e) {
            log.error(e);
        } finally {
            closeQuiet(res);
        }

        return folderImg;
    }

    private byte[] getImageInBytes(String folderImg) {
        byte[] imageInByte = null;
        BufferedImage img = null;
        ByteArrayOutputStream baos = null;

        File pathImagen = new File(folderImg);

        if (!pathImagen.exists()) {
            pathImagen = new File(getProperty("rutaImg") + "no_disponible.jpg");
        }

        try {
            img = ImageIO.read(pathImagen);
            baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
        } catch (IOException | IllegalArgumentException e) {
            log.error(e);
            pathImagen = new File(getProperty("rutaImg") + "no_disponible.jpg");
            baos = new ByteArrayOutputStream();
            try {
                img = ImageIO.read(pathImagen);
                ImageIO.write(img, "jpg", baos);
                baos.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            imageInByte = baos.toByteArray();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                } else {
                    log.info(pathImagen);
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return imageInByte;
    }

}
