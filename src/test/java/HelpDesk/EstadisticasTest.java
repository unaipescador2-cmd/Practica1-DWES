package HelpDesk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstadisticasTest {

    @Test
    void gestorVacioTodoACero() {
        GestorTickets gestor = new GestorTickets();
        assertEquals(0, gestor.totalTickets());
        assertEquals(0, gestor.ticketsAbiertos());
        assertEquals(0, gestor.ticketsCerrados());
    }

    @Test
    void dosIncidenciasAbiertas() {
        GestorTickets gestor = new GestorTickets();
        gestor.crearTicket("Falla el teclado");
        gestor.crearTicket("Sin conexión a Internet");

        assertEquals(2, gestor.totalTickets());
        assertEquals(2, gestor.ticketsAbiertos());
        assertEquals(0, gestor.ticketsCerrados());
    }

    @Test
    void dosIncidenciasConUnaCerrada() {
        GestorTickets gestor = new GestorTickets();
        gestor.crearTicket("Falla el teclado");
        Ticket segundo = gestor.crearTicket("Sin conexión a Internet");
        segundo.cerrar();

        assertEquals(2, gestor.totalTickets());
        assertEquals(1, gestor.ticketsAbiertos());
        assertEquals(1, gestor.ticketsCerrados());
    }
}