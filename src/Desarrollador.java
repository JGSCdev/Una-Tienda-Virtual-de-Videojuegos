
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public final class Desarrollador extends Usuario {

    private final Set<Producto> catalogoPropio = new LinkedHashSet<>();
    private double ganancias = 0.0;
    private double valoracionGeneral = 0.0;

    // Constructores

    public Desarrollador(String username, String contrasenia,
                         String email, String nacionalidad,
                         String nombre, String apellido,
                         LocalDate fechaNacimiento) {

        super(username, contrasenia, email, nacionalidad,
                nombre, apellido, fechaNacimiento);
    }

    public Desarrollador(int id, String username, String contrasenia,
                         String email, boolean estado,
                         LocalDateTime creacionCuenta,
                         String descripcion, String nacionalidad,
                         String nombre, String apellido,
                         LocalDate fechaNacimiento,
                         Set<Producto> catalogoPropio,
                         double ganancias, double valoracionGeneral) {

        super(id, username, contrasenia, email, estado,
                creacionCuenta, descripcion, nacionalidad,
                nombre, apellido, fechaNacimiento);

        if (catalogoPropio != null) {

            for (Producto producto : catalogoPropio) {

                if (producto == null ||
                        !perteneceAEsteDesarrollador(producto)) {

                    throw new IllegalArgumentException(
                            "El catálogo contiene productos que no pertenecen al desarrollador"
                    );
                }

                if (!this.catalogoPropio.add(producto)) {

                    throw new IllegalArgumentException(
                            "El catálogo contiene productos duplicados"
                    );
                }
            }
        }

        setGanancias(ganancias);
        setValoracionGeneral(valoracionGeneral);
    }

    public Desarrollador() {
        super();
    }

    // Getters y setters

    public Set<Producto> obtenerCatalogoPropio() {
        return Collections.unmodifiableSet(catalogoPropio);
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {

        if (!Double.isFinite(ganancias) || ganancias < 0) {
            throw new IllegalArgumentException("Ganancias inválidas");
        }

        this.ganancias = ganancias;
    }

    public double getValoracionGeneral() {
        return valoracionGeneral;
    }

    private void setValoracionGeneral(double valoracionGeneral) {

        if (!Double.isFinite(valoracionGeneral) ||
                valoracionGeneral < 0 ||
                valoracionGeneral > 5) {

            throw new IllegalArgumentException("Valoración inválida");
        }

        this.valoracionGeneral = valoracionGeneral;
    }

    // Métodos auxiliares

    private boolean perteneceAEsteDesarrollador(Producto producto) {

        if (producto == null) {
            return false;
        }

        Desarrollador propietario = producto.getDesarrollador();

        if (propietario == this) {
            return true;
        }

        return getId() > 0 &&
                propietario.getId() > 0 &&
                getId() == propietario.getId();
    }

    private Producto buscarProductoPropio(Producto productoBuscado) {

        if (productoBuscado == null) {
            return null;
        }

        for (Producto producto : catalogoPropio) {

            if (producto == productoBuscado) {
                return producto;
            }

            if (productoBuscado.getId() > 0 &&
                    producto.getId() == productoBuscado.getId() &&
                    producto.getClass() == productoBuscado.getClass()) {

                return producto;
            }
        }

        return null;
    }

    public boolean esPropietarioDe(Producto producto) {

        if (!perteneceAEsteDesarrollador(producto)) {
            return false;
        }

        return buscarProductoPropio(producto) != null;
    }

    private boolean datosComunesValidos(Producto producto) {

        return producto != null &&
                producto.getNombre() != null &&
                !producto.getNombre().isBlank() &&
                producto.getDescripcion() != null &&
                !producto.getDescripcion().isBlank() &&
                Double.isFinite(producto.getPrecio()) &&
                producto.getPrecio() >= 0 &&
                Double.isFinite(producto.getPesoGB()) &&
                producto.getPesoGB() > 0 &&
                producto.getFechaLanzamiento() != null;
    }

    private void aplicarDatosComunes(Producto actual,
                                     Producto modificado) {

        actual.setNombre(modificado.getNombre());
        actual.setDescripcion(modificado.getDescripcion());
        actual.setPrecio(modificado.getPrecio());
        actual.setPesoGB(modificado.getPesoGB());
        actual.setFechaLanzamiento(modificado.getFechaLanzamiento());

        if (modificado.isEstado()) {
            actual.darDeAlta();
        } else {
            actual.darDeBaja();
        }
    }

    // Métodos del catálogo propio

    public boolean publicarProducto(Producto producto) {

        if (producto == null || !getEstado()) {
            return false;
        }

        if (!perteneceAEsteDesarrollador(producto)) {
            return false;
        }

        return catalogoPropio.add(producto);
    }

    public boolean darDeBajaProducto(Producto producto) {

        if (!getEstado() || !esPropietarioDe(producto)) {
            return false;
        }

        Producto productoActual = buscarProductoPropio(producto);

        productoActual.darDeBaja();

        return true;
    }

    public boolean darDeAltaProducto(Producto producto) {

        if (!getEstado() || !esPropietarioDe(producto)) {
            return false;
        }

        Producto productoActual = buscarProductoPropio(producto);

        productoActual.darDeAlta();

        return true;
    }

    // Modificar Juego

    public boolean modificarJuego(Juego juegoModificado) {

        if (!getEstado() ||
                !datosComunesValidos(juegoModificado)) {

            return false;
        }

        Producto producto = buscarProductoPropio(juegoModificado);

        if (!(producto instanceof Juego)) {
            return false;
        }

        if (!perteneceAEsteDesarrollador(juegoModificado)) {
            return false;
        }

        Juego juegoActual = (Juego) producto;

        Duration duracion = juegoModificado.getDuracion();

        if (juegoModificado.getGenero() == null ||
                juegoModificado.getEdadMinima() < 0 ||
                juegoModificado.getRequisitosMinimos() == null ||
                juegoModificado.getRequisitosMinimos().isBlank() ||
                juegoModificado.getRequisitosRecomendados() == null ||
                juegoModificado.getRequisitosRecomendados().isBlank() ||
                duracion == null ||
                duracion.isNegative() ||
                duracion.isZero()) {

            return false;
        }

        // Atributos heredados de Producto

        aplicarDatosComunes(juegoActual, juegoModificado);

        // Atributos propios de Juego

        juegoActual.setGenero(juegoModificado.getGenero());
        juegoActual.setDuracion(juegoModificado.getDuracion());
        juegoActual.setEdadMinima(juegoModificado.getEdadMinima());
        juegoActual.setRequisitosMinimos(juegoModificado.getRequisitosMinimos());
        juegoActual.setRequisitosRecomendados(juegoModificado.getRequisitosRecomendados());

        return true;
    }

    // Modificar DLC

    public boolean modificarDLC(DLC dlcModificado) {

        if (!getEstado() ||
                !datosComunesValidos(dlcModificado)) {

            return false;
        }

        Producto producto = buscarProductoPropio(dlcModificado);

        if (!(producto instanceof DLC)) {
            return false;
        }

        if (!perteneceAEsteDesarrollador(dlcModificado)) {
            return false;
        }

        DLC dlcActual = (DLC) producto;

        if (dlcModificado.getContenido() == null ||
                dlcModificado.getJuego() == null) {

            return false;
        }

        // Primero intentamos modificar la relación con el Juego.

        if (!dlcActual.cambiarJuego(dlcModificado.getJuego())) {
            return false;
        }

        // Atributos heredados de Producto

        aplicarDatosComunes(dlcActual, dlcModificado);

        // Atributos propios de DLC

        dlcActual.setContenido(dlcModificado.getContenido());

        return true;
    }

    // Modificar cualquier tipo de Producto

    public boolean modificarProducto(Producto productoModificado) {

        if (productoModificado == null) {
            return false;
        }

        if (productoModificado instanceof Juego juego) {
            return modificarJuego(juego);
        }

        if (productoModificado instanceof DLC dlc) {
            return modificarDLC(dlc);
        }

        return false;
    }
}
