package Projeto.java.question2;

import java.util.HashMap;
import java.util.Map;

/**
 * A classe demonstra um cenário onde sobrescrever equals() e hashCode() é necessário em Java.
 */
public class EqualsAndHashCodeApp {

    public static void main(String[] args) {
        Customer customer1 = new Customer(1L, "Lucas de Cassia", "lucas@java.com");
        Customer customer2 = new Customer(1L, "Lucas Magalhães", "lucas.magalhaes@java.com");

        Map<Customer, String> customerData = new HashMap<>();
        customerData.put(customer1, "Dados do Cliente 1");

        System.out.println("Contém customer2: " + customerData.containsKey(customer2));
        System.out.println("Dados para customer2: " + customerData.get(customer2));

        Customer customer3 = new Customer(2L, "LucasDev", "devlucas@java.com");
        System.out.println("Contém customer3: " + customerData.containsKey(customer3));
     }
    }

