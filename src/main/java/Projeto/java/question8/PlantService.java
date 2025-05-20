package Projeto.java.question8;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar operações relacionadas a plantas.
 */
@Service
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserService userService;

    public PlantService(PlantRepository plantRepository, UserService userService) {
        this.plantRepository = plantRepository;
        this.userService = userService;
    }

    /**
     * Cria uma nova planta no sistema.
     * 
     * @param plant A planta a ser criada
     * @return A planta criada com ID gerado
     * @throws DuplicateCodeException Se já existir uma planta com o mesmo código
     * @throws ValidationException Se a planta não passar nas validações
     */
    public Plant createPlant(Plant plant) {
        validatePlant(plant);

        if (plantRepository.existsByCode(plant.getCodigo())) {
            throw new DuplicateCodeException("Uma planta com este código já existe");
        }

        // Definir informações de auditoria
        String currentUser = userService.getCurrentUsername();
        LocalDateTime now = LocalDateTime.now();

        plant.setCriadoPor(currentUser);
        plant.setDataCriacao(now);
        plant.setUltimaModificacaoPor(currentUser);
        plant.setDataUltimaModificacao(now);

        return plantRepository.save(plant);
    }

    /**
     * Atualiza uma planta existente.
     * 
     * @param code O código da planta a ser atualizada
     * @param plant Os novos dados da planta
     * @return A planta atualizada
     * @throws ResourceNotFoundException Se a planta não for encontrada
     * @throws ValidationException Se a planta não passar nas validações
     */
    public Plant updatePlant(String code, Plant plant) {
        validatePlant(plant);

        Plant existingPlant = plantRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Planta não encontrada"));

        // Não permitir alteração do código
        if (!existingPlant.getCodigo().equals(plant.getCodigo())) {
            throw new ValidationException("O código da planta não pode ser alterado");
        }

        existingPlant.setDescricao(plant.getDescricao());

        // Atualizar informações de auditoria
        String currentUser = userService.getCurrentUsername();
        LocalDateTime now = LocalDateTime.now();

        existingPlant.setUltimaModificacaoPor(currentUser);
        existingPlant.setDataUltimaModificacao(now);

        return plantRepository.save(existingPlant);
    }

    /**
     * Exclui uma planta do sistema.
     * 
     * @param code O código da planta a ser excluída
     * @throws UnauthorizedOperationException Se o usuário não for administrador
     * @throws ResourceNotFoundException Se a planta não for encontrada
     */
    public void deletePlant(String code) {
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedOperationException("Apenas administradores podem excluir plantas");
        }

        Plant plant = plantRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Planta não encontrada"));

        plantRepository.delete(plant);
    }

    /**
     * Busca uma planta pelo código.
     * 
     * @param code O código da planta
     * @return A planta encontrada ou vazio se não existir
     */
    public Optional<Plant> findPlantByCode(String code) {
        return plantRepository.findByCode(code);
    }

    /**
     * Busca plantas pela descrição (busca parcial).
     * 
     * @param description Parte da descrição a ser buscada
     * @return Lista de plantas que contêm a descrição informada
     */
    public List<Plant> findPlantsByDescription(String description) {
        return plantRepository.findByDescriptionContaining(description);
    }

    /**
     * Busca todas as plantas do sistema.
     * 
     * @return Lista com todas as plantas
     */
    public List<Plant> findAllPlants() {
        return plantRepository.findAll();
    }

    /**
     * Valida os dados de uma planta.
     * 
     * @param plant A planta a ser validada
     * @throws ValidationException Se a planta não passar nas validações
     */
    private void validatePlant(Plant plant) {
        if (plant.getCodigo() == null || plant.getCodigo().isEmpty()) {
            throw new ValidationException("O código da planta é obrigatório");
        }

        if (!plant.getCodigo().matches("\\d+")) {
            throw new ValidationException("O código da planta deve conter apenas caracteres numéricos");
        }

        if (plant.getDescricao() != null && plant.getDescricao().length() > 10) {
            throw new ValidationException("A descrição da planta não pode exceder 10 caracteres");
        }
    }
}
