package com.uq.bnb.infrastructure.persistence.inmemory;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
@Profile("inmemory")
public class ApartamentoRepositoryInMemory implements ApartamentoRepository {
    private final Map<String, Apartamento> repositorio = new HashMap<>();
    @Override
    public void guardar(Apartamento apartamento) {
        repositorio.put(apartamento.identificacion(), apartamento);
    }
    @Override
    public Optional<Apartamento> buscarPorId(String identificacion) {
        return Optional.ofNullable(repositorio.get(identificacion));
    }
    @Override
    public List<Apartamento> obtenerTodos() {
        return repositorio.values().stream().toList();
    }
}
