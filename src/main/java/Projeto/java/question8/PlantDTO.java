package Projeto.java.question8;

/**
 * DTO (Data Transfer Object) para transferência de dados de plantas entre camadas.
 */
public class PlantDTO {
    private String codigo;
    private String descricao;
    
    // Construtores
    public PlantDTO() {
    }
    
    public PlantDTO(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    
    // Getters e setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Converte este DTO para uma entidade Plant.
     */
    public Plant toEntity() {
        return new Plant(codigo, descricao);
    }
    
    /**
     * Cria um DTO a partir de uma entidade Plant.
     */
    public static PlantDTO fromEntity(Plant plant) {
        return new PlantDTO(plant.getCodigo(), plant.getDescricao());
    }
}