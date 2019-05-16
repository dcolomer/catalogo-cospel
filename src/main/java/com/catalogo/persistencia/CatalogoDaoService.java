package com.catalogo.persistencia;

public class CatalogoDaoService {

    private static CatalogoDao instance;

    private CatalogoDaoService() {
        instance = new CatalogoDao();
    }

    public static CatalogoDao getInstance() {
        if (instance == null) {
            new CatalogoDaoService();
        }

        return instance;
    }

}
