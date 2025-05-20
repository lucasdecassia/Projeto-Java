package Projeto.java.question8;

import java.util.List;
import java.util.Optional;

/**
 * Interface para acesso aos dados de plantas.
 */
public interface PlantRepository {
    
    /**
     * Salva uma planta no repositório.
     * 
     * @param plant A planta a ser salva
     * @return A planta salva com ID gerado (se for uma nova planta)
     */
    Plant save(Plant plant);
    
    /**
     * Verifica se existe uma planta com o código especificado.
     * 
     * @param code O código da planta
     * @return true se existir, false caso contrário
     */
    boolean existsByCode(String code);
    
    /**
     * Busca uma planta pelo código.
     * 
     * @param code O código da planta
     * @return A planta encontrada ou vazio se não existir
     */
    Optional<Plant> findByCode(String code);
    
    /**
     * Busca plantas que contenham a descrição especificada.
     * 
     * @param description Parte da descrição a ser buscada
     * @return Lista de plantas que contêm a descrição informada
     */
    List<Plant> findByDescriptionContaining(String description);
    
    /**
     * Busca todas as plantas do repositório.
     * 
     * @return Lista com todas as plantas
     */
    List<Plant> findAll();
    
    /**
     * Remove uma planta do repositório.
     * 
     * @param plant A planta a ser removida
     */
    void delete(Plant plant);
}