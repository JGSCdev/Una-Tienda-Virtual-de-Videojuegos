package Service;

import Coleciones.ColeccionUsuariosGenerica;
import Excepciones.UsuarioDuplicadoException;
import Excepciones.UsuarioNoEncontradoException;
import Excepciones.UsuarioServiceException;
import Modelo.Cliente;
import Modelo.Producto;
import Persistencia.ClienteDAO;
import java.util.Collection;
import java.util.List;



public class ClienteService {

    private final ColeccionUsuariosGenerica<Cliente> repositorioMemoria;
    private final ClienteDAO clienteDAO;

    //Constructor del servicio. Inicializa la estructura en memoria y el DAO, y precarga los clientes existentes en la base de datos.
    public ClienteService() {
        this.repositorioMemoria = new ColeccionUsuariosGenerica<>();
        this.clienteDAO = new ClienteDAO();
        cargarClientesDesdeBD();
    }

    private void cargarClientesDesdeBD() {//Metodo auxiliar privado para sincronizar la memoria con la base de datos al arrancar.
        List<Cliente> clientesBD = clienteDAO.listarTodos();
        for (Cliente c : clientesBD) {
            repositorioMemoria.agregar(c);
        }
    }

    // ==========================================
    //              OPERACIONES CRUD
    // ==========================================

    public void registrarCliente(Cliente cliente) {//Registra un nuevo cliente en el sistema
        if (cliente == null) {//primero verifica si el cliente es nulo

            throw new IllegalArgumentException("El cliente a registrar no puede ser nulo.");//si el cliente es nulo, lanza IllegalArgumentException.
        }

        if (repositorioMemoria.existe(cliente.getId())) { // despues valida si esta ya en la base de datos
            throw new UsuarioDuplicadoException("Ya existe un cliente con el ID en memoria: " + cliente.getId());//si el ID ya existe en memoria, lanza UsuarioDuplicadoException.
        }

        boolean insertadoEnBD = clienteDAO.insertar(cliente); // se inserta ese cliente a la base de datos

        if (!insertadoEnBD) { // despues verifica si se logro ingresar ese usuario a la base de datos
            throw new UsuarioServiceException("No se pudo registrar el cliente en la base de datos.");//si falla la inserción en la base de datos, lanza UsuarioServiceException.
        }

        repositorioMemoria.agregar(cliente);// si paso todas las verificaciones lo agrega a la coleccion en memoria
    }

    public Cliente buscarCliente(int id) {//Busca un cliente por su ID

        Cliente cliente = repositorioMemoria.buscar(id);//Primero revisa en memoria si esta ese cliente (esto es por que seria mas rapido buscarlo en la memoria antes que en la BD). si es que llega a no estar, consulta la base de datos.

        if (cliente == null) {
            // Intentamos buscar en la BD si no estaba en memoria
            cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                repositorioMemoria.agregar(cliente); // Lo cargamos a memoria para futuras consultas
            } else {
                throw new UsuarioNoEncontradoException(id); //lo lanza si el cliente no existe en ninguna parte.
            }
        }
        return cliente;
    }


    public Collection<Cliente> obtenerTodosLosClientes() {// Obtiene una colección de todos los clientes registrados
        return repositorioMemoria.obtenerTodos();
    }


    public void actualizarCliente(Cliente cliente) {//Actualiza la información personal de un cliente existente
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente a actualizar no puede ser nulo."); //si el cliente es nulo, lanza IllegalArgumentException.
        }

        // Validamos que el cliente realmente exista
        buscarCliente(cliente.getId());

        boolean actualizadoEnBD = clienteDAO.actualizar(cliente);

        if (!actualizadoEnBD) {
            throw new UsuarioServiceException("No se pudo actualizar el cliente con ID " + cliente.getId() + " en la base de datos."); // lanza UsuarioServiceException si falla la modificacion
        }

        // Actualizamos también en memoria volviéndolo a agregar
        repositorioMemoria.eliminar(cliente.getId());
        repositorioMemoria.agregar(cliente);
    }

    public void eliminarCliente(int id) { //Elimina un cliente del sistema por su ID

        buscarCliente(id);// Valida existencia (lanza UsuarioNoEncontradoException si no existe)

        boolean eliminadoEnBD = clienteDAO.eliminar(id);
        if (!eliminadoEnBD) {
            throw new UsuarioServiceException("Error al intentar eliminar el cliente con ID " + id + " de la base de datos."); // lanza UsuarioServiceException si falla la eliminación en la base de datos.
        }

        // Eliminamos en memoria
        repositorioMemoria.eliminar(id);
    }


    // ==========================================
    //      MÉTODOS DE NEGOCIO Y COLECCIONES
    // ==========================================

    public void agregarProductoAlCarrito(int clienteId, Producto producto) {// Agrega un producto al carrito de compras de un cliente.
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        Cliente cliente = buscarCliente(clienteId); // Lanza UsuarioNoEncontradoException si no existe

        boolean agregadoEnMemoria = cliente.getCarrito().add(producto);

        if (agregadoEnMemoria) {

            boolean bdExito = clienteDAO.agregarAlCarrito(clienteId, producto.getId());

            if (!bdExito) {

                cliente.getCarrito().remove(producto);
                throw new UsuarioServiceException("El producto se añadió en memoria pero falló la persistencia en la base de datos.");
            }
        }
    }

    public void agregarJuegoABiblioteca(int clienteId, int productoId) {//Agrega un juego directamente a la biblioteca del cliente.
        Cliente cliente = buscarCliente(clienteId);

        boolean bdExito = clienteDAO.agregarJuegoBiblioteca(clienteId, productoId);
        if (!bdExito) {
            throw new UsuarioServiceException("No se pudo agregar el juego a la biblioteca en la base de datos.");
        }

        // Recargamos el cliente o actualizamos su biblioteca local desde la BD para mantener consistencia
        cliente.setBiblioteca(clienteDAO.obtenerBiblioteca(clienteId));
    }

    public void agregarAmigo(int clienteId, int amigoId) { //Agrega un amigo a la red de contactos del cliente.
        if (clienteId == amigoId) {
            throw new IllegalArgumentException("Un cliente no puede agregarse a sí mismo como amigo.");
        }

        // Validamos que ambos clientes existan
        buscarCliente(clienteId);
        buscarCliente(amigoId);

        boolean bdExito = clienteDAO.agregarAmigo(clienteId, amigoId);
        if (!bdExito) {
            throw new UsuarioServiceException("No se pudo establecer la relación de amistad en la base de datos.");
        }

        // Actualizamos la lista de amigos en memoria
        Cliente cliente = buscarCliente(clienteId);
        cliente.setAmigos(clienteDAO.obtenerAmigos(clienteId));
    }
}