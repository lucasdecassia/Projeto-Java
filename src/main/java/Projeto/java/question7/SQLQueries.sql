-- Consultas SQL baseadas nas tabelas fornecidas

-- Tabelas:
-- Salesperson (ID, Nome, Idade, Salário)
-- Customer (ID, Nome, Cidade, Tipo de Indústria)
-- Orders (ID, Data do Pedido, ID Cliente, ID Vendedor, Valor)

-- a) Retornar os nomes de todos os vendedores que não fizeram nenhum pedido para o cliente Samsonic.
SELECT s.Nome
FROM Salesperson s
WHERE s.ID NOT IN (
    SELECT o.ID_Vendedor
    FROM Orders o
    JOIN Customer c ON o.ID_Cliente = c.ID
    WHERE c.Nome = 'Samsonic'
);

-- Solução alternativa usando LEFT JOIN
SELECT DISTINCT s.Nome
FROM Salesperson s
LEFT JOIN Orders o ON s.ID = o.ID_Vendedor
LEFT JOIN Customer c ON o.ID_Cliente = c.ID AND c.Nome = 'Samsonic'
WHERE c.ID IS NULL;

-- b) Atualizar os nomes dos vendedores que têm 2 ou mais pedidos, adicionando um asterisco (*) no final do nome.
UPDATE Salesperson
SET Nome = CONCAT(Nome, '*')
WHERE ID IN (
    SELECT ID_Vendedor
    FROM Orders
    GROUP BY ID_Vendedor
    HAVING COUNT(*) >= 2
);

-- c) Excluir todos os vendedores que fizeram pedidos para a cidade de Jackson.
DELETE FROM Salesperson
WHERE ID IN (
    SELECT o.ID_Vendedor
    FROM Orders o
    JOIN Customer c ON o.ID_Cliente = c.ID
    WHERE c.Cidade = 'Jackson'
);

-- d) Mostrar o valor total de vendas para cada vendedor. Se o vendedor não fez nenhuma venda, mostrar zero.
SELECT s.ID, s.Nome, COALESCE(SUM(o.Valor), 0) AS Total_Vendas
FROM Salesperson s
LEFT JOIN Orders o ON s.ID = o.ID_Vendedor
GROUP BY s.ID, s.Nome
ORDER BY s.ID;

-- Explicação das consultas:

-- Consulta a: Esta consulta encontra vendedores que não fizeram pedidos para Samsonic.
-- Ela usa uma subconsulta para encontrar todos os IDs de vendedores que têm pedidos para Samsonic,
-- e então seleciona todos os vendedores cujos IDs não estão nessa lista.
-- A solução alternativa usa LEFT JOIN para alcançar o mesmo resultado.

-- Consulta b: Esta consulta atualiza os nomes dos vendedores que têm 2 ou mais pedidos.
-- Ela usa uma subconsulta para encontrar todos os IDs de vendedores que têm pelo menos 2 pedidos,
-- e então atualiza os nomes desses vendedores adicionando um asterisco no final.

-- Consulta c: Esta consulta exclui todos os vendedores que fizeram pedidos para clientes em Jackson.
-- Ela usa uma subconsulta para encontrar todos os IDs de vendedores que têm pedidos para clientes em Jackson,
-- e então exclui esses vendedores da tabela Salesperson.

-- Consulta d: Esta consulta mostra o valor total de vendas para cada vendedor.
-- Ela usa um LEFT JOIN para incluir todos os vendedores, mesmo aqueles que não fizeram nenhuma venda.
-- A função COALESCE é usada para substituir valores NULL por 0 para vendedores sem vendas.
-- Os resultados são agrupados por ID e nome do vendedor, e ordenados por ID do vendedor.
