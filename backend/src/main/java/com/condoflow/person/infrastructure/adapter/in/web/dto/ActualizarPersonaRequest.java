package com.condoflow.person.infrastructure.adapter.in.web.dto;

import com.condoflow.person.domain.model.EstadoPersona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para PUT /api/personas/{id}: la representación completa de la persona.
 * A diferencia del alta, incluye el estado (ACTIVO o INACTIVO), que permite la baja lógica.
 * El id no viaja en el cuerpo: lo identifica la URL.
 */
public record ActualizarPersonaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre admite como máximo 100 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido admite como máximo 100 caracteres")
        String apellido,

        @NotBlank(message = "El documento es obligatorio")
        @Pattern(regexp = "^[A-Za-z0-9-]{5,50}$",
                message = "El documento debe tener entre 5 y 50 letras, números o guiones")
        String documento,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?[0-9 ]{7,30}$",
                message = "El teléfono debe tener entre 7 y 30 dígitos y puede iniciar con +")
        String telefono,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no tiene un formato válido")
        @Size(max = 150, message = "El correo electrónico admite como máximo 150 caracteres")
        String correoElectronico,

        @NotNull(message = "El estado es obligatorio (ACTIVO o INACTIVO)")
        EstadoPersona estado
) {
}
