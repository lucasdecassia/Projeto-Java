-- Question 7: SQL Queries based on the provided tables

-- Tables:
-- Salesperson (ID, Nome, Idade, Salário)
-- Customer (ID, Nome, Cidade, Tipo de Indústria)
-- Orders (ID, Data do Pedido, ID Cliente, ID Vendedor, Valor)

-- a) Return the names of all salespeople who have not made any orders for the customer Samsonic.
SELECT s.Nome
FROM Salesperson s
WHERE s.ID NOT IN (
    SELECT o.ID_Vendedor
    FROM Orders o
    JOIN Customer c ON o.ID_Cliente = c.ID
    WHERE c.Nome = 'Samsonic'
);

-- Alternative solution using LEFT JOIN
SELECT DISTINCT s.Nome
FROM Salesperson s
LEFT JOIN Orders o ON s.ID = o.ID_Vendedor
LEFT JOIN Customer c ON o.ID_Cliente = c.ID AND c.Nome = 'Samsonic'
WHERE c.ID IS NULL;

-- b) Update the names of salespeople who have 2 or more orders, adding an asterisk (*) at the end of the name.
UPDATE Salesperson
SET Nome = CONCAT(Nome, '*')
WHERE ID IN (
    SELECT ID_Vendedor
    FROM Orders
    GROUP BY ID_Vendedor
    HAVING COUNT(*) >= 2
);

-- c) Delete all salespeople who have made orders for the city of Jackson.
DELETE FROM Salesperson
WHERE ID IN (
    SELECT o.ID_Vendedor
    FROM Orders o
    JOIN Customer c ON o.ID_Cliente = c.ID
    WHERE c.Cidade = 'Jackson'
);

-- d) Show the total sales value for each salesperson. If the salesperson has not made any sales, show zero.
SELECT s.ID, s.Nome, COALESCE(SUM(o.Valor), 0) AS Total_Vendas
FROM Salesperson s
LEFT JOIN Orders o ON s.ID = o.ID_Vendedor
GROUP BY s.ID, s.Nome
ORDER BY s.ID;

-- Explanation of the queries:

-- Query a: This query finds salespeople who have not made orders for Samsonic.
-- It uses a subquery to find all salespeople IDs that have orders for Samsonic,
-- and then selects all salespeople whose IDs are not in that list.
-- The alternative solution uses LEFT JOIN to achieve the same result.

-- Query b: This query updates the names of salespeople who have 2 or more orders.
-- It uses a subquery to find all salespeople IDs that have at least 2 orders,
-- and then updates the names of those salespeople by adding an asterisk at the end.

-- Query c: This query deletes all salespeople who have made orders for customers in Jackson.
-- It uses a subquery to find all salespeople IDs that have orders for customers in Jackson,
-- and then deletes those salespeople from the Salesperson table.

-- Query d: This query shows the total sales value for each salesperson.
-- It uses a LEFT JOIN to include all salespeople, even those who have not made any sales.
-- The COALESCE function is used to replace NULL values with 0 for salespeople with no sales.
-- The results are grouped by salesperson ID and name, and ordered by salesperson ID.