package com.catalogo.beans;

import java.io.Serializable;
import java.util.Set;

public class Promocion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descripcion;
    private int prioridad;
    private Set<Integer> categorias;
    private float descuento;

    public String getDescripcion() {
        return descripcion;
    }
    public int getPrioridad() {
        return prioridad;
    }
    public Set<Integer> getCategorias() {
        return categorias;
    }
    public float getDescuento() {
        return descuento;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
    public void setCategorias(Set<Integer> categorias) {
        this.categorias = categorias;
    }
    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }
    @Override
    public String toString() {
        return "Promocion [descripcion=" + descripcion + ", prioridad="
                + prioridad + ", categorias=" + categorias + ", descuento="
                + descuento + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((categorias == null) ? 0 : categorias.hashCode());
        result = prime * result
                + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + Float.floatToIntBits(descuento);
        result = prime * result + prioridad;
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
        Promocion other = (Promocion) obj;
        if (categorias == null) {
            if (other.categorias != null)
                return false;
        } else if (!categorias.equals(other.categorias))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (Float.floatToIntBits(descuento) != Float
                .floatToIntBits(other.descuento))
            return false;
        if (prioridad != other.prioridad)
            return false;
        return true;
    }

}
