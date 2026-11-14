package Modelo;
import Enum.GenerosJuegos;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Juego extends Producto {

    private GenerosJuegos genero;
    private final List<DLC> dlCs = new ArrayList<>();
    private int edadMinima;
    private String requisitosMinimos;
    private String requisitosRecomendados;
    private double duracion;

    // Constructor para crear un juego nuevo
    public Juego(Desarrollador desarrollador, String descripcion,
                 String nombre, double pesoGB, double precio,
                 double duracion, int edadMinima,
                 GenerosJuegos genero, String requisitosMinimos,
                 String requisitosRecomendados) {

        super(desarrollador, descripcion, nombre, pesoGB, precio);

        setDuracion(duracion);
        setEdadMinima(edadMinima);
        setGenero(genero);
        setRequisitosMinimos(requisitosMinimos);
        setRequisitosRecomendados(requisitosRecomendados);
    }

    // Constructor para reconstruir un juego desde MySQL
    protected Juego(int id, Desarrollador desarrollador,
                    String descripcion, String nombre,
                    LocalDate fechaLanzamiento, double pesoGB,
                    double precio, boolean estado,
                    double duracion, int edadMinima,
                    GenerosJuegos genero, String requisitosMinimos,
                    String requisitosRecomendados, List<DLC> dlCs) {

        super(id, desarrollador, descripcion, nombre,
                fechaLanzamiento, pesoGB, precio, estado);

        setDuracion(duracion);
        setEdadMinima(edadMinima);
        setGenero(genero);
        setRequisitosMinimos(requisitosMinimos);
        setRequisitosRecomendados(requisitosRecomendados);

        if (dlCs != null) {
            for (DLC dlc : dlCs) {
                if (!agregarDlc(dlc)) {
                    throw new IllegalArgumentException(
                            "No se pudo asociar un Modelo.DLC al juego"
                    );
                }
            }
        }
    }

    // Métodos de validación

    private void validarTexto(String texto, String atributo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(atributo + " inválido");
        }
    }

    // Métodos para administrar los Modelo.DLC

    public boolean agregarDlc(DLC dlc) {

        if (dlc == null) {
            return false;
        }

        // Un Modelo.DLC no puede pertenecer a dos juegos diferentes
        if (dlc.getJuego() != null && dlc.getJuego() != this) {
            return false;
        }

        if (dlCs.contains(dlc)) {
            return false;
        }

        dlCs.add(dlc);

        // Mantener sincronizada la relación
        if (dlc.getJuego() == null) {
            dlc.setJuego(this);
        }

        return true;
    }

    public boolean eliminarDlc(DLC dlc) {

        if (dlc == null || !dlCs.remove(dlc)) {
            return false;
        }

        if (dlc.getJuego() == this) {
            dlc.setJuego(null);
        }

        return true;
    }

    // Getters y setters

    public GenerosJuegos getGenero() {
        return genero;
    }

    public void setGenero(GenerosJuegos genero) {

        if (genero == null) {
            throw new IllegalArgumentException("El género no puede ser null");
        }

        this.genero = genero;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {

        if (edadMinima < 0) {
            throw new IllegalArgumentException("Edad mínima inválida");
        }

        this.edadMinima = edadMinima;
    }

    public String getRequisitosMinimos() {
        return requisitosMinimos;
    }

    public void setRequisitosMinimos(String requisitosMinimos) {
        validarTexto(requisitosMinimos, "Requisitos mínimos");
        this.requisitosMinimos = requisitosMinimos;
    }

    public String getRequisitosRecomendados() {
        return requisitosRecomendados;
    }

    public void setRequisitosRecomendados(String requisitosRecomendados) {
        validarTexto(requisitosRecomendados, "Requisitos recomendados");
        this.requisitosRecomendados = requisitosRecomendados;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {

        if (duracion <= 0) {
            throw new IllegalArgumentException("Duración inválida");
        }

        this.duracion = duracion;
    }

    public List<DLC> getDlCs() {
        return Collections.unmodifiableList(dlCs);
    }

}
