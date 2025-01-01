package Modelo;

import java.util.ArrayList;
import java.util.Date;

public class Evento {

    private String nombre;
    private String descripcion;
    private Date fecha;
    private String lugar;
    private int cantidadParticipantesMaxima;
    private ArrayList<String> participantes;  // Ahora son ArrayList de RUTs (String)
    private ArrayList<String> asistentes;     // Ahora son ArrayList de RUTs (String)
    private String organizacion;

    // Constructor
    public Evento(String nombre, String descripcion, Date fecha, String lugar,
            int cantidadParticipantesMaxima, String organizacion) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setFecha(fecha);
        setLugar(lugar);
        setCantidadParticipantesMaxima(cantidadParticipantesMaxima);
        setOrganizacion(organizacion);
        this.participantes = new ArrayList<>();
        this.asistentes = new ArrayList<>();  
    }

    // Métodos setter con validación
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío ni nulo.");
        }
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser vacía ni nula.");
        }
        this.descripcion = descripcion;
    }

    public void setFecha(Date fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.fecha = fecha;
    }

    public void setLugar(String lugar) {
        if (lugar == null || lugar.trim().isEmpty()) {
            throw new IllegalArgumentException("El lugar no puede ser vacío ni nulo.");
        }
        this.lugar = lugar;
    }

    public void setCantidadParticipantesMaxima(int cantidadParticipantesMaxima) {
        if (cantidadParticipantesMaxima <= 0) {
            throw new IllegalArgumentException("La cantidad de participantes máxima debe ser mayor que 0.");
        }
        this.cantidadParticipantesMaxima = cantidadParticipantesMaxima;
    }

    public void setOrganizacion(String organizacion) {
        if (organizacion == null || organizacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La organización no puede ser vacía ni nula.");
        }
        this.organizacion = organizacion;
    }

    // Métodos para gestionar los participantes (usando RUTs)
    public void agregarParticipante(String rut) {
        // Verificar si el usuario ya está en la lista de participantes
        if (participantes.contains(rut)) {
            throw new IllegalArgumentException("El usuario con RUT " + rut + " ya está en la lista de participantes.");
        }

        // Verificar si se ha alcanzado el límite máximo de participantes
        if (participantes.size() >= cantidadParticipantesMaxima) {
            throw new IllegalArgumentException("No se puede agregar más participantes. El evento ha alcanzado el límite máximo de participantes.");
        }

        // Agregar al participante si no hay problemas
        participantes.add(rut);
        System.out.println("Participante agregado con éxito: " + rut);
    }

    public boolean eliminarParticipante(String rut) {
        return participantes.remove(rut);
    }

    // Método para registrar asistencia (usando RUTs)
    public void registrarAsistencia(String rut) {
        // Verificar que el usuario esté en la lista de participantes
        if (!participantes.contains(rut)) {
            throw new IllegalArgumentException("El usuario con RUT " + rut + " no está en la lista de participantes.");
        }

        // Verificar que el usuario no esté ya en la lista de asistentes
        if (asistentes.contains(rut)) {
            throw new IllegalArgumentException("El usuario con RUT " + rut + " ya está registrado como asistente.");
        }

        // Agregar al usuario como asistente
        asistentes.add(rut);
        System.out.println("Asistencia registrada con éxito para el usuario: " + rut);
    }

    public boolean eliminarAsistencia(String rut) {
        return asistentes.remove(rut);
    }

    // Métodos setter para los ArrayList de participantes y asistentes (para la carga desde archivo)
    public void setParticipantes(ArrayList<String> participantes) {
        this.participantes = participantes;
    }

    public void setAsistentes(ArrayList<String> asistentes) {
        this.asistentes = asistentes;
    }

    // Métodos getter
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public int getCantidadParticipantesMaxima() {
        return cantidadParticipantesMaxima;
    }

    public ArrayList<String> getParticipantes() {
        return participantes;
    }

    public ArrayList<String> getAsistentes() {
        return asistentes;
    }

    public String getOrganizacion() {
        return organizacion;
    }
}