import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Desarrollador extends Usuario implements ControlJuegos{
    private Set<Producto> catalogoPropio = new LinkedHashSet<>();
    private Double ganancias = 0.0;
    private Double valoracionGeneral = 0.0;

    // constructores


    public Desarrollador(String username, String contrasenia, String email, String descripcion, String nacionalidad, String alias, String nombre, String apellido, LocalDateTime fechaNacimiento) {
        super(username, contrasenia, email, descripcion, nacionalidad, alias, nombre, apellido, fechaNacimiento);
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
            }
        }
    }



}
