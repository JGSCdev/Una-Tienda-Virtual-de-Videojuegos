import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class Desarrollador extends Usuario  {
    private Set<Productos> catalogoPropio = new LinkedHashSet<>();
    private double ganancias = 0.0;
    private double valoracionGeneral = 0.0;

    // constructores

    public Desarrollador(String username, String contrasenia, String email, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento) {
        super(username, contrasenia, email, nacionalidad, nombre, apellido, fechaNacimiento);
    }

    public Desarrollador(int id, String username, String contrasenia, String email, boolean estado, LocalDateTime creacionCuenta, String descripcion, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento, Set<Productos> catalogoPropio, double ganancias, double valoracionGeneral) {
        super(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento);

        if (catalogoPropio == null) {

            this.catalogoPropio = new LinkedHashSet<>();

        } else {

            this.catalogoPropio = new LinkedHashSet<>(catalogoPropio);
        }

        if (!Double.isFinite(ganancias) || ganancias < 0) {
            throw new IllegalArgumentException("Ganancias inválidas");
        }
        else
        {
            this.ganancias = ganancias;
        }

        if (!Double.isFinite(valoracionGeneral) ||
                valoracionGeneral < 0 || valoracionGeneral > 5) {
            throw new IllegalArgumentException("Valoración inválida");
        }
        else
        {
            this.valoracionGeneral = valoracionGeneral;
        }
    }

    public Desarrollador() {
    }

    // getters y setters


    public Set<Productos> obtenerCatalogoPropio() {
        return Collections.unmodifiableSet(catalogoPropio);
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {
        if (!Double.isFinite(ganancias) || ganancias < 0) { // verifica si el numero es NaN o infinito o si el ingreso es menor a 0
            throw new IllegalArgumentException("Ganancias inválidas");
        }

        this.ganancias = ganancias;
    }

    public double getValoracionGeneral() {
        return valoracionGeneral;
    }

    private void setValoracionGeneral(Double valoracionGeneral) {
        this.valoracionGeneral = valoracionGeneral;
    }

    //metodos

    private Productos buscarProductoPropio(int id) {

        for (Productos producto : catalogoPropio) {

            if (producto.getId() == id) {
                return producto;
            }
        }

        return null;
    }

    public boolean publicarProducto(Productos producto) {

        if (producto == null || !getEstado()) {
            return false;
        }

        if (producto.getDesarrollador() != this) {
            return false;
        }

        return catalogoPropio.add(producto);
    }

    public boolean darDeBajaProducto(Productos producto) {

        if (producto == null) {
            return false;
        }

        Productos productoActual = buscarProductoPropio(producto.getId());

        if (productoActual == null ||
                productoActual.getClass() != producto.getClass()) {
            return false;
        }

        productoActual.darDeBaja();

        return true;
    }




    public boolean modificarJuego(Juegos juegoModificado) {

        if (juegoModificado == null) {
            return false;
        }

        Productos producto = buscarProductoPropio(juegoModificado.getId());

        if (!(producto instanceof Juegos)) {
            return false;
        }

        Juegos juegoActual = (Juegos) producto;

        // Atributos heredados de Productos
        juegoActual.setNombre(juegoModificado.getNombre());
        juegoActual.setDescripcion(juegoModificado.getDescripcion());
        juegoActual.setPrecio(juegoModificado.getPrecio());
        juegoActual.setPeso_GB(juegoModificado.getPeso_GB());
        juegoActual.setFecha_Lanzamiento(juegoModificado.getFecha_Lanzamiento());

        if (juegoModificado.isEstado()) {
            juegoActual.darDeAlta();
        } else {
            juegoActual.darDeBaja();
        }

        // Atributos propios de Juegos
        juegoActual.setGenero(juegoModificado.getGenero());
        juegoActual.setDuracion(juegoModificado.getDuracion());
        juegoActual.setEdadMinima(juegoModificado.getEdadMinima());
        juegoActual.setRequisitosMinimos(juegoModificado.getRequisitosMinimos());
        juegoActual.setRequisitosRecomendados(juegoModificado.getRequisitosRecomendados());

        return true;
    }

    public boolean modificarDLC(DLCs dlcModificado) {

        if (dlcModificado == null) {
            return false;
        }

        Productos producto = buscarProductoPropio(dlcModificado.getId());

        if (!(producto instanceof DLCs)) {
            return false;
        }

        DLCs dlcActual = (DLCs) producto;

        // Atributos heredados
        dlcActual.setNombre(dlcModificado.getNombre());
        dlcActual.setDescripcion(dlcModificado.getDescripcion());
        dlcActual.setPrecio(dlcModificado.getPrecio());
        dlcActual.setPeso_GB(dlcModificado.getPeso_GB());
        dlcActual.setFecha_Lanzamiento(dlcModificado.getFecha_Lanzamiento());

        if (dlcModificado.isEstado()) {
            dlcActual.darDeAlta();
        } else {
            dlcActual.darDeBaja();
        }

        // Atributos propios
        dlcActual.setContenido(dlcModificado.getContenido());

        // Relación con el juego
        dlcActual.setJuego(dlcModificado.getJuego());

        return true;
    }

    public boolean modificarProducto(Productos productoModificado) {

        if (productoModificado == null) {
            return false;
        }

        if (productoModificado instanceof Juegos juego) {
            return modificarJuego(juego);
        }

        if (productoModificado instanceof DLCs dlc) {
            return modificarDLC(dlc);
        }

        return false;
    }

}
