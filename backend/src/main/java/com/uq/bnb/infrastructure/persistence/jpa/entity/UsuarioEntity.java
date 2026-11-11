package com.uq.bnb.infrastructure.persistence.jpa.entity;

import com.uq.bnb.application.security.RolUsuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 120)
    private String correo;

    // Hash BCrypt con el prefijo del algoritmo ({bcrypt}...): nunca texto plano.
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    // Único vínculo con el negocio: coincide con el documento del titular de una reserva.
    @Column(name = "documento", nullable = false, unique = true, length = 15)
    private String documento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private RolUsuario rol;

    // Eliminación lógica: un usuario inactivo existe, pero no puede iniciar sesión.
    @Column(name = "activo", nullable = false)
    private boolean activo;

    protected UsuarioEntity() {}

    public UsuarioEntity(
            UUID id,
            String nombre,
            String correo,
            String contrasena,
            String telefono,
            String documento,
            LocalDate fechaNacimiento,
            RolUsuario rol,
            boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
        this.activo = activo;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }
}
