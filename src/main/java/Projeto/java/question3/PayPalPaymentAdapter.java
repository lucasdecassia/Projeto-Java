package Projeto.java.question3;

//classe que adapta a biblioteca do PayPal para interface PaymentProcessor.
public class PayPalPaymentAdapter implements PaymentProcessor {
    private final PayPalAPI payPalAPI = new PayPalAPI();

    @Override
    public boolean processPayment(String customerId, double amount) {
        try {
            String payPalTransactionId = payPalAPI.makePayment(customerId, amount);
            return payPalTransactionId != null && !payPalTransactionId.isEmpty();
        } catch (Exception e) {
            System.err.println("Pagamento PayPal falhou: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean refundPayment(String transactionId) {
        try {
            return payPalAPI.refund(transactionId);
        } catch (Exception e) {
            System.err.println("Reembolso PayPal falhou: " + e.getMessage());
            return false;
        }
    }
}
