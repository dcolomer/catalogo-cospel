package com.catalogo.servicios;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.persistencia.CatalogoDao;
import com.catalogo.persistencia.CatalogoDaoService;
import com.catalogo.beans.*;

/**
 * Clase que proporciona la interfaz de servicios a los servlets.
 * Carece de estado, por lo que solo se permite una unica instancia de esta clase.
 *
 */
public class ServicioCatalogo {

    private final Logger log = LogManager.getLogger(ServicioCatalogo.class);

    ServicioCatalogo() {
        log.debug("******************* Objecto ServicioCatalogo instanciado");
    }

    /**
     * Obtener las categorias de primer nivel
     */
    public List<Categoria> getCategorias() {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getCategoriasPrincipales();
    }

    /**
     * Obtener las subcategorias hijas de la categoria proporcionada por
     * parametro
     */
    public List<Categoria> getCategorias(final int catPadre) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getCategorias(catPadre);
    }

    public String getPathCategoria(final int subcategoria) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getPathCategoria(subcategoria);
    }

    public int getIdCategoriaPadre(int subcategoria) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getIdCategoriaPadre(subcategoria);
    }

    public List<Producto> getProductos(final int subcategoria) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getProductos(subcategoria);
    }

    public List<Producto> getProductosPacks(final int subcategoria) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.	getProductosPacks(subcategoria);
    }

    public List<Producto> getProductosOutlet(final int subcategoria) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getProductosOutlet(subcategoria);
    }

    public List<Producto> getProductosPorDescripcion(final String texto) {
        CatalogoDao catalogoDao = CatalogoDaoService.getInstance();
        return catalogoDao.getProductosPorDescripcion(texto);
    }

}
