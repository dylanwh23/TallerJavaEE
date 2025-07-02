package com.tallerjava.tallerjava.Comercio.dominio;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tallerjava.tallerjava.Comercio.aplicacion.AIService;
import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import com.tallerjava.tallerjava.Comercio.aplicacion.ComercioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComerciosServiceTest {

    private ComercioService comercioService;
    private ComercioRepository comercioRepository;

    @BeforeEach
    void setup() {
        // Crea un mock del repositorio
        comercioRepository = mock(ComercioRepository.class);
        
        // Inyecta el repositorio simulado en el servicio
        comercioService = new ComercioService();
        comercioService.comercioRepository = comercioRepository;
    }

    @Test
    void testAltaComercio_ComercioNoExiste() {
        // Configuración del mock
        when(comercioRepository.existeComercio("correo@test.com")).thenReturn(false);

        // Datos de prueba
        Comercio comercio = new Comercio();
        comercio.setCorreo("correo@test.com");

        // Act
        assertDoesNotThrow(() -> comercioService.altaComercio(comercio));

        // Verifica que el método save fue llamado una vez
        verify(comercioRepository, times(1)).save(comercio);
    }

    @Test
    void testAltaComercio_ComercioYaExiste() {
        // Configuración del mock
        when(comercioRepository.existeComercio("correo@test.com")).thenReturn(true);

        // Datos de prueba
        Comercio comercio = new Comercio();
        comercio.setCorreo("correo@test.com");

        // Act y Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            comercioService.altaComercio(comercio);
        });

        assertEquals("El correo ya esta utilizado", exception.getMessage());
    }

    @Test
    void testReclamo_CorreoYContraseñaIncorrectos(){
        when(comercioRepository.findComercioByCorreo("correo@test.com", "pwtest")).thenReturn(null);
        Comercio comercio = null;
        Reclamo reclamo = new Reclamo();
        reclamo.setComercio(comercio);
        reclamo.setTexto("Reclamo de prueba");
        reclamo.setFechaHora(java.time.LocalDateTime.now());

        SecurityException exception = assertThrows(SecurityException.class, () -> {
           comercioService.realizarReclamo("correo@test.com", "pwtest", reclamo.getTexto());
        });

        assertEquals("Comercio no encontrado. Intente de nuevo con los datos correctos.", exception.getMessage());
    }
    @Test
    void testReclamo_StringVacio() {
        // Configuración del mock
        Comercio comercio = new Comercio();
        comercio.setCorreo("correo@test.com");
        comercio.setContrasenia("password");
        
        // Configurar el comportamiento del repositorio para devolver un comercio válido
        when(comercioRepository.findComercioByCorreo("correo@test.com", "password"))
            .thenReturn(comercio);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            comercioService.realizarReclamo("correo@test.com", "password", "");
        });

        // Verificar el mensaje de error esperado
        assertEquals("No hay contenido", exception.getMessage());

        // Verificar que no se llama al método saveReclamo del repositorio
        verify(comercioRepository, times(0)).saveReclamo(any(Reclamo.class));
    }
    @Test
    void testCambiarEstadoPOS_Correcto() {
        // Simular un comercio válido con un POS asociado
        Comercio comercio = new Comercio();
        comercio.setCorreo("correo@test.com");
        comercio.setContrasenia("password");

        POS pos = new POS();
        pos.setId(1); // ID del POS en el comercio
        pos.setEstado(true); // Estado inicial
        comercio.addPos(pos);

        // Configurar el mock para devolver el comercio válido
        when(comercioRepository.findComercioByCorreo("correo@test.com", "password"))
                .thenReturn(comercio);

        // Act (cambiar estado del POS)
        assertDoesNotThrow(() ->
                comercioService.cambiarEstadoPOS("correo@test.com", "password", 1, false)
        );

        // Assert
        assertFalse(pos.getEstado()); // Verificar que el estado del POS cambió
        verify(comercioRepository, times(1)).mergePos(pos); // Verificar que se persistió el cambio
    }

    @Test
    void testCambiarEstadoPOS_ComercioNoExiste() {
        // Configurar el mock para devolver null (comercio no encontrado)
        when(comercioRepository.findComercioByCorreo("correo@test.com", "wrongpassword"))
                .thenReturn(null);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () ->
                comercioService.cambiarEstadoPOS("correo@test.com", "wrongpassword", 1, false)
        );

        assertEquals("Comercio no encontrado. Intente de nuevo con los datos correctos.", exception.getMessage());
        verify(comercioRepository, never()).mergePos(any(POS.class)); // Se asegura que no se intentó persistir nada
    }

    @Test
    void testCambiarEstadoPOS_POSNoExiste() {
        // Simular un comercio válido pero sin POS correspondiente
        Comercio comercio = new Comercio();
        comercio.setCorreo("correo@test.com");
        comercio.setContrasenia("password");

        // Configurar el mock
        when(comercioRepository.findComercioByCorreo("correo@test.com", "password"))
                .thenReturn(comercio);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                comercioService.cambiarEstadoPOS("correo@test.com", "password", 99, false) // ID inexistente
        );

        assertEquals("El ID del POS no existe en el comercio.", exception.getMessage());
        verify(comercioRepository, never()).mergePos(any(POS.class)); // Se asegura que no se intentó persistir nada
    }
}