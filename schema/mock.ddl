PRAGMA foreign_keys = ON;
BEGIN;

INSERT INTO types VALUES (1, 'accounts.domain.BankAccount');
INSERT INTO types VALUES (2, 'accounts.domain.AccountsRecivableAccount');
INSERT INTO types VALUES (3, 'accounts.domain.AccountsPayableAccount');
INSERT INTO types VALUES (4, 'accounts.domain.ProfessionalRevenueAccount');
INSERT INTO types VALUES (5, 'accounts.domain.CurrencyGainLossAccount');
INSERT INTO types VALUES (6, 'accounts.domain.ExpenseAccount');
INSERT INTO types VALUES (7, 'accounts.domain.NormalTransaction');
INSERT INTO types VALUES (8, 'accounts.domain.InvoiceTransaction');
INSERT INTO types VALUES (9, 'accounts.domain.PaymentTransaction');

INSERT INTO accounts VALUES (1, 1, 'ANZ', 1);
INSERT INTO ledgers VALUES (1, 1, 'Current Account', 'AUD', 1);

INSERT INTO accounts VALUES (2, 2, 'Trade Debitors', 1);
INSERT INTO ledgers VALUES (2, 2 ,'ACME Inc', 'USD', 1);

INSERT INTO accounts VALUES (3, 3, 'Trade Creditors', -1);
INSERT INTO ledgers VALUES (3, 3, 'Internode', 'AUD', -1);

INSERT INTO accounts VALUES (4, 4, 'Procedures', -1);
INSERT INTO ledgers VALUES (4, 4, 'Fees', NULL, -1);
INSERT INTO ledgers VALUES (5, 4, 'Expense Reimbursement', NULL, -1);

INSERT INTO accounts VALUES (5, 6, 'Travel Expenses', 1);
INSERT INTO ledgers VALUES (6, 5, 'Ground', NULL, 1);
INSERT INTO ledgers VALUES (7, 5, 'Flights', NULL, 1);
INSERT INTO ledgers VALUES (8, 5, 'Meals', NULL, 1);


INSERT INTO accounts VALUES (7, 6, 'Communications Expenses', 1);
INSERT INTO ledgers VALUES (10, 5, 'Telephone', NULL, 1);

--

INSERT INTO transactions VALUES (1, 8, 1062164087, 'Automation', '1033');
INSERT INTO entries VALUES (NULL, 1, 2, 2250000, 'USD', 3381750, 1);
INSERT INTO entries VALUES (NULL, 1, 4, 0, NULL, 3381750, -1);

--

INSERT INTO transactions VALUES (2, 9, 1098170000, 'Payment', '1033');
INSERT INTO entries VALUES (NULL, 2, 1, 3573000, 'AUD', 3573000, 1);
INSERT INTO entries VALUES (NULL, 2, 2, 2250000, 'USD', 3573000, -1);
INSERT INTO entries VALUES (NULL, 2, 2, 0, NULL, 191250, 1);
INSERT INTO entries VALUES (NULL, 2, 4, 0, NULL, 191250, -1);

--

INSERT INTO transactions VALUES (3, 8, 1101040600, 'Telstra Bill', NULL);
INSERT INTO entries VALUES (NULL, 3, 1, 4267, 'AUD', 4267, -1);
INSERT INTO entries VALUES (NULL, 3, 10, 0, NULL, 4267, 1);

--

INSERT INTO transactions VALUES (4, 7, 1063102035, 'Flight to SFO', NULL);
INSERT INTO entries VALUES (NULL, 4, 7, 0, NULL, 329999, 1);
INSERT INTO entries VALUES (NULL, 4, 1, 329999, 'AUD', 329999, -1);

COMMIT;
-- vim: filetype=text