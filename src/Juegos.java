import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class  Juegos extends Productos{

    private GenerosJuegos genero;
    private List<DLCs> dlCs;
    private int edadMinima;
    private String requisitosMinimos;
    private String requisitosRecomendados;
    private LocalTime duracion;

    public Juegos(Desarrollador desarrollador, String descripcion, String nombre, double peso_GB, double precio, LocalTime duracion, int edadMinima, GenerosJuegos genero, String requisitosMinimos, String requisitosRecomendados) {
        super(desarrollador, descripcion, nombre, peso_GB, precio);
        this.dlCs = new ArrayList<>();
        this.duracion = duracion;
        this.edadMinima = edadMinima;
        this.genero = genero;
        this.requisitosMinimos = requisitosMinimos;
        this.requisitosRecomendados = requisitosRecomendados;
    }

    public void agregarDlc(DLCs dlc) {
            this.dlCs.add(dlc);
    }

    public String getRequisitosMinimos() {
        return requisitosMinimos;
    }

    public void setRequisitosMinimos(String requisitosMinimos) {
        this.requisitosMinimos = requisitosMinimos;
    }

    public String getRequisitosRecomendados() {
        return requisitosRecomendados;
    }

    public void setRequisitosRecomendados(String requisitosRecomendados) {
        this.requisitosRecomendados = requisitosRecomendados;
    }

    public GenerosJuegos getGenero() {
        return genero;
    }

    public void setGenero(GenerosJuegos genero) {
        this.genero = genero;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public List<DLCs> getDlCs() {
        return dlCs;
    }

    public void setDlCs(List<DLCs> dlCs) {
        this.dlCs = dlCs;
    }
}


