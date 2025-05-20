# Testing Approach for User Registration Functionality

This document outlines the testing strategy for a user registration functionality that allows users to insert, delete, or update user information. The functionality includes fields for name, email, address, and phone, with name and email being mandatory. Emails must be unique across all users, and only administrators can delete users.

## Types of Tests

### 1. Unit Tests

Unit tests focus on testing individual components in isolation, typically mocking dependencies. For the user registration functionality, we would implement unit tests for:

#### Service Layer Tests

These tests verify the business logic in the service layer, including validation rules and business constraints.

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private SecurityService securityService;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Should create user when input is valid")
    void shouldCreateUserWhenInputIsValid() {
        // Given
        User user = new User("John Doe", "john@example.com", "123 Main St", "555-1234");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        User savedUser = userService.createUser(user);
        
        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(user);
    }
    
    @Test
    @DisplayName("Should throw exception when creating user with duplicate email")
    void shouldThrowExceptionWhenCreatingUserWithDuplicateEmail() {
        // Given
        User existingUser = new User("Jane Doe", "john@example.com", "456 Oak St", "555-5678");
        User newUser = new User("John Doe", "john@example.com", "123 Main St", "555-1234");
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        
        // When & Then
        assertThrows(DuplicateEmailException.class, () -> userService.createUser(newUser));
        verify(userRepository).findByEmail(newUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should throw exception when creating user with missing name")
    void shouldThrowExceptionWhenCreatingUserWithMissingName() {
        // Given
        User user = new User(null, "john@example.com", "123 Main St", "555-1234");
        
        // When & Then
        assertThrows(ValidationException.class, () -> userService.createUser(user));
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should throw exception when non-admin tries to delete user")
    void shouldThrowExceptionWhenNonAdminTriesToDeleteUser() {
        // Given
        Long userId = 1L;
        when(securityService.isCurrentUserAdmin()).thenReturn(false);
        
        // When & Then
        assertThrows(UnauthorizedException.class, () -> userService.deleteUser(userId));
        verify(securityService).isCurrentUserAdmin();
        verify(userRepository, never()).deleteById(anyLong());
    }
    
    @Test
    @DisplayName("Should delete user when admin requests deletion")
    void shouldDeleteUserWhenAdminRequestsDeletion() {
        // Given
        Long userId = 1L;
        when(securityService.isCurrentUserAdmin()).thenReturn(true);
        when(userRepository.existsById(userId)).thenReturn(true);
        
        // When
        userService.deleteUser(userId);
        
        // Then
        verify(securityService).isCurrentUserAdmin();
        verify(userRepository).deleteById(userId);
    }
}
```

#### Repository Layer Tests

These tests verify the data access layer, focusing on database operations.

```java
@DataJpaTest
@DisplayName("User Repository Tests")
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // Given
        User user = new User("John Doe", "john@example.com", "123 Main St", "555-1234");
        userRepository.save(user);
        
        // When
        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        
        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
    }
    
    @Test
    @DisplayName("Should not find user by non-existent email")
    void shouldNotFindUserByNonExistentEmail() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        
        // Then
        assertThat(foundUser).isEmpty();
    }
    
    @Test
    @DisplayName("Should enforce unique email constraint")
    void shouldEnforceUniqueEmailConstraint() {
        // Given
        User user1 = new User("John Doe", "john@example.com", "123 Main St", "555-1234");
        userRepository.save(user1);
        
        User user2 = new User("Jane Doe", "john@example.com", "456 Oak St", "555-5678");
        
        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
            userRepository.flush();
        });
    }
}
```

#### Controller Layer Tests

These tests verify the REST API endpoints, focusing on request handling and response generation.

```java
@WebMvcTest(UserController.class)
@DisplayName("User Controller Tests")
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    @DisplayName("Should create user and return 201 Created")
    void shouldCreateUserAndReturn201Created() throws Exception {
        // Given
        UserDto userDto = new UserDto("John Doe", "john@example.com", "123 Main St", "555-1234");
        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getAddress(), userDto.getPhone());
        user.setId(1L);
        
        when(userService.createUser(any(User.class))).thenReturn(user);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    
    @Test
    @DisplayName("Should return 400 Bad Request when creating user with invalid data")
    void shouldReturn400BadRequestWhenCreatingUserWithInvalidData() throws Exception {
        // Given
        UserDto userDto = new UserDto(null, "john@example.com", "123 Main St", "555-1234");
        
        when(userService.createUser(any(User.class))).thenThrow(new ValidationException("Name is required"));
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name is required"));
    }
    
    @Test
    @DisplayName("Should return 403 Forbidden when non-admin tries to delete user")
    void shouldReturn403ForbiddenWhenNonAdminTriesToDeleteUser() throws Exception {
        // Given
        doThrow(new UnauthorizedException("Only administrators can delete users"))
                .when(userService).deleteUser(1L);
        
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Only administrators can delete users"));
    }
    
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 2. Integration Tests

Integration tests verify that different components work together correctly. For the user registration functionality, we would implement:

