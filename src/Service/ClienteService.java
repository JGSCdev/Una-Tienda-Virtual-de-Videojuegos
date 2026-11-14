package Service;

import Coleciones.ColeccionUsuariosGenerica;
import Excepciones.UsuarioDuplicadoException;
import Excepciones.UsuarioNoEncontradoException;
import Excepciones.UsuarioServiceException;
import Modelo.Cliente;
import Modelo.Producto;
import Persistencia.ClienteDAO;
import java.util.Collection;


public class ClienteService {

    private final ColeccionUsuariosGenerica<Cliente> repositorioMemoria;
    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.repositorioMemoria = new ColeccionUsuariosGenerica<>();
        this.clienteDAO = new ClienteDAO();
        cargarClientesDesdeBD();
    }

    private void cargarClientesDesdeBD() {
        for (Cliente c : clienteDAO.listarTodos()) {
            repositorioMemoria.agregar(c);
        }
    }

    // Registrar un nuevo cliente
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente a registrar no puede ser nulo.");
        }

        if (repositorioMemoria.existe(cliente.getId())) {
            throw new UsuarioDuplicadoException("ID de cliente en memoria: " + cliente.getId());
        }

        // 1. Guardar en la base de datos
        boolean insertadoEnBD = clienteDAO.insertar(cliente);

        if (!insertadoEnBD) {
            throw new UsuarioServiceException("No se pudo registrar el cliente en la base de datos.");
        }

        // 2. Agregar a la caché en memoria
        repositorioMemoria.agregar(cliente);
    }

    // Buscar cliente por ID
    public Cliente buscarCliente(int id) {
        Cliente cliente = repositorioMemoria.buscar(id);

        if (cliente == null) {
            // Intentamos buscar en la BD por si no estaba en memoria
            cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                repositorioMemoria.agregar(cliente);
            } else {
                throw new UsuarioNoEncontradoException(id); // Lanza excepción específica si no existe en ningún lado
            }
        }
        return cliente;
    }

    // Listar todos los clientes
    public Collection<Cliente> obtenerTodosLosClientes() {
        return repositorioMemoria.obtenerTodos();
    }

    // Eliminar cliente
    public void eliminarCliente(int id) {
        // Validamos que exista antes de eliminar (lanza excepción si no está)
        buscarCliente(id);

        boolean eliminadoEnBD = clienteDAO.eliminar(id);
        if (!eliminadoEnBD) {
            throw new UsuarioServiceException("Error al intentar eliminar el cliente con ID " + id + " de la base de datos.");
        }

        repositorioMemoria.eliminar(id);
    }

    // Método de negocio: Agregar producto al carrito
    public void agregarProductoAlCarrito(int clienteId, Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        Cliente cliente = buscarCliente(clienteId); // Lanza UsuarioNoEncontradoException si no existe

        boolean agregado = cliente.getCarrito().add(producto);
        if (agregado) {
            boolean bdExito = clienteDAO.agregarAlCarrito(clienteId, producto.getId());
            if (!bdExito) {
                throw new UsuarioServiceException("El producto se añadió en memoria pero falló la persistencia en la base de datos.");
            }
        }
    }
}