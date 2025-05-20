package Projeto.java.question8;

/**
 * Exceção base para todas as exceções relacionadas ao sistema de plantas.
 */
public class PlantException extends RuntimeException {
    
    public PlantException(String message) {
        super(message);
    }
    
    public PlantException(String message, Throwable cause) {
        super(message, cause);
    }
}