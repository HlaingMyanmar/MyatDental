DELIMITER //

CREATE TRIGGER prevent_double_booking
    BEFORE INSERT ON appointments
    FOR EACH ROW
BEGIN
    DECLARE conflict_count INT;

    -- ဆရာဝန်တစ်ယောက်မှာ အဲဒီရက်နဲ့အချိန်မှာ ရှိပြီးသား appointment ရှာမယ်
    SELECT COUNT(*)
    INTO conflict_count
    FROM appointments
    WHERE dentist_id = NEW.dentist_id
      AND appointment_date = NEW.appointment_date
      AND appointment_time = NEW.appointment_time
      AND status IN ('Scheduled', 'Completed');

    -- တူညီတဲ့အချိန်မှာ appointment ရှိရင် အမှားပြမယ်
    IF conflict_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ဒီဆရာဝန်မှာ ဒီရက်နဲ့အချိန်မှာ ချိန်းဆိုထားပြီးသားရှိပါတယ်။';
END IF;
END;//

DELIMITER ;

    DELIMITER //

DELIMITER //

CREATE TRIGGER after_appointment_delete
    AFTER DELETE ON appointments
    FOR EACH ROW
BEGIN
    INSERT INTO appointment_deletion_log (
        appointment_id,
        patient_id,
        user_name,
        patient_name,
        dentist_id,
        appointment_date,
        appointment_time,
        deleted_at,
        reason
    )
    VALUES (
               OLD.appointment_id,
               OLD.patient_id,
               COALESCE(@current_user_name, 'System'),
               (SELECT name FROM patients WHERE patient_id = OLD.patient_id LIMIT 1),
        OLD.dentist_id,
        OLD.appointment_date,
        OLD.appointment_time,
        NOW(),
        COALESCE(@delete_reason, 'Appointment deleted by system')
        );
END//

DELIMITER ;