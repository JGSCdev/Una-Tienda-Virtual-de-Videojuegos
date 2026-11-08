package Modelo.Productos;

import Modelo.Cuentas.Desarrollador;

public final class DLCs extends Productos {

    private Juegos juego;
    private TipoContenido contenido;

    public DLCs(Desarrollador desarrollador, String descripcion, String nombre, double peso_GB, double precio, Juegos juego, TipoContenido contenido) {
        super(desarrollador, descripcion, nombre, peso_GB, precio);
        this.juego = juego;
        this.contenido = contenido;
    }

    public TipoContenido getContenido() {
        return contenido;
    }

    public void setContenido(TipoContenido contenido) {
        this.contenido = contenido;
    }

    public Juegos getJuego() {
        return juego;
    }

    public void setJuego(Juegos juego) {
        this.juego = juego;
    }
}
