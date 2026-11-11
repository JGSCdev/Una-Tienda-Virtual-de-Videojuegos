import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class Desarrollador extends Usuario implements iControlJuegos {
    private Set<Producto> catalogoPropio = new LinkedHashSet<>();
    private double ganancias = 0.0;
    private double valoracionGeneral = 0.0;

    // constructores

    public Desarrollador(String username, String contrasenia, String email, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento) {
        super(username, contrasenia, email, nacionalidad, nombre, apellido, fechaNacimiento);
    }

    public Desarrollador(int id, String username, String contrasenia, String email, boolean estado, LocalDateTime creacionCuenta, String descripcion, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento, Set<Producto> catalogoPropio, double ganancias, double valoracionGeneral) {
        super(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento);

        if (catalogoPropio == null) {
            this.catalogoPropio = new LinkedHashSet<>();
        } else {
            this.catalogoPropio = new LinkedHashSet<>(catalogoPropio);
        }

        this.ganancias = ganancias;
        this.valoracionGeneral = valoracionGeneral;
    }

    public Desarrollador() {
    }

    // getters y setters


    public Set<Producto> obtenerCatalogoPropio() {
        return Collections.unmodifiableSet(catalogoPropio);
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {
        if (ganancias < 0) {
            return;
        }

        this.ganancias = ganancias;
    }

    public Double getValoracionGeneral() {
        return valoracionGeneral;
    }

    private void setValoracionGeneral(Double valoracionGeneral) {
        this.valoracionGeneral = valoracionGeneral;
    }

    //metodos

    private Producto buscarProductoPropio(int id) {

        for (Producto producto : catalogoPropio) {

            if (producto.getId() == id) {
                return producto;
            }
        }

        return null;
    }

    public boolean publicarProducto(Producto producto) {

        if (producto == null) {
            return false;
        }

        return catalogoPropio.add(producto);
    }

    public boolean modificarProducto(Producto productoModificado)
    {
        if (productoModificado == null) {
            return false;
        }

        Producto aux = buscarProductoPropio(productoModificado.getId());

        if (aux == null) {
            return false;
        }

        if(productoModificado instanceof Juego)
        {

            //necesito los setters de Juego

            return true;

        }

        if(productoModificado instanceof DLC)
        {
            //necesito los setters de DLC

            return true;

        }

        return false;
    }

    public boolean darDeBajaProducto(Producto producto) {
        if (producto == null) return false;

        if (!catalogoPropio.contains(producto)) return false;

        producto.darDeBaja();
        return true;
    }


    @Override
    public void darAltaJuego(Juego juego) {

    }

    @Override
    public void darAltaDLC(DLC dlc) {

    }

    @Override
    public void DarBajaJuego(int id) {

    }

    @Override
    public void DarBajaDLC(int id) {

    }

    @Override
    public boolean modificarJuego(Juego juegoModificado) {

        if (juegoModificado == null) {
            return false;
        }

        Producto producto = buscarProductoPropio(juegoModificado.getId());

        if (!(producto instanceof Juego)) {
            return false;
        }

        //necesito los setters de Juego

        return true;
    }

    @Override
    public boolean modificarDLC(DLC dlcModificado) {

        if (dlcModificado == null) {
            return false;
        }

        Producto producto = buscarProductoPropio(dlcModificado.getId());

        if (!(producto instanceof DLC)) {
            return false;
        }

        //necesito los setters de DLC

        return true;
    }
}
