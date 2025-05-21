package Projeto.java.question2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Customer {
    private final Long id;
    private String name;
    private String email;

    public Customer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    /**
     * Sobrescrever equals() é sempre bom quando...
     * 1. Objetos precisam ser comparados com base em sua igualdade lógica em vez de igualdade de referência
     * 2. Objetos que são usados como chaves em coleções baseadas em hash (HashMap, HashSet)
     * 3. Objetos são comparados em coleções como ArrayList, LinkedList
     *
     * Aqui, dois objetos Customer iguais se eles tiverem o mesmo ID,
     * independentemente de outros atributos.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass()) return false;

        Customer customer = (Customer) object;

        return Objects.equals(id, customer.id);
    }

    /**
     * Ao sobrescrever equals(), também deverar sobrescrever hashCode() para manter o contrato...
     * 1. Se dois objetos são iguais de acordo com equals(), vai ter o mesmo hashCode()
     * 2. Se dois objetos têm o mesmo hashCode(), então eles não são necessariamente iguais
     * 3. hashCode() deve retornar o mesmo valor para o mesmo objeto durante uma única execução
     *
     * Aqui o mesmo campo (id) para hashCode() que foi usado para equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}