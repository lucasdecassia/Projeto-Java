package Projeto.java.question3;

/**
 * Interface da aplicação para processamento de pagamentos.
 */
public interface PaymentProcessor {
    boolean processPayment(String customerId, double amount);
    boolean refundPayment(String transactionId);
}
