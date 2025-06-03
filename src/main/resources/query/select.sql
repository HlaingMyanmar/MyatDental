SELECT
    a.appointment_id AS appointment_code,
    a.appointment_date AS date,
    a.appointment_time AS time,
    d.name AS doctor_name,
    p.name AS patient_name,
    p.phone AS patient_phone,
    p.date_of_birth,
    p.gender,
    a.status,
    a.purpose,
    p.township
FROM
    appointments a
    JOIN patients p ON a.patient_id = p.patient_id
    JOIN dentists d ON a.dentist_id = d.dentist_id;


Payment Reports

SELECT
    p.payment_id,
    p.payment_date,
    p.amount AS paid_amount,
    p.payment_method,
    pat.name AS patient_name,
    i.invoice_id,
    i.total_amount,
    -- Running total of paid amount
    (
        i.total_amount - (
            SELECT SUM(p2.amount)
            FROM payments p2
            WHERE p2.invoice_id = p.invoice_id
                AND p2.payment_date < p.payment_date
               OR (p2.payment_date = p.payment_date AND p2.payment_id <= p.payment_id)
        )
        ) AS remaining_balance,
    p.notes AS payment_note,
    a.appointment_date,
    d.name AS dentist_name
FROM payments p
         JOIN invoices i ON p.invoice_id = i.invoice_id
         JOIN patients pat ON i.patient_id = pat.patient_id
         LEFT JOIN appointments a ON i.appointment_id = a.appointment_id
         LEFT JOIN dentists d ON a.dentist_id = d.dentist_id
ORDER BY p.payment_date DESC, p.payment_id DESC;
