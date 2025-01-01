package paradigmas.proyecto_voluntariado;
import Modelo.*;
import Vista.*;
import Controlador.*;

public class Proyecto_Voluntariado {

    public static void main(String[] args) {
        
        GestorUsuario modelGestorUsuario = new GestorUsuario();
        GestorEventos modelGestorEvento = new GestorEventos();

        VistaHome vistaHome = new VistaHome();
        
        ControladorHome controlador = new ControladorHome(modelGestorUsuario,modelGestorEvento,vistaHome);

        controlador.inicializar();
    }
}
