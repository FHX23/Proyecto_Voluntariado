package Controlador;

import Modelo.*;
import Vista.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ControladorUsuario {

    private GestorUsuario modelUser;
    private GestorEventos modelEvento;
    private VistaUsuario vistaUsuario;

    public ControladorUsuario(GestorUsuario modelUser, GestorEventos modelEvento, VistaUsuario vistaUsuario) {
        this.modelUser = modelUser;
        this.modelEvento = modelEvento;
        this.vistaUsuario = vistaUsuario;

        // Configurar listeners
        vistaUsuario.getBtnAccion().addActionListener(e -> manejarAccion());
        vistaUsuario.getBtnFiltrar().addActionListener(e -> manejarAccion());
    }

    public void inicializar() {
        vistaUsuario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaUsuario.pack();
        vistaUsuario.setVisible(true);
        mostrarEventosDisponibles();
        // Mostrar eventos disponibles al iniciar
        mostrarEventosDisponibles();
    }

    public void mostrarFiltrado() {
    String nombreFiltro = vistaUsuario.getTxtNombreFiltro().getText().trim();

    if (nombreFiltro.isEmpty()) {
        // Si el campo está vacío, mostrar todos los eventos disponibles
        mostrarEventosDisponibles();
    } else {
        // Si hay un texto en el campo, filtrar los eventos por nombre
        List<Evento> eventosFiltrados = modelEvento.listarEventosPorNombre(nombreFiltro);

        String[] columnNames = {"Nombre", "Fecha", "Lugar", "Descripción", "Cantidad Máxima de Participantes"};
        String[][] tableData = new String[eventosFiltrados.size()][5];

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < eventosFiltrados.size(); i++) {
            Evento evento = eventosFiltrados.get(i);
            tableData[i][0] = evento.getNombre();
            tableData[i][1] = formatoFecha.format(evento.getFecha());
            tableData[i][2] = evento.getLugar();
            tableData[i][3] = evento.getDescripcion();
            tableData[i][4] = String.valueOf(evento.getCantidadParticipantesMaxima());
        }

        // Actualizar la tabla en la vista con los eventos filtrados
        vistaUsuario.updateTableData(tableData, columnNames);
    }
}
    
    private void mostrarEventosDisponibles() {
        // Obtener todos los eventos disponibles
        List<Evento> eventos = modelEvento.getListaEventos();

        String[] columnNames = {"Nombre", "Fecha", "Lugar", "Descripción", "Cantidad Máxima de Participantes"};
        String[][] tableData = new String[eventos.size()][5];

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < eventos.size(); i++) {
            Evento evento = eventos.get(i);
            tableData[i][0] = evento.getNombre();
            tableData[i][1] = formatoFecha.format(evento.getFecha());
            tableData[i][2] = evento.getLugar();
            tableData[i][3] = evento.getDescripcion();
            tableData[i][4] = String.valueOf(evento.getCantidadParticipantesMaxima());
        }

        // Actualizar la tabla en la vista
        vistaUsuario.updateTableData(tableData, columnNames);
    }

    private void manejarAccion() {
        try {
            // Leer el nombre del evento desde el campo de texto
            String nombreEvento = vistaUsuario.getTxtNombre().getText();

            // Validar que no esté vacío
            if (nombreEvento.isEmpty()) {
                throw new IllegalArgumentException("El nombre del evento no puede estar vacío.");
            }

            // Obtener el evento desde el modelo
            Evento evento = modelEvento.buscarEventoPorNombre(nombreEvento);
            if (evento == null) {
                throw new IllegalArgumentException("No se encontró un evento con el nombre especificado.");
            }

            // Verificar qué acción desea realizar el usuario
            if (vistaUsuario.getBtnRadioRegistrar().isSelected()) {
                // Registrar al usuario como participante del evento
                modelEvento.agregarParticipanteAEvento(evento.getNombre(), modelUser.getUsuarioActual().getRut());
                JOptionPane.showMessageDialog(
                    null,
                    "Te has registrado exitosamente en el evento: " + nombreEvento,
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else if (vistaUsuario.getBtnRadioDesincribirse().isSelected()) {
                // Desinscribir al usuario del evento
                modelEvento.desinscribirParticipanteDeEvento(evento.getNombre(), modelUser.getUsuarioActual().getRut());
                JOptionPane.showMessageDialog(
                    null,
                    "Te has desinscrito exitosamente del evento: " + nombreEvento,
                    "Desinscripción Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                throw new IllegalArgumentException("Debes seleccionar una acción: Registrarse o Desinscribirse.");
            }

            // Actualizar la tabla de eventos
            mostrarEventosDisponibles();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
