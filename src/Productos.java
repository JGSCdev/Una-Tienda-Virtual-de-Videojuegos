import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public abstract class Productos {

    private String nombre;
    private double precio;
    private String descripcion;
    private LocalDate fecha_Lanzamiento;
    private double peso_GB;
    private Desarrollador desarrollador;
    private Map<String,Resenias> resenia;
    private String id;
    private boolean estado;

    private static int contador;


    public Productos(Desarrollador desarrollador, String descripcion, String nombre, double peso_GB, double precio) {
        contador++;
        this.desarrollador = desarrollador;
        this.descripcion = descripcion;
        this.estado = true;
        this.fecha_Lanzamiento = LocalDate.now();
        this.id = Integer.toString(contador);
        this.nombre = nombre;
        this.peso_GB = peso_GB;
        this.precio = precio;
        this.resenia = new HashMap<>();
    }

    public double precioFinal(double descuento){
        if(descuento <= 0) return precio;
        else return (precio + (precio * (descuento / 100)));
    }

    public double valoracionPromedio(){

        double total = 0;
        for (Resenias resenias : resenia.values()){
            total += resenias.getValoracion();
        }
        return total / resenia.size();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Map<String, Resenias> getResenia() {
        return resenia;
    }

    public void setResenia(Map<String, Resenias> resenia) {
        this.resenia = resenia;
    }

    public double getPeso_GB() {
        return peso_GB;
    }

    public void setPeso_GB(double peso_GB) {
        this.peso_GB = peso_GB;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha_Lanzamiento() {
        return fecha_Lanzamiento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public static int getContador() {
        return contador;
    }

    public String getId() {
        return id;
    }



}
