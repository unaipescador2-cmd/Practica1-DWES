package HelpDesk;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GestorTicketsTest {

    @Test
    void coleccionInicialmenteVacia() {
        GestorTickets gestor = new GestorTickets();
        assertTrue(gestor.getTickets().isEmpty());
    }

    @Test
    void identificadoresConsecutivos() {
        GestorTickets gestor = new GestorTickets();
        Ticket primero = gestor.crearTicket("Falla el teclado");
        Ticket segundo = gestor.crearTicket("Sin conexión a Internet");
        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
    }

    @Test
    void busquedaDevuelveElMismoObjeto() {
        GestorTickets gestor = new GestorTickets();
        Ticket creado = gestor.crearTicket("Falla el teclado");
        Optional<Ticket> encontrado = gestor.buscarPorId(1);
        assertTrue(encontrado.isPresent());
        assertSame(creado, encontrado.get());
    }

    @Test
    void busquedaInexistenteDevuelveVacio() {
        GestorTickets gestor = new GestorTickets();
        gestor.crearTicket("Falla el teclado");
        assertTrue(gestor.buscarPorId(99).isEmpty());
    }

    @Test
    void creacionInvalidaNoAlteraColeccionNiContador() {
        GestorTickets gestor = new GestorTickets();
        assertThrows(IllegalArgumentException.class, () -> gestor.crearTicket("   "));
        assertEquals(0, gestor.getTickets().size());

        // Si el contador no se consumió, el siguiente ticket válido recibe el 1
        Ticket valido = gestor.crearTicket("Falla el teclado");
        assertEquals(1, valido.getId());
    }

    @Test
    void modificarLaListaDevueltaNoAfectaAlGestor() {
        GestorTickets gestor = new GestorTickets();
        Ticket creado = gestor.crearTicket("Falla el teclado");

        List<Ticket> copia = gestor.getTickets();
        copia.clear();

        assertEquals(1, gestor.getTickets().size());
        assertSame(creado, gestor.getTickets().get(0)); // mismos objetos, no clones
    }

    // ----- Extras útiles para la persistencia (Fase 3) -----

    @Test
    void constructorConTicketsContinuaDesdeElMayorIdMasUno() {
        List<Ticket> iniciales = List.of(
                new Ticket(1, "Falla el teclado"),
                new Ticket(5, "Sin conexión a Internet", true));
        GestorTickets gestor = new GestorTickets(iniciales);

        Ticket nuevo = gestor.crearTicket("Pantalla rota");
        assertEquals(6, nuevo.getId());
    }

    @Test
    void constructorConTicketsRechazaIdentificadoresRepetidos() {
        List<Ticket> repetidos = List.of(
                new Ticket(1, "Falla el teclado"),
                new Ticket(1, "Sin conexión a Internet"));
        assertThrows(IllegalArgumentException.class, () -> new GestorTickets(repetidos));
    }
}