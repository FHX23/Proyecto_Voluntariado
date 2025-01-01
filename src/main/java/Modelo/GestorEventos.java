package Modelo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class GestorEventos {

    private ArrayList<Evento> listaEventos;

    public GestorEventos() {
        listaEventos = new ArrayList<Evento>();
        cargarEventosDesdeArchivo();
    }

    private void cargarEventosDesdeArchivo() {
    String rutaArchivo = "src/main/Datos/Eventos.txt";
    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    try {
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            archivo.getParentFile().mkdirs();
            archivo.createNewFile();
            System.out.println("Archivo 'Eventos.txt' no existía. Se ha creado para empezar a guardar eventos.");
            return; // Salir, ya que no hay eventos para cargar
        }

        ArrayList<String> lineas = (ArrayList<String>) Files.readAllLines(Paths.get(rutaArchivo));

        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length < 6) {
                System.err.println("Línea inválida en 'Eventos.txt': " + linea);
                continue;
            }

            String nombre = partes[0];
            String descripcion = partes[1];
            String lugar = partes[2];
            String organizacion = partes[3];
            int cantidadParticipantesMaxima;

            try {
                cantidadParticipantesMaxima = Integer.parseInt(partes[4]);
            } catch (NumberFormatException e) {
                System.err.println("Número inválido en línea: " + linea);
                continue;
            }

            Date fecha;
            try {
                fecha = formatoFecha.parse(partes[5]);
            } catch (Exception e) {
                System.err.println("Fecha inválida en línea: " + linea);
                continue;
            }

            Evento evento = new Evento(nombre, descripcion, fecha, lugar, cantidadParticipantesMaxima, organizacion);

            String[] participantes = (partes.length > 6) ? partes[6].split(";") : new String[0];
            String[] asistentes = (partes.length > 7) ? partes[7].split(";") : new String[0];

            for (String rut : participantes) {
                evento.agregarParticipante(rut);
            }
            for (String rut : asistentes) {
                evento.registrarAsistencia(rut);
            }

            listaEventos.add(evento);
        }

        System.out.println("Eventos cargados correctamente desde 'Eventos.txt'.");

    } catch (IOException e) {
        throw new IllegalArgumentException("Error al manejar el archivo 'Eventos.txt': " + e.getMessage());
    }
}

    // Obtener la lista de eventos
    public ArrayList<Evento> getListaEventos() {
        return listaEventos;
    }

    // Agregar un nuevo evento
    public void agregarEvento(Evento evento) {
        // Verificar que no exista un evento con el mismo nombre y fecha
        for (Evento eventoExistente : listaEventos) {
            if (eventoExistente.getNombre().equals(evento.getNombre()) && eventoExistente.getFecha().equals(evento.getFecha())) {
                throw new IllegalArgumentException("Ya existe un evento con el mismo nombre y fecha.");
            }
        }

        listaEventos.add(evento);
        guardarEventoEnArchivo(evento);
        System.out.println("Evento agregado y guardado correctamente en 'Eventos.txt'.");
    }

    private void guardarEventoEnArchivo(Evento evento) {
        String rutaArchivo = "src/main/Datos/Eventos.txt";
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            // Convertir la lista de participantes y asistentes a cadenas separadas por punto y coma
            String participantes = String.join(";", evento.getParticipantes());
            String asistentes = String.join(";", evento.getAsistentes());

            // Formatear la fecha
            String fechaFormateada = formatoFecha.format(evento.getFecha());

            // Escribir el evento en el archivo, incluyendo los participantes y asistentes
            writer.write(evento.getNombre() + "," + evento.getDescripcion() + "," + evento.getLugar() + "," + evento.getOrganizacion()
                    + "," + evento.getCantidadParticipantesMaxima() + "," + fechaFormateada
                    + "," + participantes + "," + asistentes);
            writer.newLine(); // Para agregar un salto de línea después de cada evento
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al guardar el evento en 'Eventos.txt': " + e.getMessage());
        }
    }

    public void eliminarEvento(String nombreEvento, Date fechaEvento) {
        Evento eventoAEliminar = null;

        for (Evento evento : listaEventos) {
            if (evento.getNombre().equals(nombreEvento) && evento.getFecha().equals(fechaEvento)) {
                eventoAEliminar = evento;
                break;
            }
        }

        if (eventoAEliminar != null) {
            listaEventos.remove(eventoAEliminar);
            actualizarArchivoEventos();
            System.out.println("Evento eliminado correctamente.");
        } else {
            throw new IllegalArgumentException("No se encontró el evento con ese nombre y fecha.");
        }
    }

    // Actualizar el archivo después de modificar la lista de eventos
    private void actualizarArchivoEventos() {
        String rutaArchivo = "src/main/Datos/Eventos.txt";
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Escribir todos los eventos de nuevo en el archivo
            for (Evento evento : listaEventos) {
                // Formatear la fecha
                String fechaFormateada = formatoFecha.format(evento.getFecha());

                // Convertir la lista de participantes y asistentes a cadenas separadas por punto y coma
                String participantes = String.join(";", evento.getParticipantes());
                String asistentes = String.join(";", evento.getAsistentes());

                // Escribir el evento en el archivo, incluyendo los participantes y asistentes
                writer.write(evento.getNombre() + "," + evento.getDescripcion() + "," + evento.getLugar() + "," + evento.getOrganizacion()
                        + "," + evento.getCantidadParticipantesMaxima() + "," + fechaFormateada
                        + "," + participantes + "," + asistentes);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al actualizar el archivo 'Eventos.txt': " + e.getMessage());
        }
    }

    // Listar los eventos 
    public ArrayList<Evento> listarEventos() {
        return listaEventos;
    }

    // Listar los eventos de una organización
    public ArrayList<Evento> listarEventosPorOrganizacion(String organizacion) {
        ArrayList<Evento> eventosDeOrganizacion = new ArrayList<>();
        for (Evento evento : listaEventos) {
            if (evento.getOrganizacion().equals(organizacion)) {
                eventosDeOrganizacion.add(evento);
            }
        }
        return eventosDeOrganizacion;
    }

    public ArrayList<Evento> listarEventosPorNombre(String nombreFiltro) {
        ArrayList<Evento> eventosFiltrados = new ArrayList<>();

        for (Evento evento : listaEventos) {
            // Verificar si el nombre del evento contiene el filtro
            if (evento.getNombre().toLowerCase().contains(nombreFiltro.toLowerCase())) {
                eventosFiltrados.add(evento);
            }
        }

        return eventosFiltrados;
    }

    // Buscar un evento por su nombre
    public Evento buscarEventoPorNombre(String nombre) {
        for (Evento evento : listaEventos) {
            if (evento.getNombre().equals(nombre)) {
                return evento;
            }
        }
        throw new IllegalArgumentException("No se encontró el evento con ese nombre.");
    }

    public void agregarParticipanteAEvento(String nombreEvento, String rut) {
        // Buscar el evento por su nombre
        Evento evento = buscarEventoPorNombre(nombreEvento);

        // Validar si el evento existe
        if (evento == null) {
            throw new IllegalArgumentException("El evento con nombre '" + nombreEvento + "' no existe.");
        }

        // Obtener la fecha del evento encontrado
        Date fechaEvento = evento.getFecha();

        // Agregar al participante al evento
        evento.agregarParticipante(rut);

        // Actualizar la lista de eventos con el evento modificado
        for (int i = 0; i < listaEventos.size(); i++) {
            Evento eventoActual = listaEventos.get(i);
            if (eventoActual.getNombre().equals(nombreEvento) && eventoActual.getFecha().equals(fechaEvento)) {
                listaEventos.set(i, evento);
                actualizarArchivoEventos();
                break;
            }
        }
    }

    public void desinscribirParticipanteDeEvento(String nombreEvento, String rut) {
        // Buscar el evento por su nombre
        Evento evento = buscarEventoPorNombre(nombreEvento);

        // Validar si el evento existe
        if (evento == null) {
            throw new IllegalArgumentException("El evento con nombre '" + nombreEvento + "' no existe.");
        }

        // Obtener la fecha del evento encontrado
        Date fechaEvento = evento.getFecha();

        // Eliminar al participante del evento
        boolean eliminado = evento.eliminarParticipante(rut);

        // Si no se pudo eliminar al participante, lanzar una excepción
        if (!eliminado) {
            throw new IllegalArgumentException("El participante con RUT '" + rut + "' no está inscrito en el evento.");
        }

        // Actualizar la lista de eventos con el evento modificado
        for (int i = 0; i < listaEventos.size(); i++) {
            Evento eventoActual = listaEventos.get(i);
            if (eventoActual.getNombre().equals(nombreEvento) && eventoActual.getFecha().equals(fechaEvento)) {
                listaEventos.set(i, evento);
                actualizarArchivoEventos();
                break;
            }
        }
    }

    public void registrarAsistenciaAEvento(String nombreEvento, String rut) {
        // Buscar el evento por su nombre
        Evento evento = buscarEventoPorNombre(nombreEvento);

        // Validar si el evento existe
        if (evento == null) {
            throw new IllegalArgumentException("El evento con nombre '" + nombreEvento + "' no existe.");
        }

        // Obtener la fecha del evento encontrado
        Date fechaEvento = evento.getFecha();

        // Registrar la asistencia del participante
        evento.registrarAsistencia(rut);

        // Actualizar la lista de eventos con el evento modificado
        for (int i = 0; i < listaEventos.size(); i++) {
            Evento eventoActual = listaEventos.get(i);
            if (eventoActual.getNombre().equals(nombreEvento) && eventoActual.getFecha().equals(fechaEvento)) {
                listaEventos.set(i, evento);
                actualizarArchivoEventos();
                break;
            }
        }
    }

    public void generarReporteEventosPorOrganizacion(String organizacion) {
        // Crear la carpeta PDF si no existe
        File carpetaPDF = new File("src/main/PDF");
        if (!carpetaPDF.exists()) {
            carpetaPDF.mkdirs();
        }

        // Crear el archivo PDF en la carpeta PDF
        String rutaArchivo = "src/main/PDF/Reporte_" + organizacion + ".pdf";
        PDDocument documento = new PDDocument();

        try {
            // Crear una nueva página
            PDPage pagina = new PDPage();
            documento.addPage(pagina);

            // Crear el flujo de contenido para la página
            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);

            // Agregar título
            contenido.beginText();
            contenido.newLineAtOffset(50, 750);
            contenido.showText("Reporte de Eventos para la Organización: " + organizacion);
            contenido.endText();

            // Obtener los eventos de la organización
            ArrayList<Evento> eventos = listarEventosPorOrganizacion(organizacion);

            // Si no hay eventos, mostrar un mensaje
            if (eventos.isEmpty()) {
                contenido.beginText();
                contenido.newLineAtOffset(50, 720);
                contenido.setFont(PDType1Font.HELVETICA, 12);
                contenido.showText("No hay eventos registrados para la organización '" + organizacion + "'.");
                contenido.endText();
            } else {
                // Título de la tabla
                contenido.beginText();
                contenido.newLineAtOffset(50, 700);
                contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contenido.showText("Nombre | Fecha | Lugar | Cantidad Máxima | Participantes");
                contenido.endText();

                // Llenar la tabla con los datos de los eventos
                int yPosition = 680;
                contenido.setFont(PDType1Font.HELVETICA, 10);
                for (Evento evento : eventos) {
                    contenido.beginText();
                    contenido.newLineAtOffset(50, yPosition);
                    contenido.showText(evento.getNombre() + " | " + new SimpleDateFormat("yyyy-MM-dd").format(evento.getFecha()) + " | " + evento.getLugar()
                            + " | " + evento.getCantidadParticipantesMaxima() + " | " + evento.getParticipantes().size());
                    contenido.endText();
                    yPosition -= 15;
                }

                // Agregar estadísticas generales
                contenido.beginText();
                contenido.newLineAtOffset(50, yPosition - 20);
                contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contenido.showText("Estadísticas Generales:");
                contenido.endText();

                contenido.beginText();
                contenido.newLineAtOffset(50, yPosition - 40);
                contenido.setFont(PDType1Font.HELVETICA, 12);
                contenido.showText("Número total de eventos: " + eventos.size());
                contenido.endText();

                contenido.beginText();
                contenido.newLineAtOffset(50, yPosition - 60);
                contenido.showText("Total de participantes en todos los eventos: " + totalParticipantesEnEventos(eventos));
                contenido.endText();

                contenido.beginText();
                contenido.newLineAtOffset(50, yPosition - 80);
                contenido.showText("Total de asistentes en todos los eventos: " + totalAsistentesEnEventos(eventos));
                contenido.endText();
            }

            // Cerrar el flujo de contenido y guardar el documento
            contenido.close();
            documento.save(rutaArchivo);
            documento.close();

            System.out.println("Reporte generado correctamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
        }
    }

// Método para obtener el total de participantes en los eventos
    private int totalParticipantesEnEventos(ArrayList<Evento> eventos) {
        int total = 0;
        for (Evento evento : eventos) {
            total += evento.getParticipantes().size();
        }
        return total;
    }

// Método para obtener el total de asistentes en los eventos
    private int totalAsistentesEnEventos(ArrayList<Evento> eventos) {
        int total = 0;
        for (Evento evento : eventos) {
            total += evento.getAsistentes().size();
        }
        return total;
    }
}
