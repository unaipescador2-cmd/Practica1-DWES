package HelpDesk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArchivoTickets {

    private static final String SEPARADOR = ";";

    private final Path ruta;

    public ArchivoTickets(Path ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula");
        }
        this.ruta = ruta;
    }

    /**
     * Carga todas las incidencias del archivo.
     * - Si el archivo no existe, devuelve una lista vacía.
     * - Si hay cualquier línea inválida o un identificador repetido, lanza
     *   IOException y NO devuelve nada (no hay carga parcial).
     * - Nunca modifica el archivo.
     */
    public List<Ticket> cargar() throws IOException {
        if (!Files.exists(ruta)) {
            return new ArrayList<>();
        }

        List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
        List<Ticket> tickets = new ArrayList<>();
        Set<Integer> idsVistos = new HashSet<>();

        for (int i = 0; i < lineas.size(); i++) {
            int numeroLinea = i + 1;
            Ticket ticket = parsearLinea(lineas.get(i), numeroLinea);
            if (!idsVistos.add(ticket.getId())) {
                throw new IOException("Línea " + numeroLinea
                        + ": identificador repetido (" + ticket.getId() + ")");
            }
            tickets.add(ticket);
        }
        return tickets; // solo se devuelve si TODO el archivo era válido
    }

    /**
     * Guarda la colección completa, sustituyendo el contenido anterior.
     */
    public void guardar(List<Ticket> tickets) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Ticket t : tickets) {
            lineas.add(t.getId() + SEPARADOR + t.estaCerrado() + SEPARADOR + t.getDescripcion());
        }
        // Por defecto Files.write crea el archivo o lo trunca si ya existe
        Files.write(ruta, lineas, StandardCharsets.UTF_8);
    }

    private static Ticket parsearLinea(String linea, int numeroLinea) throws IOException {
        // Límite 3: solo se corta en los DOS primeros ';'.
        // Lo que quede a la derecha es la descripción, aunque tenga más ';'.
        String[] partes = linea.split(SEPARADOR, 3);
        if (partes.length != 3) {
            throw new IOException("Línea " + numeroLinea
                    + ": formato incorrecto (se esperaba id;estado;descripcion)");
        }

        int id;
        try {
            id = Integer.parseInt(partes[0]);
        } catch (NumberFormatException e) {
            throw new IOException("Línea " + numeroLinea
                    + ": identificador no numérico (" + partes[0] + ")");
        }

        boolean cerrado;
        if (partes[1].equals("true")) {
            cerrado = true;
        } else if (partes[1].equals("false")) {
            cerrado = false;
        } else {
            throw new IOException("Línea " + numeroLinea
                    + ": estado inválido (" + partes[1] + "), debe ser true o false");
        }

        try {
            return new Ticket(id, partes[2], cerrado); // Ticket valida id y descripción
        } catch (IllegalArgumentException e) {
            throw new IOException("Línea " + numeroLinea + ": " + e.getMessage());
        }
    }
}