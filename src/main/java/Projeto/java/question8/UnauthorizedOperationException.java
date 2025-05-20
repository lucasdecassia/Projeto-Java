package Projeto.java.question8;

/**
 * Exceção lançada quando um usuário tenta realizar uma operação para a qual não tem autorização.
 */
public class UnauthorizedOperationException extends PlantException {
    
    public UnauthorizedOperationException(String message) {
        super(message);
    }
    
    public UnauthorizedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}