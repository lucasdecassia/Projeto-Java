package Projeto.java.question5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class demonstrates techniques to prevent SQL injection attacks in web applications.
 * 
 * SQL Injection is a code injection technique that exploits vulnerabilities in applications
 * that interact with databases. It occurs when user input is directly incorporated into SQL
 * queries without proper validation or sanitization.
 */
public class SQLInjectionPrevention {

    // Database connection parameters (for demonstration only)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";
    
    /**
     * Main method to demonstrate SQL injection prevention techniques.
     */
    public static void main(String[] args) {
        // Example of a malicious input that could be used for SQL injection
        String maliciousUsername = "admin' OR '1'='1";
        
        // Demonstrate vulnerable code vs. secure code
        System.out.println("Vulnerable query would be: " + buildVulnerableQuery(maliciousUsername));
        System.out.println("Secure query uses parameterized statements instead");
        
        // Demonstrate secure methods
        try {
            List<User> users = findUsersByUsernameSecure(maliciousUsername);
            System.out.println("Found " + users.size() + " users (should be 0 with malicious input)");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    
    /**
     * VULNERABLE CODE - DO NOT USE!
     * This method demonstrates how NOT to build SQL queries.
     * It directly concatenates user input into the SQL string, making it vulnerable to SQL injection.
     */
    private static String buildVulnerableQuery(String username) {
        // NEVER DO THIS IN REAL CODE!
        return "SELECT * FROM users WHERE username = '" + username + "'";
    }
    
    /**
     * VULNERABLE CODE - DO NOT USE!
     * This method demonstrates an insecure way to query the database.
     * It is vulnerable to SQL injection attacks.
     */
    public static List<User> findUsersByUsernameVulnerable(String username) throws SQLException {
        List<User> users = new ArrayList<>();
        
        // NEVER DO THIS IN REAL CODE!
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        }
        
        return users;
    }
    
    /**
     * SECURE CODE - RECOMMENDED APPROACH
     * This method demonstrates a secure way to query the database using parameterized queries.
     * It prevents SQL injection by separating the SQL command from the data.
     */
    public static List<User> findUsersByUsernameSecure(String username) throws SQLException {
        List<User> users = new ArrayList<>();
        
        // Secure SQL with parameterized query
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters safely - this prevents SQL injection
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    users.add(user);
                }
            }
        }
        
        return users;
    }
    
    /**
     * Example of using a hypothetical ORM (like Hibernate/JPA) for secure database access.
     * ORMs typically handle parameterization automatically, providing protection against SQL injection.
     */
    public static List<User> findUsersByUsernameWithORM(String username) {
        // This is pseudocode to demonstrate how an ORM would be used
        // In a real application, you would use JPA/Hibernate or another ORM
        
        /*
        // Using JPA
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList();
        
        // Using Spring Data JPA
        return userRepository.findByUsername(username);
        
        // Using Hibernate Criteria API
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get("username"), username));
        return session.createQuery(cr).getResultList();
        */
        
        // Placeholder return for this example
        return new ArrayList<>();
    }
    
    /**
     * Additional SQL Injection Prevention Techniques:
     * 
     * 1. Input Validation:
     *    - Validate all user inputs against a whitelist of allowed characters
     *    - Reject inputs that contain suspicious SQL characters (', ", --, #, etc.)
     * 
     * 2. Use Prepared Statements:
     *    - Always use parameterized queries (PreparedStatement in JDBC)
     *    - Never concatenate user input directly into SQL strings
     * 
     * 3. Use ORMs:
     *    - Frameworks like Hibernate, JPA, or Spring Data automatically parameterize queries
     *    - They provide an additional layer of protection
     * 
     * 4. Stored Procedures:
     *    - Use stored procedures with parameterized inputs
     *    - This limits the SQL that can be executed
     * 
     * 5. Principle of Least Privilege:
     *    - Use database accounts with minimal permissions needed for the application
     *    - Restrict database user permissions to only what's necessary
     * 
     * 6. Error Handling:
     *    - Implement proper error handling to prevent detailed error messages from being exposed
     *    - Use custom error pages that don't reveal database information
     * 
     * 7. Use Query Builders:
     *    - Libraries like QueryDSL or JOOQ provide type-safe SQL building
     * 
     * 8. Database Firewalls:
     *    - Consider using a Web Application Firewall (WAF) or database firewall
     *    - These can detect and block SQL injection attempts
     * 
     * 9. Regular Security Audits:
     *    - Regularly review code for security vulnerabilities
     *    - Use static code analysis tools to detect potential SQL injection points
     */
    
    /**
     * Simple User class for demonstration purposes.
     */
    public static class User {
        private Long id;
        private String username;
        private String email;
        
        // Getters and setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
}