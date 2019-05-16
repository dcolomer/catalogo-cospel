package com.catalogo.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    /*public static Comparator<Producto> comparadorProducto = new Comparator<Producto>() {
        public int compare(Producto p1, Producto p2) {
            return p1.getId().compareTo(p2.getId());
        }
    };*/
    public static Comparator<Producto> comparadorProducto = (p1,p2) -> p1.getId().compareTo(p2.getId());

    private int id;
    private String descripcion;
    private Categoria categoria;

    private Stack<Categoria> categorias;

    private float precio;
    private float precioTarifa;
    private float dto;

    private String pathImagen;
    private byte[] imagen;



    public Integer getId() {
        return id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public float getPrecio() {
        return precio;
    }
    public float getPrecioTarifa() {
        return precioTarifa;
    }
    public float getDto() {
        return dto;
    }
    public String getPathImagen() {
        return pathImagen;
    }
    public byte[] getImagen() {
        return imagen;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    public void setPrecioTarifa(float precioTarifa) {
        this.precioTarifa = precioTarifa;
    }
    public void setDto(float dto) {
        this.dto = dto;
    }
    public void setPathImagen(String pathImagen) {
        this.pathImagen = pathImagen;
    }
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }


    @Override
    public String toString() {
        return "Producto [id=" + id + ", descripcion=" + descripcion
                + ", categoria=" + categoria + ", precio=" + precio
                + ", precioTarifa=" + precioTarifa + ", dto=" + dto
                + ", pathImagen=" + pathImagen + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((categoria == null) ? 0 : categoria.hashCode());
        result = prime * result
                + ((descripcion == null) ? 0 : descripcion.hashCode());
        result = prime * result + Float.floatToIntBits(dto);
        result = prime * result + id;
        result = prime * result + Arrays.hashCode(imagen);
        result = prime * result + Float.floatToIntBits(precio);
        result = prime * result + Float.floatToIntBits(precioTarifa);
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
        Producto other = (Producto) obj;
        if (categoria == null) {
            if (other.categoria != null)
                return false;
        } else if (!categoria.equals(other.categoria))
            return false;
        if (descripcion == null) {
            if (other.descripcion != null)
                return false;
        } else if (!descripcion.equals(other.descripcion))
            return false;
        if (Float.floatToIntBits(dto) != Float.floatToIntBits(other.dto))
            return false;
        if (id != other.id)
            return false;
        if (!Arrays.equals(imagen, other.imagen))
            return false;
        if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
            return false;
        if (Float.floatToIntBits(precioTarifa) != Float
                .floatToIntBits(other.precioTarifa))
            return false;
        return true;
    }
    public Stack<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(Stack<Categoria> categorias) {
        this.categorias = categorias;
    }

}
