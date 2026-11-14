
package Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class Desarrollador extends Usuario {

    private final Set<Producto> catalogoPropio = new LinkedHashSet<>(); // Catálogo de productos publicados por este desarrollador.
    // final evita que se pueda reemplazar la colección completa.
    private double ganancias = 0.0; // Dinero acumulado por las ventas de los productos del desarrollador.
    private double valoracionGeneral = 0.0; // Valoración general del desarrollador, en una escala de 0 a 5.


    // CONSTRUCTORES


    // Constructor para crear un desarrollador nuevo.
    // Los atributos generales de la cuenta se inicializan mediante Usuario.
    public Desarrollador(String username, String contrasenia, String email, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento) {

        super(username, contrasenia, email, nacionalidad, nombre, apellido, fechaNacimiento);
    }


    // Constructor para reconstruir un desarrollador desde la base de datos.
    // Recibe también su catálogo, ganancias y valoración general.
    public Desarrollador(int id, String username, String contrasenia, String email, boolean estado, LocalDateTime creacionCuenta, String descripcion, String nacionalidad, String nombre, String apellido, LocalDate fechaNacimiento, Set<Producto> catalogoPropio, double ganancias, double valoracionGeneral) {

        // Inicializa los atributos heredados de Usuario.
        super(id, username, contrasenia, email, estado, creacionCuenta, descripcion, nacionalidad, nombre, apellido, fechaNacimiento);

        // Si no se recibe un catálogo, se conserva el catálogo vacío que ya fue inicializado en el atributo de la clase.
        if (catalogoPropio != null) {

            // si logra pasar este if, se entra en un bucle que se validan dos cosas:
            for (Producto producto : catalogoPropio) {

                // Primero valida si el producto hace referencia a un nulo o si ese producto no pertenece a su creador
                if (producto == null || !perteneceAEsteDesarrollador(producto)) {

                    throw new IllegalArgumentException(
                            "El catálogo contiene productos que no pertenecen al desarrollador"
                    );
                }

                // despues agrega el producto al catálogo. Si ya existe, add() devuelve false.
                if (!this.catalogoPropio.add(producto)) {

                    throw new IllegalArgumentException(
                            "El catálogo contiene productos duplicados"
                    );
                }
            }
        }

        // Utiliza los setters para validar las ganancias y la valoración recibidas.
        setGanancias(ganancias);
        setValoracionGeneral(valoracionGeneral);
    }

    public Desarrollador() {
        super();
    }


    // GETTERS Y SETTERS

    public Set<Producto> obtenerCatalogoPropio() {
        return Collections.unmodifiableSet(catalogoPropio); // Devuelve una vista de solo lectura del catálogo. Evita que otras clases puedan agregar o eliminar productos sin utilizar los métodos de Desarrollador.
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {

        // Comprueba que el valor sea un número infinito o no definido y no negativo.
        if (!Double.isFinite(ganancias) || ganancias < 0) {
            throw new IllegalArgumentException("Ganancias inválidas");
        }

        this.ganancias = ganancias;
    }

    public double getValoracionGeneral() {
        return valoracionGeneral;
    }

    // Modifica la valoración general. Es privado para evitar que otras clases puedan establecer libremente una valoración que debería calcularse a partir de las valoraciones recibidas.
    private void setValoracionGeneral(double valoracionGeneral) {

        // Comprueba que el valor sea un número infinito o no definido y que la valoracion este entre ese rango de numeros.
        if (!Double.isFinite(valoracionGeneral) || valoracionGeneral < 0 || valoracionGeneral > 5) {

            throw new IllegalArgumentException("Valoración inválida");
        }

        this.valoracionGeneral = valoracionGeneral;
    }


    // MÉTODOS AUXILIARES



    private boolean perteneceAEsteDesarrollador(Producto producto) { // Comprueba si un producto pertenece a este desarrollador.

        if (producto == null) { // primero revisa si la referencia a la que apunta el producto es nula
            return false;
        }

        Desarrollador propietario = producto.getDesarrollador();// Obtiene el desarrollador que publicó el producto.


        if (propietario == null) { // Comprueba que exista un propietario.
            return false;
        }

        if (propietario == this) { // Si ambos atributos apuntan al mismo objeto, sabemos que el producto pertenece a este desarrollador.

            return true;
        }

        // Si son objetos diferentes, comparamos sus ids. Solo se consideran validos para esta comparación los ids positivos, porque 0 representa que todavía no tiene un id asignado por la base de datos.
        return getId() > 0 && propietario.getId() > 0 && getId() == propietario.getId();
    }


    private Producto buscarProductoPropio(Producto productoBuscado) { // Devuelve el producto encontrado o null si no existe.

        if (productoBuscado == null) { // primero revisa si el producto buscado no apunte a null
            return null;
        }

        for (Producto producto : catalogoPropio) { // despues recorre el catalogo propio, para buscar ese producto

            if (producto.equals(productoBuscado)) { // Primero comprueba si se trata exactamente del mismo objeto.
                return producto;
            }


            if (productoBuscado.getId() > 0 && producto.getId() == productoBuscado.getId() && producto.getClass() == productoBuscado.getClass()) {
                // Si son objetos diferentes, comprueba si representan el mismo producto mediante su ID y su tipo de clase.
                // El ID debe ser positivo para evitar comparar productos que todavía no fueron persistidos.
                return producto;
            }
        }

        // si termina el recorrido sin encontrar nada que coincida, significa que el producto no está en el catálogo.
        return null;
    }




    // este metodo puede ser utilizado por Gestion_Tienda, para verificar los permisos del desarrollador.
    public boolean esPropietarioDe(Producto producto) { // comprueba que un producto pertenezca al desarrollador y que además se encuentre registrado en su catálogo propio.

        if (!perteneceAEsteDesarrollador(producto)) { // Primero verifica quién es el propietario del producto.
            return false;
        }

        // Despues comprueba que el producto este registrado dentro del catálogo propio.
        return buscarProductoPropio(producto) != null;
    }



    private boolean datosComunesValidos(Producto producto) { // Comprueba que los atributos comunes de un producto tengan valores válidos antes de modificarlo.

        return producto != null &&

                // El nombre no puede ser nulo ni estar vacío.
                producto.getNombre() != null &&
                !producto.getNombre().isBlank() &&

                // La descripción tampoco puede ser nula ni estar vacía.
                producto.getDescripcion() != null &&
                !producto.getDescripcion().isBlank() &&

                // El precio no debe ser infinito o desconocido y tampoco negativo.
                Double.isFinite(producto.getPrecio()) &&
                producto.getPrecio() >= 0 &&

                // El peso no debe ser infinito ni desconocido y estrictamente positivo.
                Double.isFinite(producto.getPesoGB()) &&
                producto.getPesoGB() > 0 &&

                // La fecha de lanzamiento no puede ser nula.
                producto.getFechaLanzamiento() != null;



    }

    private void aplicarDatosComunes(Producto actual, Producto modificado) { // Copia los atributos comunes de un producto modificado sobre el producto que ya se encuentra en el catálogo.

        // Actualiza los atributos heredados de Producto.
        actual.setNombre(modificado.getNombre());
        actual.setDescripcion(modificado.getDescripcion());
        actual.setPrecio(modificado.getPrecio());
        actual.setPesoGB(modificado.getPesoGB());
        actual.setFechaLanzamiento(modificado.getFechaLanzamiento());

        if (modificado.isEstado()) { // También actualiza el estado del producto.
            // De esta manera puede darse de alta o de baja durante una modificación.

            actual.darDeAlta();
        } else {

            actual.darDeBaja();
        }
    }


    // METODOS DEL CATALOGO PROPIO

    public boolean publicarProducto(Producto producto) { // Publica un producto y lo incorpora al catalogo propio.

        if (producto == null || !getEstado()) { // Comprueba que el producto no sea nulo y que la cuenta del desarrollador esté activa.
            return false;
        }

        if (!perteneceAEsteDesarrollador(producto)) { // Comprueba que el producto pertenezca a este desarrollador.
            return false;
        }


        return catalogoPropio.add(producto); // Agrega el producto al conjunto y devuelve false si ya se encontraba en el catalogo.
    }


    public boolean darDeBajaProducto(Producto producto) { // No elimina físicamente el producto de la colección.

        if (!getEstado() || !esPropietarioDe(producto)) { // El desarrollador debe tener una cuenta activa y el producto debe pertenecer a su catalogo.
            return false;
        }

        Producto productoActual = buscarProductoPropio(producto); // Recupera la referencia del producto almacenado para modificar el objeto que realmente está en el catalogo.

        productoActual.darDeBaja(); // Cambia su estado a inactivo.

        return true;
    }

    public boolean darDeAltaProducto(Producto producto) { // Reactiva un producto que se encontraba dado de baja.
                                                            // No agrega nuevamente el producto porque nunca fue eliminado fisicamente del catalogo.


        if (!getEstado() || !esPropietarioDe(producto)) { // Comprueba que el desarrollador esté activo y que el producto sea de su propiedad.
            return false;
        }

        Producto productoActual = buscarProductoPropio(producto); // Obtiene el producto almacenado en el catalogo.

        productoActual.darDeAlta(); // Cambia su estado a activo.

        return true;
    }


    // MODIFICAR JUEGO



    public boolean modificarJuego(Juego juegoModificado) { // Modifica los atributos de un Juego que ya se encuentra registrado en el catalogo propio.


        if (!getEstado() || !datosComunesValidos(juegoModificado)) { // Comprueba que el desarrollador este activo y que los atributos comunes del juego sean válidos.

            return false;
        }

        Producto producto = buscarProductoPropio(juegoModificado); // Busca el juego dentro del catálogo propio.

        if (!(producto instanceof Juego)) { // Comprueba que el producto encontrado sea un Juego.
            return false;
        }

        if (!perteneceAEsteDesarrollador(juegoModificado)) { // Comprueba que el juego modificado pertenezca al desarrollador que está realizando la operación.
            return false;
        }


        Juego juegoActual = (Juego) producto; // Convierte la referencia de Producto a Juego para acceder a los atributos específicos de esa clase.

        double duracion = juegoModificado.getDuracion(); // Obtiene la duración del juego.

        // Valida los atributos propios de Juego.
        if (juegoModificado.getGenero() == null ||

                // La edad mínima no puede ser negativa.
                juegoModificado.getEdadMinima() < 0 ||

                // Los requisitos mínimos no pueden ser nulos ni vacíos.
                juegoModificado.getRequisitosMinimos() == null || juegoModificado.getRequisitosMinimos().isBlank() ||

                // Los requisitos recomendados tampoco pueden ser nulos ni vacíos.
                juegoModificado.getRequisitosRecomendados() == null || juegoModificado.getRequisitosRecomendados().isBlank() ||

                // La duración no debe ser un número infinito o desconocido pero positivo.
                !Double.isFinite(duracion) || duracion <= 0) {

            return false;
        }


        aplicarDatosComunes(juegoActual, juegoModificado); // Actualiza los atributos heredados de Producto.

        // Actualiza los atributos específicos de Juego.
        juegoActual.setGenero(juegoModificado.getGenero());
        juegoActual.setDuracion(juegoModificado.getDuracion());
        juegoActual.setEdadMinima(juegoModificado.getEdadMinima());
        juegoActual.setRequisitosMinimos(juegoModificado.getRequisitosMinimos());
        juegoActual.setRequisitosRecomendados(juegoModificado.getRequisitosRecomendados());

        return true;
    }


    // MODIFICAR DLC



    public boolean modificarDLC(DLC dlcModificado) { // Modifica los atributos de un DLC que ya se encuentra registrado en el catalogo propio.

        if (!getEstado() || !datosComunesValidos(dlcModificado)) { // Comprueba que el desarrollador este activo y que los atributos comunes del DLC sean validos.

            return false;
        }

        Producto producto = buscarProductoPropio(dlcModificado); // Busca el DLC dentro del catálogo propio.

        if (!(producto instanceof DLC)) { // Comprueba que el producto encontrado sea un DLC.
            return false;
        }

        if (!perteneceAEsteDesarrollador(dlcModificado)) { // Comprueba que el DLC pertenezca a este desarrollador.
            return false;
        }


        DLC dlcActual = (DLC) producto; // Convierte la referencia de Producto a DLC para acceder a sus atributos específicos.


        if (dlcModificado.getContenido() == null || dlcModificado.getJuego() == null) // Comprueba que el DLC tenga un tipo de contenido y que esté asociado a un juego.
        {
            return false;
        }

        // Modifica la relación entre el DLC y su juego. Se utiliza cambiarJuego() para mantener sincronizada la relación en ambos objetos.
        if (!dlcActual.cambiarJuego(dlcModificado.getJuego()))
        {
            return false; // Si no se puede establecer la relación, cancela la operación.
        }

        aplicarDatosComunes(dlcActual, dlcModificado); // Actualiza los atributos heredados de Producto.

        dlcActual.setContenido(dlcModificado.getContenido()); // Actualiza el atributo específico de DLC.

        return true;
    }


    // MODIFICAR CUALQUIER PRODUCTO



    public boolean modificarProducto(Producto productoModificado) { // Permite modificar un producto sin necesidad de conocer previamente si se trata de un Juego o de un DLC.

        if (productoModificado == null) { // Comprueba que no se haya recibido una referencia hacia un nulo.
            return false;
        }

        if (productoModificado instanceof Juego juego) { // Si el producto es un Juego, delega su modificacion en el metodo específico de esa clase.
            return modificarJuego(juego);
        }

        if (productoModificado instanceof DLC dlc) { // Si el producto es un DLC, delega su modificacion en el metodo específico de esa clase.
            return modificarDLC(dlc);
        }

        return false; // si resulta que no es ninguna de las 2 cosas devuelve falso
    }
}
