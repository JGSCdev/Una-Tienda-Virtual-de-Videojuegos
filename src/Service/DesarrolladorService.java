package Service;

import Colecciones.ColeccionUsuariosGenerica;
import Excepciones.UsuarioDuplicadoException;
import Excepciones.UsuarioNoEncontradoException;
import Excepciones.UsuarioServiceException;
import Modelo.Desarrollador;
import Modelo.Producto;
import Persistencia.DesarrolladorDAO;

import java.util.Collection;
import java.util.List;


public class DesarrolladorService {

    private final ColeccionUsuariosGenerica<Desarrollador> repositorioMemoria;
    private final DesarrolladorDAO desarrolladorDAO;

     //Constructor del servicio. Inicializa la estructura en memoria y el DAO, y precarga los desarrolladores existentes en la base de datos.
    public DesarrolladorService() {
        this.repositorioMemoria = new ColeccionUsuariosGenerica<>();
        this.desarrolladorDAO = new DesarrolladorDAO();
        cargarDesarrolladoresDesdeBD();
    }


    private void cargarDesarrolladoresDesdeBD() {//Metodo auxiliar privado para sincronizar la memoria con la base de datos al arrancar.
        List<Desarrollador> desarrolladoresBD = desarrolladorDAO.listarTodos();
        for (Desarrollador d : desarrolladoresBD) {
            repositorioMemoria.agregar(d);
        }
    }

    // ==========================================
    //              OPERACIONES CRUD
    // ==========================================

    public void registrarDesarrollador(Desarrollador desarrollador) {//Registra un nuevo desarrollador en el sistema
        if (desarrollador == null) { // primero verifica si el usuario es nulo
            throw new IllegalArgumentException("El desarrollador a registrar no puede ser nulo.");// lanza IllegalArgumentException si el desarrollador es nulo.
        }

        if (repositorioMemoria.existe(desarrollador.getId())) { // despues verifica si ese desarrollador esta en la memoria
            throw new UsuarioDuplicadoException("Ya existe un desarrollador con el ID en memoria: " + desarrollador.getId());// lanza UsuarioDuplicadoException si el ID ya existe en memoria.
        }

        boolean insertadoEnBD = desarrolladorDAO.insertar(desarrollador); // despues intenta insertar al desarrollador en la BD

        if (!insertadoEnBD) { // verifica si logro la insercion
            throw new UsuarioServiceException("No se pudo registrar el desarrollador en la base de datos.");// lanza UsuarioServiceException si falla la inserción en la base de datos.
        }
        repositorioMemoria.agregar(desarrollador);// si logro estar en la base de datos, tambien lo agrega en memoria
    }


    public Desarrollador buscarDesarrollador(int id) {//Busca un desarrollador por su ID
        Desarrollador dev = repositorioMemoria.buscar(id);//Primero revisa la caché en memoria; si no está, consulta la base de datos.

        if (dev == null) {
            dev = desarrolladorDAO.buscarPorId(id);
            if (dev != null) {
                repositorioMemoria.agregar(dev); // Lo cargamos a memoria para futuras consultas
            } else {
                throw new UsuarioNoEncontradoException(id);// lanza UsuarioNoEncontradoException si el desarrollador no existe.
            }
        }
        return dev;
    }

    public Collection<Desarrollador> obtenerTodosLosDesarrolladores() {//Obtiene una colección de todos los desarrolladores registrados
        return repositorioMemoria.obtenerTodos(); //retorna una colección inmodificable de desarrolladores en memoria.
    }


    public void actualizarDesarrollador(Desarrollador desarrollador) {//Actualiza la información de un desarrollador existente
        if (desarrollador == null) {
            throw new IllegalArgumentException("El desarrollador a actualizar no puede ser nulo.");
        }

        // Validamos que exista previamente
        buscarDesarrollador(desarrollador.getId());// lanza UsuarioNoEncontradoException si el desarrollador no existe.

        boolean actualizadoEnBD = desarrolladorDAO.actualizar(desarrollador);
        if (!actualizadoEnBD) {
            throw new UsuarioServiceException("No se pudo actualizar el desarrollador con ID " + desarrollador.getId() + " en la base de datos.");// lanza UsuarioServiceException si falla la actualización en la base de datos.
        }

        // Actualizamos en la caché en memoria
        repositorioMemoria.eliminar(desarrollador.getId());
        repositorioMemoria.agregar(desarrollador);
    }


    /** Restablece la contraseña de un desarrollador desde el panel de gestores. */
    public boolean restablecerContraseniaComoGestor(int id, String nuevaContrasenia) {
        Desarrollador desarrollador = buscarDesarrollador(id);
        if (!desarrollador.validarContraseniaNueva(nuevaContrasenia)) {
            return false;
        }
        if (!desarrolladorDAO.actualizarContrasenia(id, nuevaContrasenia)) {
            return false;
        }
        return desarrollador.restablecerContrasenia(nuevaContrasenia);
    }

    /** Cambia el estado de la cuenta de un desarrollador desde el panel de gestores. */
    public boolean cambiarEstadoComoGestor(int id, boolean activo) {
        Desarrollador desarrollador = buscarDesarrollador(id);
        if (desarrollador.getEstado() == activo) {
            return true;
        }
        if (!desarrolladorDAO.actualizarEstado(id, activo)) {
            return false;
        }
        desarrollador.setEstado(activo);
        return true;
    }

    /** Sincroniza en memoria las ganancias después de una compra confirmada en MySQL. */
    public void acreditarGananciasEnMemoria(int desarrolladorId, double importe) {
        if (desarrolladorId <= 0 || !Double.isFinite(importe) || importe < 0) {
            return;
        }
        Desarrollador desarrollador = repositorioMemoria.buscar(desarrolladorId);
        if (desarrollador != null && Double.isFinite(desarrollador.getGanancias() + importe)) {
            desarrollador.setGanancias(desarrollador.getGanancias() + importe);
        }
    }

    public void eliminarDesarrollador(int id) {// Elimina un desarrollador del sistema por su ID
        // Valida existencia (lanza excepción si no existe)
        buscarDesarrollador(id);// lanza UsuarioNoEncontradoException si el desarrollador no existe.

        boolean eliminadoEnBD = desarrolladorDAO.eliminar(id);
        if (!eliminadoEnBD) {
            throw new UsuarioServiceException("Error al intentar eliminar el desarrollador con ID " + id + " de la base de datos.");// lanza UsuarioServiceException si falla la eliminación en la base de datos.
        }

        // Eliminamos en memoria
        repositorioMemoria.eliminar(id);
    }



    // ==========================================
    //      MÉTODOS DE NEGOCIO Y CATÁLOGO
    // ==========================================


    public void publicarProducto(int desarrolladorId, Producto producto) {//Publica un producto dentro del catálogo propio del desarrollador.
        if (producto == null) {
            throw new IllegalArgumentException("El producto a publicar no puede ser nulo.");// lanza IllegalArgumentException si el producto es nulo.
        }

        Desarrollador dev = buscarDesarrollador(desarrolladorId); // Lanza UsuarioNoEncontradoException si el desarrollador no existe.

        // Utiliza el metodo de negocio definido en la clase Desarrollador
        boolean publicado = dev.publicarProducto(producto);

        if (!publicado) {
            throw new UsuarioServiceException("No se pudo publicar el producto. Verifique que la cuenta esté activa o que el producto no esté duplicado.");// lanza UsuarioServiceException si las reglas de negocio del desarrollador rechazan la publicación.
        }

    }
}
