package Projeto.java.question3;

/**
 * Esta classe adapta a biblioteca do Stripe para a interface PaymentProcessor.
 */
public class StripePaymentAdapter implements PaymentProcessor {
    private final StripeAPI stripeAPI = new StripeAPI();

    @Override
    public boolean processPayment(String customerId, double amount) {
        try {
            StripeAPI.PaymentResult result = stripeAPI.charge(customerId, amount);
            return result.isSuccessful();
        } catch (Exception e) {
            System.err.println("Stripe payment failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean refundPayment(String transactionId) {
        try {
            StripeAPI.RefundResult result = stripeAPI.issueRefund(transactionId);
            return result.isSuccessful();
        } catch (Exception e) {
            System.err.println("Stripe refund failed: " + e.getMessage());
            return false;
        }
    }
}
