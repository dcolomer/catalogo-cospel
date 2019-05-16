package com.catalogo.servicios;

public class ServicioCatalogoService {

    private static ServicioCatalogo servicioCatalogo;

    private ServicioCatalogoService() {
        servicioCatalogo = new ServicioCatalogo();
    }

    public static synchronized ServicioCatalogo getInstancia() {
        if (servicioCatalogo == null) {
            new ServicioCatalogoService();
        }

        return servicioCatalogo;
    }

}
