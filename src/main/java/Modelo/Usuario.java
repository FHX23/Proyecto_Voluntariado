package Modelo;

import org.mindrot.jbcrypt.BCrypt;

public class Usuario {

    private String nombre;
    private String contraseña;
    private String tipoUsuario;
    private String rut;

    public Usuario(String nombre, String contraseña, String tipoUsuario, String rut) {
        setNombre(nombre);
        setContraseña(contraseña);
        setTipoUsuario(tipoUsuario);
        setRut(rut);
    }
    
    public Usuario(String nombre, String tipoUsuario, String rut) {
        setNombre(nombre);
        setTipoUsuario(tipoUsuario);
        setRut(rut);
    }
    
    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío o ser nulo.");
        }
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        if (contraseña == null || contraseña.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía o ser nula.");
        }
        this.contraseña = BCrypt.hashpw(contraseña, BCrypt.gensalt()); // Encripta la contraseña
    }
    
    public void setContraseñaHasheada(String contraseña){
        this.contraseña = contraseña;
    }
    
    public boolean validarContraseña(String contraseñaIngresada) {
        if (this.contraseña == null || contraseñaIngresada == null) {
            return false;
        }
        return BCrypt.checkpw(contraseñaIngresada, this.contraseña); // Compara la contraseña ingresada con el hash
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de usuario no puede estar vacío o ser nulo.");
        }
        this.tipoUsuario = tipoUsuario.trim().toLowerCase(); // Guardar en minusculas
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        if (!esRutValido(rut)) {
            throw new IllegalArgumentException("El RUT proporcionado no es válido.");
        }
        this.rut = rut;
    }

    // Método para validar el RUT
    private boolean esRutValido(String rut) {
        if (rut == null || !rut.matches("\\d{7,8}-[\\dkK]")) {
            return false; // Formato incorrecto
        }

        String[] partes = rut.split("-");
        String numeros = partes[0];
        String digitoVerificador = partes[1].toUpperCase();

        int suma = 0;
        int multiplicador = 2;

        // Calcular el dígito verificador
        for (int i = numeros.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(numeros.charAt(i)) * multiplicador;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }

        int resto = suma % 11;
        String digitoCalculado = resto == 1 ? "K" : (resto == 0 ? "0" : String.valueOf(11 - resto));

        return digitoCalculado.equals(digitoVerificador);
    }
}
