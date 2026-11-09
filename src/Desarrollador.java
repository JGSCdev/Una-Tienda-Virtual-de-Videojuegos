import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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
        this.catalogoPropio = catalogoPropio;
        this.ganancias = ganancias;
        this.valoracionGeneral = valoracionGeneral;
    }

    public Desarrollador() {
    }

    // getters y setters


    public Set<Producto> obtenerCatalogoPropio() {
        return catalogoPropio;
    }

    public Double getGanancias() {
        return ganancias;
    }

    public void setGanancias(Double ganancias) {
        this.ganancias = ganancias;
    }

    public Double getValoracionGeneral() {
        return valoracionGeneral;
    }

    private void setValoracionGeneral(Double valoracionGeneral) {
        this.valoracionGeneral = valoracionGeneral;
    }

    //metodos

    public void publicarProducto(Producto producto)
    {
        catalogoPropio.add(producto);
    }

    public void modificarProducto()
    {
        // por definir
    }

    public void darDeBajaProducto(Producto Producto)
    {

        Iterator<Producto> iterator = catalogoPropio.iterator();

        while(iterator.hasNext())
        {
            Producto aux = iterator.next();

            if(aux.equals(Producto))
            {
                catalogoPropio.remove(aux);
                break;
            }
        }
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
    public void modificarJuego(Juego juego) {

    }

    @Override
    public void modificarDLC(DLC dlc) {

    }
}
