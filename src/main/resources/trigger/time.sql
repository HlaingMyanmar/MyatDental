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