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
     */
    public Optional<Plant> findPlantByCode(String code) {
        return plantRepository.findByCode(code);
    }

    /**
     * Busca plantas pela descrição (busca parcial).
     */
    public List<Plant> findPlantsByDescription(String description) {
        return plantRepository.findByDescriptionContaining(description);
    }

    /**
     * Busca todas as plantas do sistema.
     */
    public List<Plant> findAllPlants() {
        return plantRepository.findAll();
    }

    /**
     * Valida os dados de uma planta.
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