```java
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("User Registration Integration Tests")
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
    @DisplayName("Should create, update, and delete user through API")
    void shouldCreateUpdateAndDeleteUserThroughAPI() throws Exception {
        // Create user
        UserDto userDto = new UserDto("John Doe", "john@example.com", "123 Main St", "555-1234");
        
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        Long userId = JsonPath.parse(response).read("$.id", Long.class);
        
        // Update user
        UserDto updatedUserDto = new UserDto("John Updated", "john@example.com", "456 New St", "555-5678");
        
        mockMvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.address").value("456 New St"));
        
        // Verify in database
        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("John Updated");
        assertThat(updatedUser.getAddress()).isEqualTo("456 New St");
        
        // Delete user (as admin)
        // Note: In a real test, you would need to authenticate as an admin
        mockMvc.perform(delete("/api/users/" + userId)
                .header("X-Admin-Auth", "true")) // Simplified admin authentication for example
                .andExpect(status().isNoContent());
        
        // Verify deletion
        assertThat(userRepository.findById(userId)).isEmpty();
    }
    
    @Test
    @DisplayName("Should reject duplicate email during registration")
    void shouldRejectDuplicateEmailDuringRegistration() throws Exception {
        // Create first user
        UserDto user1 = new UserDto("John Doe", "john@example.com", "123 Main St", "555-1234");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user1)))
                .andExpect(status().isCreated());
        
        // Try to create second user with same email
        UserDto user2 = new UserDto("Jane Doe", "john@example.com", "456 Oak St", "555-5678");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("duplicate email")));
    }
    
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 3. End-to-End Tests

End-to-end tests verify the entire application flow from the user interface to the database and back. For the user registration functionality, we would implement:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("User Registration E2E Tests")
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
    @DisplayName("Should register new user through UI")
    void shouldRegisterNewUserThroughUI() {
        // Navigate to registration page
        driver.get("http://localhost:" + port + "/register");
        
        // Fill in registration form
        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys("john@example.com");
        driver.findElement(By.id("address")).sendKeys("123 Main St");
        driver.findElement(By.id("phone")).sendKeys("555-1234");
        
        // Submit form
        driver.findElement(By.id("submit-button")).click();
        
        // Verify success message
        WebElement successMessage = driver.findElement(By.className("success-message"));
        assertThat(successMessage.getText()).contains("Registration successful");
        
        // Verify user appears in user list (assuming redirect to user list page)
        WebElement userTable = driver.findElement(By.id("user-table"));
        assertThat(userTable.getText()).contains("John Doe");
        assertThat(userTable.getText()).contains("john@example.com");
    }
    
    @Test
    @DisplayName("Should show validation errors for invalid input")
    void shouldShowValidationErrorsForInvalidInput() {
        // Navigate to registration page
        driver.get("http://localhost:" + port + "/register");
        
        // Submit form without filling required fields
        driver.findElement(By.id("submit-button")).click();
        
        // Verify validation errors
        WebElement nameError = driver.findElement(By.id("name-error"));
        WebElement emailError = driver.findElement(By.id("email-error"));
        
        assertThat(nameError.getText()).contains("Name is required");
        assertThat(emailError.getText()).contains("Email is required");
    }
}
```

## Edge Cases and Scenarios to Test

### 1. Validation Tests

- **Empty Required Fields**: Test that the system properly validates and rejects submissions with empty name or email.
- **Invalid Email Format**: Test that the system validates email format (e.g., must contain @, domain, etc.).
- **Maximum Field Lengths**: Test behavior when inputs exceed maximum allowed lengths.
- **Special Characters**: Test handling of special characters in name, email, address, and phone.
- **Whitespace Handling**: Test how the system handles leading/trailing whitespace in inputs.

### 2. Business Rule Tests

- **Duplicate Email**: Test that the system prevents creating users with duplicate emails.
- **Admin Deletion Rights**: Test that only administrators can delete users.
- **Non-Admin Deletion Attempt**: Test that non-administrators receive appropriate error messages when attempting to delete users.
- **Update Existing User**: Test that updating a user's information works correctly.
- **Update to Duplicate Email**: Test that updating a user's email to one that already exists is prevented.

### 3. Security Tests

- **Authentication**: Test that unauthenticated users cannot access protected endpoints.
- **Authorization**: Test that users with different roles have appropriate access levels.
- **Input Sanitization**: Test that the system properly sanitizes inputs to prevent injection attacks.
- **CSRF Protection**: Test that the system implements CSRF protection for form submissions.

### 4. Performance Tests

- **Concurrent User Registration**: Test system behavior when multiple users register simultaneously.
- **Large Dataset Handling**: Test system performance with a large number of registered users.
- **Response Time**: Test that the registration process completes within acceptable time limits.

## Example Test Case

Here's a detailed example of a test case for the user registration functionality:

### Test Case: Register New User with Valid Information

**Objective**: Verify that a new user can be registered with valid information.

**Preconditions**:
- The application is running.
- The database is accessible.
- No user with email "john@example.com" exists in the system.

**Test Steps**:
1. Navigate to the user registration page.
2. Enter "John Doe" in the name field.
3. Enter "john@example.com" in the email field.
4. Enter "123 Main St" in the address field.
5. Enter "555-1234" in the phone field.
6. Click the "Register" button.

**Expected Results**:
1. The system displays a success message.
2. A new user record is created in the database with the provided information.
3. The user is redirected to a confirmation page or user list.

**Postconditions**:
- A new user with email "john@example.com" exists in the system.
- The user can be retrieved by searching for their email.

## Conclusion

A comprehensive testing strategy for the user registration functionality should include unit tests, integration tests, and end-to-end tests. By covering various scenarios and edge cases, we can ensure that the functionality works correctly, validates inputs properly, enforces business rules, and provides a good user experience.

The tests should be automated and included in the CI/CD pipeline to ensure that any changes to the codebase do not break existing functionality. Regular test execution and code coverage analysis will help maintain the quality of the application over time.