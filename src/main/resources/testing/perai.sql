-- Drop existing database if any
DROP DATABASE IF EXISTS mdcdb;

-- Create database with utf8mb4 charset for multilingual support
CREATE DATABASE mdcdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mdcdb;

-- Patients Table
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

-- Dentists Table
CREATE TABLE dentists (
    dentist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100)
) ENGINE=InnoDB;

-- Appointments Table with parent_appointment_id for recurring/follow-up appointments
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    dentist_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status ENUM('Scheduled', 'Completed', 'Cancelled', 'No-Show', 'Rescheduled') DEFAULT 'Scheduled',
    purpose TEXT,
    notes TEXT,
    parent_appointment_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id),
    FOREIGN KEY (parent_appointment_id) REFERENCES appointments(appointment_id)
) ENGINE=InnoDB;

-- Treatments Table
CREATE TABLE treatments (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    standard_price DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

-- Treatment Records Table (Treatment done in each appointment)
CREATE TABLE treatment_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    treatment_id INT NOT NULL,
    tooth_number VARCHAR(20),
    notes TEXT,
    price DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id)
) ENGINE=InnoDB;

-- Invoices Table (Can be linked to appointment or standalone)
CREATE TABLE invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    appointment_id INT NULL,
    discount INT DEFAULT 0,
    total_amount DECIMAL(12,2) NOT NULL,
    balance_due DECIMAL(12,2) NOT NULL,
    status ENUM('Unpaid', 'Partially Paid', 'Paid') DEFAULT 'Unpaid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
) ENGINE=InnoDB;

-- Linking Treatments to Invoice (Many-to-many)
CREATE TABLE invoice_treatment_records (
    invoice_id INT NOT NULL,
    record_id INT NOT NULL,
    PRIMARY KEY (invoice_id, record_id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
    FOREIGN KEY (record_id) REFERENCES treatment_records(record_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Payments Table for partial/full payments
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method ENUM('Cash', 'Bank_Transfer', 'Mobile_Money'),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
) ENGINE=InnoDB;

-- Appointment Deletion Log for audit trail
CREATE TABLE appointment_deletion_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    patient_id INT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    patient_name VARCHAR(255) NOT NULL,
    dentist_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time VARCHAR(50) NOT NULL,
    deleted_at DATETIME NOT NULL,
    reason TEXT
) ENGINE=InnoDB;
