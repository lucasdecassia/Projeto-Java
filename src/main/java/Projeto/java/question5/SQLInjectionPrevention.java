package Projeto.java.question5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLInjectionPrevention {

    // Parâmetros de conexão com o banco de dados (apenas para demonstração)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";


    public static void main(String[] args) {
        // Uma entrada maliciosa que poderia ser usada
        String maliciousUsername = "admin' OR '1'='1";

        // Código vulnerável vs. código seguro
        System.out.println("A consulta vulnerável seria: " + buildVulnerableQuery(maliciousUsername));
        System.out.println("A consulta segura usa declarações parametrizadas em vez disso");

        // Métodos seguros
        try {
            List<User> users = findUsersByUsernameSecure(maliciousUsername);
            System.out.println("Encontrados " + users.size() + " usuários (deve ser 0 com entrada maliciosa)");
        } catch (SQLException e) {
            System.err.println("Erro de banco de dados: " + e.getMessage());
        }
    }

    private static String buildVulnerableQuery(String username) {
        return "SELECT * FROM users WHERE username = '" + username + "'";
    }

    public static List<User> findUsersByUsernameVulnerable(String username) throws SQLException {
        List<User> users = new ArrayList<>();

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

    public static List<User> findUsersByUsernameSecure(String username) throws SQLException {
        List<User> users = new ArrayList<>();

        // SQL seguro com consulta parametrizada
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define parâmetros com segurança - isso previne injeção SQL
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

    public static List<User> findUsersByUsernameWithORM(String username) {
        // Este é um pseudocódigo para demonstrar como um ORM seria usado
        // Em uma aplicação real, você usaria JPA/Hibernate ou outro ORM

        /*
        // Usando JPA
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList();

        // Usando Spring Data JPA
        return userRepository.findByUsername(username);

        // Usando Hibernate Criteria API
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get("username"), username));
        return session.createQuery(cr).getResultList();
        */

        // Retorno de espaço reservado para este exemplo
        return new ArrayList<>();
    }
}
