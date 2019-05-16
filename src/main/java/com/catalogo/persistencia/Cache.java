package com.catalogo.persistencia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.catalogo.beans.Categoria;
import com.catalogo.beans.Producto;

class Cache {

    private final Logger log = LogManager.getLogger(Cache.class);

    private Map<Integer, List<Categoria>> categoriasCacheadas;
    private Map<Integer, String> pathCategoriasCacheadas;

    private Map<Integer, Integer> idCategoriaPadreCacheadas;

    private Map<Integer, List<Producto>> productosCacheados;
    private Map<Integer, List<Producto>> productosPacksCacheados;
    private Map<Integer, List<Producto>> productosOutletCacheados;

    Cache() {
        productosCacheados = new HashMap<Integer, List<Producto>>();
        productosPacksCacheados = new HashMap<Integer, List<Producto>>();
        productosOutletCacheados = new HashMap<Integer, List<Producto>>();

        categoriasCacheadas = new HashMap<Integer, List<Categoria>>();
        pathCategoriasCacheadas = new HashMap<Integer, String>();
        idCategoriaPadreCacheadas = new HashMap<Integer, Integer>();

        log.debug("******************* Objecto Cache instanciado");
    }

    public Map<Integer, List<Categoria>> getCategoriasCacheadas() {
        return categoriasCacheadas;
    }

    public Map<Integer, List<Producto>> getProductosCacheados() {
        return productosCacheados;
    }

    public Map<Integer, List<Producto>> getProductosPacksCacheados() {
        return productosPacksCacheados;
    }

    public Map<Integer, List<Producto>> getProductosOutletCacheados() {
        return productosOutletCacheados;
    }

    public Map<Integer, String> getPathCategoriasCacheadas() {
        return pathCategoriasCacheadas;
    }

    public Map<Integer, Integer> getIdCategoriaPadreCacheadas() {
        return idCategoriaPadreCacheadas;
    }

}
