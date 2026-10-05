package HelpDesk;

public class Ticket {

    private final int id;
    private final String descripcion;
    private boolean cerrado;

    // Constructor para tickets nuevos: siempre empiezan abiertos
    public Ticket(int id, String descripcion) {
        this(id, descripcion, false);
    }

    // Constructor para recuperar tickets desde el archivo (con su estado)
    public Ticket(int id, String descripcion, boolean cerrado) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser positivo");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni estar en blanco");
        }
        if (descripcion.contains("\n") || descripcion.contains("\r")) {
            throw new IllegalArgumentException("La descripción debe ser de una sola línea");
        }
        this.id = id;
        this.descripcion = descripcion;
        this.cerrado = cerrado;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean estaCerrado() {
        return cerrado;
    }

    /**
     * Cierra el ticket.
     * @return true si estaba abierto y se ha cerrado; false si ya estaba cerrado.
     */
    public boolean cerrar() {
        if (cerrado) {
            return false;
        }
        cerrado = true;
        return true;
    }

    @Override
    public String toString() {
        return id + " | " + descripcion + " | " + (cerrado ? "CERRADO" : "ABIERTO");
    }
}
