-- Drop and recreate database
DROP DATABASE IF EXISTS mdcdb;
CREATE DATABASE mdcdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mdcdb;

-- 1. Main Tables
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    township VARCHAR(30),
    address TEXT,
    date_of_birth DATE,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other'),
    medical_history TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE dentists (
    dentist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100)
) ENGINE=InnoDB;

-- 2. Treatment Plans
CREATE TABLE treatment_plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    title VARCHAR(100) NOT NULL, -- e.g., "Braces Adjustment"
    total_cost DECIMAL(12,2) NOT NULL,
    installments_allowed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE treatments (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL, -- e.g., "Braces Adjustment", "Tooth Extraction"
    description TEXT,
    standard_price DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE treatment_plan_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT NOT NULL,
    treatment_id INT NOT NULL,
    estimated_price DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    note TEXT,
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id) ON DELETE CASCADE,
    FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3. Appointments and Charges
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    dentist_id INT NOT NULL,
    plan_id INT NULL, -- Linked to long-term plan
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status ENUM('Scheduled', 'Completed', 'Cancelled', 'Rescheduled') DEFAULT 'Scheduled',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id) ON DELETE SET NULL,
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE additional_charges (
    charge_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    description VARCHAR(200) NOT NULL, -- e.g., "Replacement due to damage"
    amount DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Financial Management
CREATE TABLE invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    appointment_id INT NULL,
    plan_id INT NULL, -- For installment plans
    total_amount DECIMAL(12,2) NOT NULL,
    amount_paid DECIMAL(12,2) DEFAULT 0.00,
    balance_due DECIMAL(12,2) GENERATED ALWAYS AS (total_amount - amount_paid) STORED,
    status ENUM('Unpaid', 'Partially Paid', 'Paid') DEFAULT 'Unpaid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE SET NULL,
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Flexible payment method table
CREATE TABLE payment_methods (
    method_id INT AUTO_INCREMENT PRIMARY KEY,
    method_name VARCHAR(100) UNIQUE NOT NULL
) ENGINE=InnoDB;

-- Insert common methods
INSERT INTO payment_methods (method_name) VALUES ('Cash'), ('Bank Transfer'), ('Mobile Money');

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method_id INT NOT NULL,
    notes TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(method_id)
) ENGINE=InnoDB;

-- 5. Appointment Deletion Log
CREATE TABLE appointment_deletion_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    appointment_id INT NOT NULL,
    patient_id INT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    patient_name VARCHAR(255) NOT NULL,
    dentist_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    deleted_at DATETIME NOT NULL,
    reason TEXT
) ENGINE=InnoDB;

-- 6. Indexes for Performance
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_dentist_id ON appointments(dentist_id);
CREATE INDEX idx_invoices_patient_id ON invoices(patient_id);
CREATE INDEX idx_payments_invoice_id ON payments(invoice_id);
CREATE INDEX idx_treatment_plan_items_plan_id ON treatment_plan_items(plan_id);
CREATE INDEX idx_treatment_plan_items_treatment_id ON treatment_plan_items(treatment_id);
