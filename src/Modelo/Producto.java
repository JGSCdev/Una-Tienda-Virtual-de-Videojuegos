package Modelo;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Producto implements Comparable<Producto> {

    private String nombre;
    private double precio;
    private String descripcion;
    private LocalDate fechaLanzamiento;
    private double pesoGB;
    private final Desarrollador desarrollador;
    private int id;
    private boolean estado = true;

    // Constructor para crear un producto nuevo
    public Producto(Desarrollador desarrollador, String descripcion,
                    String nombre, double pesoGB, double precio) {

        if (desarrollador == null) {
            throw new IllegalArgumentException("El desarrollador no puede ser null");
        }

        this.desarrollador = desarrollador;
        setDescripcion(descripcion);
        setNombre(nombre);
        setPesoGB(pesoGB);
        setPrecio(precio);

        this.fechaLanzamiento = LocalDate.now();
    }

    // Constructor para reconstruir un producto desde la base de datos
    protected Producto(int id, Desarrollador desarrollador,
                       String descripcion, String nombre,
                       LocalDate fechaLanzamiento, double pesoGB,
                       double precio, boolean estado) {

        this(desarrollador, descripcion, nombre, pesoGB, precio);

        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        this.id = id;
        setFechaLanzamiento(fechaLanzamiento);
        this.estado = estado;
    }

    // Métodos auxiliares de validación

    private void validarTexto(String texto, String atributo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(atributo + " inválido");
        }
    }

    // Métodos propios

    public double precioFinal(double descuento) {

        if (!Double.isFinite(descuento) ||
                descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("Descuento inválido");
        }

        return precio * (1 - descuento / 100);
    }

    public void darDeAlta() {
        this.estado = true;
    }

    public void darDeBaja() {
        this.estado = false;
    }

    // Getters y setters

    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        validarTexto(descripcion, "Descripción");
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        if (fechaLanzamiento == null) {
            throw new IllegalArgumentException("Fecha inválida");
        }

        this.fechaLanzamiento = fechaLanzamiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }

        if (this.id != 0 && this.id != id) {
            throw new IllegalStateException("El ID ya fue asignado");
        }

        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        validarTexto(nombre, "Nombre");
        this.nombre = nombre;
    }

    public double getPesoGB() {
        return pesoGB;
    }

    public void setPesoGB(double pesoGB) {

        if (!Double.isFinite(pesoGB) || pesoGB <= 0) {
            throw new IllegalArgumentException("Peso inválido");
        }

        this.pesoGB = pesoGB;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {

        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("Precio inválido");
        }

        this.precio = precio;
    }

    // Comparable

    @Override
    public int compareTo(Producto otro) {

        Objects.requireNonNull(otro, "El producto no puede ser null");

        if (this.id <= 0 || otro.id <= 0) {
            throw new IllegalStateException(
                    "No se pueden comparar productos sin ID asignado"
            );
        }

        int comparacionID = Integer.compare(this.id, otro.id);

        if (comparacionID != 0) {
            return comparacionID;
        }

        return this.getClass().getName()
                .compareTo(otro.getClass().getName());
    }

    // equals y hashCode

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Producto producto = (Producto) o;

        return id > 0 && id == producto.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
