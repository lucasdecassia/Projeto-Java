package Projeto.java.question3;

public class ExternalLibAdapter implements PaymentProcessor {
    private final ExternalLibAPI stripeAPI = new ExternalLibAPI();

    @Override
    public boolean processPayment(String customerId, double amount) {
        try {
            ExternalLibAPI.PaymentResult result = stripeAPI.charge(customerId, amount);
            return result.isSuccessful();
        } catch (Exception e) {
            System.err.println("ExternalLib payment failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean refundPayment(String transactionId) {
        try {
            ExternalLibAPI.RefundResult result = stripeAPI.issueRefund(transactionId);
            return result.isSuccessful();
        } catch (Exception e) {
            System.err.println("ExternalLib refund failed: " + e.getMessage());
            return false;
        }
    }
}
