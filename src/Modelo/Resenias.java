package Modelo;

import Modelo.Cuentas.Cliente;
import Modelo.Cuentas.Usuarios;

import java.time.LocalDate;

public class Resenias{

    private double valoracion;
    private String comentario;
    private Cliente cliente;
    private LocalDate fecha_publicacion;

    public Resenias(String comentario, Cliente cliente, double valoracion) {
        this.comentario = comentario;
        this.fecha_publicacion = LocalDate.now();
        this.cliente = cliente;
        this.valoracion = valoracion;
    }

    public Resenias(Cliente cliente, double valoracion) {
        this.cliente = cliente;
        this.valoracion = valoracion;
        this.fecha_publicacion = LocalDate.now();
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }
}
