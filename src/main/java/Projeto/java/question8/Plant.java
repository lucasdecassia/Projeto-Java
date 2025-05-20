package Projeto.java.question8;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa uma planta no sistema.
 * Cada planta tem um código único e uma descrição opcional.
 */
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

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getUltimaModificacaoPor() {
        return ultimaModificacaoPor;
    }

    public void setUltimaModificacaoPor(String ultimaModificacaoPor) {
        this.ultimaModificacaoPor = ultimaModificacaoPor;
    }

    public LocalDateTime getDataUltimaModificacao() {
        return dataUltimaModificacao;
    }

    public void setDataUltimaModificacao(LocalDateTime dataUltimaModificacao) {
        this.dataUltimaModificacao = dataUltimaModificacao;
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
