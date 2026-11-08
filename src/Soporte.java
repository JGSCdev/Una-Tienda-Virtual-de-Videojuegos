import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Soporte extends Gestores {

    private List<String> quejas;

    public Soporte(String username, String contrasenia, String email) {
        super(username, contrasenia, email);
        this.quejas = new ArrayList<>();
    }

    public void recibirQueja(String queja) {
        if (queja == null || queja.isBlank()) {
            throw new IllegalArgumentException("La queja no puede estar vacía");
        }

        quejas.add(queja);
    }

    public List<String> consultarQuejas() {
        return Collections.unmodifiableList(quejas);
    }

    public void atenderConsulta(String queja) {
        quejas.remove(queja);
    }

}