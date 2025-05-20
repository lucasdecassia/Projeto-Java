package Projeto.java.question8;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de plantas.
 */
@Repository
public class InMemoryPlantRepository implements PlantRepository {
    
    private final Map<String, Plant> plantsByCode = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);
    
    @Override
    public Plant save(Plant plant) {
        if (plant.getId() == null) {
            plant.setId(idSequence.getAndIncrement());
        }
        
        plantsByCode.put(plant.getCodigo(), plant);
        return plant;
    }
    
    @Override
    public boolean existsByCode(String code) {
        return plantsByCode.containsKey(code);
    }
    
    @Override
    public Optional<Plant> findByCode(String code) {
        return Optional.ofNullable(plantsByCode.get(code));
    }
    
    @Override
    public List<Plant> findByDescriptionContaining(String description) {
        if (description == null || description.isEmpty()) {
            return Collections.emptyList();
        }
        
        return plantsByCode.values().stream()
                .filter(plant -> plant.getDescricao() != null && 
                        plant.getDescricao().toLowerCase().contains(description.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Plant> findAll() {
        return new ArrayList<>(plantsByCode.values());
    }
    
    @Override
    public void delete(Plant plant) {
        if (plant != null && plant.getCodigo() != null) {
            plantsByCode.remove(plant.getCodigo());
        }
    }
}