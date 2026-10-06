package HelpDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorTickets {

    private final List<Ticket> tickets;
    private int siguienteId;

    // Gestor vacío: la numeración empieza en 1
    public GestorTickets() {
        this.tickets = new ArrayList<>();
        this.siguienteId = 1;
    }

    // Gestor con tickets ya existentes (los usará la carga desde archivo)
    public GestorTickets(List<Ticket> iniciales) {
        this();
        int mayorId = 0;
        for (Ticket t : iniciales) {
            if (buscarPorId(t.getId()).isPresent()) {
                throw new IllegalArgumentException("Identificador repetido: " + t.getId());
            }
            tickets.add(t);
            mayorId = Math.max(mayorId, t.getId());
        }
        this.siguienteId = mayorId + 1;
    }

    /**
     * Crea un ticket abierto con el siguiente identificador.
     * Si la descripción no es válida, Ticket lanza la excepción ANTES de
     * que se añada nada y ANTES de que se consuma el identificador.
     */
    public Ticket crearTicket(String descripcion) {
        Ticket nuevo = new Ticket(siguienteId, descripcion); // puede lanzar excepción
        tickets.add(nuevo);
        siguienteId++;
        return nuevo;
    }

    public Optional<Ticket> buscarPorId(int id) {
        for (Ticket t : tickets) {
            if (t.getId() == id) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    // Copia de la lista, pero con los mismos objetos Ticket
    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }

    // ----- Estadísticas -----

    public int totalTickets() {
        return tickets.size();
    }

    public int ticketsAbiertos() {
        int contador = 0;
        for (Ticket t : tickets) {
            if (!t.estaCerrado()) {
                contador++;
            }
        }
        return contador;
    }

    public int ticketsCerrados() {
        return totalTickets() - ticketsAbiertos();
    }
}