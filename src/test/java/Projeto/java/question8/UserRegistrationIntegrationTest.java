package Projeto.java.question8;


@AutoConfigureMockMvc
@DisplayName("Testes de Integração de Registro de Usuário")
class UserRegistrationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar, atualizar e excluir usuário através da API")
    void shouldCreateUpdateAndDeleteUserThroughAPI() throws Exception {
        UserDto userDto = new UserDto("Lucas", "lucas@java.com", "123 Main St", "555-1234");

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long userId = JsonPath.parse(response).read("$.id", Long.class);

        UserDto updatedUserDto = new UserDto("John Updated", "lucas@java.com", "456 New St", "555-5678");

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.address").value("456 New St"));

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("John Updated");
        assertThat(updatedUser.getAddress()).isEqualTo("456 New St");

        mockMvc.perform(delete("/api/users/" + userId)
                        .header("X-Admin-Auth", "true")) // Autenticação de administrador simplificada para exemplo
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(userId)).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar e-mail duplicado durante o registro")
    void shouldRejectDuplicateEmailDuringRegistration() throws Exception {
        UserDto user1 = new UserDto("Lucas", "lucas@java.com", "123 Main St", "555-1234");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user1)))
                .andExpect(status().isCreated());

        UserDto user2 = new UserDto("Lucas", "lucas@java.com", "456 Oak St", "555-5678");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("e-mail duplicado")));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}