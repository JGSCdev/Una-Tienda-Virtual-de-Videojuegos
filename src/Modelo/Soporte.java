
package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Soporte extends Gestores {

    private List<String> quejas;

    public Soporte(String contrasenia, String pin) {
        super(contrasenia, pin); // Inicializa los atributos heredados de Gestores
        this.quejas = new ArrayList<>(); // Crea la lista donde se almacenarán las quejas
    }

    public void recibirQueja(String queja) {

        if (queja == null || queja.isBlank()) { // Evita almacenar quejas nulas o vacías
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }

        quejas.add(queja); // Agrega la queja a la lista
    }

    public List<String> consultarQuejas() {
        // Devuelve una vista no modificable para evitar cambios externos en la lista
        return Collections.unmodifiableList(quejas);
    }

    public boolean atenderQueja(String queja) {

        if (queja == null || queja.isBlank()) { // Comprueba que se haya recibido una queja válida
            throw new IllegalArgumentException(
                    "La queja no puede estar vacía"
            );
        }

        // Elimina la queja y devuelve true si estaba en la lista
        return quejas.remove(queja);
    }
}
