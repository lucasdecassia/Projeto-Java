package Projeto.java.question3;

/**
 * Aqui demostro como podemos usar um padrão de projeto para desacoplar código de uma biblioteca de terceiros
 * que pode ser substituída no futuro.
 */
public class DecouplingApp {

    public static void main(String[] args) {
        PaymentProcessor payPalPaymentAdapter = new PayPalPaymentAdapter();

        boolean success = payPalPaymentAdapter.processPayment("BankJava", 100.0);
        System.out.println("Pagamento processado com sucesso: " + success);

        PaymentProcessor externalLibAdapter = new ExternalLibAdapter();

        success = externalLibAdapter.processPayment("BankJava", 100.0);
        System.out.println("Pagamento processado com sucesso com novo provedor: " + success);
     }
    }

    /**
     * 
     * 1. O código de aplicação depende da interface PaymentProcessor, onde pode obtar por meio de um adaptador.
     * 2. Facilmente pode alternar entre diferentes provedores de pagamento criando novos adaptadores, pois é so implementar a interface de contrato.
     */

