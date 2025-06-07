
-- Existing schema with modifications
DROP DATABASE IF EXISTS mdcdb;
CREATE DATABASE mdcdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mdcdb;

-- 1. အဓိကဇယားများ
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

-- 2. ကုသမှုအစီအစဉ်များ (Long-term Treatments)
CREATE TABLE treatment_plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    title VARCHAR(100) NOT NULL, -- e.g., "Braces Adjustment"
    total_cost DECIMAL(12,2) NOT NULL,
    installments_allowed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
) ENGINE=InnoDB;

-- 3. ရက်ချိန်းနှင့် ငွေကြေးစီမံခန့်ခွဲမှု
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
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id),
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id)
) ENGINE=InnoDB;

CREATE TABLE treatments (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL, -- e.g., "Braces Adjustment", "Tooth Extraction"
    description TEXT,
    standard_price DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

-- 4. ငွေကြေးစီမံခန့်ခွဲမှု စနစ်
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
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id)
) ENGINE=InnoDB;

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method ENUM('Cash', 'Bank_Transfer', 'Mobile_Money'),
    notes TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
) ENGINE=InnoDB;

-- 5. အပိုဝန်ဆောင်ခ/ပြင်ဆင်မှုများ
CREATE TABLE additional_charges (
    charge_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    description VARCHAR(200) NOT NULL, -- e.g., "Replacement due to damage"
    amount DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
) ENGINE=InnoDB;

-- 6. အသုံးဝင်သော အခြားဇယားများ
CREATE TABLE appointment_deletion_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
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


CREATE TABLE treatment_plan_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    plan_id INT NOT NULL,
    treatment_id INT NOT NULL,
    estimated_price DECIMAL(12,2),
    note TEXT,
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(plan_id),
    FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id)
);
