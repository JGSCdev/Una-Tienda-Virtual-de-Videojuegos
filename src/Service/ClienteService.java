package Service;

import Colecciones.ColeccionUsuariosGenerica;
import Excepciones.UsuarioDuplicadoException;
import Excepciones.UsuarioNoEncontradoException;
import Excepciones.UsuarioServiceException;
import Modelo.Cliente;
import Modelo.Producto;
import Persistencia.ClienteDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Servicio de clientes. Coordina las reglas del modelo, la colección en memoria
 * y la persistencia en MySQL.
 */
public class ClienteService {

    private final ColeccionUsuariosGenerica<Cliente> repositorioMemoria;
    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.repositorioMemoria = new ColeccionUsuariosGenerica<>();
        this.clienteDAO = new ClienteDAO();
        cargarClientesDesdeBD();
    }

    // Carga al iniciar la aplicación los clientes que existen en la base de datos.
    private void cargarClientesDesdeBD() {
        List<Cliente> clientesBD = clienteDAO.listarTodos();
        for (Cliente cliente : clientesBD) {
            repositorioMemoria.agregar(cliente);
        }
    }

    // =====================================================
    //                    OPERACIONES CRUD
    // =====================================================

    public void registrarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente a registrar no puede ser nulo.");
        }

        if (cliente.getId() != 0 || repositorioMemoria.existe(cliente.getId())) {
            throw new UsuarioDuplicadoException(
                    "El cliente ya tiene un ID asignado o está registrado."
            );
        }

        boolean insertadoEnBD = clienteDAO.insertar(cliente);

        if (!insertadoEnBD) {
            throw new UsuarioServiceException(
                    "No se pudo registrar el cliente en la base de datos."
            );
        }

        repositorioMemoria.agregar(cliente);
    }

    public Cliente buscarCliente(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser positivo.");
        }

        Cliente cliente = repositorioMemoria.buscar(id);

        if (cliente == null) {
            cliente = clienteDAO.buscarPorId(id);

            if (cliente != null) {
                repositorioMemoria.agregar(cliente);
            } else {
                throw new UsuarioNoEncontradoException(id);
            }
        }

        return cliente;
    }

    public Collection<Cliente> obtenerTodosLosClientes() {
        return repositorioMemoria.obtenerTodos();
    }

    public void actualizarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente a actualizar no puede ser nulo.");
        }

        buscarCliente(cliente.getId());

        if (!clienteDAO.actualizar(cliente)) {
            throw new UsuarioServiceException(
                    "No se pudo actualizar el cliente con ID " + cliente.getId() + "."
            );
        }

        repositorioMemoria.eliminar(cliente.getId());
        repositorioMemoria.agregar(cliente);
    }

    /** Restablece la contraseña de un cliente desde el panel de gestores. */
    public boolean restablecerContraseniaComoGestor(int id, String nuevaContrasenia) {
        Cliente cliente = buscarCliente(id);
        if (!cliente.validarContraseniaNueva(nuevaContrasenia)) {
            return false;
        }

        // Primero se confirma la escritura en MySQL; el objeto en memoria se
        // modifica únicamente cuando la persistencia fue exitosa.
        if (!clienteDAO.actualizarContrasenia(id, nuevaContrasenia)) {
            return false;
        }
        return cliente.restablecerContrasenia(nuevaContrasenia);
    }

    /** Cambia el estado de la cuenta de un cliente desde el panel de gestores. */
    public boolean cambiarEstadoComoGestor(int id, boolean activo) {
        Cliente cliente = buscarCliente(id);
        if (cliente.getEstado() == activo) {
            return true;
        }
        if (!clienteDAO.actualizarEstado(id, activo)) {
            return false;
        }
        cliente.setEstado(activo);
        return true;
    }

    public void eliminarCliente(int id) {

        buscarCliente(id);

        if (!clienteDAO.eliminar(id)) {
            throw new UsuarioServiceException(
                    "No se pudo eliminar el cliente con ID " + id + "."
            );
        }

        repositorioMemoria.eliminar(id);
    }

    // =====================================================
    //                 CARRITO DE COMPRAS
    // =====================================================

    /** Agrega un producto al carrito y sincroniza la tabla cliente_carrito. */
    public boolean agregarProductoAlCarrito(int clienteId, Producto producto) {

        if (producto == null || producto.getId() <= 0 || !producto.isEstado()) {
            return false;
        }

        Cliente cliente = buscarCliente(clienteId);

        // Se utiliza el método del modelo, ya que getCarrito() devuelve
        // una vista no modificable de la colección.
        if (!cliente.getEstado() || cliente.getBiblioteca().contains(producto)
                || !cliente.agregarAlCarrito(producto)) {
            return false;
        }

        if (!clienteDAO.agregarAlCarrito(clienteId, producto.getId())) {
            cliente.eliminarDelCarrito(producto);
            throw new UsuarioServiceException(
                    "No se pudo guardar el producto en el carrito de la base de datos."
            );
        }

        return true;
    }

    /** Quita un producto del carrito y persiste la eliminación. */
    public boolean eliminarProductoDelCarrito(int clienteId, Producto producto) {

        if (producto == null) {
            return false;
        }

        Cliente cliente = buscarCliente(clienteId);

        if (!cliente.getCarrito().contains(producto)) {
            return false;
        }

        if (!clienteDAO.eliminarDelCarrito(clienteId, producto.getId())) {
            return false;
        }

        return cliente.eliminarDelCarrito(producto);
    }

    /** Vacía el carrito en la base de datos y en memoria. */
    public boolean vaciarCarrito(int clienteId) {

        Cliente cliente = buscarCliente(clienteId);

        if (!clienteDAO.vaciarCarrito(clienteId)) {
            return false;
        }

        cliente.setCarrito(new ArrayList<>());
        return true;
    }

    // =====================================================
    //                       COMPRAS
    // =====================================================

    /**
     * Compra un producto. La operación de base de datos es transaccional:
     * descuenta el saldo, incorpora el producto a la biblioteca y lo quita
     * del carrito. El modelo se actualiza únicamente si MySQL confirma todo.
     */
    public boolean comprarProducto(int clienteId, Producto producto) {

        if (producto == null || producto.getId() <= 0 || !producto.isEstado()) {
            return false;
        }

        Cliente cliente = buscarCliente(clienteId);
        double precio = producto.getPrecio();

        if (!cliente.getEstado()
                || !Double.isFinite(precio)
                || precio < 0
                || cliente.getSaldo() < precio
                || cliente.getBiblioteca().contains(producto)) {
            return false;
        }

        boolean compraPersistida = clienteDAO.comprarProducto(
                clienteId,
                producto.getId(),
                precio
        );

        if (!compraPersistida) {
            return false;
        }

        // Estas validaciones coinciden con las comprobadas antes de la transacción.
        // Si el estado local estuviera desactualizado, recargamos al cliente.
        if (!cliente.comprarProducto(producto)) {
            repositorioMemoria.eliminar(clienteId);
            Cliente actualizado = clienteDAO.buscarPorId(clienteId);
            if (actualizado != null) {
                repositorioMemoria.agregar(actualizado);
            }
        }

        return true;
    }

    // =====================================================
    //                    SALDO DEL CLIENTE
    // =====================================================

    /** Agrega saldo de forma persistente. */
    public boolean agregarSaldo(int clienteId, double monto) {

        if (!Double.isFinite(monto) || monto <= 0) {
            return false;
        }

        Cliente cliente = buscarCliente(clienteId);

        if (!cliente.getEstado()
                || !Double.isFinite(cliente.getSaldo() + monto)) {
            return false;
        }

        if (!clienteDAO.agregarSaldo(clienteId, monto)) {
            return false;
        }

        // El DAO ya confirmó la actualización; el modelo aplica la misma suma.
        return cliente.agregarSaldo(monto);
    }

    // =====================================================
    //                       AMIGOS
    // =====================================================

    public void agregarAmigo(int clienteId, int amigoId) {

        if (clienteId == amigoId) {
            throw new IllegalArgumentException(
                    "Un cliente no puede agregarse a sí mismo como amigo."
            );
        }

        Cliente cliente = buscarCliente(clienteId);
        buscarCliente(amigoId);

        if (!clienteDAO.agregarAmigo(clienteId, amigoId)) {
            throw new UsuarioServiceException(
                    "No se pudo establecer la relación de amistad en la base de datos."
            );
        }

        cliente.setAmigos(clienteDAO.obtenerAmigos(clienteId));
    }

    public boolean eliminarAmigo(int clienteId, int amigoId) {

        if (clienteId <= 0 || amigoId <= 0 || clienteId == amigoId) {
            return false;
        }

        Cliente cliente = buscarCliente(clienteId);
        Cliente amigo = buscarCliente(amigoId);

        if (!clienteDAO.eliminarAmigo(clienteId, amigoId)) {
            return false;
        }

        cliente.eliminarAmigo(amigo);
        cliente.setAmigos(clienteDAO.obtenerAmigos(clienteId));
        return true;
    }
}
