package Controlador;

import Modelo.*;
import Vista.*;

import javax.swing.*;

public class ControladorHome {
    private GestorUsuario modelUser;
    private GestorEventos modelEvento;
    private VistaHome vistaHome;
    private VistaUsuario vistaUsuario;
    private VistaOrganizacion vistaOrganizacion;

    public ControladorHome(GestorUsuario modelUser, GestorEventos modelEvento, VistaHome vistaHome) {
        this.modelUser = modelUser;
        this.modelEvento = modelEvento;
        this.vistaHome = vistaHome;

        // Inicializar vistas adicionales
        this.vistaUsuario = new VistaUsuario();
        this.vistaOrganizacion = new VistaOrganizacion();

        // Configurar los listeners para los botones en VistaHome
        this.vistaHome.getBtnLogin().addActionListener(e -> logearse());
        this.vistaHome.getBtnRegistrarse().addActionListener(e -> mostrarVistaRegistrarse());
    }

    public void inicializar() {
        vistaHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaHome.pack();
        vistaHome.setVisible(true);
    }

    private void logearse() {
        String rut = this.vistaHome.getTxtRUT().getText();
        String contraseña = this.vistaHome.getTxtContraseña().getText();
        try {
            // Intentar autenticar al usuario
            this.modelUser.setUsuarioActual(this.modelUser.autenticarUsuario(rut, contraseña));

            String tipoUsuario = this.modelUser.getUsuarioActual().getTipoUsuario();

            // Si no se lanza ninguna excepción, el login fue exitoso
            JOptionPane.showMessageDialog(
                    null,
                    "Inicio de sesión exitoso. Bienvenido, " + this.modelUser.getUsuarioActual().getNombre() + "!",
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Mostrar una vista diferente según el tipo de usuario
            if (tipoUsuario.equals("user")) {
                // Crear el controlador para usuarios
                ControladorUsuario controladorUsuario = new ControladorUsuario(
                        modelUser,
                        modelEvento,
                        vistaUsuario
                );
                controladorUsuario.inicializar(); // Inicializar la vista de usuario
            } else if (tipoUsuario.equals("organizacion")) {
                // Crear el controlador para organizaciones
                ControladorOrganizacion controladorOrganizacion = new ControladorOrganizacion(
                        modelUser,
                        modelEvento,
                        vistaOrganizacion
                );
                controladorOrganizacion.inicializar(); // Inicializar la vista de organización
            }

            // Ocultar la vista principal
            vistaHome.setVisible(false);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error de login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarVistaRegistrarse() {
        VistaRegistrarse vistaRegistrarse = new VistaRegistrarse();
        ControladorRegistrarse controladorRegistrarse = new ControladorRegistrarse(modelUser, vistaRegistrarse);

        vistaHome.setVisible(false);
        controladorRegistrarse.inicializar(() -> {
            vistaHome.setVisible(true);
        });
    }
}
