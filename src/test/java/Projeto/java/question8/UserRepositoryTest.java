package Projeto.java.question8;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
 @DisplayName("Testes do Repositório de Usuário")
 public class UserRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        @Test
        @DisplayName("Deve encontrar usuário por e-mail")
        void shouldFindUserByEmail() {
            User user = new User("Lucas", "lucas@java.com", "123 Main St", "555-1234");
            userRepository.save(user);

            Optional<User> foundUser = userRepository.findByEmail("lucas@java.com");

            assertThat(foundUser).isPresent();
            assertThat(foundUser.get().getName()).isEqualTo("Lucas");
        }

        @Test
        @DisplayName("Não deve encontrar usuário por e-mail inexistente")
        void shouldNotFindUserByNonExistentEmail() {
            Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

            assertThat(foundUser).isEmpty();
        }

        @Test
        @DisplayName("Deve aplicar restrição de e-mail único")
        void shouldEnforceUniqueEmailConstraint() {
            User user1 = new User("Lucas", "lucas@java.com", "123 Main St", "555-1234");
            userRepository.save(user1);

            User user2 = new User("Lucas", "lucas@java.com", "456 Oak St", "555-5678");

            assertThrows(DataIntegrityViolationException.class, () -> {
                userRepository.save(user2);
                userRepository.flush();
            });
        }
    }

