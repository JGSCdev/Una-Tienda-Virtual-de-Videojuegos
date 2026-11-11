import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Cliente extends Usuario implements Comparable<Cliente>{
    private Set<Producto> biblioteca;
    private double saldo = 0.0;
    private TreeSet<Cliente> amigos;
    private double tiempoConectado = 0.0;
    private List<Producto> carrito;
    private LocalDateTime fechaUltimaConexion = LocalDateTime.now();

    // cnostructores


    public Cliente(String username, String contrasenia, String email, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento) {
        super(username, contrasenia, email, nacionalidad, nombre, apellido, fechaNacimiento);
        this.biblioteca = new HashSet<>();
        this.amigos = new TreeSet<>();
        this.carrito = new ArrayList<>();
    }

    public Cliente(int id, String username, String contrasenia, String email, boolean estado, LocalDateTime creacionCuenta, String descripcion, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento, Set<Producto> biblioteca, double saldo, TreeSet<Cliente> amigos, double tiempoConectado, List<Producto> carrito, LocalDateTime fechaUltimaConexion) {
        super(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento);

        if (biblioteca == null) { // Verifica si se intenta poner una coleccion nula, esto se usa en todas las colecciones
            this.biblioteca = new HashSet<>(); // En ese caso le generara una coleccion vacia
        } else {
            this.biblioteca = new HashSet<>(biblioteca); // Si resulto que no es nula devuele la collecion completa
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
        this.biblioteca = new HashSet<>();
        this.amigos = new TreeSet<>();
        this.carrito = new ArrayList<>();
    }

    //Getters y setters


    public Set<Producto> getBiblioteca() {
        return Collections.unmodifiableSet(biblioteca); // aca devuelve una coleccion que no puede ser modificada por la clase que llame a este getter, lo que no crea una copia. Es una vista no modificable de la colección original.
    }

    public double getSaldo() {
        return saldo;
    }


    public SortedSet<Cliente> getAmigos() {
        return Collections.unmodifiableSortedSet(amigos);// aca devuelve una coleccion que no puede ser modificada por la clase que llame a este getter
    }

    public double getTiempoConectado() {
        return tiempoConectado;
    }

    public List<Producto> getCarrito() {
        return Collections.unmodifiableList(carrito);// aca devuelve una coleccion que no puede ser modificada por la clase que llame a este getter
    }

    public LocalDateTime getFechaUltimaConexion() {
        return fechaUltimaConexion;
    }



    public void setTiempoConectado(double tiempoConectado) {
        if(tiempoConectado < 0) // si el tiempo es menor a cero no hace ningun cambio
        {
            return;
        }
        this.tiempoConectado = tiempoConectado; // suma el tiempo conectado al total de tiempo en general
    }

    public void setFechaUltimaConexion(LocalDateTime fechaUltimaConexion) { //este set sirve mas que nada para poder asignarle un tiempo especifico, por que ya hay un metodo que actualiza esta variable
        if(fechaUltimaConexion == null)
        {
            return;
        }
        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    // metodos

    public boolean comprarProducto(Producto producto) {

        if (!getEstado()) { // primero revisa si el usuario esta baneado/fuera del sistema
            return false;
        }

        if (producto == null) { // despues revisa si el producto que quiere comprar es nulo
            return false;
        }

        if (!producto.isEstado()) { // verifica si el juego esta disponible para su compra
            return false;
        }

        double precio = producto.getPrecio(); // se extrae el precio del producto

        if (precio < 0 || saldo < precio) { // revisa si el precio es menor a cero o si el saldo del cliente es menor al precio del juego que quiere comprar
            return false;
        }

        if (!biblioteca.add(producto)) { // añade el juego a la biblioteca con el add del Set lo que significa que si se logro agregar el juego pasa este filtro, pero si ya estaba en la biblioteca no se descuenta el dinero
            return false;
        }

        saldo -= precio; // si el juego logro pasar todos los filtros se le descuenta dinero del saldo

        if(carrito.contains(producto)) // si es que estaba en carrito, lo elimina del carrito para
        {
            eliminarDelCarrito(producto);
        }

        return true;
    }


    public boolean agregarAlCarrito(Producto producto) {

        if (producto == null || carrito.contains(producto) || !producto.isEstado() || !getEstado()) { // revisa si el producto es nulo o si ya estaba en el carrito o si su estado es falso (osea que no esta disponible para su compra) o si el usuario esta baneado/fuera del sistema
            return false;
        }

        return carrito.add(producto); // si pasa ese filtro el producto entra al carrito
    }

    public boolean eliminarDelCarrito(Producto producto) {
        return carrito.remove(producto);
    }

    public boolean agregarAmigo(Cliente amigo) {

        if (!getEstado()) { // primero revisa si el usuario esta baneado/fuera del sistema
            return false;
        }

        if (amigo == null) { // despues revisa si el amigo que intentamos agregar esta nulo
            return false;
        }

        Integer idAmigo = amigo.getId(); // despues copiamos su id como Integer en lugar de int

        if(idAmigo == null || amigo.equals(this)) // despues revisa si el id del amigo es nulo (gracias a Integer que puede compararse con nulos)
        {
            return false;
        }
        return amigos.add(amigo);
    }

    public boolean eliminarAmigo(Cliente amigo) {

        if (amigo == null) {
            return false;
        }

        return amigos.remove(amigo);
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

    public void agregarTiempoConectado(double tiempoConectado) {
        if(tiempoConectado < 0) // si el tiempo es menor a cero no hace ningun cambio
        {
            return;
        }
        this.tiempoConectado += tiempoConectado; // suma el tiempo conectado al total de tiempo en general
    }



    @Override
    public int compareTo(Cliente otro) {

        if (otro == null) {
            throw new NullPointerException("El cliente no puede ser null");
        }

        return Integer.compare(this.getId(), otro.getId());
    }
}
