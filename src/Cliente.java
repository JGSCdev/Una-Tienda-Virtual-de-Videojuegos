import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Cliente extends Usuario implements Comparable<Cliente>{
    private Set<Producto> biblioteca = new HashSet<>();
    private double saldo = 0.0;
    private TreeSet<Cliente> amigos = new TreeSet<>();
    private double tiempoConectado = 0.0;
    private List<Producto> carrito = new ArrayList<>();
    private LocalDateTime fechaUltimaConexion = LocalDateTime.now();

    // cnostructores


    public Cliente(String username, String contrasenia, String email, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento) {
        super(username, contrasenia, email, nacionalidad, nombre, apellido, fechaNacimiento);
    }

    public Cliente(int id, String username, String contrasenia, String email, boolean estado, LocalDateTime creacionCuenta, String descripcion, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento, Set<Producto> biblioteca, double saldo, TreeSet<Cliente> amigos, double tiempoConectado, List<Producto> carrito, LocalDateTime fechaUltimaConexion) {
        super(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento);

        if (biblioteca == null) {
            this.biblioteca = new HashSet<>();
        } else {
            this.biblioteca = new HashSet<>(biblioteca);
        }

        this.saldo = saldo;

        if (amigos == null) {
            this.amigos = new TreeSet<>();
        } else {
            this.amigos = new TreeSet<>(amigos);
        }

        this.tiempoConectado = tiempoConectado;

        if (carrito == null) {
            this.carrito = new ArrayList<>();
        } else {
            this.carrito = new ArrayList<>(carrito);
        }

        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    public Cliente() {
    }

    //getters y setters


    public Set<Producto> getBiblioteca() {
        return biblioteca;
    }

    public double getSaldo() {
        return saldo;
    }


    public TreeSet<Cliente> getAmigos() {
        return amigos;
    }

    public double getTiempoConectado() {
        return tiempoConectado;
    }

    public List<Producto> getCarrito() {
        return carrito;
    }

    public LocalDateTime getFechaUltimaConexion() {
        return fechaUltimaConexion;
    }



    public void setTiempoConectado(double tiempoConectado) {
        if(tiempoConectado < 0)
        {
            return;
        }
        this.tiempoConectado = tiempoConectado;
    }

    public void setFechaUltimaConexion(LocalDateTime fechaUltimaConexion) {
        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    // metodos

    public boolean comprarProducto(Producto producto) {

        if (producto == null) {
            return false;
        }

        double precio = producto.getPrecio();

        if (precio < 0 || saldo < precio) {
            return false;
        }

        if (!biblioteca.add(producto)) {
            return false;
        }

        saldo -= precio;

        return true;
    }

    public boolean agregarAlCarrito(Producto producto) {
        if (producto == null || carrito.contains(producto)) {
            return false;
        }

        return carrito.add(producto);
    }

    public void eliminarDelCarrito(Producto producto) {
        carrito.remove(producto);
    }

    public boolean agregarAmigo(Cliente amigo) {

        if (amigo == null) {
            return false;
        }

        Integer idAmigo = amigo.getId();

        if(idAmigo == null || amigo.equals(this))
        {
            return false;
        }
        return amigos.add(amigo);
    }

    public void eliminarAmigo(Cliente amigo) {
        amigos.remove(amigo);
    }

    public boolean agregarSaldo(double monto)
    {
        if(monto <= 0)
        {
            return false;
        }

        saldo = saldo + monto;
        return true;
    }

    public void registrarConexion() {
        fechaUltimaConexion = LocalDateTime.now();
    }


    @Override
    public int compareTo(Cliente otro) {
        return Integer.compare(this.getId(), otro.getId());
    }
}
