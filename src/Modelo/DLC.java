package Modelo;
import Enum.TipoContenido;
import java.time.LocalDate;

public final class DLC extends Producto {

    private Juego juego;
    private TipoContenido contenido;

    // Constructor para crear un Modelo.DLC nuevo
    public DLC(Desarrollador desarrollador, String descripcion,
               String nombre, double pesoGB, double precio,
               Juego juego, TipoContenido contenido) {

        super(desarrollador, descripcion, nombre, pesoGB, precio);

        setContenido(contenido);

        if (juego == null) {
            throw new IllegalArgumentException(
                    "El Modelo.DLC debe estar asociado a un juego"
            );
        }

        if (!juego.agregarDlc(this)) {
            throw new IllegalArgumentException(
                    "No se pudo asociar el Modelo.DLC al juego"
            );
        }
    }

    // Constructor para reconstruir un Modelo.DLC desde MySQL
    protected DLC(int id, Desarrollador desarrollador,
                  String descripcion, String nombre,
                  LocalDate fechaLanzamiento, double pesoGB,
                  double precio, boolean estado,
                  Juego juego, TipoContenido contenido) {

        super(id, desarrollador, descripcion, nombre,
                fechaLanzamiento, pesoGB, precio, estado);

        setContenido(contenido);

        if (juego == null) {
            throw new IllegalArgumentException(
                    "El Modelo.DLC debe estar asociado a un juego"
            );
        }

        if (!juego.agregarDlc(this)) {
            throw new IllegalArgumentException(
                    "No se pudo asociar el Modelo.DLC al juego"
            );
        }
    }

    // Métodos propios

    public boolean cambiarJuego(Juego nuevoJuego) {

        if (this.juego == nuevoJuego) {
            return true;
        }

        Juego juegoAnterior = this.juego;

        // Desvincular del juego anterior
        if (juegoAnterior != null) {
            juegoAnterior.eliminarDlc(this);
        }

        // Asociar al nuevo juego
        if (nuevoJuego != null && !nuevoJuego.agregarDlc(this)) {

            // Si falla, restaurar la relación anterior
            if (juegoAnterior != null) {
                juegoAnterior.agregarDlc(this);
            }

            return false;
        }

        return true;
    }

    // Getters y setters

    public TipoContenido getContenido() {
        return contenido;
    }

    public void setContenido(TipoContenido contenido) {

        if (contenido == null) {
            throw new IllegalArgumentException(
                    "El contenido no puede ser null"
            );
        }

        this.contenido = contenido;
    }

    public Juego getJuego() {
        return juego;
    }

    // Método interno utilizado por Modelo.Juego para mantener
    // sincronizada la relación en ambos objetos.
    void setJuego(Juego juego) {
        this.juego = juego;
    }
}
