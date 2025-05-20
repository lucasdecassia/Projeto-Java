package Projeto.java.question8;

import Projeto.java.question5.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Usuário")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve criar usuário quando a entrada é válida")
    void shouldCreateUserWhenInputIsValid() {
        User user = new User("Lucas", "lucas@java.com", "123 Main St", "555-1234");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Lucas");
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com e-mail duplicado")
    void shouldThrowExceptionWhenCreatingUserWithDuplicateEmail() {
        User existingUser = new User("Lucas", "lucas@java.com", "456 Oak St", "555-5678");
        User newUser = new User("Lucas", "lucas@java.com", "123 Main St", "555-1234");
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(newUser));
        verify(userRepository).findByEmail(newUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com nome ausente")
    void shouldThrowExceptionWhenCreatingUserWithMissingName() {
        User user = new User(null, "lucas@java.com", "123 Main St", "555-1234");

        assertThrows(ValidationException.class, () -> userService.createUser(user));
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não-admin tenta excluir usuário")
    void shouldThrowExceptionWhenNonAdminTriesToDeleteUser() {
        Long userId = 1L;
        when(securityService.isCurrentUserAdmin()).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> userService.deleteUser(userId));
        verify(securityService).isCurrentUserAdmin();
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve excluir usuário quando admin solicita exclusão")
    void shouldDeleteUserWhenAdminRequestsDeletion() {
        Long userId = 1L;
        when(securityService.isCurrentUserAdmin()).thenReturn(true);
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(securityService).isCurrentUserAdmin();
        verify(userRepository).deleteById(userId);
    }
}