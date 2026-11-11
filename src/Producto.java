public class Producto implements Comparable <Producto>{
     private int id;
     private boolean estado;


    public int getId()
    {
        return id;
    }

    public void darDeBaja() {
        this.estado = false;
    }

    public void reactivar() {
        this.estado = true;
    }

    @Override
    public int compareTo(Producto o) {
        return 0;
    }
}
