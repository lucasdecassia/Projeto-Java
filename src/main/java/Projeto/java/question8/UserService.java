package Projeto.java.question8;

/**
 * Interface para serviços relacionados a usuários.
 */
public interface UserService {
    
    /**
     * Obtém o nome de usuário do usuário atualmente autenticado.
     * 
     * @return O nome de usuário
     */
    String getCurrentUsername();
    
    /**
     * Verifica se o usuário atualmente autenticado é um administrador.
     * 
     * @return true se o usuário for administrador, false caso contrário
     */
    boolean isCurrentUserAdmin();
}