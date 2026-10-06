package HelpDesk;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AplicacionHelpDesk {

    private final Scanner teclado = new Scanner(System.in);
    private final ArchivoTickets archivo;
    private GestorTickets gestor;

    public AplicacionHelpDesk(ArchivoTickets archivo) {
        this.archivo = archivo;
    }

    public static void main(String[] args) {
        ArchivoTickets archivo = new ArchivoTickets(Path.of("tickets.txt"));
        new AplicacionHelpDesk(archivo).iniciar();
    }

    // ----- Arranque -----

    public void iniciar() {
        try {
            if (cargarDatos()) {
                ejecutarMenu();
            }
        } finally {
            teclado.close();
        }
    }


    private boolean cargarDatos() {
        try {
            List<Ticket> cargados = archivo.cargar();
            gestor = new GestorTickets(cargados);
            System.out.println("Incidencias cargadas: " + cargados.size());
            return true;
        } catch (IOException e) {
            System.out.println("No se han podido cargar las incidencias: " + e.getMessage());
            System.out.println("El programa no arrancará y el archivo no se ha modificado.");
            return false;
        }
    }

    private void ejecutarMenu() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> crearIncidencia();
                case 2 -> listarIncidencias();
                case 3 -> buscarIncidencia();
                case 4 -> cerrarIncidencia();
                case 5 -> mostrarEstadisticas();
                case 6 -> guardarIncidencias();
                case 0 -> {
                    salir = true;
                    System.out.println("Fin del programa. Los cambios no guardados se han perdido.");
                }
                default -> System.out.println("Opción incorrecta. Elige un número del 0 al 6.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("HELPDESK DEL CENTRO");
        System.out.println("1. Crear incidencia");
        System.out.println("2. Listar incidencias");
        System.out.println("3. Buscar incidencia por identificador");
        System.out.println("4. Cerrar incidencia");
        System.out.println("5. Mostrar estadísticas");
        System.out.println("6. Guardar incidencias");
        System.out.println("0. Salir");
        System.out.println("(Guarda con la opción 6 antes de salir o perderás los cambios)");
    }



    private void crearIncidencia() {
        System.out.print("Descripción de la incidencia: ");
        String descripcion = teclado.nextLine();
        try {
            Ticket nuevo = gestor.crearTicket(descripcion);
            System.out.println("Incidencia creada con el identificador " + nuevo.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("No se ha creado la incidencia: " + e.getMessage());
        }
    }

    private void listarIncidencias() {
        List<Ticket> tickets = gestor.getTickets();
        if (tickets.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
            return;
        }
        for (Ticket t : tickets) {
            System.out.println(t);
        }
    }

    private void buscarIncidencia() {
        int id = leerEntero("Identificador a buscar: ");
        Optional<Ticket> encontrado = gestor.buscarPorId(id);
        if (encontrado.isPresent()) {
            System.out.println(encontrado.get());
        } else {
            System.out.println("No existe ninguna incidencia con el identificador " + id);
        }
    }

    private void cerrarIncidencia() {
        int id = leerEntero("Identificador de la incidencia a cerrar: ");
        Optional<Ticket> encontrado = gestor.buscarPorId(id);
        if (encontrado.isEmpty()) {
            System.out.println("No existe ninguna incidencia con el identificador " + id);
        } else if (encontrado.get().cerrar()) {
            System.out.println("Incidencia " + id + " cerrada correctamente.");
        } else {
            System.out.println("La incidencia " + id + " ya estaba cerrada.");
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("Total de incidencias: " + gestor.totalTickets());
        System.out.println("Abiertas: " + gestor.ticketsAbiertos());
        System.out.println("Cerradas: " + gestor.ticketsCerrados());
    }

    private void guardarIncidencias() {
        try {
            archivo.guardar(gestor.getTickets());
            System.out.println("Incidencias guardadas correctamente.");
        } catch (IOException e) {
            System.out.println("No se han podido guardar las incidencias: " + e.getMessage());
        }
    }

    // ----- Lectura del teclado -----

    /** Lee la opción del menú. Si no es un número, devuelve -1 (opción incorrecta). */
    private int leerOpcion() {
        System.out.print("Elige una opción: ");
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Pide un entero y repite la pregunta hasta que la entrada sea numérica. */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número entero. Inténtalo de nuevo.");
            }
        }
    }
}