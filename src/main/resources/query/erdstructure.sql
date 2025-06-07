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
CREATE TABLE treatment_categories (
                                      category_id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL
);

CREATE TABLE treatments (
                            treatment_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description TEXT,
                            standard_price DECIMAL(12,2) NOT NULL,
                            category_id INT NULL,
                            FOREIGN KEY (category_id) REFERENCES treatment_categories(category_id) ON DELETE SET NULL
);

CREATE TABLE treatment_plans (
                                 plan_id INT AUTO_INCREMENT PRIMARY KEY,
                                 patient_id INT NULL,  -- NULL => Template Plan
                                 is_template BOOLEAN DEFAULT FALSE,
                                 title VARCHAR(100) NOT NULL,
                                 total_cost DECIMAL(12,2) NOT NULL,
                                 installments_allowed BOOLEAN DEFAULT FALSE,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

CREATE TABLE treatment_plan_items (
                                      item_id INT AUTO_INCREMENT PRIMARY KEY,
                                      plan_id INT NOT NULL,
                                      treatment_id INT NOT NULL,
                                      estimated_price DECIMAL(12,2) NOT NULL DEFAULT 0.00,
                                      note TEXT,
                                      status ENUM('Planned', 'In Progress', 'Completed') DEFAULT 'Planned',
                                      FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id) ON DELETE CASCADE,
                                      FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id) ON DELETE RESTRICT
);

-- 3. Appointments and Charges
CREATE TABLE appointments (
                              appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                              patient_id INT NOT NULL,
                              dentist_id INT NULL,
                              plan_id INT NULL,
                              appointment_date DATE NOT NULL,
                              appointment_time TIME NOT NULL,
                              status ENUM('Scheduled', 'Completed', 'Cancelled', 'Rescheduled') DEFAULT 'Scheduled',
                              notes TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
                              FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id) ON DELETE SET NULL,
                              FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id) ON DELETE SET NULL
);

CREATE TABLE treatment_records (
                                   record_id INT AUTO_INCREMENT PRIMARY KEY,
                                   appointment_id INT NOT NULL,
                                   treatment_id INT NOT NULL,
                                   tooth_number VARCHAR(20),
                                   notes TEXT,
                                   price DECIMAL(12,2) NOT NULL,
                                   FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE CASCADE,
                                   FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id) ON DELETE RESTRICT
);

CREATE TABLE additional_charges (
                                    charge_id INT AUTO_INCREMENT PRIMARY KEY,
                                    appointment_id INT NOT NULL,
                                    description VARCHAR(200) NOT NULL,
                                    amount DECIMAL(12,2) NOT NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE CASCADE
);

-- 4. Financial Management
CREATE TABLE invoices (
                          invoice_id INT AUTO_INCREMENT PRIMARY KEY,
                          patient_id INT NOT NULL,
                          appointment_id INT NULL,
                          plan_id INT NULL,
                          total_amount DECIMAL(12,2) NOT NULL,
                          amount_paid DECIMAL(12,2) DEFAULT 0.00,
                          balance_due DECIMAL(12,2) GENERATED ALWAYS AS (total_amount - amount_paid) STORED,
                          status ENUM('Unpaid', 'Partially Paid', 'Paid') DEFAULT 'Unpaid',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
                          FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE SET NULL,
                          FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id) ON DELETE SET NULL
);

CREATE TABLE invoice_treatment_records (
                                           invoice_id INT NOT NULL,
                                           record_id INT NOT NULL,
                                           PRIMARY KEY (invoice_id, record_id),
                                           FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
                                           FOREIGN KEY (record_id) REFERENCES treatment_records(record_id) ON DELETE CASCADE
);

CREATE TABLE payment_methods (
                                 method_id INT AUTO_INCREMENT PRIMARY KEY,
                                 method_name VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO payment_methods (method_name) VALUES ('Cash'), ('Bank Transfer'), ('Mobile Money');

CREATE TABLE payments (
                          payment_id INT AUTO_INCREMENT PRIMARY KEY,
                          invoice_id INT NOT NULL,
                          amount DECIMAL(12,2) NOT NULL,
                          payment_date DATETIME NOT NULL,
                          payment_method_id INT NOT NULL,
                          notes TEXT,
                          FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
                          FOREIGN KEY (payment_method_id) REFERENCES payment_methods(method_id)
);

CREATE TABLE invoice_additional_charges (
                                            invoice_id INT NOT NULL,
                                            charge_id INT NOT NULL,
                                            PRIMARY KEY (invoice_id, charge_id),
                                            FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
                                            FOREIGN KEY (charge_id) REFERENCES additional_charges(charge_id) ON DELETE CASCADE
);

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
);

-- 6. New Tables (Extended)

-- Patient Documents (attachments)
CREATE TABLE patient_documents (
                                   document_id INT AUTO_INCREMENT PRIMARY KEY,
                                   patient_id INT NOT NULL,
                                   file_name VARCHAR(255) NOT NULL,
                                   file_path VARCHAR(500) NOT NULL,
                                   uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- Reminders
CREATE TABLE reminders (
                           reminder_id INT AUTO_INCREMENT PRIMARY KEY,
                           patient_id INT NOT NULL,
                           message VARCHAR(255),
                           remind_date DATE NOT NULL,
                           remind_time TIME,
                           is_sent BOOLEAN DEFAULT FALSE,
                           FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- Insurance Providers
CREATE TABLE insurance_providers (
                                     insurance_id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     contact_info VARCHAR(255)
);

-- Insurance Claims
CREATE TABLE insurance_claims (
                                  claim_id INT AUTO_INCREMENT PRIMARY KEY,
                                  patient_id INT NOT NULL,
                                  insurance_id INT NOT NULL,
                                  policy_number VARCHAR(100),
                                  claim_date DATE NOT NULL,
                                  amount_claimed DECIMAL(12,2),
                                  amount_approved DECIMAL(12,2),
                                  status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
                                  notes TEXT,
                                  FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
                                  FOREIGN KEY (insurance_id) REFERENCES insurance_providers(insurance_id) ON DELETE CASCADE
);

-- User Action Logs
CREATE TABLE user_action_logs (
                                  log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_name VARCHAR(100) NOT NULL,
                                  action VARCHAR(255) NOT NULL,
                                  target_table VARCHAR(100),
                                  target_id INT,
                                  action_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  details TEXT
);

-- Tooth Chart Support (optional mapping table)
CREATE TABLE tooth_chart (
                             chart_id INT AUTO_INCREMENT PRIMARY KEY,
                             patient_id INT NOT NULL,
                             tooth_number VARCHAR(20) NOT NULL,
                             condition_description TEXT,
                             last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- 7. Indexes for Performance
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_dentist_id ON appointments(dentist_id);
CREATE INDEX idx_invoices_patient_id ON invoices(patient_id);
CREATE INDEX idx_payments_invoice_id ON payments(invoice_id);
CREATE INDEX idx_treatment_plan_items_plan_id ON treatment_plan_items(plan_id);
CREATE INDEX idx_treatment_plan_items_treatment_id ON treatment_plan_items(treatment_id);
