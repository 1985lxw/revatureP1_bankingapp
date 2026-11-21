CREATE TABLE IF NOT EXISTS customers (
  customer_id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(25),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS employees (
  employee_id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
  account_id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  customer_id UUID NOT NULL,
  account_number VARCHAR(255) NOT NULL UNIQUE,
  account_type VARCHAR(25) NOT NULL,
  balance NUMERIC(19,2) NOT NULL DEFAULT 0.00,
  status VARCHAR(25) NOT NULL DEFAULT 'ACTIVE',
  status_changed_by_employee_id UUID,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  closed_at TIMESTAMP,
  CONSTRAINT fk_account_customer
    FOREIGN KEY (customer_id) 
    REFERENCES customers(customer_id),

  CONSTRAINT fk_account_employee
    FOREIGN KEY (status_changed_by_employee_id)
    REFERENCES employees(employee_id),

  CONSTRAINT chk_account_type
    CHECK (account_type IN ('CHECKING', 'SAVINGS')),

  CONSTRAINT chk_account_status
    CHECK (status IN ('ACTIVE', 'FROZEN', 'CLOSED')),

  CONSTRAINT chk_account_balance
    CHECK (balance >= 0),

  CONSTRAINT chk_closed_at
    CHECK (
      (status = 'CLOSED' AND closed_at IS NOT NULL)
      OR
      (status <> 'CLOSED' AND closed_at IS NULL)
    )
);

CREATE TABLE IF NOT EXISTS transactions (
  transaction_id UUID NOT NULL PRIMARY KEY,
  transaction_type VARCHAR(25) NOT NULL,
  amount NUMERIC(19,2) NOT NULL,
  src_account_id UUID NOT NULL,
  dest_account_id UUID NOT NULL,
  initiated_by_customer_id UUID,
  initiated_by_employee_id UUID,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_transaction_source
    FOREIGN KEY (src_account_id)
    REFERENCES accounts(account_id),

  CONSTRAINT fk_transaction_destination
    FOREIGN KEY (dest_account_id)
    REFERENCES accounts(account_id),

  CONSTRAINT fk_transaction_customer
    FOREIGN KEY (initiated_by_customer_id)
    REFERENCES customers(customer_id),

  CONSTRAINT fk_transaction_employee
    FOREIGN KEY (initiated_by_employee_id)
    REFERENCES employees(employee_id),

  CONSTRAINT chk_transaction_type
    CHECK (
      transaction_type IN (
        'DEPOSIT',
        'WITHDRAWAL',
        'TRANSFER',
        'WIRE_TRANSFER'
      )
    ),

  CONSTRAINT chk_transaction_amount
    CHECK (amount > 0),

  CONSTRAINT chk_transaction_actor
    CHECK (
      (initiated_by_customer_id IS NOT NULL)::INTEGER +
      (initiated_by_employee_id IS NOT NULL)::INTEGER = 1
    ),

  CONSTRAINT chk_transaction_accounts
    CHECK (
      src_account_id IS NOT NULL
      OR dest_account_id IS NOT NULL
    )
);

-- CREATE INDEX IF NOT EXISTS idx_accounts_customer
-- ON accounts(customer_id);

-- CREATE INDEX IF NOT EXISTS idx_transactions_source
-- ON transactions(src_account_id);

-- CREATE INDEX IF NOT EXISTS idx_transactions_destination
-- ON transactions(dest_account_id);

-- CREATE INDEX IF NOT EXISTS idx_transactions_created
-- ON transactions(created_at DESC);