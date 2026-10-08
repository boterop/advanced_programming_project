package com.project.advanced.project.application.dto.request

data class OcupanteRequest(
    @field:NotBlank(message = "El documento de identidad no puede estar vacio")
    @field:Size(min = 7, max = 10, message = "El documento de identidad debe tener 9 caracteres")
    val documentoIdentidad: String,
    @field:NotBlank(message = "El nombre no puede estar vacio")
    val nombre: String,
    @field:NotBlank(message = "El correo electrónico no puede estar vacio")
    @field:Email(message = "El correo electrónico no es valido")
    val correoElectronico: String,
    @field:NotNull(message = "La fecha de nacimiento es obligatoria")
    @field:Past(message = "La fecha de nacimiento no puede ser futura")
    val fechaNacimiento: LocalDate,
)
