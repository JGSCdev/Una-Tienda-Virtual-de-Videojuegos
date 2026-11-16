// Santiago Vazquez
// Ian Gosssela
// Jesús Gonzalo Silva Correa

import GUI.Menu;


public class Main {
    public static void main(String[] args) {
        try {
            Menu menu = new Menu();
            menu.iniciar();
        } catch (Exception e) {
            System.out.println("No se pudo iniciar la tienda: " + e.getMessage());
            e.printStackTrace();
        }    }
}

