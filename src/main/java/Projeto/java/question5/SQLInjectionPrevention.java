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
 * Injeção SQL é uma técnica de injeção de código que explora vulnerabilidades em aplicações
 * que interagem com bancos de dados. Ocorre quando a entrada do usuário é incorporada diretamente
 * em consultas SQL sem validação ou sanitização adequada.
 */
public class SQLInjectionPrevention {

    // Parâmetros de conexão com o banco de dados (apenas para demonstração)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";


    /**
     * Método principal para demonstrar técnicas de prevenção.
     */
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

    /**
     * O método demonstra como NÃO construir consultas SQL.
     * Ele concatena diretamente a entrada do usuário na string SQL, tornando vulnerável a injeção SQL.
     */
    private static String buildVulnerableQuery(String username) {
        return "SELECT * FROM users WHERE username = '" + username + "'";
    }

    /**
     * O método demonstra uma maneira insegura de consultar o banco de dados.
     * É vulnerável a ataques de injeção SQL.
     */
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

    /**
     * O método demonstra uma maneira segura de consultar o banco de dados usando consultas parametrizadas.
     * Ele previne a injeção SQL separando o comando SQL dos dados.
     */
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

    /**
     * O uso de um ORM hipotético (como Hibernate/JPA) para acesso seguro ao banco de dados.
     * ORMs normalmente lidam com parametrização automaticamente, fornecendo proteção contra injeção SQL.
     */
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

    /**
     * Técnicas de Prevenção de Injeção SQL ->
     * 
     * 1. Validação de Entrada:
     *    - Validar todas as entradas do usuário contra uma lista branca de caracteres permitidos
     *    - Rejeitar entradas que contenham caracteres SQL suspeitos (', ", --, #, etc.)
     * 
     * 2. Declarações Preparadas:
     *    - usar sempre consultas parametrizadas (PreparedStatement em JDBC)
     *    - Não concatenar a entrada do usuário diretamente em strings SQL
     * 
     * 3. ORMs:
     *    - Frameworks como Hibernate, JPA ou Spring Data parametrizam consultas automaticamente
     *    - Eles fornecem uma camada adicional de proteção
     * 
     * 4. Procedimentos Armazenados:
     *    - O uso de procedimentos armazenados com entradas parametrizadas
     *    - Isso limita o SQL que pode ser executado
     * 
     * 5. Princípio do Menor Privilégio:
     *    - Contas de banco de dados com permissões mínimas necessárias para a aplicação
     *    - Restrinjir as permissões do usuário do banco de dados apenas ao que é necessário
     * 
     * 6. Tratamento de Erros:
     *    - Implementar tratamento adequado de erros para evitar que mensagens detalhadas sejam expostas
     *    - Páginas de erro personalizadas que não revelem informações do banco de dados
     * 
     * 7. Construtores de Consultas:
     *    - Bibliotecas como QueryDSL ou JOOQ fornecem construção de SQL com segurança de tipo
     * 
     * 8. Firewalls de Banco de Dados:
     *    - Considerar usar um Firewall de Aplicação Web (WAF) ou firewall de banco de dados
     *    - Estes podem detectar e bloquear tentativas de injeção SQL
     * 
     * 9. Auditorias de Segurança Regulares:
     *    - Revisar regularmente o código em busca de vulnerabilidades de segurança
     *    - Ferramentas de análise estática de código para detectar pontos potenciais de injeção SQL
     */


}
