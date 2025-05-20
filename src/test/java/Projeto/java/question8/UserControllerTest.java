package Projeto.java.question8;

@WebMvcTest(UserController.class)
@DisplayName("Testes do Controlador de Usuário")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Deve criar usuário e retornar 201 Created")
    void shouldCreateUserAndReturn201Created() throws Exception {
        UserDto userDto = new UserDto("Lucas", "lucas@java.com", "123 Main St", "555-1234");
        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getAddress(), userDto.getPhone());
        user.setId(1L);

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lucas"))
                .andExpect(jsonPath("$.email").value("lucas@java.com"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao criar usuário com dados inválidos")
    void shouldReturn400BadRequestWhenCreatingUserWithInvalidData() throws Exception {
        UserDto userDto = new UserDto(null, "lucas@java.com", "123 Main St", "555-1234");

        when(userService.createUser(any(User.class))).thenThrow(new ValidationException("Nome é obrigatório"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Nome é obrigatório"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden quando não-admin tenta excluir usuário")
    void shouldReturn403ForbiddenWhenNonAdminTriesToDeleteUser() throws Exception {
        doThrow(new UnauthorizedException("Apenas administradores podem excluir usuários"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Apenas administradores podem excluir usuários"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}