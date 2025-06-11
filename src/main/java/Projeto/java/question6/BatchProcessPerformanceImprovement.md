# Melhorando o Desempenho de Processos em Lote

## Etapas de Diagnóstico

### 1. Perfilamento e Monitoramento

```java
public void processarLote() {
    long tempoInicial = System.currentTimeMillis();
    log.info("Iniciando processo em lote");

    // Operações de banco de dados
    long tempoInicioBD = System.currentTimeMillis();
    executarOperacoesBancoDados();
    log.info("Operações de banco de dados concluídas em {} ms", System.currentTimeMillis() - tempoInicioBD);

    // Operações FTP
    long tempoInicioFTP = System.currentTimeMillis();
    executarOperacoesFTP();
    log.info("Operações FTP concluídas em {} ms", System.currentTimeMillis() - tempoInicioFTP);

    log.info("Processo em lote concluído em {} ms", System.currentTimeMillis() - tempoInicial);
}
```

### 2. Análise de Desempenho do Banco de Dados

```sql
EXPLAIN SELECT * FROM tabela_grande WHERE coluna_nao_indexada = 'valor';
```

### 3. Análise de E/S e Rede 

## Estratégias de Melhoria de Desempenho

### 1. Otimização de Banco de Dados

#### Otimização de Consultas

```sql
CREATE INDEX idx_cliente_id ON pedidos(cliente_id);
```

#### Otimização de Processamento em Lote

```java
public void inserirEmMassa(List<Pedido> pedidos) {
    String sql = "INSERT INTO pedidos (id, cliente_id, valor) VALUES (?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        conn.setAutoCommit(false);

        for (int i = 0; i < pedidos.size(); i++) {
            Pedido pedido = pedidos.get(i);
            pstmt.setLong(1, pedido.getId());
            pstmt.setLong(2, pedido.getClienteId());
            pstmt.setBigDecimal(3, pedido.getValor());
            pstmt.addBatch();

            // Executar em lotes de 1000
            if (i % 1000 == 0) {
                pstmt.executeBatch();
            }
        }

        pstmt.executeBatch();
        conn.commit();
    } catch (SQLException e) {
        // Tratar exceção
    }
}
```

### 2. Otimização de Transferência FTP

**Exemplo de transferência paralela de arquivos->**

```java
public void transferirArquivosEmParalelo(List<File> arquivos, String destino) {
    ExecutorService executor = Executors.newFixedThreadPool(10);

    for (File arquivo : arquivos) {
        executor.submit(() -> {
            try {
                transferirArquivo(arquivo, destino);
            } catch (Exception e) {
                log.error("Erro ao transferir arquivo: {}", arquivo.getName(), e);
            }
        });
    }

    executor.shutdown();
    try {
        executor.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

### 3. Otimização em Nível de Aplicação

```java
public class CacheCliente {
    private final Map<Long, Cliente> cache = new ConcurrentHashMap<>();
    private final ClienteRepositorio repositorio;

    public CacheCliente(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Cliente getCliente(Long id) {
        return cache.computeIfAbsent(id, repositorio::findById);
    }

    public void invalidar(Long id) {
        cache.remove(id);
    }

    public void invalidarTodos() {
        cache.clear();
    }
}
```

### 4. Otimização em Nível de Sistema

## Estudo de Caso: Otimizando um Processo em Lote de Dados de Clientes

### Situação Inicial
- Um processo em lote noturno que extrai dados de clientes de um banco de dados, processa-os e carrega relatórios para um servidor FTP.
- O processo leva 4 horas para ser concluído, processando 1 milhão de registros de clientes.
- As consultas ao banco de dados são lentas e as transferências FTP são sequenciais.

### Descobertas de Diagnóstico
- As consultas ao banco de dados não estavam usando índices efetivamente.
- Grandes conjuntos de resultados estavam sendo carregados na memória de uma só vez.
- As transferências FTP eram feitas sequencialmente com tamanhos de buffer pequenos.
- Nenhuma paralelização estava sendo usada para cálculos intensivos de CPU.

### Otimizações Aplicadas
1. **Otimização de Banco de Dados**:
   - Adicionados índices apropriados às tabelas de clientes e transações.
   - Reescritas consultas para usar colunas indexadas.
   - Implementada paginação para processar dados em blocos de 10.000 registros.

2. **Otimização de FTP**:
   - Implementadas transferências paralelas de arquivos (10 transferências simultâneas).
   - Aumentado o tamanho do buffer de 8KB para 64KB.
   - Comprimidos arquivos antes da transferência.

3. **Otimização de Aplicação**:
   - Implementado multi-threading para processamento de dados (8 threads).
   - Adicionado cache para dados de referência.
   - Otimizado o uso de memória processando dados em blocos.

### Resultados
- Tempo de processamento em lote reduzido de 4 horas para 45 minutos (melhoria de 85%).
- Tempo de consulta ao banco de dados reduzido em 70%.
- Tempo de transferência FTP reduzido em 60%.
- Utilização da CPU melhorada de 25% para 70%.
- Uso de memória reduzido em 40%.

## Ferramentas para Análise de Desempenho

1. **Ferramentas de Perfilamento**:
   - Java VisualVM
   - YourKit Java Profiler
   - JProfiler
   - Async-profiler

2. **Ferramentas de Banco de Dados**:
   - Plano de Execução
   - Ferramentas específicas de monitoramento de banco de dados (Oracle Enterprise Manager, MySQL Workbench)
   - SchemaSpy para análise de esquema de banco de dados

3. **Monitoramento de Sistema**:
   - Prometheus + Grafana
   - New Relic
   - Datadog
   - Nagios

4. **Análise de Rede**:
   - Wireshark
   - iperf
   - netstat
   - tcpdump
