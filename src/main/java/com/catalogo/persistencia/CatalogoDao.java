package com.catalogo.persistencia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.beans.Categoria;
import com.catalogo.beans.Producto;
import com.catalogo.beans.Promocion;

public class CatalogoDao extends AbstractDao {

    private static final Logger log = LogManager.getLogger(CatalogoDao.class);

    private static final int CATEGORIA_RAIZ = 0;
    private static final int OUTLET = 707;
    private static final int PACKS = 740;

    private final Cache cache;

    private List<Categoria> categoriasPrincipales;
    private List<Promocion> promociones;
    private Map<Integer, Float> dtoProductosPromo;

    public CatalogoDao() {

        cache = new Cache();

        // Cachear las categorias principales
        categoriasPrincipales = getCategorias(CATEGORIA_RAIZ);

        // Cachear los descuentos de cada categoria (promos)
        promociones = getDescuentosCategoria();

        // Cachear los descuentos de productos individuales (promos-packs)
        dtoProductosPromo = getDescuentosProductos();

        log.debug("******************* Objeto CatalogoDao instanciado");
    }

    /**
     * Devolver una lista con las categorias principales
     */
    public List<Categoria> getCategoriasPrincipales() {
        return categoriasPrincipales;
    }

    /**
     * Obtener las subcategorias hijas de la categoria proporcionada por
     * parametro
     */
    public List<Categoria> getCategorias(final int catPadre) {

        if (categoriasCacheadas().containsKey(catPadre)) {
            log.debug("Recuperando de la cache las subcategorias de la categoria " + catPadre);
            return categoriasCacheadas().get(catPadre);
        }

        final List<Categoria> categorias = new ArrayList<>();

        final String SQL = "SELECT category_id, category FROM category_descriptions where "
                + "lang_code='ES' AND category_id IN (SELECT category_id FROM categories where "
                + "parent_id = "
                + catPadre
                + " AND status='A') ORDER BY category";

        try (Connection con = ds.getConnection();
                 ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                categorias.add(crearCategoria(res));
            }

            // Cachear la lista de categorias
            categoriasCacheadas().put(catPadre, categorias);

        } catch (SQLException e) {
            log.error(e);
        }

