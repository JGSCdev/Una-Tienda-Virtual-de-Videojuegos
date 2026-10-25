import java.time.LocalDate;

public class Resenias{

    private double valoracion;
    private String comentario;
    private Usuarios usuario;
    private LocalDate fecha_publicacion;

    public Resenias(String comentario, Usuarios usuario, double valoracion) {
        this.comentario = comentario;
        this.fecha_publicacion = LocalDate.now();
        this.usuario = usuario;
        this.valoracion = valoracion;
    }

    public Resenias(Usuarios usuario, double valoracion) {
        this.usuario = usuario;
        this.valoracion = valoracion;
        this.fecha_publicacion = LocalDate.now();
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(LocalDate fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }
}
