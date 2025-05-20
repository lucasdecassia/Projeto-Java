package Projeto.java.question8;

/**
 * Exceção lançada quando se tenta criar uma planta com um código que já existe.
 */
public class DuplicateCodeException extends PlantException {
    
    public DuplicateCodeException(String message) {
        super(message);
    }
    
    public DuplicateCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}