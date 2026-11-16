package Gestion;


import Interfaces.iControlJuegos;
import Modelo.Producto;
import Modelo.Juego;
import Modelo.DLC;
import Modelo.Desarrollador;
import Service.DLCservice;
import Service.JuegoService;

import java.util.*;

public class Gestion_catalogo implements iControlJuegos {

    private Map<Integer, Producto> catalogo;

    private JuegoService juegoService;
    private DLCservice dlcService;

    // Constructor
    public Gestion_catalogo() {

        catalogo = new LinkedHashMap<>();

        juegoService = new JuegoService();
        dlcService = new DLCservice();

        cargarCatalogo();
    }

    // Cargar los productos desde los servicios
    private void cargarCatalogo() {

        for (Juego juego : juegoService.listarTodos()) {

            if (catalogo.containsKey(juego.getId())) {
                throw new IllegalStateException(
                        "Existe un ID de producto duplicado: " + juego.getId()
                );
            }

            catalogo.put(juego.getId(), juego);
        }

        for (DLC dlc : dlcService.listarTodos()) {

            if (catalogo.containsKey(dlc.getId())) {
                throw new IllegalStateException(
                        "Existe un ID de producto duplicado: " + dlc.getId()
                );
            }

            catalogo.put(dlc.getId(), dlc);
        }
    }

    // Agregar un producto
    public boolean agregarProducto(Producto producto) {

        if (producto == null || producto.getId() != 0) {
            return false;
        }

        Desarrollador desarrollador = producto.getDesarrollador();

        if (desarrollador == null || !desarrollador.getEstado()) {
            return false;
        }

        boolean agregado = false;

        if (producto instanceof Juego) {

            agregado = juegoService.agregar((Juego) producto);

        } else if (producto instanceof DLC) {

            agregado = dlcService.agregar((DLC) producto);
        }

        if (!agregado) {
            return false;
        }

        // Comprobamos que el ID generado no esté ocupado
        if (catalogo.containsKey(producto.getId())) {
            throw new IllegalStateException(
                    "El ID generado ya pertenece a otro producto"
            );
        }

        // Publicamos el producto en el catálogo del desarrollador
        if (!desarrollador.publicarProducto(producto)) {
            throw new IllegalStateException(
                    "No se pudo publicar el producto en el catálogo del desarrollador"
            );
        }

        catalogo.put(producto.getId(), producto);

        return true;
    }

    // Buscar producto por ID
    public Producto buscarProductoPorID(int id) {

        if (id <= 0) {
            return null;
        }

        return catalogo.get(id);
    }

    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {

        List<Producto> encontrados = new ArrayList<>();

        if (nombre == null || nombre.isBlank()) {
            return encontrados;
        }

        for (Producto producto : catalogo.values()) {

            if (producto.getNombre().toLowerCase()
                    .contains(nombre.toLowerCase())) {

                encontrados.add(producto);
            }
        }

        return encontrados;
    }

    // Obtener el catálogo completo
    public List<Producto> obtenerCatalogo() {

        return new ArrayList<>(catalogo.values());
    }

    // Dar de baja un producto
    public boolean darDeBajaProducto(int id) {

        Producto producto = buscarProductoPorID(id);

        if (producto == null || !producto.isEstado()) {
            return false;
        }

        boolean resultado = false;

        if (producto instanceof Juego) {

            resultado = juegoService.darDeBaja(id);

        } else if (producto instanceof DLC) {

            resultado = dlcService.darDeBaja(id);
        }

        if (resultado) {
            producto.darDeBaja();
        }

        return resultado;
    }

    // Reactivar un producto
    public boolean reactivarProducto(int id) {

        Producto producto = buscarProductoPorID(id);

        if (producto == null || producto.isEstado()) {
            return false;
        }

        boolean resultado = false;

        if (producto instanceof Juego) {

            resultado = juegoService.darDeAlta(id);

        } else if (producto instanceof DLC) {

            resultado = dlcService.darDeAlta(id);
        }

        if (resultado) {
            producto.darDeAlta();
        }

        return resultado;
    }

    // Obtener solamente los productos activos
    public List<Producto> obtenerProductosActivos() {

        List<Producto> activos = new ArrayList<>();

        for (Producto producto : catalogo.values()) {

            if (producto.isEstado()) {
                activos.add(producto);
            }
        }

        return activos;
    }

    // Modificar un producto
    public boolean modificarProducto(Producto producto) {

        if (producto == null || producto.getId() <= 0) {
            return false;
        }

        if (!catalogo.containsKey(producto.getId())) {
            return false;
        }

        boolean resultado = false;

        if (producto instanceof Juego) {

            resultado = juegoService.modificar((Juego) producto);

        } else if (producto instanceof DLC) {

            resultado = dlcService.modificar((DLC) producto);
        }

        if (resultado) {
            catalogo.put(producto.getId(), producto);
        }

        return resultado;
    }

    // Eliminar físicamente un producto
    public boolean eliminarProducto(int id) {

        Producto producto = buscarProductoPorID(id);

        if (producto == null) {
            return false;
        }

        boolean eliminado = false;

        if (producto instanceof Juego) {

            eliminado = juegoService.eliminar(id);

        } else if (producto instanceof DLC) {

            eliminado = dlcService.eliminar(id);
        }

        if (eliminado) {
            catalogo.remove(id);
        }

        return eliminado;
    }

    // =====================================================
    //             IMPLEMENTACIÓN DE iControlJuegos
    // =====================================================

    // En esta clase el alta representa publicar un producto nuevo.
    @Override
    public boolean darAltaJuego(Juego juego) {
        return agregarProducto(juego);
    }

    @Override
    public boolean darAltaDLC(DLC dlc) {
        return agregarProducto(dlc);
    }

    @Override
    public boolean darBajaJuego(int id) {
        if (!(buscarProductoPorID(id) instanceof Juego)) {
            return false;
        }
        return darDeBajaProducto(id);
    }

    @Override
    public boolean darBajaDLC(int id) {
        if (!(buscarProductoPorID(id) instanceof DLC)) {
            return false;
        }
        return darDeBajaProducto(id);
    }

    @Override
    public boolean modificarJuego(Juego juego) {
        if (juego == null ||
                !(buscarProductoPorID(juego.getId()) instanceof Juego)) {
            return false;
        }
        return modificarProducto(juego);
    }

    @Override
    public boolean modificarDLC(DLC dlc) {
        if (dlc == null ||
                !(buscarProductoPorID(dlc.getId()) instanceof DLC)) {
            return false;
        }
        return modificarProducto(dlc);
    }

}
