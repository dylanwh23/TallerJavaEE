import com.tallerjava.tallerjava.Comercio.dominio.POS;
import com.tallerjava.tallerjava.Comercio.dominio.Reclamo;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.AuthRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.cambiarEstadoPosRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.core.Response;

import com.tallerjava.tallerjava.Comercio.interfase.ComercioAPI;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.ReclamoRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.ModificarComercioRequest;
import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.infraestructura.persistencia.ComercioRepositoryImp;
import com.tallerjava.tallerjava.Comercio.aplicacion.ComercioService;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ComerciosIntegrationTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private ComercioAPI comercioAPI;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("TestPU");
    }

    @BeforeEach
    void init() {
        em = emf.createEntityManager();

        // Configuración de dependencias
        ComercioRepositoryImp comercioRepository = new ComercioRepositoryImp();
        comercioRepository.setEm(em);

        ComercioService comercioService = new ComercioService();
        comercioService.comercioRepository = comercioRepository;

        comercioAPI = new ComercioAPI();
        comercioAPI.setComercioService(comercioService);

        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @AfterAll
    static void cleanup() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void testRegistroComercio() {
        Comercio comercio = new Comercio();
        comercio.setNombre("Comercio Demo");
        comercio.setCorreo("contacto@comerciodemo.com");
        comercio.setTelefono("123456789");
        comercio.setContrasenia("securepassword");

        // Usar API para registrar
        comercioAPI.registroComercio(comercio);

        em.flush(); // Forzar persistencia

        Comercio savedComercio = em.createQuery(
                        "SELECT c FROM Comercio c WHERE c.correo = :correo", Comercio.class)
                .setParameter("correo", "contacto@comerciodemo.com")
                .getSingleResult();

        assertNotNull(savedComercio);
        assertEquals("Comercio Demo", savedComercio.getNombre());
    }

    @Test
    void testModificarComercio() {
        // Crear y guardar comercio inicial
        Comercio comercio = new Comercio();
        comercio.setNombre("Comercio Original");
        comercio.setCorreo("original@test.com");
        comercio.setTelefono("123456789");
        comercio.setContrasenia("password123");
        comercioAPI.registroComercio(comercio);

        em.flush(); // Persistir datos iniciales

        // Crear el request de autenticación
        AuthRequest authRequest = new AuthRequest();
        authRequest.setCorreo("original@test.com");
        authRequest.setContrasenia("password123");

        // Crear el request de modificación
        ModificarComercioRequest modificarRequest = new ModificarComercioRequest();
        modificarRequest.setNombre("Nuevo Nombre");
        modificarRequest.setTelefono("987654321");
        modificarRequest.setNuevoCorreo("original@test.com");
        modificarRequest.setNuevaContrasenia("password123");

        Response response = comercioAPI.modificarComercio(authRequest, modificarRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verificar los cambios
        Comercio updatedComercio = em.createQuery(
                        "SELECT c FROM Comercio c WHERE c.correo = :correo", Comercio.class)
                .setParameter("correo", "original@test.com")
                .getSingleResult();

        assertEquals("Nuevo Nombre", updatedComercio.getNombre());
        assertEquals("987654321", updatedComercio.getTelefono());
    }

    @Test
    void testAgregarPOS() {
        // Crear y guardar comercio inicial
        Comercio comercio = new Comercio();
        comercio.setNombre("Comercio POS");
        comercio.setCorreo("pos@test.com");
        comercio.setTelefono("123456789");
        comercio.setContrasenia("password123");
        comercioAPI.registroComercio(comercio);

        em.flush(); // Persistir datos iniciales

        // Crear el request de agregar POS
        AuthRequest agregarPosRequest = new AuthRequest();
        agregarPosRequest.setCorreo("pos@test.com");
        agregarPosRequest.setContrasenia("password123");

        Response response = comercioAPI.agregarPos(agregarPosRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verificar que el POS se haya registrado
        Comercio comercioConPOS = em.createQuery(
                        "SELECT c FROM Comercio c WHERE c.correo = :correo", Comercio.class)
                .setParameter("correo", "pos@test.com")
                .getSingleResult();

        assertNotNull(comercioConPOS.getPos());
        assertEquals(1, comercioConPOS.getPos().size());
    }

    @Test
    void testRealizarReclamo() {
        // Crear y guardar comercio inicial
        Comercio comercio = new Comercio();
        comercio.setNombre("Comercio Reclamo");
        comercio.setCorreo("reclamo@test.com");
        comercio.setTelefono("123456789");
        comercio.setContrasenia("password123");
        comercioAPI.registroComercio(comercio);

        em.flush(); // Persistir datos iniciales

        // Crear el request de autenticación
        AuthRequest authRequest = new AuthRequest();
        authRequest.setCorreo("reclamo@test.com");
        authRequest.setContrasenia("password123");

        // Crear el request del reclamo (solo con texto)
        ReclamoRequest reclamoRequest = new ReclamoRequest();
        reclamoRequest.setTexto("Este es un reclamo de prueba");

        Response response = comercioAPI.realizarReclamo(authRequest, reclamoRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Verificar que el reclamo se haya registrado
        Reclamo reclamo = em.createQuery(
                        "SELECT r FROM Reclamo r WHERE r.comercio.correo = :correo", Reclamo.class)
                .setParameter("correo", "reclamo@test.com")
                .getSingleResult();

        assertNotNull(reclamo);
        assertEquals("Este es un reclamo de prueba", reclamo.getTexto());
    }

    @Test
    void testCambiarEstadoPos() {
        // Paso 1: Crear y guardar el comercio inicial
        Comercio comercio = new Comercio();
        comercio.setNombre("Comercio Estado POS");
        comercio.setCorreo("estado@test.com");
        comercio.setTelefono("123456789");
        comercio.setContrasenia("password123");
        comercioAPI.registroComercio(comercio);

        em.flush(); // Asegurar persistencia inicial

        // Paso 2: Agregar un POS al comercio
        AuthRequest agregarPosRequest = new AuthRequest();
        agregarPosRequest.setCorreo("estado@test.com");
        agregarPosRequest.setContrasenia("password123");

        Response agregarPosResponse = comercioAPI.agregarPos(agregarPosRequest);

        assertEquals(Response.Status.OK.getStatusCode(), agregarPosResponse.getStatus()); // Verificar éxito al agregar POS

        em.flush(); // Asegurar persistencia del POS

        // Obtener el ID del POS agregado (ID generado automáticamente)
        Comercio comercioConPOS = em.createQuery(
                        "SELECT c FROM Comercio c WHERE c.correo = :correo", Comercio.class)
                .setParameter("correo", "estado@test.com")
                .getSingleResult();

        assertNotNull(comercioConPOS.getPos());
        assertEquals(1, comercioConPOS.getPos().size()); // Debe haber exactamente un POS agregado

        int idPOS = comercioConPOS.getPos().get(0).getId();
        Boolean estadoInicial = comercioConPOS.getPos().get(0).getEstado();
        assertTrue(estadoInicial); // Verificar que el POS está inicialmente activo

        // Paso 3: Crear el request de autenticación
        AuthRequest authRequest = new AuthRequest();
        authRequest.setCorreo("estado@test.com");
        authRequest.setContrasenia("password123");

        // Paso 4: Cambiar el estado del POS a inactivo (false)
        cambiarEstadoPosRequest cambiarEstadoRequest = new cambiarEstadoPosRequest();
        cambiarEstadoRequest.setIdPOS(idPOS);
        cambiarEstadoRequest.setEstado(false);

        Response cambiarEstadoResponse = comercioAPI.cambiarEstadoPOS(authRequest, cambiarEstadoRequest);

        assertEquals(Response.Status.OK.getStatusCode(), cambiarEstadoResponse.getStatus()); // Verificar éxito en la solicitud

        em.flush(); // Asegurar persistencia del cambio de estado

        // Verificar que el estado del POS ha cambiado en la base de datos
        Comercio comercioActualizado = em.createQuery(
                        "SELECT c FROM Comercio c WHERE c.correo = :correo", Comercio.class)
                .setParameter("correo", "estado@test.com")
                .getSingleResult();

        assertNotNull(comercioActualizado.getPos());
        assertEquals(1, comercioActualizado.getPos().size()); // Asegurarse de que aún hay un POS

        POS posActualizado = comercioActualizado.getPos().get(0);
        assertFalse(posActualizado.getEstado()); // Verificar que el estado ahora es inactivo
    }
}