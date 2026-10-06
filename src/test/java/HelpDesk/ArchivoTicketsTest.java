package HelpDesk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchivoTicketsTest {

    @TempDir
    Path carpeta;

    @Test
    void archivoInexistenteDevuelveColeccionVacia() throws IOException {
        ArchivoTickets archivo = new ArchivoTickets(carpeta.resolve("no-existe.txt"));
        assertTrue(archivo.cargar().isEmpty());
    }

    @Test
    void guardarYCargarConservaIdsDescripcionesYEstados() throws IOException {
        ArchivoTickets archivo = new ArchivoTickets(carpeta.resolve("tickets.txt"));
        archivo.guardar(List.of(
                new Ticket(1, "Falla el teclado"),
                new Ticket(2, "Sin conexión a Internet", true)));

        List<Ticket> cargados = archivo.cargar();

        assertEquals(2, cargados.size());
        assertEquals(1, cargados.get(0).getId());
        assertEquals("Falla el teclado", cargados.get(0).getDescripcion());
        assertFalse(cargados.get(0).estaCerrado());
        assertEquals(2, cargados.get(1).getId());
        assertEquals("Sin conexión a Internet", cargados.get(1).getDescripcion());
        assertTrue(cargados.get(1).estaCerrado());
    }

    @Test
    void descripcionConPuntoYComaSeConserva() throws IOException {
        ArchivoTickets archivo = new ArchivoTickets(carpeta.resolve("tickets.txt"));
        archivo.guardar(List.of(new Ticket(1, "Router; cable roto; revisar")));

        List<Ticket> cargados = archivo.cargar();

        assertEquals("Router; cable roto; revisar", cargados.get(0).getDescripcion());
    }

    @Test
    void estadoInvalidoLanzaExcepcion() throws IOException {
        Path ruta = carpeta.resolve("tickets.txt");
        Files.writeString(ruta, "1;quizas;Falla el teclado\n");
        ArchivoTickets archivo = new ArchivoTickets(ruta);

        assertThrows(IOException.class, archivo::cargar);
    }

    @Test
    void identificadorNoNumericoLanzaExcepcion() throws IOException {
        Path ruta = carpeta.resolve("tickets.txt");
        Files.writeString(ruta, "abc;false;Falla el teclado\n");
        ArchivoTickets archivo = new ArchivoTickets(ruta);

        assertThrows(IOException.class, archivo::cargar);
    }

    @Test
    void identificadoresRepetidosLanzanExcepcion() throws IOException {
        Path ruta = carpeta.resolve("tickets.txt");
        Files.writeString(ruta, "1;false;Uno\n1;true;Otro\n");
        ArchivoTickets archivo = new ArchivoTickets(ruta);

        assertThrows(IOException.class, archivo::cargar);
    }

    @Test
    void archivoInvalidoNoSeSobrescribe() throws IOException {
        Path ruta = carpeta.resolve("tickets.txt");
        String contenidoOriginal = "1;false;Valida\nesto no es una linea valida\n";
        Files.writeString(ruta, contenidoOriginal);
        ArchivoTickets archivo = new ArchivoTickets(ruta);

        assertThrows(IOException.class, archivo::cargar);

        assertEquals(contenidoOriginal, Files.readString(ruta));
    }

    @Test
    void numeracionContinuaTrasCargar() throws IOException {
        Path ruta = carpeta.resolve("tickets.txt");
        Files.writeString(ruta, "1;false;Falla el teclado\n2;true;Sin conexión a Internet\n");

        GestorTickets gestor = new GestorTickets(new ArchivoTickets(ruta).cargar());
        Ticket nuevo = gestor.crearTicket("Pantalla rota");

        assertEquals(3, nuevo.getId());
    }
}
