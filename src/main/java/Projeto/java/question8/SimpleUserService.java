package Projeto.java.question8;

import org.springframework.stereotype.Service;

/**
 * Implementação simples do serviço de usuários para fins de demonstração.
 * Em um ambiente real, isso seria integrado com um sistema de autenticação.
 */
@Service
public class SimpleUserService implements UserService {

    // Em um ambiente real, essas informações viriam de um contexto de segurança
    private static final String CURRENT_USER = "usuario_sistema";
    private static final boolean IS_ADMIN = true; // Para fins de demonstração, consideramos o usuário como admin

    @Override
    public String getCurrentUsername() {
        // Em um ambiente real, isso seria obtido do contexto de segurança
        return CURRENT_USER;
    }

    @Override
    public boolean isCurrentUserAdmin() {
        // Em um ambiente real, isso seria verificado com base nas permissões do usuário
        return IS_ADMIN;
    }
}
