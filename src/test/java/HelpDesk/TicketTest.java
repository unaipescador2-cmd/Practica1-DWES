package HelpDesk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void ticketNuevoEmpiezaAbierto() {
        Ticket t = new Ticket(1, "Falla el teclado");
        assertFalse(t.estaCerrado());
    }

    @Test
    void cerrarCambiaElEstadoACerrado() {
        Ticket t = new Ticket(1, "Falla el teclado");
        assertTrue(t.cerrar());
        assertTrue(t.estaCerrado());
    }

    @Test
    void cerrarDosVecesDevuelveFalseLaSegundaVez() {
        Ticket t = new Ticket(1, "Falla el teclado");
        t.cerrar();
        assertFalse(t.cerrar());
        assertTrue(t.estaCerrado());
    }

    @Test
    void rechazaDescripcionEnBlanco() {
        assertThrows(IllegalArgumentException.class, () -> new Ticket(1, "   "));
    }

    @Test
    void rechazaDescripcionNula() {
        assertThrows(IllegalArgumentException.class, () -> new Ticket(1, null));
    }

    @Test
    void rechazaIdentificadorCero() {
        assertThrows(IllegalArgumentException.class, () -> new Ticket(0, "Falla el teclado"));
    }
}