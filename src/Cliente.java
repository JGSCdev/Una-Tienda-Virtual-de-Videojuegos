import java.time.LocalDateTime;
import java.util.*;

public class Cliente extends Usuario{
    private Set<Producto> biblioteca = new HashSet<>();
    private Double saldo = 0.0;
    private List<Producto> historial = new ArrayList<>();
    private TreeSet<Cliente> amigos = new TreeSet<>();
    private Double tiempoConectado = 0.0;
    private List<Producto> carrito = new ArrayList<>();
    private LocalDateTime fechaUltimaConexion;


}
