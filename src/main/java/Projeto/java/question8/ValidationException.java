package Projeto.java.question8;

/**
 * Exceção lançada quando ocorre um erro de validação nos dados de uma planta.
 */
public class ValidationException extends PlantException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}