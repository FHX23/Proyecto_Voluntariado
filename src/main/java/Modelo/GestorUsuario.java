package Modelo;

import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;

public class GestorUsuario {

    private ArrayList<Usuario> listaUsuarios;
    private Usuario usuarioActual;
    public GestorUsuario() {
        listaUsuarios = new ArrayList();
        cargarUsuariosDesdeArchivo();
    }

    private void cargarUsuariosDesdeArchivo() {
        String rutaArchivo = "src/main/Datos/Usuarios.txt";

        try {
            File archivo = new File(rutaArchivo);

            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                System.out.println("Archivo 'Usuarios.txt' no existía. Se ha creado para empezar a guardar usuarios.");
                // Crear un usuario por defecto si el archivo no existe
                Usuario usuarioPorDefecto = new Usuario("Administrador", "123", "admin", "12345678-5");
                System.out.println("Se creo usuario admin por defecto");
                listaUsuarios.add(usuarioPorDefecto); // Agregar el usuario por defecto a la lista
                return; // Salir, ya que no hay datos para cargar
            }

            // Leer las líneas del archivo
            ArrayList<String> lineas = (ArrayList<String>) Files.readAllLines(Paths.get(rutaArchivo));

            // Procesar cada línea y agregar a la lista
            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    String nombre = partes[0];
                    String contraseña = partes[1];
                    String tipo = partes[2];
                    String rut = partes[3];

                    // Crear un objeto Usuario y agregarlo a la lista
                    Usuario usuario = new Usuario(nombre, tipo, rut);//constructor sin contraseña
                    usuario.setContraseñaHasheada(contraseña); // no rehasear contraseña
                    listaUsuarios.add(usuario);
                } else {
                    System.out.println("Línea con formato incorrecto: " + linea);
                }
            }

            System.out.println("Usuarios cargados correctamente desde 'Usuarios.txt'.");

        } catch (IOException e) {
            System.err.println("Error al manejar el archivo 'Usuarios.txt': " + e.getMessage());
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void agregarUsuario(Usuario user) {
        // Verificar si el RUT ya existe en la lista
        for (Usuario usuarioExistente : listaUsuarios) {
            if (usuarioExistente.getRut().equals(user.getRut())) {
                throw new IllegalArgumentException("El RUT ya existe en el sistema.");
            }
        }

        listaUsuarios.add(user);
        guardarUsuarioEnArchivo(user);

        System.out.println("Usuario agregado y guardado correctamente en 'Usuarios.txt'.");
    }

    private void guardarUsuarioEnArchivo(Usuario user) {
        String rutaArchivo = "src/main/Datos/Usuarios.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            // Escribir el nuevo usuario en el archivo, con formato: nombre,contraseña,tipo usuario,rut
            writer.write(user.getNombre() + "," + user.getContraseña() + "," + user.getTipoUsuario() + "," + user.getRut());
            writer.newLine(); // Para agregar un salto de línea después de cada usuario
        } catch (IOException e) {
            System.err.println("Error al guardar el usuario en 'Usuarios.txt': " + e.getMessage());
        }
    }

    public Usuario autenticarUsuario(String rut, String contraseña) {
        // Validar que los campos no estén vacíos
        if (rut == null || rut.trim().isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas. Por favor, intente nuevamente.");
        }
        if (contraseña == null || contraseña.trim().isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas. Por favor, intente nuevamente.");
        }

        // Buscar al usuario en la lista
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getRut().equals(rut) && usuario.validarContraseña(contraseña)) {
                return usuario; // Retornar el usuario autenticado
            }
        }

        // Si no se encontró el usuario o la contraseña es incorrecta
        throw new IllegalArgumentException("Credenciales inválidas. Por favor, intente nuevamente.");
    }
}
