# Plataforma de Voluntariado Comunitario

Este proyecto es una aplicación diseñada para conectar a personas con organizaciones locales, facilitando la participación en eventos de voluntariado. La plataforma busca centralizar la difusión de proyectos comunitarios, permitiendo una interacción más eficiente entre voluntarios y organizaciones.

## Funcionalidades Implementadas

Actualmente, la aplicación incluye las siguientes características funcionales:

### 1. Registro de Usuarios
- Los usuarios pueden registrarse como personas o como organizaciones.
- Se valida que el RUT ingresado no coincida con el de una cuenta existente.

### 2. Gestión de Proyectos
- Las organizaciones pueden publicar eventos de voluntariado, especificando:
  - Fecha
  - Lugar
  - Descripción
  - Número de voluntarios requeridos
- **Nota:** Las notificaciones por correo para la cancelación de eventos aún no están implementadas.

### 3. Visualización e Inscripción a Eventos
- Los voluntarios pueden:
  - Ver la lista de eventos disponibles.
  - Filtrar eventos por ubicación o fecha.
  - Inscribirse en los eventos que les interesen.
  - Consultar los eventos en los que están inscritos y cancelar su participación si es necesario.
- **Nota:** Las notificaciones por correo para nuevos proyectos no están disponibles.

### 4. Registro de Asistencia
- Las organizaciones pueden registrar la asistencia de los voluntarios en los eventos.
- **Nota:** El registro de inasistencias aún no está disponible.

### 5. Generación de Reportes
- Las organizaciones pueden generar reportes en formato PDF con la información de:
  - Eventos realizados.
  - Número de voluntarios inscritos y asistentes.
- **Nota:** La selección por periodo de tiempo no está habilitada.

## Requisitos
- **Java**: Versión 11 o superior.
- **NetBeans**: Para la gestión y ejecución del proyecto.
- **Base de datos**: Configuración necesaria para almacenar datos de usuarios, eventos y reportes.

## Instalación
1. Clona este repositorio:
   ```bash
   git clone https://github.com/FHX23/Proyecto_Voluntariado.git
