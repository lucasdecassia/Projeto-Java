package Projeto.java.question8;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gerenciamento de plantas.
 */
@RestController
@RequestMapping("/api/plantas")
public class PlantController {
    
    private final PlantService plantService;
    
    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }
    
    /**
     * Cria uma nova planta.
     * 
     * @param plantDTO DTO com os dados da planta a ser criada
     * @return A planta criada
     */
    @PostMapping
    public ResponseEntity<PlantDTO> createPlant(@RequestBody PlantDTO plantDTO) {
        Plant plant = plantService.createPlant(plantDTO.toEntity());
        return new ResponseEntity<>(PlantDTO.fromEntity(plant), HttpStatus.CREATED);
    }
    
    /**
     * Atualiza uma planta existente.
     * 
     * @param code Código da planta a ser atualizada
     * @param plantDTO DTO com os novos dados da planta
     * @return A planta atualizada
     */
    @PutMapping("/{code}")
    public ResponseEntity<PlantDTO> updatePlant(@PathVariable String code, @RequestBody PlantDTO plantDTO) {
        Plant plant = plantService.updatePlant(code, plantDTO.toEntity());
        return ResponseEntity.ok(PlantDTO.fromEntity(plant));
    }
    
    /**
     * Busca uma planta pelo código.
     * 
     * @param code Código da planta
     * @return A planta encontrada ou 404 se não existir
     */
    @GetMapping("/{code}")
    public ResponseEntity<PlantDTO> getPlantByCode(@PathVariable String code) {
        return plantService.findPlantByCode(code)
                .map(plant -> ResponseEntity.ok(PlantDTO.fromEntity(plant)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca plantas pela descrição.
     * 
     * @param description Parte da descrição a ser buscada
     * @return Lista de plantas que contêm a descrição informada
     */
    @GetMapping("/search")
    public ResponseEntity<List<PlantDTO>> searchPlantsByDescription(@RequestParam String description) {
        List<PlantDTO> plants = plantService.findPlantsByDescription(description).stream()
                .map(PlantDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plants);
    }
    
    /**
     * Busca todas as plantas.
     * 
     * @return Lista com todas as plantas
     */
    @GetMapping
    public ResponseEntity<List<PlantDTO>> getAllPlants() {
        List<PlantDTO> plants = plantService.findAllPlants().stream()
                .map(PlantDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plants);
    }
    
    /**
     * Exclui uma planta.
     * 
     * @param code Código da planta a ser excluída
     * @return 204 No Content se a exclusão for bem-sucedida
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deletePlant(@PathVariable String code) {
        plantService.deletePlant(code);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Manipulador de exceções para DuplicateCodeException.
     */
    @ExceptionHandler(DuplicateCodeException.class)
    public ResponseEntity<String> handleDuplicateCodeException(DuplicateCodeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    
    /**
     * Manipulador de exceções para ValidationException.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Manipulador de exceções para ResourceNotFoundException.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    /**
     * Manipulador de exceções para UnauthorizedOperationException.
     */
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<String> handleUnauthorizedOperationException(UnauthorizedOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}