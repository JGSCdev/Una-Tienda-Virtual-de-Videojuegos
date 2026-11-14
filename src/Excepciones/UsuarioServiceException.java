package Excepciones;

// Excepción base para cualquier error de la capa de servicio de usuarios
public class UsuarioServiceException extends RuntimeException {
    public UsuarioServiceException(String mensaje) {
        super(mensaje);
    }
    public UsuarioServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}