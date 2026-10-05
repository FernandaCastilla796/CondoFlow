package com.condoflow.residence.application.service;

import com.condoflow.person.domain.exception.PersonaNoEncontradaException;
import com.condoflow.person.domain.model.Persona;
import com.condoflow.person.domain.port.in.ConsultarPersonaUseCase;
import com.condoflow.residence.application.command.RegistrarResidenciaCommand;
import com.condoflow.residence.domain.exception.ResidenciaVigenteDuplicadaException;
import com.condoflow.residence.domain.model.EstadoResidencia;
import com.condoflow.residence.domain.model.Residencia;
import com.condoflow.residence.domain.model.TipoResidencia;
import com.condoflow.residence.domain.port.out.ResidenciaRepositoryPort;
import com.condoflow.unit.domain.exception.UnidadNoEncontradaException;
import com.condoflow.unit.domain.model.EstadoUnidad;
import com.condoflow.unit.domain.model.Unidad;
import com.condoflow.unit.domain.port.in.ConsultarUnidadUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Reglas del caso de uso de residencias (Capítulos 07 y 08), probadas sin Spring ni base de datos. */
@ExtendWith(MockitoExtension.class)
class ResidenciaServiceTest {

    private static final Persona MARIA = Persona.nueva("Maria", "Lopez", "7845123", "+591 70000001",
            "maria@condoflow.com").conId(1L);
    private static final Unidad A102 = new Unidad(1L, "A-102", "Departamento", EstadoUnidad.ACTIVA);
    private static final LocalDate INICIO = LocalDate.of(2026, 9, 1);

    @Mock
    private ResidenciaRepositoryPort repositoryPort;
    @Mock
    private ConsultarPersonaUseCase consultarPersona;
    @Mock
    private ConsultarUnidadUseCase consultarUnidad;

    private ResidenciaService service;

    @BeforeEach
    void setUp() {
        service = new ResidenciaService(repositoryPort, consultarPersona, consultarUnidad);
    }

    private static RegistrarResidenciaCommand comando(Long personaId, Long unidadId) {
        return new RegistrarResidenciaCommand(personaId, unidadId, TipoResidencia.INQUILINO, INICIO);
    }

    @Test
    void registraResidenciaVigenteCuandoPersonaYUnidadExisten() {
        when(consultarPersona.buscarPorId(1L)).thenReturn(Optional.of(MARIA));
        when(consultarUnidad.buscarPorId(1L)).thenReturn(Optional.of(A102));
        when(repositoryPort.existeVigente(1L, 1L)).thenReturn(false);
        when(repositoryPort.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        Residencia resultado = service.registrar(comando(1L, 1L));

        assertThat(resultado.getEstado()).isEqualTo(EstadoResidencia.VIGENTE);
        assertThat(resultado.getFechaFin()).isNull();
    }

    @Test
    void rechazaPersonaInexistente() {
        when(consultarPersona.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(comando(99L, 1L)))
                .isInstanceOf(PersonaNoEncontradaException.class);
        verify(repositoryPort, never()).guardar(any());
    }

    @Test
    void rechazaUnidadInexistente() {
        when(consultarPersona.buscarPorId(1L)).thenReturn(Optional.of(MARIA));
        when(consultarUnidad.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(comando(1L, 99L)))
                .isInstanceOf(UnidadNoEncontradaException.class);
        verify(repositoryPort, never()).guardar(any());
    }

    @Test
    void rechazaSegundaResidenciaVigenteEnLaMismaUnidad() {
        when(consultarPersona.buscarPorId(1L)).thenReturn(Optional.of(MARIA));
        when(consultarUnidad.buscarPorId(1L)).thenReturn(Optional.of(A102));
        when(repositoryPort.existeVigente(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(comando(1L, 1L)))
                .isInstanceOf(ResidenciaVigenteDuplicadaException.class);
        verify(repositoryPort, never()).guardar(any());
    }

    @Test
    void listarDevuelveTodasLasResidencias() {
        Residencia r = new Residencia(5L, 1L, 1L, TipoResidencia.INQUILINO, INICIO, null, EstadoResidencia.VIGENTE);
        when(repositoryPort.listar()).thenReturn(List.of(r));

        assertThat(service.listar()).containsExactly(r);
    }

    @Test
    void listarPorPersonaInexistenteLanzaNoEncontrada() {
        when(consultarPersona.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.listarPorPersona(99L))
                .isInstanceOf(PersonaNoEncontradaException.class);
    }

    @Test
    void listarPorPersonaDevuelveSusResidencias() {
        Residencia r = new Residencia(5L, 1L, 1L, TipoResidencia.INQUILINO, INICIO, null, EstadoResidencia.VIGENTE);
        when(consultarPersona.buscarPorId(1L)).thenReturn(Optional.of(MARIA));
        when(repositoryPort.listarPorPersonaId(1L)).thenReturn(List.of(r));

        assertThat(service.listarPorPersona(1L)).containsExactly(r);
    }
}
