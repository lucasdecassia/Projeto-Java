package Projeto.java.question2;

import java.util.HashMap;
import java.util.Map;

class EqualsAndHashCodeApp {

    public static void main(String[] args) {
        Customer customer1 = new Customer(1L, "Lucas de Cassia", "lucas@java.com");
        Customer customer2 = new Customer(1L, "Lucas Magalhães", "lucas.magalhaes@java.com");
        Customer customer3 = new Customer(1L, "Lucas Oliveira", "lucas.oliveira@java.com");

        System.out.println("Deve retornar true se customers forem iguais: " + customer1.equals(customer2));
        //Foi criado metodo equals() para comparar os Ids.
        //Deve comparar Ids iguais e retornar true.
     }

    }

