SELECT COUNT(*) AS total_appointments
FROM appointments
WHERE patient_id = ?;

SELECT COUNT(*) AS completed_appointments
FROM appointments
WHERE patient_id = ?
  AND status = 'Completed';


SELECT
    t.name AS treatment_name,
    COUNT(tr.record_id) AS times_used,
    SUM(tr.price) AS total_amount
FROM treatment_records tr
         JOIN treatments t ON tr.treatment_id = t.treatment_id
         JOIN appointments a ON tr.appointment_id = a.appointment_id
WHERE a.patient_id = ?
GROUP BY t.name
ORDER BY times_used DESC;


SELECT
    tp.plan_id,
    tp.title,
    tp.total_cost,
    tp.installments_allowed,
    COUNT(tpi.item_id) AS total_items,
    SUM(CASE WHEN tpi.status = 'Completed' THEN 1 ELSE 0 END) AS completed_items,
    ROUND((SUM(CASE WHEN tpi.status = 'Completed' THEN 1 ELSE 0 END) / COUNT(tpi.item_id)) * 100, 2) AS completion_percentage
FROM treatment_plans tp
         LEFT JOIN treatment_plan_items tpi ON tp.plan_id = tpi.plan_id
WHERE tp.patient_id = ?
GROUP BY tp.plan_id, tp.title, tp.total_cost, tp.installments_allowed;
