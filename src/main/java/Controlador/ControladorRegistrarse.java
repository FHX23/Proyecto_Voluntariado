package Controlador;

import Modelo.GestorUsuario;
import Modelo.Usuario;
import Vista.VistaRegistrarse;

import javax.swing.*;

public class ControladorRegistrarse {
    private GestorUsuario modelUser;
    private VistaRegistrarse vistaRegistrarse;

    public ControladorRegistrarse(GestorUsuario modelUser, VistaRegistrarse vistaRegistrarse) {
        this.modelUser = modelUser;
        this.vistaRegistrarse = vistaRegistrarse;

        // Configurar los listeners para los botones en VistaRegistrarse
        this.vistaRegistrarse.getBtnVolver().addActionListener(e -> volver());
        this.vistaRegistrarse.getBtnRegistrarse().addActionListener(e -> registrarse());
    }

    public void inicializar(Runnable callbackVolver) {
        vistaRegistrarse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaRegistrarse.pack();
        vistaRegistrarse.setVisible(true);
        this.callbackVolver = callbackVolver;
    }

    private Runnable callbackVolver;

    private void volver() {
        vistaRegistrarse.setVisible(false);
        if (callbackVolver != null) callbackVolver.run();
    }

    private void registrarse() {
        String rut = vistaRegistrarse.getTxtRUT().getText();
        String nombre = vistaRegistrarse.getTxtNombre().getText();
        String contraseña = vistaRegistrarse.getTxtContraseña().getText();

        String tipoUsuario;
        if (vistaRegistrarse.getBtnRadioUser().isSelected()) {
            tipoUsuario = "user";
        } else if (vistaRegistrarse.getBtnRadioOrganizacion().isSelected()) {
            tipoUsuario = "organizacion";
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe seleccionar un tipo de usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Usuario newUser = new Usuario(nombre, contraseña, tipoUsuario, rut);
            modelUser.agregarUsuario(newUser);

            JOptionPane.showMessageDialog(
                    null,
                    "Usuario registrado exitosamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            volver();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error al registrar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
