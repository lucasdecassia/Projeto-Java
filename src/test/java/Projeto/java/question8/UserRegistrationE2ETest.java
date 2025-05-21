package Projeto.java.question8;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes E2E de Registro de Usuário")
class UserRegistrationE2ETest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Deve registrar novo usuário através da UI")
    void shouldRegisterNewUserThroughUI() {
        // Navegar para a página de registro
        driver.get("http://localhost:" + port + "/register");

        // Preencher formulário de registro
        driver.findElement(By.id("name")).sendKeys("Lucas");
        driver.findElement(By.id("email")).sendKeys("lucas@java.com");
        driver.findElement(By.id("address")).sendKeys("123 Main St");
        driver.findElement(By.id("phone")).sendKeys("555-1234");

        // Enviar formulário
        driver.findElement(By.id("submit-button")).click();

        // Verificar mensagem de sucesso
        WebElement successMessage = driver.findElement(By.className("success-message"));
        assertThat(successMessage.getText()).contains("Registro bem-sucedido");

        // Verificar se o usuário aparece na lista de usuários (assumindo redirecionamento para a página de lista de usuários)
        WebElement userTable = driver.findElement(By.id("user-table"));
        assertThat(userTable.getText()).contains("Lucas");
        assertThat(userTable.getText()).contains("lucas@java.com");
    }

    @Test
    @DisplayName("Deve mostrar erros de validação para entrada inválida")
    void shouldShowValidationErrorsForInvalidInput() {
        // Navegar para a página de registro
        driver.get("http://localhost:" + port + "/register");

        // Enviar formulário sem preencher campos obrigatórios
        driver.findElement(By.id("submit-button")).click();

        // Verificar erros de validação
        WebElement nameError = driver.findElement(By.id("name-error"));
        WebElement emailError = driver.findElement(By.id("email-error"));

        assertThat(nameError.getText()).contains("Nome é obrigatório");
        assertThat(emailError.getText()).contains("E-mail é obrigatório");
    }
}