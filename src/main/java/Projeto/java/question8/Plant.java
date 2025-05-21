package Projeto.java.question8;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma planta no sistema.
 * Cada planta tem um código único e uma descrição opcional.
 */
@Getter
@Setter
public class Plant {
    private Long id;
    private String codigo;
    private String descricao;
    private String criadoPor;
    private LocalDateTime dataCriacao;
    private String ultimaModificacaoPor;
    private LocalDateTime dataUltimaModificacao;

    // Construtores
    public Plant() {
    }

    public Plant(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(codigo, plant.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
