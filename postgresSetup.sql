CREATE TABLE IF NOT EXISTS `customers` (
  `customer_id` UUID NOT NULL PRIMARY KEY DEFAULT 'get_random_uuid()',
  `first_name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(25) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP',
  `updated_at` TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP',
  `password_hash` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `employees` (
  `employee_id` UUID NOT NULL PRIMARY KEY DEFAULT 'get_random_uuid()',
  `first_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT 'NOW()',
  `last_name` VARCHAR(255) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `accounts` (
  `account_id` UUID NOT NULL PRIMARY KEY DEFAULT 'get_random_uuid()',
  `customer_id` UUID NOT NULL,
  `type` VARCHAR(25) NOT NULL,
  `balance` BIGINT NOT NULL,
  `status` VARCHAR(25) NOT NULL,
  `status_changed_by_employee_id` UUID NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT 'NOW()',
  `account_number` VARCHAR(255) NOT NULL UNIQUE,
  `updated_at` TIMESTAMP NOT NULL,
  `closed_at` TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS `transactions` (
  `transaction_id` UUID NOT NULL PRIMARY KEY,
  `transaction_type` VARCHAR(25) NOT NULL,
  `amount` BIGINT NOT NULL,
  `src_account_id` UUID NOT NULL,
  `dest_account_id` UUID NOT NULL,
  `initiated_by_customer_id` UUID NULL,
  `initiated_by_employee_id` UUID NULL
);

ALTER TABLE `customers`
ADD FOREIGN KEY (`customer_id`) REFERENCES `accounts`(`customer_id`);

ALTER TABLE `employees`
ADD FOREIGN KEY (`employee_id`) REFERENCES `accounts`(`status_changed_by_employee_id`);

ALTER TABLE `accounts`
ADD FOREIGN KEY (`account_id`) REFERENCES `transactions`(`src_account_id`);

ALTER TABLE `accounts`
ADD FOREIGN KEY (`account_id`) REFERENCES `transactions`(`dest_account_id`);

ALTER TABLE `customers`
ADD FOREIGN KEY (`customer_id`) REFERENCES `transactions`(`initiated_by_customer_id`);

ALTER TABLE `employees`
ADD FOREIGN KEY (`employee_id`) REFERENCES `transactions`(`initiated_by_employee_id`);
