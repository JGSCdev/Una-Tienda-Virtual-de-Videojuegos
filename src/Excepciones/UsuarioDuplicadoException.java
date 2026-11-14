package Excepciones;

// Específica cuando se intenta registrar un usuario duplicado (por ID, username o email)
public class UsuarioDuplicadoException extends UsuarioServiceException {
    public UsuarioDuplicadoException(String identificador) {
        super("Ya existe un usuario registrado con el identificador: " + identificador);
    }
}