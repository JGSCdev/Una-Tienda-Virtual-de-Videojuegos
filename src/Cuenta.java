import java.time.LocalDateTime;

public class Cuenta {
    private String id;
    private static Integer contadorID = 0;
    private String username;
    private String contrasenia;
    private String email;
    private Boolean estado = true;
    private LocalDateTime creacionCuenta;

    //constructores

    //por defecto de cuenta


    public Cuenta(String username, String contrasenia, String email) {
        this.username = username;
        this.contrasenia = contrasenia;
        this.email = email;
        this.creacionCuenta = LocalDateTime.now();
        contadorID++;
        id = contadorID.toString();
    }

    public Cuenta() {
        contadorID++;
        id = contadorID.toString();
    }

    //Getters y setters
    public LocalDateTime getCreacionCuenta() {
        return creacionCuenta;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static Integer getContadorID() {
        return contadorID;
    }

    public String getId() {
        return id;
    }

    // metodos
}
