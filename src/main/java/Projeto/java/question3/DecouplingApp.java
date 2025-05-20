package Projeto.java.question3;

/**
 * Aqui demostro como podemos usar um padrão de projeto para desacoplar código de uma biblioteca de terceiros
 * que pode ser substituída no futuro.
 */
public class DecouplingApp {

    public static void main(String[] args) {
        PaymentProcessor paymentProcessor = new PayPalPaymentAdapter();

        boolean success = paymentProcessor.processPayment("BankJava", 100.0);
        System.out.println("Pagamento processado com sucesso: " + success);

        paymentProcessor = new StripePaymentAdapter();

        success = paymentProcessor.processPayment("BankJava", 100.0);
        System.out.println("Pagamento processado com sucesso com novo provedor: " + success);
     }
    }

    /**
     * 
     * 1. O código de aplicação depende da interface PaymentProcessor, não das bibliotecas de terceiros.
     * 2. Facilmente pode alternar entre diferentes provedores de pagamento criando novos adaptadores.
     * 3. Código específico de bibliotecas de terceiros é isolado nas classes adaptadoras.
     * 4. Mudanças nas bibliotecas de terceiros afetam apenas as classes adaptadoras.
     */

