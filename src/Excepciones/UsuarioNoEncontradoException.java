package Excepciones;

// Específica cuando se intenta buscar un usuario que no existe
public class UsuarioNoEncontradoException extends UsuarioServiceException {
    public UsuarioNoEncontradoException(int id) {
        super("No se encontró ningún usuario con el ID: " + id);
    }
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}