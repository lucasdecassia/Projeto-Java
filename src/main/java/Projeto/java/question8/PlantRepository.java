package Projeto.java.question8;

import java.util.List;
import java.util.Optional;

/**
 * Interface para acesso aos dados de plantas.
 */
public interface PlantRepository {
    
    /**
     * Salva uma planta no repositório.
     */
    Plant save(Plant plant);
    
    /**
     * Verifica se existe uma planta com o código especificado.
     */
    boolean existsByCode(String code);
    
    /**
     * Busca uma planta pelo código.
     */
    Optional<Plant> findByCode(String code);
    
    /**
     * Busca plantas que contenham a descrição especificada.
     */
    List<Plant> findByDescriptionContaining(String description);
    
    /**
     * Busca todas as plantas do repositório.
     */
    List<Plant> findAll();
    
    /**
     * Remove uma planta do repositório.
     */
    void delete(Plant plant);
}