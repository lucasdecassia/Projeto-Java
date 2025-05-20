package Projeto.java.question8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do sistema de gerenciamento de plantas.
 */
@Configuration
public class PlantConfig {
    
    /**
     * Cria o bean do repositório de plantas.
     */
    @Bean
    public PlantRepository plantRepository() {
        return new InMemoryPlantRepository();
    }
    
    /**
     * Cria o bean do serviço de usuários.
     */
    @Bean
    public UserService userService() {
        return new SimpleUserService();
    }
    
    /**
     * Cria o bean do serviço de plantas.
     */
    @Bean
    public PlantService plantService(PlantRepository plantRepository, UserService userService) {
        return new PlantService(plantRepository, userService);
    }
}