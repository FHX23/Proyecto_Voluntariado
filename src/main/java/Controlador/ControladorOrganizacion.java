package Controlador;

import Modelo.*;
import Vista.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ControladorOrganizacion {

    private GestorUsuario modelUser;
    private GestorEventos modelEvento;
    private VistaOrganizacion vistaOrganizacion;
    private VistaCrearEvento vistaCrearEvento;
    private VistaListarYEliminar vistaListarYEliminar;
    private VistaAsistencia vistaAsistencia;

    public ControladorOrganizacion(GestorUsuario modelUser, GestorEventos modelEvento, VistaOrganizacion vistaOrganizacion) {
        this.modelUser = modelUser;
        this.modelEvento = modelEvento;
        this.vistaOrganizacion = vistaOrganizacion;

        // Configurar los eventos de los botones en la vista
        this.vistaOrganizacion.getBtnCrearEvento().addActionListener(e -> mostrarVistaCrearEvento());
        this.vistaOrganizacion.getBtnListarYEliminar().addActionListener(e -> mostrarVistaListarYEliminar());
        this.vistaOrganizacion.getBtnAsistencia().addActionListener(e -> mostrarVistaAsistencia());
        this.vistaOrganizacion.getBtnPDF().addActionListener(e -> generarPDF());
    }

    public void inicializar() {
        vistaOrganizacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaOrganizacion.pack();
        vistaOrganizacion.setVisible(true);
    }

    private void mostrarVistaCrearEvento() {
        // Inicializar la vista de crear evento si no está creada
        if (vistaCrearEvento == null) {
            vistaCrearEvento = new VistaCrearEvento();
            vistaCrearEvento.getBtnCrearEvento().addActionListener(e -> crearEvento());
            vistaCrearEvento.getBtnVolver().addActionListener(e -> volverAVistaOrganizacion(vistaCrearEvento));
        }

        // Ocultar la vista de organización y mostrar la de crear evento
        vistaOrganizacion.setVisible(false);
        vistaCrearEvento.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaCrearEvento.pack();
        vistaCrearEvento.setVisible(true);
    }

    private void mostrarVistaAsistencia() {
        // Verifica si la vista de asistencia ya fue inicializada
        if (vistaAsistencia == null) {
            vistaAsistencia = new VistaAsistencia();

            // Configurar los listeners de los botones
            vistaAsistencia.getBtnRegistrar().addActionListener(e -> registrarAsistencia());
            vistaAsistencia.getBtnVolver().addActionListener(e -> volverAVistaOrganizacion(vistaAsistencia));
        }

        // Ocultar la vista de organización y mostrar la de asistencia
        vistaOrganizacion.setVisible(false);
        vistaAsistencia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaAsistencia.pack();
        vistaAsistencia.setVisible(true);
    }

    private void mostrarVistaListarYEliminar() {
        // Inicializar la vista de listar y eliminar si no está creada
        if (vistaListarYEliminar == null) {
            vistaListarYEliminar = new VistaListarYEliminar();
            vistaListarYEliminar.getBtnBorrar().addActionListener(e -> eliminarEvento());
            vistaListarYEliminar.getBtnVolver().addActionListener(e -> volverAVistaOrganizacion(vistaListarYEliminar));
        }

        // Actualizar los datos de la tabla antes de mostrar la vista
        mostrarEventosOrganizacion();

        // Ocultar la vista de organización y mostrar la de listar y eliminar
        vistaOrganizacion.setVisible(false);
        vistaListarYEliminar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaListarYEliminar.pack();
        vistaListarYEliminar.setVisible(true);
    }

    private void generarPDF() {
        // Obtener el nombre de la organización
        String organizacion = modelUser.getUsuarioActual().getRut();

        try {
            // Llamar al método en el modelo para generar el reporte en PDF
            modelEvento.generarReporteEventosPorOrganizacion(organizacion);

            // Si no hay errores, mostrar mensaje de éxito
            JOptionPane.showMessageDialog(
                    null,
                    "El reporte ha sido generado exitosamente.",
                    "Reporte Generado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            // Si ocurre un error, mostrar mensaje de error
            JOptionPane.showMessageDialog(
                    null,
                    "Hubo un error al generar el reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void crearEvento() {
        try {
            // Leer los datos de la vista
            String nombre = vistaCrearEvento.getTxtNombre().getText();
            String descripcion = vistaCrearEvento.getTxtAreaDescipcion().getText();
            String lugar = vistaCrearEvento.getTxtLugar().getText();
            String organizacion = modelUser.getUsuarioActual().getRut(); // Usar el RUT del usuario actual
            int cantidadParticipantesMaxima = Integer.parseInt(vistaCrearEvento.getTxtCantidadParticipantes().getText());

            // Validar y convertir la fecha desde el JTextField
            String fechaStr = vistaCrearEvento.getTxtFecha().getText();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatoFecha.parse(fechaStr);

            // Crear el objeto Evento
            Evento nuevoEvento = new Evento(nombre, descripcion, fecha, lugar, cantidadParticipantesMaxima, organizacion);

            // Agregar el evento al modelo
            modelEvento.agregarEvento(nuevoEvento);
            System.out.println("Evento creado con éxito.");

            // Mostrar mensaje de confirmación
            JOptionPane.showMessageDialog(
                    null,
                    "El evento '" + nombre + "' ha sido creado exitosamente.",
                    "Evento Creado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            volverAVistaOrganizacion(vistaCrearEvento); // Volver a la vista de organización
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "La cantidad máxima de participantes debe ser un número válido.",
                    "Error en los datos",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "La fecha debe tener el formato yyyy-MM-dd.",
                    "Error en los datos",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error al crear el evento",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarEvento() {
        try {
            String nombreEvento = vistaListarYEliminar.getTxtNombre().getText();
            String fechaEventoStr = vistaListarYEliminar.getTxtFecha().getText();

            // Convertir la fecha desde String a Date
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaEvento = formatoFecha.parse(fechaEventoStr);

            // Llamar al método para eliminar el evento
            modelEvento.eliminarEvento(nombreEvento, fechaEvento);
            JOptionPane.showMessageDialog(
                    null,
                    "Evento eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Actualizar la tabla después de eliminar
            mostrarEventosOrganizacion();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al convertir la fecha. Asegúrate de usar el formato yyyy-MM-dd.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void registrarAsistencia() {
        try {
            // Leer datos ingresados en la vista
            String nombreEvento = vistaAsistencia.getTxtNombre().getText(); // Campo de texto para el nombre del evento
            String fechaEventoStr = vistaAsistencia.getTxtFecha().getText(); // Campo de texto para la fecha del evento (no se usa directamente)
            String rut = vistaAsistencia.getTxtRut().getText(); // Campo de texto para el RUT

            // Validar que los campos no estén vacíos
            if (nombreEvento.isEmpty() || fechaEventoStr.isEmpty() || rut.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            // Buscar el evento por su nombre
            Evento evento = modelEvento.buscarEventoPorNombre(nombreEvento);

            // Validar si el evento existe
            if (evento == null) {
                throw new IllegalArgumentException("No se encontró un evento con el nombre especificado.");
            }

            // Verificar si el RUT está en la lista de participantes
            if (!evento.getParticipantes().contains(rut)) {
                throw new IllegalArgumentException("El RUT ingresado no está registrado como participante de este evento.");
            }

            // Verificar si el RUT ya está en la lista de asistentes
            if (evento.getAsistentes().contains(rut)) {
                throw new IllegalArgumentException("Este participante ya está registrado como asistente.");
            }

            // Registrar al RUT en la lista de asistentes
            modelEvento.registrarAsistenciaAEvento(evento.getNombre(), rut);

            // Mostrar mensaje de confirmación
            JOptionPane.showMessageDialog(
                    null,
                    "Asistencia registrada con éxito para el participante con RUT: " + rut,
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            vistaAsistencia.getTxtRut().setText("");

        } catch (IllegalArgumentException e) {
            // Mostrar mensaje de error si algo falla
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error al registrar asistencia",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarEventosOrganizacion() {
        String organizacionActual = modelUser.getUsuarioActual().getRut();
        var eventos = modelEvento.listarEventosPorOrganizacion(organizacionActual);

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

        vistaListarYEliminar.updateTableData(tableData, columnNames);
    }

    private void volverAVistaOrganizacion(JFrame vistaActual) {
        vistaActual.setVisible(false);
        vistaOrganizacion.setVisible(true);
    }
}
