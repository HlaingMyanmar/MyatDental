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