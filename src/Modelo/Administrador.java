

package Modelo;

import Interfaces.iControlJuegos;
import Modelo.DLC;
import Modelo.Juego;
import Service.DLCservice;
import Service.JuegoService;

    public final class Administrador extends Gestores implements iControlJuegos
    {

        private final JuegoService juegoService;
        private final DLCservice dlcService;

        public Administrador(String contrasenia, String pin) {
            super(contrasenia, pin);
            this.juegoService = new JuegoService();
            this.dlcService = new DLCservice();
        }

        public Administrador() {
            super();
            this.juegoService = new JuegoService();
            this.dlcService = new DLCservice();
        }

        public Administrador(int id, String contrasenia, String pin) {
            super(id, contrasenia, pin);
            this.juegoService = new JuegoService();
            this.dlcService = new DLCservice();
        }

        // Métodos propios del administrador

        public void realizarBackup() {
            System.out.println("Backup realizado correctamente (operación simulada).");
        }

        public void restaurarBackup() {
            System.out.println("Restauración realizada correctamente (operación simulada).");
        }

        // Métodos de la interfaz iControlJuegos

        @Override
        public boolean darAltaJuego(Juego juego) {
            return juegoService.agregar(juego);
        }

        @Override
        public boolean darAltaDLC(DLC dlc) {
            return dlcService.agregar(dlc);
        }

        @Override
        public boolean darBajaJuego(int id) {
            return juegoService.darDeBaja(id);
        }

        @Override
        public boolean darBajaDLC(int id) {
            return dlcService.darDeBaja(id);
        }

        @Override
        public boolean modificarJuego(Juego juego) {
            return juegoService.modificar(juego);
        }

        @Override
        public boolean modificarDLC(DLC dlc) {
            return dlcService.modificar(dlc);
        }
    }
