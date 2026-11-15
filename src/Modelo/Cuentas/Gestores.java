package Modelo.Cuentas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Gestores {

    private int id;
    private String contrasenia;
    private String pin;

    // Colección compartida de usuarios registrados
    private static Map<Integer, Usuario> listaUsuarios = new HashMap<>();

    // Constructor para crear un gestor nuevo
    public Gestores(String contrasenia, String pin) {
        this.id = 0;
        this.contrasenia = contrasenia;
        this.pin = pin;
    }

    // Constructor para recuperar un gestor de la BD
    public Gestores(int id, String contrasenia, String pin) {
        this.id = id;
        this.contrasenia = contrasenia;
        this.pin = pin;
    }

    public Gestores() {
        this.id = 0;
    }

    // -------------------- GETTERS --------------------

    public int getId() {
        return id;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getPin() {
        return pin;
    }

    public Map<Integer, Usuario> getListaUsuarios() {
        return Collections.unmodifiableMap(listaUsuarios);
    }

    // Asigna el ID generado automáticamente por la base de datos.
    // Solo permite asignarlo si el gestor todavía no tiene un ID.
    public void asignarIdGenerado(int id) {

        if (this.id != 0) {
            throw new IllegalStateException("El gestor ya tiene un ID asignado");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }

        this.id = id;
    }

    //-------------------------------------METODOS---------------------------------------------------

    public static void agregarUsuario(Usuario usuario) {

        if (usuario == null) { // Comprueba que el objeto recibido no sea nulo
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        int id = usuario.getId(); // Obtiene el ID del usuario

        if (id <= 0) { // Comprueba que el usuario tenga un ID válido
            throw new IllegalArgumentException("El usuario debe tener un ID válido");
        }

        if (listaUsuarios.containsKey(id)) { // Verifica que no exista otro usuario con el mismo ID
            throw new IllegalArgumentException("El usuario ya existe");
        }

        listaUsuarios.put(id, usuario); // Agrega el usuario al mapa
    }

    public static Usuario buscarUsuarioPorID(int id) {
        // get(id) devuelve el usuario asociado a esa clave
        return listaUsuarios.get(id);
    }

    public void modificarUsuario(int id, String username, String email) {

        Usuario usuario = buscarUsuarioPorID(id); // Busca al usuario mediante su ID

        if (usuario == null) { // Comprueba que el usuario exista
            throw new IllegalArgumentException("No existe un usuario con ese ID");
        }

        // Los setters de Usuario validan el nombre y el email
        usuario.setUsername(username);
        usuario.setEmail(email);
    }

    public void darBajaUsuario(int id) {

        Usuario usuario = buscarUsuarioPorID(id); // Busca al usuario que se quiere dar de baja

        if (usuario != null && usuario.getEstado()) { // Comprueba que exista y que su cuenta esté activa
            usuario.darDeBaja(); // Realiza una baja lógica
        }
    }

    public boolean restablecerContrasenia(int id, String nuevaContrasenia) {

        Usuario usuario = buscarUsuarioPorID(id); // Busca al usuario cuya contraseña se quiere restablecer

        if (usuario == null) { // Si no existe, no se puede realizar el restablecimiento
            return false;
        }

        // Delega a Usuario la validación de la contraseña nueva y el cambio correspondiente
        return usuario.restablecerContrasenia(nuevaContrasenia);
    }

    public void reactivarUsuario(int id) {

        Usuario usuario = buscarUsuarioPorID(id); // Busca al usuario que se quiere reactivar

        if (usuario != null && !usuario.getEstado()) { // Comprueba que exista y que la cuenta esté inactiva
            usuario.reactivar(); // Cambia el estado a true
        }
    }
}