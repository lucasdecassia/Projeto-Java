package Projeto.java.question3;

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