        return categorias;
    }

    public int getIdCategoriaPadre(int subcategoria) {

        if (getIdCategoriaPadreCacheadas().containsKey(subcategoria)) {
            log.debug("Recuperando de la cache el parent_id de la categoria " + subcategoria);
            return getIdCategoriaPadreCacheadas().get(subcategoria);
        }

        final String SQL = "SELECT parent_id FROM categories where category_id = " + subcategoria;

       try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            if (res.next()) {
                int id_parent = res.getInt("parent_id");
                getIdCategoriaPadreCacheadas().put(subcategoria, id_parent);
                return id_parent;
            }
        } catch (SQLException e) {
            log.error(e);
        }

        return 0;
    }

    /**
     * Obtener la ruta como un String de la categoria pasada por parametro
     */
    public String getPathCategoria(final int subcategoria) {

        if (pathCategoriasCacheadas().containsKey(subcategoria)) {
            log.debug("Recuperando de la cache el path de la categoria " + subcategoria);
            return pathCategoriasCacheadas().get(subcategoria);
        }

        final String SQL = "SELECT id_path FROM categories where category_id = "
                + subcategoria;
        String resultado = "/";

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                final String categorias = res.getString("id_path");
                final String[] arrayCat = categorias.split("/");
                for (String categoria_id : arrayCat) {
                    resultado = procesarPathCategorias(resultado, con,
                            categoria_id);
                }
            }

            pathCategoriasCacheadas().put(subcategoria, resultado);

        } catch (SQLException e) {
            log.error(e);
        }

        return resultado;
    }

    private String procesarPathCategorias(String resultado, Connection con,
                                          String categoria_id) {
        final String SQL = "SELECT category FROM category_descriptions where "
                + "lang_code = 'ES' AND category_id = "
                + Integer.parseInt(categoria_id);

        try (ResultSet res = con.createStatement().executeQuery(SQL)){
            if (res.next()) {
                resultado += "/" + res.getString("category");
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return resultado;
    }

    /**
     * Obtener los productos cuya descripcion contiene la subcadena pasada por parametro.
     * Los productos obtenidos son de cualquier categoria.
     */
    public List<Producto> getProductosPorDescripcion(final String texto) {

        final List<Producto> productos = new ArrayList<>();

        final String SQL = "select d.product_id, d.product, c.category_id from "
                + "product_descriptions d, products_categories c where "
                + "d.lang_code='ES' AND d.product LIKE '%" + texto + "%' AND "
                + "c.product_id = d.product_id";

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                int cat = res.getInt("category_id");
                if (cat != OUTLET && cat != PACKS) {
                    productos.add(crearProducto(res, cat, false));
                }
            }
        } catch (SQLException e) {
            log.error(e);
        }

        return productos;
    }

    /**
     * Obtener un objeto Categoria a partir de un identificador de categoria
     */
    private Categoria getCategoria(final int subcategoria) {

        Categoria categoria = new Categoria();
        categoria.setId(subcategoria);

        final String SQL = "select category from category_descriptions where lang_code = 'ES' and "
                + " category_id = " + subcategoria;

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            if (res.next()) {
                categoria.setDescripcion(res.getString("category"));
            }
        } catch (SQLException e) {
            log.error(e);
        }

        return categoria;
    }

    /**
     * Obtener los productos que pertenecen a la categoria proporcionada
     * por parametro.
     */
    public List<Producto> getProductos(final int subcategoria) {

        if (productosCacheados().containsKey(subcategoria)) {
            log.debug("Recuperando de la cache los productos de la categoria " + subcategoria);
            return productosCacheados().get(subcategoria);
        }

        final List<Producto> productos = new ArrayList<>();

		final String SQL = "select product_id, product from product_descriptions where "
                + "lang_code='ES' AND product_id IN (select product_id from products_categories "
                + "where category_id = " + subcategoria + ") ORDER BY product_id";

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                productos.add(crearProducto(res, subcategoria, false));
            }

            // Cacheamos los productos de la categoria en curso
            productosCacheados().put(subcategoria, productos);

        } catch (SQLException e) {
            log.error(e);
        }

        return productos;
    }

    /**
     * Obtener los productos que pertenecen a la categoria PACKS
     */
    public List<Producto> getProductosPacks(final int subcategoria) {

        if (productosPacksCacheados().containsKey(subcategoria)) {
            log.debug("Recuperando de la cache los productos de la categoria PACKS");
            return productosPacksCacheados().get(subcategoria);
        }

        final List<Producto> productos = getProductos(subcategoria);

        for (Producto producto : productos) {
            String SQL = "select category_id from products_categories "
                    + "where product_id = "
                    + producto.getId()
                    + " and link_type = 'M'";

            try (Connection con = ds.getConnection();
                 ResultSet res = con.createStatement().executeQuery(SQL))
            {
                while (res.next()) {
                    producto.setCategoria(getCategoria(res
                            .getInt("category_id")));

                    producto.setCategorias(getPilaCategoriasPacks(producto));

                    if (productoEnPromo(producto)) {
                        producto.setDto(dtoPromo(producto));
                    } else {
                        producto.setDto(getDtoCategoria(producto));
                    }

                    producto.setPrecio(getPrecioNeto(producto));
                }

            } catch (SQLException e) {
                log.error(e);
            }
        }

        // Cacheamos los productos de la categoria en curso
        productosPacksCacheados().put(subcategoria, productos);

        return productos;
    }

    /**
     * Obtener una pila de objetos Categoria a las que pertenece un determinado producto
     */
    private Stack<Categoria> getPilaCategoriasPacks(Producto producto) {

        final String SQL = "select category_id from products_categories "
                + "where product_id = " + producto.getId()
                + " and link_type <> 'M'";

        final Stack<Categoria> pila = new Stack<>();

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                Categoria categoria = getCategoria(res.getInt("category_id"));
                pila.push(categoria);
            }
        } catch (SQLException e) {
            log.error(e);
        }

        return pila;
    }

    /**
     * Obtener una lista de objetos Promocion por cada promocion de catalogo creada
     * en cs_cart para categorias.
     * A partir de esta lista se obtienen los descuentos de cada categoria.
     */
    public List<Promocion> getDescuentosCategoria() {

        final List<Promocion> promociones = new ArrayList<>();

        /*
         * Obtener los codigos de categoria a partir de todas las promociones
         */
        final String SQL = "select priority, name, "
                + "mid(reverse(mid(left(reverse(conditions), instr(reverse(conditions), ':')),6)),3) as categorias, "
                + "mid(reverse(mid(left(reverse(bonuses), instr(reverse(bonuses), ':')),5)),3) as dto "
                + "from promotions p, promotion_descriptions d "
                + "where d.lang_code='ES' AND p.promotion_id=d.promotion_id AND status='A' and "
                + "zone='catalog' and conditions like '%categories%' "
                + "ORDER BY name, priority";

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                promociones.add(crearPromocion(res));
            }

            return promociones;

        } catch (SQLException e) {
            log.error(e);
        }

        return Collections.emptyList();
    }

    private Promocion crearPromocion(ResultSet res) {
        Promocion promocion = new Promocion();
        try {
            String descripcion = res.getString("name");
            int prioridad = res.getInt("priority");
            float dto = res.getFloat("dto");

            promocion.setDescripcion(descripcion);
            promocion.setPrioridad(prioridad);

            Set<Integer> cat = new HashSet<Integer>();
            String[] categorias = res.getString("categorias").split(",");

            for (String categoria : categorias) {
                cat.add(Integer.parseInt(categoria));
            }

            promocion.setCategorias(cat);
            promocion.setDescuento(dto);
        } catch (SQLException e) {
            log.error(e);
        }

        return promocion;
    }

    /**
     * Obtener un Mapa por cada promocion de catalogo creada
     * en cs_cart para productos.
     * A partir de esta lista se obtienen los descuentos de productos individuales.
     */
    public Map<Integer, Float> getDescuentosProductos() {
        /*
         * Obtener los codigos de categoria a partir de todas las promociones
         */
        final String SQL = "select "
                + "mid(reverse(mid(left(reverse(conditions), instr(reverse(conditions), ':')),6)),3) as productos, "
                + "mid(reverse(mid(left(reverse(bonuses), instr(reverse(bonuses), ':')),5)),3) as dto "
                + "from promotions p, promotion_descriptions d "
                + "where d.lang_code = 'ES' AND p.promotion_id=d.promotion_id AND status='A' and "
                + "zone='catalog' and conditions like '%products%'";

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            Map<Integer, Float> dtoProductosPromo = new HashMap<>();

            while (res.next()) {
                float dto = res.getFloat("dto");
                String[] productos = res.getString("productos").split(",");

                for (String producto : productos) {
                    dtoProductosPromo.put(Integer.parseInt(producto), dto);
                }
            }

            return dtoProductosPromo;

        } catch (SQLException e) {
            log.error(e);
        }

        return Collections.emptyMap();
    }

    /**
     * Obtener los productos que pertenecen a la categoria OUTLET
     */
    public List<Producto> getProductosOutlet(final int subcategoria) {

        if (productosOutletCacheados().containsKey(subcategoria)) {
            log.debug("Recuperando de la cache los productos de la categoria OUTLET");
            return productosOutletCacheados().get(subcategoria);
        }

        final List<Producto> productos = new ArrayList<>();

        final String SQL = "select p.product_id, d.product, p.list_price, pr.price from "
                + "product_descriptions d, products p, product_prices pr where "
                + "d.lang_code='ES' AND d.product_id IN (select product_id from products_categories "
                + "where category_id="
                + subcategoria
                + ") AND p.product_id=d.product_id AND "
                + "p.product_id=pr.product_id";

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                productos.add(crearProducto(res, subcategoria, true));
            }

            productosOutletCacheados().put(subcategoria, productos);

        } catch (SQLException e) {
            log.error(e);
        }

        return productos;
    }

    private Producto crearProducto(ResultSet res, int cat, boolean isOutlet) {
        Producto producto = new Producto();
        try {
            producto.setId(res.getInt("product_id"));
            producto.setDescripcion(res.getString("product"));
            producto.setCategoria(getCategoria(cat));

            if (isOutlet) {
                producto.setCategorias(null);
                producto.setPrecioTarifa(res.getFloat("list_price"));
                producto.setPrecio(res.getFloat("price"));
                producto.setDto(getDtoOutlet(producto));
            } else {
                producto.setCategorias(getPilaCategorias(producto));
                producto.setPrecioTarifa(getPrecioTarifa(producto.getId()));
                producto.setDto(getDtoCategoria(producto));
                producto.setPrecio(getPrecioNeto(producto));
            }

            producto.setImagen(new ImagenDao()
                    .obtenerImagenArticulo(producto));
        } catch (SQLException e) {
            log.error(e);
        }

        return producto;
    }

    private float getDtoOutlet(Producto producto) {

        if (producto.getPrecioTarifa() > 0) {
            BigDecimal bPrecio = new BigDecimal(
                    String.valueOf(producto.getPrecio()));
            BigDecimal bPrecioTarifa = new BigDecimal(String.valueOf(producto
                    .getPrecioTarifa()));
            BigDecimal bResultado = bPrecio.divide(bPrecioTarifa, 2,
                    RoundingMode.HALF_UP);

            return 100 * (1.0F - bResultado.floatValue());
        } else {
            return 0F;
        }

    }

    /**
     * Obtener una pila de objetos Categoria a los que pertenece un
     * determinado producto.
     */
    private Stack<Categoria> getPilaCategorias(final Producto producto) {

        Stack<Categoria> categorias = new Stack<>();

        int cat_id = producto.getCategoria().getId();
        final String SQL = "SELECT id_path FROM categories where category_id = " + cat_id;

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                String strCategorias = res.getString("id_path");
                String[] arrayCat = strCategorias.split("/");
                for (String categoria_id : arrayCat) {
                    procesarCategorias(categorias, con, categoria_id);
                }
            }
        } catch (SQLException e) {
            log.error(e);
        }

        return categorias;
    }

    private void procesarCategorias(Stack<Categoria> categorias,
                                    Connection con, String categoria_id) {

        final String SQL = "SELECT category_id, category FROM category_descriptions where "
                + "lang_code='ES' AND category_id = "
                + Integer.parseInt(categoria_id);

        try (ResultSet res = con.createStatement().executeQuery(SQL))
        {
            while (res.next()) {
                categorias.push(crearCategoria(res));
            }
        } catch (SQLException e) {
            log.error(e);
        }
    }

    private float getPrecioTarifa(final int product_id) {
        final String SQL = "select price from product_prices where product_id = " + product_id;
        float precio = 0.0f;

        try (Connection con = ds.getConnection();
             ResultSet res = con.createStatement().executeQuery(SQL))
        {
            if (res.next()) {
                precio = res.getFloat("price");
            }
        } catch (SQLException e) {
            log.error(e);
        }

        return precio;
    }

    private float getDtoCategoria(final Producto producto) {

        Integer idCategoria = producto.getCategoria().getId();
        Collection<Promocion> promosCandidatas = new ArrayList<>();

        for (Promocion promo : getPromociones()) {
            if (promo.getCategorias().contains(idCategoria)) {
                promosCandidatas.add(promo);
            }
        }

        if (promosCandidatas.isEmpty()) {
            Float dto = null;
            while (dto == null && !producto.getCategorias().empty()) {
                dto = getDtoCategoriaPadre(producto.getCategorias().pop());
            }

            if (dto == null) {
                return 0.0f;
            } else {
                return dto;
            }

        } else {
            return getPriorityPromo(promosCandidatas).getDescuento();
        }
    }

    private Float getDtoCategoriaPadre(Categoria categoria) {
        Integer idCategoria = categoria.getId();
        ArrayList<Promocion> promosCandidatas = new ArrayList<>();

        for (Promocion promo : getPromociones()) {
            if (promo.getCategorias().contains(idCategoria)) {
                promosCandidatas.add(promo);
            }
        }

        if (promosCandidatas.isEmpty()) {
            return null;
        } else {
            if (promosCandidatas.size() == 1) {
                return promosCandidatas.get(0).getDescuento();
            } else {
                return getPriorityPromo(promosCandidatas).getDescuento();
            }
        }
    }

    private Promocion getPriorityPromo(Collection<Promocion> promosCandidatas) {
        Promocion promoPrioritaria = null;
        int min = 1000;
        for (Promocion promo : promosCandidatas) {
            int priority = promo.getPrioridad();
            if (priority < min) {
                promoPrioritaria = promo;
                min = priority;
            }
        }
        return promoPrioritaria;
    }

    private float getPrecioNeto(Producto producto) {

        return producto.getPrecioTarifa() - producto.getPrecioTarifa()
                * producto.getDto() / 100;
    }

    private Categoria crearCategoria(ResultSet res) {
        final Categoria categoria = new Categoria();
        try {
            categoria.setId(res.getInt("category_id"));
            categoria.setDescripcion(res.getString("category"));
        } catch (SQLException e) {
            log.error(e);
        }

        return categoria;
    }

    private boolean productoEnPromo(Producto producto) {
        return getDtoProductosPromo().containsKey(producto.getId());
    }

    private float dtoPromo(Producto producto) {
        return getDtoProductosPromo().get(producto.getId());
    }

    private Map<Integer, List<Categoria>> categoriasCacheadas() {
        return cache.getCategoriasCacheadas();
    }

    private Map<Integer, String> pathCategoriasCacheadas() {
        return cache.getPathCategoriasCacheadas();
    }

    private Map<Integer, Integer> getIdCategoriaPadreCacheadas() {
        return cache.getIdCategoriaPadreCacheadas();
    }

    private Map<Integer, List<Producto>> productosCacheados() {
        return cache.getProductosCacheados();
    }

    private Map<Integer, List<Producto>> productosPacksCacheados() {
        return cache.getProductosPacksCacheados();
    }

    private Map<Integer, List<Producto>> productosOutletCacheados() {
        return cache.getProductosOutletCacheados();
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public Map<Integer, Float> getDtoProductosPromo() {
        return dtoProductosPromo;
    }

}
