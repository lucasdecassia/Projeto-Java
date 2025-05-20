package Projeto.java.question3;

/**
 * API Mock do PayPal (simulando a biblioteca de terceiros).
 */
public class PayPalAPI {
    public String makePayment(String customerId, double amount) {
        System.out.println("PayPal: Processing payment of $" + amount + " for customer " + customerId);
        return "PAY-" + System.currentTimeMillis();
    }

    public boolean refund(String transactionId) {
        System.out.println("PayPal: Refunding transaction " + transactionId);
        return true;
    }
}