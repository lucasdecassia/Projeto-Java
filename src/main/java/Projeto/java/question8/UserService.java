package Projeto.java.question8;

/**
 * Interface para serviços relacionados a usuários.
 */
public interface UserService {
    
    /**
     * Obtém o nome de usuário do usuário atualmente autenticado.
     */
    String getCurrentUsername();
    
    /**
     * Verifica se o usuário atualmente autenticado é um administrador.
     */
    boolean isCurrentUserAdmin();
}