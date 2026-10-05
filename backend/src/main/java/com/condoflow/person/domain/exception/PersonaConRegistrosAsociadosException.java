package com.condoflow.person.domain.exception;

/**
 * Regla de negocio: una persona con residencias o reservas no se borra, porque se perdería el historial
 * (RN-01). La protege la FK de PostgreSQL; la alternativa es la baja lógica (estado INACTIVO).
 */
public class PersonaConRegistrosAsociadosException extends RuntimeException {

    public PersonaConRegistrosAsociadosException(Long id) {
        super("No se puede eliminar la persona " + id + " porque tiene residencias o reservas registradas. "
                + "Puede cambiar su estado a INACTIVO.");
    }
}
