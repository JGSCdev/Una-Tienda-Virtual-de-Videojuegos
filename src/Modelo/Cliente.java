package Modelo;

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

        //biblioteca
        if (biblioteca == null) { // Verifica si se intenta poner una coleccion nula, esto se usa en todas las colecciones

            this.biblioteca = new HashSet<>(); // En ese caso le generara una coleccion vacia

        } else {

            this.biblioteca = new HashSet<>(biblioteca); // Si resulto que no es nula devuele la collecion completa

        }

        //lista de amigos agregados
        if (amigos == null) {

            this.amigos = new TreeSet<>();

        } else {

            this.amigos = new TreeSet<>(amigos);

        }

        //saldo

        if (!Double.isFinite(saldo) || saldo < 0) {

            throw new IllegalArgumentException("Saldo inválido");
        }
        else
        {
            this.saldo = saldo;
        }

        //tiempo conectado

        if (!Double.isFinite(tiempoConectado) || tiempoConectado < 0) {

            throw new IllegalArgumentException("Tiempo conectado inválido");

        }
        else
        {
            this.tiempoConectado = tiempoConectado;
        }

        //el carrito del cliente

        if (carrito == null) {

            this.carrito = new ArrayList<>();

        } else {

            this.carrito = new ArrayList<>(carrito);
        }

        // fecha de su ultima coneccion a la tienda (y al sistema en general)

        if(fechaUltimaConexion == null)
        {
            this.fechaUltimaConexion = LocalDateTime.now();
        }
        else
        {
            this.fechaUltimaConexion = fechaUltimaConexion;
        }
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



    public void setTiempoConectado(double tiempoConectado) { //este set sirve mas que nada para poder asignarle un tiempo especifico, por que ya hay un metodo que actualiza esta variable
        if (!Double.isFinite(tiempoConectado) || tiempoConectado < 0) {
            return;
        }
        this.tiempoConectado = tiempoConectado;
    }

    public void setFechaUltimaConexion(LocalDateTime fechaUltimaConexion) { //este set sirve mas que nada para poder asignarle una fecha en especifico, por que ya hay un metodo que actualiza esta variable
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

        if (!Double.isFinite(precio) || precio < 0 || saldo < precio) { // revisa si el precio es menor a cero o si el saldo del cliente es menor al precio del juego que quiere comprar y tambien si el precio es finito
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

        if (amigo == null) { // primero revisa si el amigo es nulo
            return false;
        }

        if (this.getId() <= 0 || amigo.getId() <= 0) { // despues revisa si los ids son mayores a cero (sirve para detectar si el id no tiene asignado un id en la base de datos)
            return false;
        }

        if (amigo.equals(this)) { // antes de terminar revisa si ese "amigo" no es el mismo
            return false;
        }

        return amigos.add(amigo); // y ya la ultima validacion la hace el TreeSet
    }

    public boolean eliminarAmigo(Cliente amigo) {

        if (amigo == null) { // primero revisa si el amigo es nulo
            return false;
        }

        return amigos.remove(amigo); // despues el TreeSet verifica si se pudo eliminar ese amigo
    }

    public boolean agregarSaldo(double monto) {
        if (!Double.isFinite(monto) || monto <= 0) { // no agrega el saldo si es menor a cero o si el saldo que se quiere ingresar representa un NaN o un numero infinito
            return false;
        }

        if (!Double.isFinite(saldo + monto)) { // despues revisa si la suma del saldo y el monto dan un numero infinito
            return false;
        }

        saldo += monto;
        return true;
    }

    public void registrarConexion() {
        fechaUltimaConexion = LocalDateTime.now();
    }

    public void agregarTiempoConectado(double tiempoConectado) {
        if (!Double.isFinite(tiempoConectado) || tiempoConectado < 0) { // no agrega el tiempo si es menor a cero o si el tiempo que se quiere ingresar representa un NaN o un numero infinito
            return;
        }

        if (!Double.isFinite(this.tiempoConectado + tiempoConectado)) { // revisa si la suma del tiempo conectado con el tiempo que se quiere agregar es un numero infinito
            return;
        }

        this.tiempoConectado += tiempoConectado;
    }



    @Override
    public int compareTo(Cliente otro) {

        if (otro == null) {
            throw new NullPointerException("El cliente no puede ser null"); // si llega a ser null, el compare to va a lanzar una NullPointerException
        }

        return Integer.compare(this.getId(), otro.getId());
    }

    @Override
    public String toString() {
        return super.toString() + "Datos del Modelo.Cliente " +
                                    "\n-----------------------------\n" +
                                    "Saldo disponible: " + saldo +
                                    "\nTiempo Conectado: " + tiempoConectado +
                                    "\nUltima Coneccion: " + fechaUltimaConexion +
                                    "\n-----------------------------\n";
    }
}
