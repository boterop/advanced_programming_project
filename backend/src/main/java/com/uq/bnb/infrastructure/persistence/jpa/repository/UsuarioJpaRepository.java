package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.application.exception.UsuarioDuplicadoException;
import com.uq.bnb.application.port.UsuarioRepository;
import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.infrastructure.persistence.jpa.entity.UsuarioEntity;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class UsuarioJpaRepository implements UsuarioRepository {
    private final UsuarioJpaDataRepository dataRepository;

    public UsuarioJpaRepository(UsuarioJpaDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<CuentaUsuario> buscarPorId(ID id) {
        return dataRepository.findById(UUID.fromString(id.valor())).map(UsuarioJpaRepository::aDominio);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<CuentaUsuario> buscarPorCorreo(String correo) {
        return dataRepository.findByCorreo(correo).map(UsuarioJpaRepository::aDominio);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean existePorCorreo(String correo) {
        return dataRepository.existsByCorreo(correo);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean existePorDocumento(String documento) {
        return dataRepository.existsByDocumento(documento);
    }

    @Override
    public void guardar(CuentaUsuario cuenta) {
        try {
            // saveAndFlush: si dos registros simultáneos pasaron la verificación previa, la restricción
            // única de la base de datos falla aquí y se traduce a 409 en lugar de un 500.
            dataRepository.saveAndFlush(aEntidad(cuenta));
        } catch (DataIntegrityViolationException e) {
            throw new UsuarioDuplicadoException("correo electrónico o documento de identidad");
        }
    }

    private static UsuarioEntity aEntidad(CuentaUsuario cuenta) {
        return new UsuarioEntity(
                UUID.fromString(cuenta.id().valor()),
                cuenta.nombre(),
                cuenta.correo(),
                cuenta.contrasenaCifrada(),
                cuenta.telefono(),
                cuenta.documento(),
                cuenta.fechaNacimiento(),
                cuenta.rol(),
                cuenta.activo());
    }

    private static CuentaUsuario aDominio(UsuarioEntity entidad) {
        return new CuentaUsuario(
                new ID(entidad.getId().toString()),
                entidad.getNombre(),
                entidad.getCorreo(),
                entidad.getContrasena(),
                entidad.getTelefono(),
                entidad.getDocumento(),
                entidad.getFechaNacimiento(),
                entidad.getRol(),
                entidad.isActivo());
    }
}
