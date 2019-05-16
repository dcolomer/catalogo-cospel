package com.catalogo.beans;

import java.io.Serializable;

public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String descripcion;

    public int getId() {
        return id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Categoria other = (Categoria) obj;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (id != other.id)
            return false;
        return true;
    }

}
