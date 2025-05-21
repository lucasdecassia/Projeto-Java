package Projeto.java.question3;

/**
 * API Mock do ExternalLibAPI (simulando a biblioteca de terceiros).
 */
public class ExternalLibAPI {
    public PaymentResult charge(String customerId, double amount) {
        System.out.println("ExternalLib: Processing payment of $" + amount + " for customer " + customerId);
        return new PaymentResult(true, "ch_" + System.currentTimeMillis());
    }

    public RefundResult issueRefund(String chargeId) {
        System.out.println("ExternalLib: Refunding charge " + chargeId);
        return new RefundResult(true, "re_" + System.currentTimeMillis());
    }

    public static class PaymentResult {
        private final boolean successful;
        private final String chargeId;

        public PaymentResult(boolean successful, String chargeId) {
            this.successful = successful;
            this.chargeId = chargeId;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public String getChargeId() {
            return chargeId;
        }
    }

    public static class RefundResult {
        private final boolean successful;
        private final String refundId;

        public RefundResult(boolean successful, String refundId) {
            this.successful = successful;
            this.refundId = refundId;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public String getRefundId() {
            return refundId;
        }
    }
}
