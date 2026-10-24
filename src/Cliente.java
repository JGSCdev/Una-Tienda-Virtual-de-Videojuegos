import java.time.LocalDateTime;
import java.util.*;

public class Cliente extends Usuario{
    private Set<Producto> biblioteca = new HashSet<>();
    private Double saldo = 0.0;
    private List<Producto> historial = new ArrayList<>();
    private TreeSet<Cliente> amigos = new TreeSet<>();
    private Double tiempoConectado = 0.0;
    private List<Producto> carrito = new ArrayList<>();
    private LocalDateTime fechaUltimaConexion = LocalDateTime.now();

    // cnostructores


    public Cliente(String username, String contrasenia, String email, String descripcion, String nacionalidad, String alias, String nombre, String apellido, LocalDateTime fechaNacimiento) {
        super(username, contrasenia, email, descripcion, nacionalidad, alias, nombre, apellido, fechaNacimiento);
    }

    //getters y setters


    public Set<Producto> getBiblioteca() {
        return biblioteca;
    }

    public Double getSaldo() {
        return saldo;
    }

    public List<Producto> getHistorial() {
        return historial;
    }

    public TreeSet<Cliente> getAmigos() {
        return amigos;
    }

    public Double getTiempoConectado() {
        return tiempoConectado;
    }

    public List<Producto> getCarrito() {
        return carrito;
    }

    public LocalDateTime getFechaUltimaConexion() {
        return fechaUltimaConexion;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void setTiempoConectado(Double tiempoConectado) {
        this.tiempoConectado = tiempoConectado;
    }

    public void setFechaUltimaConexion(LocalDateTime fechaUltimaConexion) {
        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    // metodos

    public boolean comprarProducto(Producto producto)
    {
        if( !(saldo >= producto.getPrecio()) )
        {
            return false;
        }

        carrito.add(producto);
        saldo = saldo - producto.getPrecio();

        return true;
    }

    public boolean agregarAlCarrito(Producto producto)
    {
        for(Producto aux : carrito)
        {
            if(producto.equals(aux))
            {
                return false;
            }
        }

        carrito.add(producto);
        return true;
    }

    public boolean agregarAmigo(Cliente amigo)
    {
        for(Cliente aux : amigos)
        {
            if(amigo.equals(aux))
            {
                return false;
            }
        }

        amigos.add(amigo);
        return true;
    }
}
