DROP DATABASE IF EXISTS mdcdb;
CREATE DATABASE mdcdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mdcdb;



CREATE TABLE patients (
                          patient_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          phone VARCHAR(15) NOT NULL,
                          township VARCHAR(30),
                          address TEXT,
                          date_of_birth DATE,
                          age int not null,
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

CREATE TABLE appointments (
                              appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                              patient_id INT NOT NULL,
                              dentist_id INT NOT NULL,
                              appointment_date DATE NOT NULL,
                              appointment_time TIME NOT NULL,
                              status ENUM('Scheduled', 'Completed', 'Cancelled') DEFAULT 'Scheduled',
                              purpose TEXT,
                              notes TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
                              FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id)
) ENGINE=InnoDB;

CREATE TABLE treatments (
                            treatment_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description TEXT,
                            standard_price DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

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

CREATE TABLE invoices (
                          invoice_id INT AUTO_INCREMENT PRIMARY KEY,
                          patient_id INT NOT NULL,
                          appointment_id INT,
                          discount int ,
                          total_amount DECIMAL(12,2) NOT NULL,
                          balance_due DECIMAL(12,2) NOT NULL,
                          status ENUM('Unpaid', 'Partially Paid', 'Paid') DEFAULT 'Unpaid',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
                          FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
) ENGINE=InnoDB;

CREATE TABLE invoice_treatment_records (
                                           invoice_id INT NOT NULL,
                                           record_id INT NOT NULL,
                                           PRIMARY KEY (invoice_id, record_id),
                                           FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
                                           FOREIGN KEY (record_id) REFERENCES treatment_records(record_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE payments (
                          payment_id INT AUTO_INCREMENT PRIMARY KEY,
                          invoice_id INT NOT NULL,
                          amount DECIMAL(12,2) NOT NULL,
                          payment_date DATE NOT NULL,
                          payment_method ENUM('Cash', 'Bank Transfer', 'Mobile Money'),
                          notes TEXT,
                          FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
) ENGINE=InnoDB;

CREATE TABLE appointment_deletion_log (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          appointment_id int NOT NULL,
                                          patient_id int NOT NULL,
                                          user_name VARCHAR(255) NOT NULL,
                                          patient_name VARCHAR(255) NOT NULL,
                                          dentist_id int NOT NULL,
                                          appointment_date DATE NOT NULL,
                                          appointment_time VARCHAR(50) NOT NULL,
                                          deleted_at DATETIME NOT NULL,
                                          reason TEXT


);